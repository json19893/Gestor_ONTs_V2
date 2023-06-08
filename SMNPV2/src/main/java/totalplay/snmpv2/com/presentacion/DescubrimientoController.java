package totalplay.snmpv2.com.presentacion;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import totalplay.snmpv2.com.helper.EncryptorHelper;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.DescubrimientoManualDto;
import totalplay.snmpv2.com.negocio.dto.DescubrimientoNCEUsuariosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IUpdateTotalOntsService;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IdescubrimientoService;
import totalplay.snmpv2.com.negocio.services.IlimpiezaOntsService;
import totalplay.snmpv2.com.persistencia.entidades.BitacoraEventosEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IBitacoraEventosRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IUsuariosPermitidosRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsDescubrimientoNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsErroneas;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorEjecucionRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.ItblDescubrimientoManualRepositorio;
import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;
import totalplay.snmpv2.com.persistencia.entidades.UsuariosPermitidosEntidad;
import totalplay.snmpv2.com.persistencia.entidades.monitorPoleoNCEEntidad;
@Slf4j
@RestController
//@RequestMapping(path = "/snmpv2")
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

	@Autowired
	IBitacoraEventosRepository ibitacoraEventos;
	@Autowired
	ItblDescubrimientoManualRepositorio descubrimientoManual;
	@Autowired
    IUpdateTotalOntsService updateTotales;
	@Autowired
	IasyncMethodsService asyncMethods;
	@Autowired
	IinventarioOntsTempNCERepository tempNCE; 
	@Autowired
	ImonitorPoleoNCERepository monitorNCE;
	@Autowired
	IUsuariosPermitidosRepositorio usuariosPermitidos;
	@Autowired
	IinventarioOntsDescubrimientoNCERepository inventarioDesNCE;
	
	
	
	
	private Integer valMaxOlts = 50;
	String idProceso="";
	Utils util =new Utils();
	@Value("${ruta.archivo.txt}")
	private String ruta;
	@Value("${ruta.archivo.nce}")
	private String ruta2;
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	//@Scheduled(cron = "0 2 0 * * *", zone = "CST")
	@GetMapping("/descubrimiento")
	public GenericResponseDto getDescubrimientoOnts() throws IOException {
		String idProceso="";
		MonitorEjecucionEntity monitorDescubrimiento;

		try {
			log.info("================== "+INICIO_DESC+" DESCUBRIMIENTO ====================================");
			inventarioTmp.deleteAll();
			inventarioErroneas.deleteAll();
			Date fechaInicio = util.getDate();
			List<CatOltsEntity> olts=new ArrayList<CatOltsEntity>();
			List<CompletableFuture<GenericResponseDto>> thredOlts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			monitorDescubrimiento = monitor.save(new MonitorEjecucionEntity(INICIO_DESC+"DESCUBRIMIENTO",fechaInicio,null,INICIO));
			idProceso = monitorDescubrimiento.getId();
			olts= catOltRepository.findByEstatus(1);
			log.info("Total olts primera ejecucion: "+ olts.size());
			thredOlts  = getProceso(olts,idProceso,false,"System");
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			olts= catOltRepository.findByEstatusAndDescubrio(1,false); 
			log.info("Total ols segunda ejecucion: "+ olts.size());
			if (olts.isEmpty()){
			thredOlts  = getProceso(olts,idProceso,false,"System");
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			}

			limpiezaOnts.updateDescripcion(monitorDescubrimiento, INICIO_DESC+"LIMPIEZA");
			limpiezaOnts.getInventarioPuertos(monitorDescubrimiento, null);
			limpiezaOnts.getInventarioaux(monitorDescubrimiento);
			updateTotales.updateTotalOntsFromOlts();
			monitorDescubrimiento.setDescripcion(FINAL_EXITO+" DESCUBRIMIENTO & LIMPIEZA");
			monitorDescubrimiento.setFecha_fin( util.getDate());
			monitor.save(monitorDescubrimiento);
			
		} catch (Exception e) {
			Optional<MonitorEjecucionEntity> monitorEnt=monitor.findById(idProceso);
			monitorEnt.get().setDescripcion(EJECUCION_ERROR + e);
			monitorEnt.get().setFecha_fin( util.getDate());
			monitor.save(monitorEnt.get());
			log.error(EJECUCION_ERROR, e);
			return new GenericResponseDto(EJECUCION_ERROR + e, 1);
			
		}	
		return new GenericResponseDto(EJECUCION_EXITOSA, 0);

	}

	public List<CompletableFuture<GenericResponseDto>> getProceso ( List<CatOltsEntity> olts,String idProceso,Boolean manual,String usuario) throws IOException{
			valMaxOlts = (olts.size() /40) + 1;
			List<CompletableFuture<GenericResponseDto>> thredOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
			for (int i = 0; i < olts.size(); i += valMaxOlts) {
				Integer limMax = i + valMaxOlts;
				if (limMax >= olts.size()) {
					limMax = olts.size();
				}
				List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = descubrimientoService
						.getDescubrimiento(segmentOlts, idProceso, manual,usuario,false);
				thredOlts.add(executeProcess);
			}
			return thredOlts;
	}
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@PostMapping("/descubrimientoManual")
	public GenericResponseDto descubrimientoManual(@RequestBody DescubrimientoManualDto datos) throws Exception {

		try {
			ibitacoraEventos.save(new BitacoraEventosEntity(LocalDateTime.now().toString(),DES_MANUAL,datos.getUsuario(),DESC_EVENTO_MANUAL + datos.getOlts()));
			long proc = descubrimientoManual.countByEstatus(0);
			if (proc > 0) {
				return new GenericResponseDto(PROCESANDO, 1);
			}
			
			MonitorEjecucionEntity desc=monitor.findFirstByOrderByIdDesc();
			if(desc.getFecha_fin()==null){
				return new GenericResponseDto(PROCESANDO, 1);
			}
			inventarioTmp.deleteAll();
			File file = new File(ruta);
			if(file.exists()){
				file.delete();
			}
			List<CompletableFuture<GenericResponseDto>> thredOlts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			List<CatOltsEntity> olts=catOltRepository.getOltsByIp(datos.getOlts());
		
			thredOlts  = getProceso(olts,idProceso,true,datos.getUsuario());
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			//Limpieza de datos para inventario final
			if(inventarioTmp.count()>0) {
				limpiezaOnts.LimpiezaManual(olts, desc);
			}			
			updateTotales.updateTotalOntsFromOlts();
		} catch (Exception e) {
			return new GenericResponseDto(EJECUCION_ERROR, 1);
		}
		return new GenericResponseDto(EJECUCION_EXITOSA, 0);
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping("/descubrimientoNCE/{idOlt}/{user}")
	public GenericResponseDto descubrimientoNCE(@PathVariable("idOlt") Integer idOlt, @PathVariable("user") String user) throws Exception {
		monitorPoleoNCEEntidad monitor=null;
		try {
			
			File file = new File(ruta2);
			
			if(file.exists())
				file.delete();
						
			util.crearArchivos(ruta2, util.prefixLog("Inicia el proceso de descubrimiento."));
			
			monitor= monitorNCE.save(new monitorPoleoNCEEntidad(util.getDate(),"POLEO DE NCE, OLT: "+ idOlt,INICIO, idOlt));
			//1. TODO: La limpieza de tabla se realizarà a media noche
			tempNCE.deleteAll();
			//2. TODOSe realizarà la limpieza de regstros por usuario en la noche
			//3. TODO: En un areglo guardas los id del proceso de ejecucion en la tabla de usuarios 
			
			//Encontrar el registro del usuario
			UsuariosPermitidosEntidad usuario =  usuariosPermitidos.getUsuario(user);
			//UsuariosPermitidosEntidad usuario = usuarios.get(1);
			
			
			//buscar si ya habìa realizado un descubrimiento sobre esa olt
			List<DescubrimientoNCEUsuariosDto> descubrimientos = usuario.getDescubrimientos();
			
			
			
			//seter los valores del descubrimiento
			DescubrimientoNCEUsuariosDto descubrimiento = new  DescubrimientoNCEUsuariosDto();
			descubrimiento.setId_ejecucion(monitor.getId());
			descubrimiento.setOlt(idOlt);
			
			Integer index = descubrimientos.indexOf(descubrimiento);
			
			
			if( index != -1) {
				
				try {
					inventarioDesNCE.deleteEjecucion(descubrimientos.get(index).getId_ejecucion());					
				}catch (Exception e) {
					// TODO: handle exception
				}
				
				descubrimientos.remove(index.intValue());
			}
			
			descubrimientos.add(descubrimiento);
			
			usuario.setDescubrimientos(descubrimientos);
			
			usuariosPermitidos.save(usuario);
			
			
			System.out.println(usuario.getNombreCompleto());
			
			
			
			List<CatOltsEntity> olts = new ArrayList<CatOltsEntity>();
			CatOltsEntity olt=catOltRepository.getOlt(idOlt);
			olts.add(olt);
			
			valMaxOlts = (olts.size() /1);///40) + 1;
			List<CompletableFuture<GenericResponseDto>> thredOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
			for (int i = 0; i < olts.size(); i += valMaxOlts) {
				Integer limMax = i + valMaxOlts;
				if (limMax >= olts.size()) {
					limMax = olts.size();
				}
				List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = descubrimientoService
						.getDescubrimiento(segmentOlts, monitor.getId(), false,"", true);
				thredOlts.add(executeProcess);
			}
			
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			
			//hacer le cruce de las mètricas
			limpiezaOnts.LimpiezaNCE(olts);
			//Enviar el descubrimiento a una tabla final
			try {
				tempNCE.outToInv();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			monitor.setFecha_fin(util.getDate());
			
			monitorNCE.save(monitor);
		}catch (Exception e) {
			if(monitor != null)
				monitor.setFecha_fin(util.getDate());
				monitor.setDescripcion("POLEO DE NCE, OLT: "+ idOlt);
				monitorNCE.save(monitor);
			
			return  new GenericResponseDto(EJECUCION_ERROR, 1);
		}
		
		if(monitor != null) {
			return new GenericResponseDto(monitor.getId(), 0);
		}
		
		return new GenericResponseDto(EJECUCION_ERROR, 1);
		
	}
	
	
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping("/updateConfiguration")
	public GenericResponseDto updateConfiguration() throws Exception {

		try {			
			List<CatOltsEntity> olts=new ArrayList<CatOltsEntity>();
			olts= catOltRepository.findAll();
			
			Integer MaxOlts = (olts.size() /40) + 1;
			List<CompletableFuture<GenericResponseDto>> thredOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
			
			for (int i = 0; i < olts.size(); i += MaxOlts) {
				Integer limMax = i + MaxOlts;
				if (limMax >= olts.size()) {
					limMax = olts.size();
				}
				List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.putConfiguracion(segmentOlts);
				thredOlts.add(executeProcess);
			}
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			
		} catch (Exception e) {
			return  new GenericResponseDto(EJECUCION_ERROR, 1);
		}
		return new GenericResponseDto(EJECUCION_EXITOSA, 0);
	}

	
	@GetMapping("/auxiliar")
	public String auxiliar() throws Exception {
		EncryptorHelper encryptorHelper = EncryptorHelper.getINSTANCE();
	    String uriDesencripter = encryptorHelper.encryptString("mongodb://superAdmin:pass1234@10.180.199.76:27017/");
		return uriDesencripter;
	}

	
	
	
	
}
