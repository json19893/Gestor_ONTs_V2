package totalplay.monitor.snmp.com.persistencia.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.monitor.snmp.com.persistencia.entidad.CargaOntSaEntity;

public interface ICargaOntSaRepository extends MongoRepository<CargaOntSaEntity, String> {

    @Aggregation(
            pipeline = {
                    "{\n" +
                            "\t\t\"$lookup\":{\n" +
                            "\t\t\tfrom: \"tb_inventario_onts\",\n" +
                            "\t\t\tlocalField:\"serie\",\n" +
                            "\t\t\tforeignField:\"numero_serie\",\n" +
                            "\t\t\tas: \"onts\",\n" +
                            "\t\t}\n" +
                            "    }"," {\n" +
                    "        $match:{onts:{$ne:[]}}\n" +
                    "    }",
                    "{\n" +
                            "            $unwind: \"$onts\"\n" +
                            "    }",
                    "{ $replaceRoot: { newRoot: \"$onts\" } }",
                    "{\n" +
                            "       $set:{\n" +
                            "           sa:true\n" +
                            "       }\n" +
                            "   }",
                    "{\n" +
                            "    '$merge': {\n" +
                            "      'into': 'tb_inventario_onts', \n" +
                            "      'on': '_id', \n" +
                            "      'whenMatched': 'replace', \n" +
                            "      'whenNotMatched': 'insert'\n" +
                            "    }\n" +
                            "  }"
            }
    )
    void updateSAporSerieOnt();
}
