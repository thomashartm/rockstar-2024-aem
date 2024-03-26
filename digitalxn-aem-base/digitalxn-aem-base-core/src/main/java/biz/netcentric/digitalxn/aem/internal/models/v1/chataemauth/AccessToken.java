package biz.netcentric.digitalxn.aem.internal.models.v1.chataemauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccessToken {

    private static final String ALGORITHM = "HmacSHA256";
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    private Map<String, String> headers;
    private Map<String, String> payload;
    private String signature;

    public AccessToken() {
        this.headers = new HashMap<>();
        this.headers.put("typ", "JWT");
        this.headers.put("alg", "HS256");
        this.payload = new HashMap<>();
    }

    public void addPayload(String key, String value) {
        payload.put(key, value);
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String base64Encode(String str) {
        return base64Encode(str.getBytes(StandardCharsets.UTF_8));
    }

    public String encode(final String secret) throws JsonProcessingException {
        String encodedHeader = base64Encode(OBJECT_MAPPER.writeValueAsString(headers));
        String encodedPayload = base64Encode(OBJECT_MAPPER.writeValueAsString(payload));
        signature = hmacWithJava(ALGORITHM, encodedHeader + "." + encodedPayload, secret);

        return String.format(Locale.ROOT, "%s.%s.%s",
                encodedHeader,
                encodedPayload,
                signature);
    }

    public static String hmacWithJava(String algorithm, String data, String key) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        Mac mac;
        try {
            mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return base64Encode(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public String toString() {
        return "Token{" +
                "headers=" + headers +
                ", payload=" + payload +
                ", signature='" + signature + '\'' +
                '}';
    }

}
