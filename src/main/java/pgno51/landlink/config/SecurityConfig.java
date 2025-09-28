package pgno51.landlink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class SecurityConfig {

    @Value("${app.encryption.secret}")
    private String secret;

    @Bean
    public SecretKey aesSecretKey() {
        // Expect Base64-encoded 256-bit key or a plain text passphrase (fallback padded/truncated)
        try {
            byte[] decoded = Base64.getDecoder().decode(secret);
            return new SecretKeySpec(decoded, "AES");
        } catch (IllegalArgumentException e) {
            // Fallback: derive raw bytes from passphrase (NOT PBKDF2 – for demo only)
            byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
            byte[] key = new byte[32]; // 256-bit
            for (int i = 0; i < key.length; i++) key[i] = i < bytes.length ? bytes[i] : 0;
            return new SecretKeySpec(key, "AES");
        }
    }
}
