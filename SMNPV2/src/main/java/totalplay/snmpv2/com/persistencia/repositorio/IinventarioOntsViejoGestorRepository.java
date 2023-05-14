package totalplay.snmpv2.com.persistencia.repositorio;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsViejoGestorEntity;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Meta;
import org.springframework.data.repository.query.Param;


public interface IinventarioOntsViejoGestorRepository extends MongoRepository<InventarioOntsViejoGestorEntity, String> {
	
	@Aggregation(pipeline = { 
			    "{\n"
			  + "	'$lookup':{\n"
			  + "		from: 'tb_inventario_onts',\n"
			  + "		localField:'numero_serie',\n"
			  + "		foreignField:'numero_serie',\n"
			  + "		as: 'inventario',\n"
			  + "	}\n"
			  + "}\n"
			  , "{\n"
			  + "	'$lookup':{\n"
			  + "		from: 'cat_olts',\n"
			  + "		localField:'ip_olt',\n"
			  + "		foreignField:'ip',\n"
			  + "		as: 'olt',\n"
			  + "	}\n"
			  + "}\n"
			  , "{\n"
			  + "	'$lookup':{\n"
			  + "		from: 'cat_causas_ultima_caida',\n"
			  + "		localField:'last_down_cause',\n"
			  + "		foreignField:'id_causa',\n"
			  + "		as: 'causa',\n"
			  + "	}\n"
			  + "}\n"
			  , "{\n"
			  + "	$set:{\n"
			  + "	   condicion: { $cond: [ {$eq :[ '$inventario',  []  ]}, 1, 2] },\n"
			  + "	   cause: { $cond: [ {$eq :[ '$causa',  []  ]}, 3, 4] }\n"
			  + "	}\n"
			  + "}\n"
			  , "{ $unwind:{ path:'$inventario',  preserveNullAndEmptyArrays: true} }\n"
			  , "{ $unwind:{ path:'$causa',  preserveNullAndEmptyArrays: true} }\n"
			  , "{ $unwind:{ path:'$olt'} }\n"
			  , "{$match:{$or:[{inventario:null}, {'inventario.tipo':null} ]}}\n"
			  , "{\n"
			  + "	$project: {\n"
			  + "		ont:{ $cond: [ {$eq :[ '$condicion',  1  ]}, \n"
			  + "						{\n"
			  + "							numero_serie:'$numero_serie',\n"
			  + "							id_olt:'$olt.id_olt',\n"
			  + "							id_region:'$olt.id_region',\n"
			  + "							tecnologia:'$olt.tecnologia',\n"
			  + "							frame:'$frame',\n"
			  + "							slot:'$slot',\n"
			  + "							port:'$port',\n"
			  + "							lastDownTime:'$last_down_time',\n"
			  + "							descripcionAlarma:{ $cond: [ {$eq :[ '$cause',  3  ]},  'not cause found',  '$causa.descripcion' ] },\n"
			  + "							estatus:'$estatus',\n"
			  + "							fecha_descubrimiento:{$dateSubtract:{ startDate: new Date(), unit: \"hour\", amount: 6}}\n"
			  + "						}, \n"
			  + "						'$inventario' ] }\n"
			  + "	}\n"
			  + "}\n"
			  , "{\n"
			  + "	$set:{\n"
			  + "		'ont.tipo':'E'\n"
			  + "	}\n"
			  + "	\n"
			  + "}\n"
			  , "{ $replaceRoot: { newRoot: '$ont' } }\n"
	})
	List<InventarioOntsEntity> getFaltantes();
	
	
	
}
