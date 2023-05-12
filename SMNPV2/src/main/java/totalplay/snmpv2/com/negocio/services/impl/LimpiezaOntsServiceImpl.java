package totalplay.snmpv2.com.negocio.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;

import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.LimpiezaManualDto;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;

import totalplay.snmpv2.com.negocio.services.IlimpiezaOntsService;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;

import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsPdmRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioPuertosRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorActualizacionEstatusRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorEjecucionRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorPoleoRepository;

import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IdiferenciasManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IdiferenciasRepository;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;

import totalplay.snmpv2.com.persistencia.repositorio.IinventarioAuxTransRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsAuxRepository;

import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsAuxManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsPdmEntity;

import totalplay.snmpv2.com.persistencia.entidades.MonitorEjecucionEntity;

@Slf4j
@Service
public class LimpiezaOntsServiceImpl extends Constantes implements IlimpiezaOntsService {

	@Autowired
	IcatOltsRepository catOltRepository;
	@Autowired
	IinventarioOntsRepository inventarioOnts;
	@Autowired
	IinventarioPuertosRepository inventarioPuertos;
	@Autowired
	IinventarioOntsTempRepository inventarioTmp;
	@Autowired
	IdiferenciasRepository diferencias;
	@Autowired
	IasyncMethodsService asyncMethods;
	@Autowired
	IpoleoMetricasService poleometricas;
	@Autowired
	ImonitorActualizacionEstatusRepository monitorEstatus;
	@Autowired
	IinventarioOntsAuxRepository inventarioAux;
	@Autowired
	IdiferenciasManualRepository diferenciasManual;
	@Autowired
	ImonitorPoleoRepository monitorPoleo;
	@Autowired
	IinventarioAuxTransRepository inventarioTrans;
	@Autowired
	IinventarioOntsPdmRepository inventarioPdm;
	@Autowired
	ImonitorEjecucionRepository monitorDescubrimiento;
	@Autowired
	IinventarioOntsAuxManualRepository inventarioAuxManual;
	
	@Override
	public boolean getInventarioPuertos(MonitorEjecucionEntity monitor, List<CatOltsEntity> olts) {
		
		
		try {
			if(monitor  != null)
				updateDescripcion(monitor, INICIO_DESC+"INVENTARIO PUERTOS");
			
			if(olts == null)
			  olts= catOltRepository.findByEstatus(1);
			
			List<CompletableFuture<GenericResponseDto>> thredOlts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			int ValMaxOlts = (olts.size()/40) + 1;
			
			for (int i = 0; i < olts.size(); i += ValMaxOlts) {
				Integer limMax = i + ValMaxOlts;
				
				if (limMax >= olts.size()) {
					limMax = olts.size();
				}
				List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.getFaltantes(segmentOlts);
				thredOlts.add(executeProcess);
			}
			
			CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
			if(monitor  != null)
				updateDescripcion(monitor, EJECUCION_EXITOSA+"INVENTARIO PUERTOS");
		}catch (Exception e) {
			log.error("error", e);
			if(monitor  != null)
				updateDescripcion(monitor, EJECUCION_ERROR+"INVENTARIO PUERTOS");
		}
		
		
			
		return true;
	}
	
	@Override
	public boolean getInventarioaux(MonitorEjecucionEntity monitor) {
		
		List<DiferenciasManualEntity> diferenciaManuales;
		List<InventarioOntsAuxEntity> diferenciaFinales;
		String idEjecucion;
		try {
			//Marcar con cero las duplicadas de la tabla de temporales
			 try {
				 updateDescripcion(monitor, INICIO_DESC+" DETECIÒN DUPLICADOS");
				 inventarioTmp.findDuplicadas();
			 }catch (Exception e) {
				log.info("Falló la detecciò de duplicados "+e);
			 }
			 
			 //Enviar a la tabla de diferencias las olts duplicadas
			 log.info(":::::::::::::::::::::::::: enviar a duplicados  :::::::::::::::::::::::");
			 updateDescripcion(monitor, INICIO_DESC+" AISLAR DUPLICADOS");
			 idEjecucion =  monitorEstatus.getLastFinishId().getId();//findFirstByOrderByIdDesc().getId();
			 poleometricas.getOntsFaltantes(1,idEjecucion, false, false, "auxiliar",2, null);
			 inventarioTmp.sendTbDiferencias();
			
			//Migrar los datos de inventariotmp a aux
			 log.info(":::::::::::::::::::::::::: enviar a auxiliar  :::::::::::::::::::::::");
			 updateDescripcion(monitor, INICIO_DESC+" MIGRACIÒN AUXILIAR");
			 inventarioAux.deleteAll();
			 try {
				 inventarioTmp.sendToAux();		
			 }catch(Exception e) {
				log.error("error", e); 
			 }
			 
			//Obtener las difencias que van al inventario auxiliar
//NO	 //
//			 log.info(":::::::::::::::::::::::::: get diferecias  :::::::::::::::::::::::");
//			 updateDescripcion(monitor, INICIO_DESC+" OBTENER DIFERENCIAS DEFINITIVAS");
//			 diferenciaFinales =  diferencias.findDiferencias();
//			 inventarioAux.saveAll(diferenciaFinales);
			
			
			
			//Obtener las onts que se van al inventario de carga manual
			 log.info(":::::::::::::::::::::::::: get carga manual  :::::::::::::::::::::::");
			 updateDescripcion(monitor, INICIO_DESC+" OBTENER DIFERENCIAS MANUALES");
			diferenciasManual.deleteAll();	  
			diferenciaManuales = diferencias.getAllDiferencias();//diferencias.findDiferenciasManual();
			diferenciasManual.saveAll(diferenciaManuales);
			
				
			 //hacer el cruce de estatus
			String idPoleo =  monitorPoleo.getLastFinishId().getId();
			updateDescripcion(monitor, INICIO_DESC+" CRUCES  MÈTRICAS");
			crucesMetricas(1,idEjecucion, "auxiliar_descubrimiento", null, false );
			crucesMetricas(2,idPoleo, "auxiliar_descubrimiento", null, false  );
			crucesMetricas(4,idPoleo, "auxiliar_descubrimiento", null, false );
			crucesMetricas(14,idPoleo, "auxiliar_descubrimiento", null, false  );
			crucesMetricas(16,idPoleo, "auxiliar_descubrimiento", null, false );
			
			//Obtener los faltantes de inventario
			updateDescripcion(monitor, INICIO_DESC+" OBTENER  FALTANTES");
			getInventario();
			
			//Econtar los oids repetidos (ver la posibilidad de aislarlos)
			updateDescripcion(monitor, INICIO_DESC+" QUITAR OIDS REPETIDOS");
			cleanOidsRepetidos();
			
			 updateDescripcion(monitor, INICIO_DESC+" OBTENER VIPS");
			 getEmpresarialesVips(false);
			 updateDescripcion(monitor, INICIO_DESC+" OBTENER PDM");
			 deleteInventarioPdm();
			
			//Respaldar inventario
			try {
				log.info(":::::::::::::::::::::::::: repaldar inventario  :::::::::::::::::::::::");
				updateDescripcion(monitor, INICIO_DESC+" RESPALDAR  PDM");
				inventarioOnts.sentToResp();
				
			}catch (Exception e) {
				log.error("Falló el respaldo del inventario "+e);
			}
			
			//Cambiar el inventario por inventario auxliar
			try {
				log.info(":::::::::::::::::::::::::: actualizar inventario  :::::::::::::::::::::::");
				updateDescripcion(monitor, INICIO_DESC+" ACTUALIZAR INVENTARIO");
				inventarioAux.sendToInventario();
				
			}catch (Exception e) {
				log.error("Falló la actualización del inventario "+e);
			}
			 
		} catch (Exception e) {
			log.info(e.toString());
		}
		
		
		return true;
	}
	
	@Override
	public void updateDescripcion(MonitorEjecucionEntity monitor, String descripcion) {		
				monitor.setDescripcion(descripcion);
				monitorDescubrimiento.save(monitor);
	}
	
	@Override
	public void LimpiezaManual(List<CatOltsEntity> olts, MonitorEjecucionEntity monitor ) {	
		
		List<LimpiezaManualDto> limpieza;
		try {
			inventarioAuxManual.deleteAll();
			
			getInventarioPuertos(monitor, olts);
			
			updateDescripcion(monitor, INICIO_DESC+" OBTENCIÓN INFO TABLAS");
			log.info(INICIO_DESC+" OBTENCIÓN INFO TABLAS");
			limpieza =  inventarioTmp.getOntsInventarios();
			
			if(limpieza.size()>0) {
				inventarioOnts.deleteAll(limpieza.get(0).getEliminar());
				diferencias.saveAll(limpieza.get(0).getDuplicados());
				diferenciasManual.saveAll(limpieza.get(0).getManual());
				inventarioAuxManual.saveAll(limpieza.get(0).getInventarioAux());
			}
			
			
			String idPoleo =  monitorPoleo.getLastFinishId().getId();
			
			updateDescripcion(monitor, INICIO_DESC+" CRECE MÉTRICAS");
			log.info(INICIO_DESC+" CRECE MÉTRICAS");
			
			crucesMetricas(2,idPoleo, "auxiliar_descubrimiento_manual", olts, true );
			crucesMetricas(4,idPoleo, "auxiliar_descubrimiento_manual", olts, true );
			crucesMetricas(14,idPoleo, "auxiliar_descubrimiento_manual", olts, true );
			crucesMetricas(16,idPoleo, "auxiliar_descubrimiento_manual", olts, true );
			
			getEmpresarialesVips(true);
			
			updateDescripcion(monitor, INICIO_DESC+" SEND INVENTARIO");
			log.info(INICIO_DESC+" SEND INVENTARIO");
			
			
			saveOnts(inventarioAuxManual.getInv());
			log.info("FIN");
		}catch (Exception e) {
			log.error(e.toString());
		}
	}
	
	private void crucesMetricas(int metrica, String idPoleo, String tabla, List<CatOltsEntity> olts, boolean manual ) {
		
		log.info(":::::::::::::::::::::::::: cruce metrica " +metrica+"  :::::::::::::::::::::::");
		if(olts == null)
			olts= catOltRepository.findByEstatus(1);
		
		poleometricas.getOntsFaltantes(metrica, idPoleo, false, false, tabla, 2, null);
		
		inventarioTrans.deleteAll();
		
		List<CompletableFuture<GenericResponseDto>> thredOlts=new ArrayList<CompletableFuture<GenericResponseDto>>();
		int ValMaxOlts = (olts.size()/40) + 1;
		
		for (int i = 0; i < olts.size(); i += ValMaxOlts) {
			Integer limMax = i + ValMaxOlts;
			
			if (limMax >= olts.size()) {
				limMax = olts.size();
			}
			List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
			CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.getMetrica(segmentOlts,metrica, manual);
			
			thredOlts.add(executeProcess);
		}
		
		CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
		try {
			if(manual)
				inventarioTrans.outToInvAuxManual();
			else 
				inventarioTrans.outToInvAux();
		
		}catch (Exception e) {
			log.error(e.toString());
		}
		inventarioTrans.deleteAll();
		
	}
	
	
	private void getInventario() {
		
		log.info(":::::::::::::::::::::::::: Completar inventario "+ ":::::::::::::::::::::::");
		
		List<CatOltsEntity> olts= inventarioOnts.getOlts();
		
		List<CompletableFuture<GenericResponseDto>> thredOlts=new ArrayList<CompletableFuture<GenericResponseDto>>();
		int ValMaxOlts = (olts.size()/40) + 1;
		
		for (int i = 0; i < olts.size(); i += ValMaxOlts) {
			Integer limMax = i + ValMaxOlts;
			
			if (limMax >= olts.size()) {
				limMax = olts.size();
			}
			List<CatOltsEntity> segmentOlts = new ArrayList<CatOltsEntity>(olts.subList(i, limMax));
			CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.getFaltatesInv(segmentOlts);
			
			thredOlts.add(executeProcess);
		}
		
		CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
		
	}
	
	private void cleanOidsRepetidos() {
		log.info(":::::::::::::::::::::::::: Limpiar oids repetidos "+ ":::::::::::::::::::::::");
		
		try {
			List<InventarioOntsAuxEntity> repetidos = inventarioAux.getOidsRepetidos();
			inventarioAux.saveAll(repetidos);
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
	}
	
	private void getEmpresarialesVips(boolean manual) {
		log.info(":::::::::::::::::::::::::: Obtener onts empresariales "+ ":::::::::::::::::::::::");
		
		try {
			List repetidos = null;
			if (manual)
				repetidos = inventarioOnts.getEmpresarialesVipsManuales();
			else
				repetidos = inventarioOnts.findEmpresarialesAndVips();
			
			
			List<CompletableFuture<GenericResponseDto>> thredOnts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			int ValMaxOlts = (repetidos.size()/40) + 1;
			
			for (int i = 0; i < repetidos.size(); i += ValMaxOlts) {
				Integer limMax = i + ValMaxOlts;
				
				if (limMax >= repetidos.size()) {
					limMax = repetidos.size();
				}
				List segmentOlts;
				if(manual)
					 segmentOlts = new ArrayList<InventarioOntsAuxManualEntity>(repetidos.subList(i, limMax));
				else
					 segmentOlts = new ArrayList<InventarioOntsAuxEntity>(repetidos.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.saveEmpresariales(segmentOlts, manual);
				
				thredOnts.add(executeProcess);
			}
			
			CompletableFuture.allOf(thredOnts.toArray(new CompletableFuture[thredOnts.size()])).join();
			
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
	}
	
	@Override
	public void deleteInventarioPdm() {
		log.info(":::::::::::::::::::::::::: Eliminar inventario de PDM "+ ":::::::::::::::::::::::");
		
		try {
			List<InventarioOntsPdmEntity> inventario = inventarioPdm.getInventario();
			
			
			List<CompletableFuture<GenericResponseDto>> thredOnts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			int ValMaxOlts = (inventario.size()/40) + 1;
			
			for (int i = 0; i < inventario.size(); i += ValMaxOlts) {
				Integer limMax = i + ValMaxOlts;
				
				if (limMax >= inventario.size()) {
					limMax = inventario.size();
				}
				List<InventarioOntsPdmEntity> segmentOlts = new ArrayList<InventarioOntsPdmEntity>(inventario.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.deletePdm(segmentOlts);
				
				thredOnts.add(executeProcess);
			}
			
			CompletableFuture.allOf(thredOnts.toArray(new CompletableFuture[thredOnts.size()])).join();
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
	}
	@Override
	public void saveOnts(List<InventarioOntsEntity> inventario) {
		log.info(":::::::::::::::::::::::::: Inserta inventario  "+ ":::::::::::::::::::::::");
		
		try {
			
			
			
			List<CompletableFuture<GenericResponseDto>> thredOnts=new ArrayList<CompletableFuture<GenericResponseDto>>();
			int ValMaxOlts = (inventario.size()/40) + 1;
			
			for (int i = 0; i < inventario.size(); i += ValMaxOlts) {
				Integer limMax = i + ValMaxOlts;
				
				if (limMax >= inventario.size()) {
					limMax = inventario.size();
				}
				List<InventarioOntsEntity> segmentOlts = new ArrayList<InventarioOntsEntity>(inventario.subList(i, limMax));
				CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.saveInventario(segmentOlts);
				
				thredOnts.add(executeProcess);
			}
			
			CompletableFuture.allOf(thredOnts.toArray(new CompletableFuture[thredOnts.size()])).join();
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
	}
	
}
