package totalplay.snmpv2.com.presentacion;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.ICronUpdateService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.oltsNcePolearEntidad;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IoltsNcePolearRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoEstatusOltsNCERepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CronUpdateController {   
	@Autowired
	IoltsNcePolearRepository oltsNCERepository;
	@Autowired
	ICronUpdateService cronUpdate;
	@Autowired
	IcatOltsRepository catOlts;
	@Autowired
	IpoleoEstatusOltsNCERepository poleoOlts;
	
	Utils utl=new Utils();
	
	@Scheduled(fixedRate =300000)
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @GetMapping(value = "/updateStatuslOlt", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDto updateStatuslOlt() {
    	//Encontrar los registros con estatus cero 
    	try {
    		List<oltsNcePolearEntidad> oltsNCE = oltsNCERepository.getOltEstatus(0);
        	
        	//Iterar los registros para polear
        	for(oltsNcePolearEntidad olt: oltsNCE) {
        		olt.setEstatus_poleo(1);
        		oltsNCERepository.save(olt);
        		//Encontrar la Olt
        		CatOltsEntity o = catOlts.getOlt(olt.getId_olt());
        		
        		//TODO: polear la olt 
        		poleoOlts.deleteAll();
        		cronUpdate.updateStatusOlt(o);
        		
        		
        		//Hacer el join e insertar en inventario
        		try {
        			System.out.println("inicia join");
        			poleoOlts.getUpdateOnts();
        			System.out.println("termina join");
        		}catch (Exception e) {
        			log.info(e.toString());
    			}
        		
        		//TODO:actualizar el estatus y fecha de poleo de la entidad
        		olt.setEstatus_poleo(2);
        		olt.setFecha_poleo(utl.getDate());
        		oltsNCERepository.save(olt);
        		
        		
        	}
			
		} catch (Exception e) {
			log.info("Error al actaulizar las olts");
		}
    	
    	
        return null;
    }
}
