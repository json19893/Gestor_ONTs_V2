package totalplay.snmpv2.com.presentacion;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IconnciliacionService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsNCERepository;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.CatOltsVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.repositorio.ICatAccesoDirectoRepository;
import totalplay.snmpv2.com.persistencia.vertica.repositorio.ICatOltsVerticaRepository;


@Slf4j
@RestController
public class IntegracionController extends Constantes {
	@Autowired
	ICatAccesoDirectoRepository repositorio;
	@Autowired
	ICatOltsVerticaRepository repositorioOlts;
	@Autowired
	IinventarioOntsNCERepository inventarioNCE;
	@Autowired
	IasyncMethodsService asyncMethods;
	@Autowired
	IconnciliacionService conciliacion;
	

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@RequestMapping(value = "/getInfoVertica", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getInfoVertica() throws Exception {
		Integer totalOnts=0; 
		Integer maxSublist= 6;
		
		try {
			//Se obtienen y actualizan las ip's
			log.info("Se inicia la actualizaciòn de inventarios");
			List<CatOltsVerticaEntity> olts =  repositorioOlts.getUniqueIps();
			conciliacion.saveIps(olts);
			
			log.info("Termina la actualizaciòn de inventarios");
			
			
			inventarioNCE.deleteAll();
		
			List<BmsGestorOraVerticaEntity> onts =  repositorio.getUniqueSerial();
			totalOnts = onts.size();
			
			Integer MaxSublistItems = (onts.size() /maxSublist)+ 1;
			System.out.println("inició la segmentación");
			
			for(int k = 0; k < onts.size(); k += MaxSublistItems) {
				Integer limMaxItems = k + MaxSublistItems;
				if (limMaxItems >= onts.size()) {
					limMaxItems = onts.size();
				}
				List<BmsGestorOraVerticaEntity> segmentOnts = new ArrayList<BmsGestorOraVerticaEntity>(onts.subList(k, limMaxItems));
				
				List<CompletableFuture<GenericResponseDto>> thredOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
				Integer MaxOnts = (segmentOnts.size() /40)+ 1;
				for(int i = 0; i < segmentOnts.size(); i += MaxOnts) {
					Integer limMax = i + MaxOnts;
					if (limMax >= segmentOnts.size()) {
						limMax = segmentOnts.size();
					}
					List<BmsGestorOraVerticaEntity> segmentOlts = new ArrayList<BmsGestorOraVerticaEntity>(segmentOnts.subList(i, limMax));
					CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.saveOntsNCE(segmentOlts);
					thredOlts.add(executeProcess);
				}
				CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
				
			}
			//Se libera la memoria
			onts=null;
					
			conciliacion.updateTables();
		}catch (Exception e) {
			return "Error Al hacer la conciliaciòn";
		}	
		return "Se recuperaron " + totalOnts;
	}
}
