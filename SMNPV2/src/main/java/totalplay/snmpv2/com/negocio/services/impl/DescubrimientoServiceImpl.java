package totalplay.snmpv2.com.negocio.services.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import totalplay.snmpv2.com.configuracion.Utils;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.configuracionDto;
import totalplay.snmpv2.com.negocio.services.IGenericMetrics;
import totalplay.snmpv2.com.negocio.services.IdescubrimientoService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.EstatusPoleoManualEntidad;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpNCEEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsTempRepository;
import totalplay.snmpv2.com.persistencia.repositorio.ItblDescubrimientoManualRepositorio;
import totalplay.snmpv2.com.configuracion.Constantes;
@Service
@Slf4j
public class DescubrimientoServiceImpl extends Constantes implements IdescubrimientoService {
    @Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	IGenericMetrics genericMetrics;
	@Autowired 
	IcatOltsRepository catOLtsRepository;
	@Autowired
	ItblDescubrimientoManualRepositorio descubrimientoManual;
	@Autowired
	IinventarioOntsTempRepository inventarioTmp;
	Utils util=new Utils();
	@Override
	@Async("taskExecutor")
	public CompletableFuture<GenericResponseDto> getDescubrimiento(List<CatOltsEntity> olts, String idProceso,
			boolean manual,String usuario, boolean nce) throws IOException {
		try {
	
				for (CatOltsEntity o : olts) {
					boolean pin=util.vaidaPin(o.getIp());
					o.setPin(pin?1:0);
					o.setEstatus(pin?1:0);
					o.setDescripcion(pin?"---":NO_PIN);
					o.setOnts_exito(!pin?0:null);
					o.setOnts_error(!pin?0:null);
					catOLtsRepository.save(o);
					if(pin)
						descubrimiento(o.getId_olt(), idProceso,manual,usuario, nce);
			}

		} catch (Exception e) {
			log.error("error", e);
			return CompletableFuture.completedFuture(new GenericResponseDto(EJECUCION_ERROR, 1));
		}

		return CompletableFuture.completedFuture(new GenericResponseDto(EJECUCION_EXITOSA, 0));
	}

	public boolean descubrimiento(Integer olt, String idProceso,boolean manual,String usuario, boolean nce) throws IOException {
		String oid="";
		String idDescubrimientoManual="";
		EstatusPoleoManualEntidad registro= new EstatusPoleoManualEntidad();
		CompletableFuture<GenericResponseDto> descubrimiento=null;
		
		try {
			AggregationOperation match = Aggregation.match(Criteria.where("id_olt").is(olt));
			AggregationOperation lookup = Aggregation.lookup("cat_configuracion", "id_configuracion",
					"id_configuracion", "configuracion");
			Aggregation aggregation = Aggregation.newAggregation(match, lookup);
			AggregationResults<CatOltsEntity> out = mongoTemplate.aggregate(aggregation, "cat_olts",
					CatOltsEntity.class);

			configuracionDto configuracion = Utils.getConfiguracion(out.getMappedResults());
			//oid= Utils.getMetrica(configuracion.getTecnologia(), 0);
			
			if(manual){
				idDescubrimientoManual=	descubrimientoManual.save(new EstatusPoleoManualEntidad(configuracion.getIdOlt(),configuracion.getIp(),
				configuracion.getNombreOlt(),LocalDate.now().toString(),0,INICIO_PROCESO_MANUAL,null,usuario)).getId();
				registro=descubrimientoManual.findByid(idDescubrimientoManual);
			}
			
			if(nce)
				 descubrimiento=genericMetrics.poleo(configuracion, idProceso, 0,olt ,InventarioOntsTmpNCEEntity.class, true, "", false,manual, nce,false);
			else
				descubrimiento=genericMetrics.poleo(configuracion, idProceso, 0,olt ,InventarioOntsTmpEntity.class, true, "", false,manual, nce,false);
			
			CompletableFuture.allOf(descubrimiento);
			if(manual){
				if(descubrimiento.get().getCod()==0){
					registro.setDescripcion(FIN_EXITO_PROCESO_MANUAL);
						registro.setOnts(inventarioTmp.finOnts(configuracion.getIdOlt()));
						registro.setEstatus(1);
						descubrimientoManual.save(registro);
				}else{
						registro.setDescripcion(FIN_ERROR_PROCESO_MANUAL);
						registro.setEstatus(2);
						descubrimientoManual.save(registro);
				}
			}
			//oid= Utils.getMetrica(configuracion.getTecnologia(), 1);
			/*CompletableFuture<GenericResponseDto> estatus=genericMetrics.poleo(configuracion, idProceso, oid, 1,olt ,estatusTemporalEntity.class);
			CompletableFuture.allOf(estatus);*/
		} catch (Exception e) {
			log.error("error", e);
			if(manual){
				registro.setDescripcion(FIN_ERROR_PROCESO_MANUAL+" "+e);
				registro.setEstatus(2);
			}
			return false;
		}
		return true;
	}
	
	
	
	
    
}
