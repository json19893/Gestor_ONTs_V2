package totalplay.monitor.snmp.com.persistencia.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import totalplay.monitor.snmp.com.negocio.dto.InventarioRechazadoNCEDto;
import totalplay.monitor.snmp.com.negocio.dto.dataRegionResponseDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesTecnologiaDto;
import totalplay.monitor.snmp.com.persistencia.entidad.InventarioRechazadasNCEEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;

public interface IinventarioRechazadosNCERepositorio extends MongoRepository<InventarioRechazadasNCEEntidad, String> {
	
	@Aggregation(pipeline = { "{'$match':{'$and':[{'tecnologia':?0},{'estatus':?1}]}}" })
	List<catOltsEntidad> findCatOltsByStatus(@Param("tipo") String tipo,@Param("estatus") Integer estatus );
	
	
	
	
}
