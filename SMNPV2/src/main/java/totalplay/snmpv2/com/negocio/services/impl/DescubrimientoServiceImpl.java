package totalplay.snmpv2.com.negocio.services.impl;

import java.io.IOException;
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
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsTmpEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
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
	Utils util=new Utils();
	@Override
	@Async("taskExecutor")
	public CompletableFuture<GenericResponseDto> getDescubrimiento(List<CatOltsEntity> olts, String idProceso,
			boolean manual) throws IOException {
		try {
			if (!manual)
				for (CatOltsEntity o : olts) {
					boolean pin=util.vaidaPin(o.getIp());
					o.setPin(pin?1:0);
					o.setEstatus(pin?1:0);
					o.setDescripcion(pin?"---":NO_PIN);
					o.setOnts_exito(!pin?0:null);
					o.setOnts_error(!pin?0:null);
					catOLtsRepository.save(o);
					if(pin)
						descubrimiento(o.getId_olt(), idProceso);
			}else {
					
			}

		} catch (Exception e) {

		}

		return null;
	}

	public boolean descubrimiento(Integer olt, String idProceso) throws IOException {
		String oid="";
		try {
			AggregationOperation match = Aggregation.match(Criteria.where("id_olt").is(olt));
			AggregationOperation lookup = Aggregation.lookup("cat_configuracion", "id_configuracion",
					"id_configuracion", "configuracion");
			Aggregation aggregation = Aggregation.newAggregation(match, lookup);
			AggregationResults<CatOltsEntity> out = mongoTemplate.aggregate(aggregation, "cat_olts",
					CatOltsEntity.class);

			configuracionDto configuracion = Utils.getConfiguracion(out.getMappedResults());
			//oid= Utils.getMetrica(configuracion.getTecnologia(), 0);
			log.info("Tecnologia:::: "+oid);
			CompletableFuture<GenericResponseDto> descubrimiento=genericMetrics.poleo(configuracion, idProceso, 0,olt ,InventarioOntsTmpEntity.class, true, "", false);
			CompletableFuture.allOf(descubrimiento);
			oid= Utils.getMetrica(configuracion.getTecnologia(), 1);
			/*CompletableFuture<GenericResponseDto> estatus=genericMetrics.poleo(configuracion, idProceso, oid, 1,olt ,estatusTemporalEntity.class);
			CompletableFuture.allOf(estatus);*/
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
    
}
