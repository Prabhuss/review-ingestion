package com.zuzu.review;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * SQS -> Lambda handler (BatchSize=1, fail-fast).
 * Any exception during processing is wrapped in a RuntimeException so
 * the invocation fails and SQS will retry / DLQ the message.
 */
public class ReviewIngestFromSqsHandler implements RequestHandler<SQSEvent, String> {

  private static final Logger log = LoggerFactory.getLogger(ReviewIngestFromSqsHandler.class);

  private final IngestWorker worker = new IngestWorker();

  @Override
  public String handleRequest(SQSEvent event, Context context) {
    long t0 = System.currentTimeMillis();
    int processed = 0;

    // With BatchSize=1 there will normally be exactly one message here.
    for (SQSEvent.SQSMessage msg : event.getRecords()) {
      final String body = msg.getBody();

      // Try to parse body as S3 notification; if it isn't, just skip (ack) this message.
      S3EventNotification s3evt = S3EventNotification.parseJson(body);
      List<S3EventNotification.S3EventNotificationRecord> recs = s3evt.getRecords();
      if (recs == null || recs.isEmpty()) {
        log.warn("Skipping non-S3 message id={}, bodyLength={}", msg.getMessageId(), body.length());
        continue; // ack this message; nothing to do
      }

      for (S3EventNotification.S3EventNotificationRecord r : recs) {
        if (r.getEventSource() == null || !r.getEventSource().contains("aws:s3")) {
          log.warn("Skipping non-S3 record in message id={}", msg.getMessageId());
          continue;
        }
        String bucket = r.getS3().getBucket().getName();
        String key = URLDecoder.decode(r.getS3().getObject().getKey(), StandardCharsets.UTF_8);

        try {
          Long id = worker.processObject(bucket, key); // may throw checked Exception
          if (id != null) {
            log.debug("Processed {}/{} -> review id={}", bucket, key, id);
          } else {
            log.debug("Skipped {}/{} (non-JSON file)", bucket, key);
          }
        } catch (Exception e) {
          // Fail-fast: wrap checked exception so Lambda invocation fails.
          log.error("Processing failed for s3://{}/{} (msgId={}): {}", bucket, key, msg.getMessageId(), e.toString(), e);
          throw new RuntimeException(e);
        }
      }
      processed++;
    }

    long elapsed = System.currentTimeMillis() - t0;
    log.info("SQS ingest done: processedMsgs={}, failedMsgs=0, elapsedMs={}", processed, elapsed);
    return "ok";
  }
}
