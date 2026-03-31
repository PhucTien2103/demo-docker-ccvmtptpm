package nhom13.vn.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
//factory để tạo entity manager, quản lý kết nối đến database
public class JPAConfig {

    private static final EntityManagerFactory factory = buildFactory();

    private static EntityManagerFactory buildFactory() {
        Map<String, Object> overrides = new HashMap<>();
        String dbUrl = System.getenv("DB_URL");

        if (dbUrl == null || dbUrl.isBlank()) {
            String host = envOrDefault("DB_HOST", "localhost");
            String port = envOrDefault("DB_PORT", "1433");
            String dbName = envOrDefault("DB_NAME", "XinNghiPhep");
            dbUrl = String.format(
                    "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
                    host, port, dbName
            );
        }

        overrides.put("jakarta.persistence.jdbc.url", dbUrl);
        overrides.put("jakarta.persistence.jdbc.user", envOrDefault("DB_USER", "sa"));
        overrides.put("jakarta.persistence.jdbc.password", envOrDefault("DB_PASSWORD", "123456"));
        return Persistence.createEntityManagerFactory("XinNghiPhepPU", overrides);
    }

    private static String envOrDefault(String name, String defaultValue) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
