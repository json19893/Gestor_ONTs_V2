package totalplay.services.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import totalplay.services.com.persistencia.entidad.InventarioOntResp;

import java.util.List;

@Repository
public interface InventarioOntRespRepository extends MongoRepository<InventarioOntResp, String> {
    @Aggregation(pipeline = {"{'$match':{'$and':[{'id_olt':?0},{'uid':?1}]}},{'frame':?2},{'port':?3},{'slot':?4}"})
    List<InventarioOntResp> getOntsRespaldo(
            @Param("idOlt") Integer idOlt,
            @Param("uid") String uid,
            @Param("frame") Integer frame,
            @Param("port") Integer port,
            @Param("slot") Integer slot);

}
