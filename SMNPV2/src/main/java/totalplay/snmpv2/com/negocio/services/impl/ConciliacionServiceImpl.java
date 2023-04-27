package totalplay.snmpv2.com.negocio.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Constantes;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.CadenasMetricasDto;
import totalplay.snmpv2.com.negocio.dto.EjecucionDto;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IasyncMethodsService;
import totalplay.snmpv2.com.negocio.services.IconnciliacionService;
import totalplay.snmpv2.com.negocio.services.IlimpiezaCadena;
import totalplay.snmpv2.com.negocio.services.IlimpiezaOntsService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.ConfiguracionMetricaEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioNCEEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsRespNCEEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IconfiguracionMetricaRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRespNCERepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ImonitorEjecucionRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoAliasRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoCpuRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDownBytesRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDownPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDropDownPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoDropUpPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoEstatusRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoFrameSlotPortRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoLastDownCauseRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoLastDownTimeRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoLastUpTimeRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoMemoryRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoProfNameRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoTimeOutRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoUpBytesRepositorio;
import totalplay.snmpv2.com.persistencia.repositorio.IpoleoUpPacketsRepositorio;
import totalplay.snmpv2.com.persistencia.vertica.entidades.BmsGestorOraVerticaEntity;
import totalplay.snmpv2.com.persistencia.vertica.entidades.CatOltsVerticaEntity;

import static totalplay.snmpv2.com.configuracion.Utils.getCurrentDateTime;

@Service
@Slf4j
public class ConciliacionServiceImpl extends Constantes implements IconnciliacionService {
	
	@Autowired
	IinventarioOntsRepository inventario;
	@Autowired
	IinventarioOntsNCERepository inventarioNCE;
	@Autowired
	ImonitorEjecucionRepository ejecucion;
	@Autowired
	IinventarioOntsRespNCERepository inventarioRespNCE;
	@Autowired
	IlimpiezaOntsService limpieza; 
	@Autowired
	IcatOltsNCERepository oltsNCE;
	@Autowired
	IcatOltsRepository olts;
	@Autowired
	IasyncMethodsService asyncMethods;
	
	
	public CompletableFuture<GenericResponseDto> updateTables() {
		/*String idEjecucion = ejecucion.findFirstByOrderByIdDesc().getId();
		System.out.println("Inicia la bùsqueda de faltantes");
		List<InventarioOntsEntity> ontsInv = inventarioNCE.getFaltantesInv(idEjecucion);
		limpieza.saveOnts(ontsInv);
		System.out.println("Inicia la bùsqueda de sobrantes");
		*/List<InventarioOntsRespNCEEntity>  ontsNCE = inventario.getSobrantesInv();
		List<InventarioOntsEntity> invSobrantes = inventario.eliminarSobrantes();
		inventarioRespNCE.insert(ontsNCE);
		deleteInv(invSobrantes);
		return null;
	}

	@Override
	public CompletableFuture<GenericResponseDto> saveIps(List<CatOltsVerticaEntity> olts) {
		
		oltsNCE.deleteAll();
		List<CatOltsNCEEntity> res = new ArrayList<CatOltsNCEEntity>();
		
		for(CatOltsVerticaEntity o: olts) {
			
			CatOltsNCEEntity aux= new CatOltsNCEEntity();
			
			aux.setIp_olt(o.getIp_olt());
			aux.setNombre_olt(o.getNombre_olt());
			res.add( aux );
			
		}
		
		oltsNCE.insert(res);
		
		getFaltantesOlts();
		
		return null;
	}
	
	private void getFaltantesOlts(){
		List<CatOltsEntity> faltantes= oltsNCE.getFaltantes();
		int idOlt =  olts.getLastIdOlt().getId_olt();
		
		
		for(CatOltsEntity o: faltantes) {
			if(o.getId_olt() == null) {
				idOlt++;
				o.setId_olt(idOlt);
			}			
		}
		
		olts.saveAll(faltantes);
	}
	
	private void deleteInv(List<InventarioOntsEntity> onts) {
		
		List<CompletableFuture<GenericResponseDto>> thredOlts = new ArrayList<CompletableFuture<GenericResponseDto>>();
		Integer MaxOnts = (onts.size() /40)+ 1;
		for(int i = 0; i < onts.size(); i += MaxOnts) {
			Integer limMax = i + MaxOnts;
			if (limMax >= onts.size()) {
				limMax = onts.size();
			}
			List<InventarioOntsEntity> segmentOlts = new ArrayList<InventarioOntsEntity>(onts.subList(i, limMax));
			CompletableFuture<GenericResponseDto> executeProcess = asyncMethods.deleteInventario(segmentOlts);
			thredOlts.add(executeProcess);
		}
		CompletableFuture.allOf(thredOlts.toArray(new CompletableFuture[thredOlts.size()])).join();
	}
	
	
}