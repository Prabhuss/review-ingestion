package com.zuzu.review;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class IngestWorker {
  private static final Logger log = LoggerFactory.getLogger(IngestWorker.class);

  // Reuse across warm Lambda invocations
  private static final S3Client s3 = S3Client.create();
  private static final EntityManagerFactory emf = OrmConfig.emf();
  private static final ReviewService service = new ReviewService(new ReviewRepository(emf));

  /**
   * Download object (bucket/key), parse JSON, and persist via existing ReviewService.
   * Returns the review id.
   */
  public Long processObject(String bucket, String rawKey) throws Exception {
    // S3 event keys may be URL-encoded; decode defensively
    final String key = URLDecoder.decode(rawKey, StandardCharsets.UTF_8);
    log.debug("Fetching s3://{}/{}", bucket, key);

    // Guard: Check content type before downloading
    var head = s3.headObject(HeadObjectRequest.builder().bucket(bucket).key(key).build());
    String ct = head.contentType();
    if (ct != null && !ct.toLowerCase(Locale.ROOT).contains("json")) {
      log.warn("Skipping due to non-JSON content-type {} for s3://{}/{}", ct, bucket, key);
      return null; // Skip processing, return null to indicate no review was created
    }

    var bytes = s3.getObject(
      GetObjectRequest.builder().bucket(bucket).key(key).build(),
      ResponseTransformer.toBytes()
    );

    String json = bytes.asUtf8String();

    if (json == null || json.isBlank()) {
      throw new IllegalArgumentException("Empty JSON for s3://" + bucket + "/" + key);
    }

    Long id = service.processJson(json);
    log.info("Ingest OK: s3://{}/{} -> reviewId={}", bucket, key, id);
    return id;
  }

}
