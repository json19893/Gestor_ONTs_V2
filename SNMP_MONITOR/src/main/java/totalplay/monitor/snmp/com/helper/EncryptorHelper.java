package totalplay.monitor.snmp.com.helper;

import lombok.Getter;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

public class EncryptorHelper {
    private static Logger log = LoggerFactory
            .getLogger(EncryptorHelper.class);
    @Getter
    public static EncryptorHelper INSTANCE = new EncryptorHelper();
    @Value("${spring.data.mongodb.uri.test}")
    private String propertyMongoUri;
    @Autowired
    private Environment environment;
    private PooledPBEStringEncryptor mEncryptor;

    public String getPropertymongoUri() {
        return propertyMongoUri;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public EncryptorHelper() {
        String secretKey = System.getProperty("jasypt.encryptor.password")
                == null ? "MYSECRET_PASSWORD" : System.getProperty("jasypt.encryptor.password");

        mEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(secretKey);

        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("2");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        mEncryptor.setConfig(config);
    }

    public String encryptString(String textPlain) {
        return mEncryptor.encrypt(textPlain);
    }

    public String deencryptString(String textEncrypt) {
        return mEncryptor.decrypt(textEncrypt);
    }

    /*@Bean(name = "encryptorBean")
    public void stringEncryptor() {
        String secretKey = getEnvironment().getProperty("jasypt.encryptor.password");

        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();

        config.setPassword(secretKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("2");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        mEncryptor = encryptor;
    }*/
}

