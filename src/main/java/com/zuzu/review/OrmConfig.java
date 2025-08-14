package com.zuzu.review;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class OrmConfig {
  private static final Logger log = LoggerFactory.getLogger(OrmConfig.class);
  private static EntityManagerFactory emf;
  private static Properties configProps;

  public static synchronized EntityManagerFactory emf() {
    if (emf == null) {
      // Load properties from classpath application.properties (optional)
      configProps = loadProps();

      DataSource ds = newDataSource();
      ensureSchema(ds);
      Map<String, Object> props = new HashMap<>();
      props.put(Environment.DATASOURCE, ds);
      props.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
      props.put(Environment.HBM2DDL_AUTO, cfg("HIBERNATE_DDL", "validate"));
      props.put(Environment.SHOW_SQL, cfg("LOG_SQL", "false"));
      props.put(Environment.FORMAT_SQL, "true");
      props.put(Environment.ORDER_INSERTS, "true");
      props.put(Environment.ORDER_UPDATES, "true");
      props.put(Environment.STATEMENT_BATCH_SIZE, "50");
      emf = Persistence.createEntityManagerFactory("reviewsPU", props);
    }
    return emf;
  }

  private static String cfg(String k, String d) {
    String v = System.getenv(k);
    if (v != null && !v.isBlank()) return v;
    if (configProps != null) {
      v = configProps.getProperty(k);
      if (v != null && !v.isBlank()) return v;
    }
    return d;
  }

  private static Properties loadProps() {
    Properties p = new Properties();
    // Try application.properties on the classpath
    try (InputStream in = OrmConfig.class.getResourceAsStream("/application.properties")) {
      if (in != null) {
        p.load(in);
      }
    } catch (IOException ignored) { }
    return p;
  }

private static void ensureSchema(DataSource ds) {
    log.info("Checking database schema and connectivity...");
    try (Connection c = ds.getConnection()) {
      if (!tableExists(c, "public", "hotels")) {
        log.info("Hotels table not found, running schema.sql to create tables...");
        runSchemaSql(c);
        // verify again
        if (!tableExists(c, "public", "hotels")) {
          throw new IllegalStateException("Schema creation attempted but 'hotels' table still missing.");
        }
        log.info("Schema created successfully");
      }
      log.info("Schema check OK");
    } catch (Exception e) {
      String msg = "Database schema check/creation failed: " + e.getMessage();
      log.error(msg, e);
      throw new RuntimeException(msg, e);
    }
  }

  private static boolean tableExists(Connection c, String schema, String table) throws Exception {
    DatabaseMetaData md = c.getMetaData();
    try (ResultSet rs = md.getTables(null, schema, table, new String[]{"TABLE"})) {
      if (rs.next()) return true;
    }
    // Fallback without schema (some drivers)
    try (ResultSet rs = md.getTables(null, null, table, new String[]{"TABLE"})) {
      return rs.next();
    }
  }

  private static void runSchemaSql(Connection c) throws Exception {
    try (InputStream in = OrmConfig.class.getResourceAsStream("/schema.sql")) {
      if (in == null) throw new IOException("schema.sql not found on classpath");
      String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
      try (Statement st = c.createStatement()) {
        for (String part : sql.split(";")) {
          String s = part.trim();
          if (!s.isEmpty()) {
            st.execute(s);
          }
        }
      }
    }
  }
private static DataSource newDataSource() {
    HikariConfig hc = new HikariConfig();
    String baseUrl = cfg("DB_URL", "jdbc:postgresql://localhost:5432/reviews");
    String url = baseUrl;
    String suffix = "connectTimeout=5&socketTimeout=15&tcpKeepAlive=true";
    if (!baseUrl.contains("connectTimeout")) {
      url = baseUrl + (baseUrl.contains("?") ? "&" : "?") + suffix;
    }
    hc.setJdbcUrl(url);
    hc.setUsername(cfg("DB_USER", "postgres"));
    hc.setPassword(cfg("DB_PASSWORD", "postgres"));
    hc.setMaximumPoolSize(Integer.parseInt(cfg("DB_POOL_SIZE", "2")));
    hc.setMinimumIdle(Integer.parseInt(cfg("DB_MIN_IDLE", "0")));
    hc.setIdleTimeout(20000);
    hc.setConnectionTimeout(5000);
    hc.setValidationTimeout(5000);
    hc.setInitializationFailTimeout(5000);
    log.info("Initializing datasource: url='{}' (pool max={})", url.replaceAll("//([^/@:]+)", "//***"), hc.getMaximumPoolSize());
    return new HikariDataSource(hc);
  }

  public static void updateSchema() {
    // Load properties similarly to emf() so env vars or application.properties apply
    configProps = loadProps();
    DataSource ds = newDataSource();
    try (Connection c = ds.getConnection()) {
      // Force drop-and-create using schema.sql (contains DROP TABLE IF EXISTS ...)
      runSchemaSql(c);
      System.out.println("Schema (re)creation executed successfully.");
    } catch (Exception e) {
      throw new RuntimeException("Schema (re)creation failed: " + e.getMessage(), e);
    }
  }

  private static void runUpdateSql(Connection c) throws SQLException {
    String sql = String.join("\n",
      "ALTER TABLE reviews ADD COLUMN IF NOT EXISTS hotel_name varchar(255);",
      "ALTER TABLE reviews ADD COLUMN IF NOT EXISTS provider_id int;",
      "ALTER TABLE reviews ADD COLUMN IF NOT EXISTS provider_review_id bigint;",
      "ALTER TABLE reviews ADD COLUMN IF NOT EXISTS language varchar(8);",
      "",
      "ALTER TABLE reviews ALTER COLUMN platform TYPE varchar(64);",
      "ALTER TABLE reviews ALTER COLUMN rating TYPE numeric(3,1) USING rating::numeric;",
      "ALTER TABLE reviews ALTER COLUMN comment TYPE text;",
      "ALTER TABLE reviews ALTER COLUMN review_dt TYPE timestamptz USING review_dt::timestamptz;",
      "ALTER TABLE reviews ALTER COLUMN raw_payload TYPE jsonb USING raw_payload::jsonb;",
      "",
      "CREATE UNIQUE INDEX IF NOT EXISTS uk_platform_provider_review ON reviews(platform, provider_review_id);",
      "CREATE INDEX IF NOT EXISTS idx_reviews_hotel ON reviews(hotel_id);",
      "CREATE INDEX IF NOT EXISTS idx_reviews_review_dt ON reviews(review_dt);"
    );
    try (Statement st = c.createStatement()) {
      for (String part : sql.split(";")) {
        String s = part.trim();
        if (!s.isEmpty()) {
          st.execute(s);
        }
      }
    }
  }
}
