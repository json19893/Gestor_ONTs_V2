package totalplay.monitor.snmp.com.negocio.service.impl;

import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import totalplay.monitor.snmp.com.negocio.service.IDiferenciaCargaManualService;
import totalplay.monitor.snmp.com.negocio.service.IinsertaOntsService;
import totalplay.monitor.snmp.com.persistencia.entidad.DiferenciasEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.DiferenciasManualEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.InventarioOntsDescubrimientoNCEEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsPdmEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IdiferenciasManualRepository;
import totalplay.monitor.snmp.com.persistencia.repository.IdiferenciasRepository;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsDescubrimientoNCERepository;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsPdmRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsTempNCERepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Esta clase contiene funciones para consultar las olts...
 * Condicion: Una ont no puede existir en varias olts al mismo tiempo.
 */
@Service
@Slf4j
public class InsertaOntsServiceImpl implements IinsertaOntsService {
	@Autowired
	IinventarioOntsPdmRepositorio inventarioPdm;
	@Autowired
	IinventarioOntsTempNCERepository tempNCE;
	@Autowired
	IinventarioOntsRepositorio inventarioOnts;
	@Autowired
	IdiferenciasRepository diferencias;
	@Autowired
    IinventarioOntsDescubrimientoNCERepository inventarioDescNCE;
	
	@Override
	public String insertInventario(String serie, String tipo, String ejecucion) {
		//TODO: Buscar en inventario si existe una ont con la serie
		try{
			inventarioOntsEntidad ontInv =  inventarioOnts.getOntBySerialNumber(serie);
			//inventarioOntsEntidad ontNCE =  tempNCE.finOntSerie(serie) ;			
			inventarioOntsEntidad ontNCE = inventarioDescNCE.findOnt(serie, ejecucion);
			inventarioOntsPdmEntidad ontPDM =  inventarioPdm.finOntSerie(serie);
			
			if(ontNCE == null ) return "No hay una ont Aceptada para inyectar en inventario";
			
			ontNCE.setTipo(tipo);
			
			
			if(ontInv != null) {
				//TODO: Si existe la ont con la misma serie validar si tieenen diferencte id_olt
				//TODO: Si tienen diferente olt, validar si està en duplicados, sino ingresar ambas,
				//borrar de inventario la existente y enviar ambas a carga manual
				
				if(ontInv.getId_olt() == ontNCE.getId_olt()) {
					ontNCE.setVip(ontNCE.getVip());
					inventarioOnts.delete(ontInv);
					inventarioOnts.save(ontNCE);
					
					if(ontPDM != null )
						inventarioPdm.delete(ontPDM);
				}else {
					DiferenciasEntity dif = diferencias.getOntBySerialNumber(serie);
					if(dif != null ) {
						//seterar los campos en diferencia y en carga manual
						DiferenciasEntity auxDif = new  DiferenciasEntity();
						DiferenciasManualEntity auxManual = new DiferenciasManualEntity();
						
									
						auxDif.setOid(ontNCE.getOid());
						auxDif.setUid(ontNCE.getUid());
						auxDif.setId_olt(ontNCE.getId_olt());
						auxDif.setId_metrica(ontNCE.getId_metrica());
						auxDif.setId_metrica(ontNCE.getId_metrica());
						auxDif.setFecha_descubrimiento(ontNCE.getFecha_descubrimiento());
						auxDif.setEstatus(ontNCE.getEstatus());
						auxDif.setId_ejecucion(ontNCE.getId_ejecucion());
						auxDif.setId_region(ontNCE.getId_region());
						auxDif.setId_puerto(ontNCE.getId_puerto());
						auxDif.setNumero_serie(ontNCE.getNumero_serie());
						auxDif.setTecnologia(ontNCE.getTecnologia());
						auxDif.setIndex(ontNCE.getIndex());
						auxDif.setIndexFSP(ontNCE.getIndexFSP());
						
						/*auxManual.setOid(ontNCE.getOid());
						auxManual.setUid(ontNCE.getUid());
						auxManual.setId_olt(ontNCE.getId_olt());
						auxManual.setId_metrica(ontNCE.getId_metrica());
						auxManual.setId_metrica(ontNCE.getId_metrica());
						auxManual.setFecha_descubrimiento(ontNCE.getFecha_descubrimiento());
						auxManual.setEstatus(ontNCE.getEstatus());
						auxManual.setId_ejecucion(ontNCE.getId_ejecucion());
						auxManual.setId_region(ontNCE.getId_region());
						auxManual.setId_puerto(ontNCE.getId_puerto());
						auxManual.setNumero_serie(ontNCE.getNumero_serie());
						auxManual.setTecnologia(ontNCE.getTecnologia());
						auxManual.setIndex(ontNCE.getIndex());
						auxManual.setIndexFSP(ontNCE.getIndexFSP());
						*/
						diferencias.save(auxDif);
						//diferenciasManual.save(auxManual);
						inventarioOnts.delete(ontInv);
						inventarioOnts.save(ontNCE);
						if(ontPDM != null )
							inventarioPdm.delete(ontPDM);						
						
						return "Se gurdó el registro en carga manual debido a que hay repetidos";
						
					}
					
				}
			
				
			}else {	
				inventarioOnts.save(ontNCE);
				if(ontPDM != null )
					inventarioPdm.delete(ontPDM);
				return "Se gurdó el registro en inventario Final";
			}
		}catch (Exception e) {
			return "Problemas al guardar el registro";
		}
		
		return "Se guardò correctamente el registro";
	}

}
