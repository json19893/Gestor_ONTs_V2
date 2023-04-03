package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.query.Param;


public interface IcatOltsRepository extends MongoRepository<CatOltsEntity, String> {
	
	List<CatOltsEntity> findByEstatus(Integer estatus);
	
	@Aggregation(pipeline = {"{'$match':{id_olt:?0} } "})
	CatOltsEntity  getOlt(@Param("idOLt") Integer idOLt);
	
	List<CatOltsEntity> findByEstatusAndDescubrio(Integer estatus,boolean descubrio);
	
	@Aggregation(pipeline = {
			"{'$match':{ip: {$in : ?0} } }"
			})
	List<CatOltsEntity> getOltsByIp(@Param("idOLts") List<String> idOLts);
}
