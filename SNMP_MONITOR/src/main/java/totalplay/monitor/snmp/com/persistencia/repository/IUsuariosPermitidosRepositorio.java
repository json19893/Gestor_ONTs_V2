package totalplay.monitor.snmp.com.persistencia.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.responseOltsOntsDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesOntsEmpDto;
import totalplay.monitor.snmp.com.persistencia.entidad.UsuariosPermitidosEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwActualizacionEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.vwTotalOntsEntidad;


@Repository
public interface IUsuariosPermitidosRepositorio extends MongoRepository<UsuariosPermitidosEntidad, String> {
	@Aggregation(pipeline = {
			"{$match:{ nombreUsuario: ?0 } }"
			})
	UsuariosPermitidosEntidad getUsuario(@Param("usuarios") String usuario);
		
}