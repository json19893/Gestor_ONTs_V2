package totalplay.services.com.configuracion;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import totalplay.services.com.helper.EncryptorHelper;


@Configuration
public class MongoConnection extends AbstractMongoClientConfiguration {
    @Value("${spring.data.mongodb.uri}")
    private String uriConnection;

    public String getUriConnection() {
        return uriConnection;
    }

    @Autowired
    ApplicationContext apc;
    private String db = "totalplayDb";

    @Override
    protected String getDatabaseName() {
        return db;
    }

    @Override
    public MongoClient mongoClient() {
        EncryptorHelper encryptorHelper = EncryptorHelper.getINSTANCE();
        String uriDesencripter = encryptorHelper.deencryptString(getUriConnection());



        if (uriDesencripter.isEmpty() && uriDesencripter == null) {
            new Exception("La uri para la conexion de mongo esta vacia");
        }

        MongoClient client = MongoClients.create(uriDesencripter);

        return client;
    }
}
