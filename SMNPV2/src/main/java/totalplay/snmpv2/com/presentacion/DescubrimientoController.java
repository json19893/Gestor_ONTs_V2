package totalplay.snmpv2.com.presentacion;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IdescubrimientoService;
import totalplay.snmpv2.com.negocio.services.IlimpiezaOntsService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsErroneas;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorEjecucionRepository;
import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;
@Slf4j
@RestController
public class DescubrimientoController extends Constantes {

	@Autowired
	IcatOltsRepository catOltRepository;
	@Autowired
	IdescubrimientoService descubrimientoService;
	@Autowired
	ImonitorEjecucionRepository monitor;
	@Autowired
	IlimpiezaOntsService limpiezaOnts;
	@Autowired
	IinventarioOntsTempRepository inventarioTmp;
	@Autowired
	IinventarioOntsErroneas inventarioErroneas;

	private Integer valMaxOlts = 50;
	Utils util =new Utils();
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping("/descubrimiento")
	public GenericResponseDto getDescubrimientoOnts() throws IOException {
		String idProceso="";
		try {
			log.info("================== "+INICIO_DESC+" DESCUBRIMIENTO ====================================");
			
			inventarioTmp.deleteAll();
			inventarioErroneas.deleteAll();
			String fechaInicio = LocalDateTime.now().toString();
			List<CatOltsEntity> olts=new ArrayList<CatOltsEntity>();
			List<CompletableFuture<GenericResponseDto>> thredOlts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			
			idProceso=	monitor.save(new MonitorEjecucionEntity(INICIO_DESC+"DESCUBRIMIENTO",fechaInicio,null,INICIO)).getId();
		 	
			olts= catOltRepository.findByEstatus(1);
			log.info("Total olts primera ejecucion: "+ olts.size());
			thredOlts  = getProceso(olts,idProceso);
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			
			olts= catOltRepository.findByEstatusAndDescubrio(1,false); 
			log.info("Total ols segunda ejecucion: "+ olts.size());
			if (olts.isEmpty()){
			thredOlts  = getProceso(olts,idProceso);
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			}
			
			Optional<MonitorEjecucionEntity> monitorEnt=monitor.findById(idProceso);
			
			monitorEnt.get().setDescripcion(FINAL_EXITO+" DESCUBRIMIENTO");
			monitorEnt.get().setFecha_fin( LocalDateTime.now().toString());
			monitor.save(monitorEnt.get());
		} catch (Exception e) {
			Optional<MonitorEjecucionEntity> monitorEnt=monitor.findById(idProceso);
			monitorEnt.get().setDescripcion(EJECUCION_ERROR + e);
			monitorEnt.get().setFecha_fin( LocalDateTime.now().toString());
			monitor.save(monitorEnt.get());
			log.error(EJECUCION_ERROR, e);
			return new GenericResponseDto(EJECUCION_ERROR + e, 0);
			
		}
		
		
		limpiezaOnts.getInventarioPuertos();
		limpiezaOnts.getInventarioaux();
	
		return new GenericResponseDto(EJECUCION_EXITOSA, 0);
	}

	public List<CompletableFuture<GenericResponseDto>> getProceso ( List<CatOltsEntity> olts,String idProceso) throws IOException{
		valMaxOlts = (olts.size() /40) + 1;
			List<CompletableFuture<GenericResponseDto>> thredOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
			for (int i = 0; i < olts.size(); i += valMaxOlts) {
				Integer limMax = i + valMaxOlts;
				if (limMax >= olts.size()) {
					limMax = olts.size();
				}
				List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = descubrimientoService
						.getDescubrimiento(segmentOlts, idProceso, false);
				thredOlts.add(executeProcess);
			}
			return thredOlts;
	}

}
