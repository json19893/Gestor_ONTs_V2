package totalplay.snmp.com.negocio.service.impl;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.bson.Document;
import org.springframework.stereotype.Service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import totalplay.snmp.com.configuracion.utils.utils;
import totalplay.snmp.com.negocio.dto.responseDto;
import totalplay.snmp.com.negocio.service.IlimpiezaService;

@Service
public class limpiezaServiceImpl extends utils implements IlimpiezaService {

	@Override
	public CompletableFuture<responseDto> limpieza(String idEjecucion) throws Exception {
		responseDto response = new responseDto();

		return CompletableFuture.completedFuture(response);
	}

	@Override
	public boolean putDiferencias(String idEjecucion) {
		boolean response = true;

		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_inventario_onts_tmp");
System.out.println("------------------------INICIO DEL PROCESO DE DIFERENCIA----------------------------------------");
		collection
				.aggregate(Arrays.asList(new Document("$match", new Document("id_ejecucion", idEjecucion)),
						new Document("$group", new Document("_id", "$numero_serie")
								.append("count", new Document("$sum", 1L))
								.append("datos", new Document("$push", new Document("id_ont", "$_id")
										.append("numero_serie", "$numero_serie").append("oid", "$oid")
										.append("fecha_descubrimiento", "$fecha_descubrimiento")
										.append("id_ejecucion", "$id_ejecucion").append("id_olt", "$id_olt")))),
						new Document("$match", new Document("count", new Document("$gt", 1L))),
						new Document("$unwind", "$datos"),
						new Document("$lookup", new Document("from", "tb_poleos_metricas_onts")
								.append("let", new Document("oid", "$datos.oid").append("olt", "$datos.id_olt"))
								.append("pipeline",
										Arrays.asList(new Document("$match", new Document("$expr", new Document("$and",
												Arrays.asList(new Document("$eq", Arrays.asList("$oid", "$$oid")),
														new Document("$eq", Arrays.asList("$id_olt", "$$olt"))))))))
								.append("as", "metricas")),
						new Document(
								"$unwind",
								new Document("path", "$metricas").append("preserveNullAndEmptyArrays", true)),
						new Document("$project",
								new Document("id_ont", "$datos.id_ont").append("numero_serie", "$datos.numero_serie")
										.append("oid", "$datos.oid")
										.append("fecha_descubrimiento", "$datos.fecha_descubrimiento")
										.append("id_olt", "$datos.id_olt")
										.append("id_ejecucion", "$datos.id_ejecucion").append("estatus",
												new Document("$ifNull",
														Arrays.asList("$metricas.estatus", 0L, false)))),
						new Document("$unset", Arrays.asList("_id")),
						new Document("$out", new Document("db", "totalplayDb").append("coll", "tb_diferencias")))).forEach(doc -> System.out.println(doc.toJson()));
		System.out.println("------------------------FIN DEL PROCESO DE DIFERENCIA----------------------------------------");
		return response;
	}

	@Override
	public boolean putCambioEstatus(String idEjecucion) {
		boolean response = true;
		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_diferencias");
		System.out.println("------------------------INICIO DEL PROCESO DE DE CAMBIO DE ESTATUS DE DIFERENCIAS----------------------------------------");
		collection
				.aggregate(
						Arrays.asList(new Document("$match", 
							    new Document("$and", Arrays.asList(new Document("$or", Arrays.asList(new Document("estatus", 
							                        new Document("$eq", 0L)), 
							                        new Document("estatus", 
							                        new Document("$eq", 2L)))), 
							                new Document("id_ejecucion", idEjecucion)))), 
							    new Document("$lookup", 
							    new Document("from", "tb_inventario_onts_tmp")
							            .append("localField", "id_ont")
							            .append("foreignField", "_id")
							            .append("as", "inventario")), 
							    new Document("$unwind", "$inventario"), 
							    new Document("$project", 
							    new Document("_id", "$inventario._id")
							            .append("numero_serie", "$inventario.numero_serie")
							            .append("oid", "$inventario.oid")
							            .append("fecha_descubrimiento", "$inventario.fecha_descubrimiento")
							            .append("id_olt", "$inventario.id_olt")
							            .append("estatus", "$estatus")
							            .append("id_ejecucion", "$id_ejecucion")
							            .append("_class", "$inventario._class")), 
							    new Document("$set", 
							    new Document("estatus", 0L)), 
							    new Document("$merge", 
							    new Document("into", "tb_inventario_onts_tmp")
							            .append("on", "_id")
							            .append("whenMatched", "replace")))	
				).forEach(doc -> System.out.println(doc.toJson()));
		System.out.println("------------------------FIN DEL PROCESO DE DE CAMBIO DE ESTATUS DE DIFERENCIAS----------------------------------------");
		return response;
	}

	@Override
	public boolean putInventario(String idMonitoreoEjecucion) throws Exception {
		boolean response = true;
		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_inventario_onts_tmp");
		System.out.println("------------------------INICIO DEL PROCESO DE INVENTARIO----------------------------------------");
		collection
				.aggregate(Arrays.asList(new Document("$match", 
					    new Document("$and", Arrays.asList(new Document("estatus", 1L), 
				                new Document("id_ejecucion", idMonitoreoEjecucion)))), 
				    new Document("$unset", "_id"), 
				    new Document("$merge", 
				    new Document("into", "tb_inventario_onts_1")
				            .append("on", "numero_serie")
				            .append("whenMatched", "replace")
				            .append("whenNotMatched", "insert")))).maxAwaitTime(9000000, TimeUnit.MILLISECONDS).maxTime(9000000, TimeUnit.MILLISECONDS).forEach(doc -> System.out.println(doc.toJson()));
		System.out.println("------------------------FIN DEL PROCESO DE INVENTARIO----------------------------------------");
		
		return response;
	}

	@Override
	public boolean putCambioEstatusGeneral(String idMonitoreoEjecucion) throws Exception {
		boolean response = true;
		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_inventario_onts_tmp");
		System.out.println("------------------------INICIO DEL PROCESO DE CAMBIO DE ESTATUS GENERAL----------------------------------------");
		collection
				.aggregate(Arrays.asList(new Document("$match", new Document("id_ejecucion", idMonitoreoEjecucion)),
						new Document("$set", new Document("estatus", 0L)),
						new Document("$merge",
								new Document("into", "tb_inventario_onts_tmp").append("on", "_id")
										.append("whenMatched", "replace").append("whenNotMatched", "insert")))).forEach(doc -> System.out.println(doc.toJson()));
		System.out.println("------------------------FIN DEL PROCESO DE CAMBIO DE ESTATUS GENERAL----------------------------------------");
		return response;
	}
	
	@Override
	public boolean putHistorialTmp() throws Exception {
		boolean response = true;
		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_inventario_onts_tmp");
		System.out.println("------------------------INICIO DEL PROCESO DE RESPALDO DE EJECUCION Y BORRADO DE TMP----------------------------------------");
		collection
				.aggregate(Arrays.asList(new Document("$merge", 
					    new Document("into", "tb_inventario_onts_tmp_historial")
			            .append("on", "_id")
			            .append("whenMatched", "replace")
			            .append("whenNotMatched", "insert")))).forEach(doc -> System.out.println(doc.toJson()));
		collection.deleteMany(new Document());
		System.out.println("------------------------FIN DEL PROCESO DE RESPALDO DE EJECUCION Y BORRADO DE TMP----------------------------------------");
		return response;
	}

	@Override
	public boolean putInventarioEstatus(String idMonitoreoEjecucion) throws Exception {
		boolean response = true;
		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_inventario_onts_1");
		MongoCollection<Document> poleo = database.getCollection("tb_poleos_metricas_onts");
		System.out.println("------------------------INICIO DEL PROCESO DE CAMBIO DE ESTATUS DE INVENTARIO----------------------------------------");
		collection
				.aggregate(Arrays.asList(new Document("$match", 
					    new Document("id_ejecucion", idMonitoreoEjecucion)), 
					    new Document("$lookup", 
					    new Document("from", "tb_poleos_metricas_onts")
					            .append("let", 
					    new Document("oid", "$oid")
					                .append("id_metrica", "$id_metrica"))
					            .append("localField", "id_olt")
					            .append("foreignField", "id_olt")
					            .append("pipeline", Arrays.asList(new Document("$match", 
					                new Document("$expr", 
					                new Document("$and", Arrays.asList(new Document("$eq", Arrays.asList("$oid", "$$oid")), 
					                                new Document("$eq", Arrays.asList("$id_metrica", 1L))))))))
					            .append("as", "poleoMetrica")), 
					    new Document("$set", 
					    new Document("estatus", 
					    new Document("$arrayElemAt", Arrays.asList("$poleoMetrica.estatus", 0L)))), 
					    new Document("$unset", Arrays.asList("poleoMetrica")), 
					    new Document("$out", "tb_inventario_onts"))).forEach(doc -> System.out.println(doc.toJson()));
		collection.deleteMany(new Document());
		poleo.aggregate(Arrays.asList(new Document("$merge", 
			    new Document("into", "tb_poleos_metricas_onts_historica")
	            .append("on", "_id")
	            .append("whenMatched", "replace")
	            .append("whenNotMatched", "insert")))).forEach(doc -> System.out.println(doc.toJson()));
		poleo.deleteMany(new Document());
		System.out.println("------------------------FIN DEL PROCESO DE DE CAMBIO DE ESTATUS DE INVENTARIO----------------------------------------");
		return response;
	}

	@Override
	public boolean getHistoricoCambios() throws Exception {
		boolean response = true;
		MongoDatabase database = mongoConection();
		MongoCollection<Document> collection = database.getCollection("tb_estado_onts");
		System.out.println("------------------------INICIO DEL PROCESO DE HISTORIAL DE CAMBIOS----------------------------------------");
		collection
				.aggregate(Arrays.asList(new Document("$unionWith", 
					    new Document("coll", "tb_inventario_onts")), 
					    new Document("$group", 
					    new Document("_id", 
					    new Document("numero_serie", "$numero_serie")
					                .append("id_olt", "$id_olt"))
					            .append("datos", 
					    new Document("$push", 
					    new Document("estatus", "$estatus")
					                    .append("id_ejecucion", "$id_ejecucion")
					                    .append("oid", "$oid")
					                    .append("numero_serie", "$numero_serie")
					                    .append("fecha_descubrimiento", "$fecha_descubrimiento")))), 
					    new Document("$set", 
					    new Document("numero_serie", "$_id.numero_serie")), 
					    new Document("$set", 
					    new Document("diferencia", 
					    new Document("$let", 
					    new Document("vars", 
					    new Document("estatus1", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 0L)))
					                        .append("estatus2", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 1L))))
					                    .append("in", 
					    new Document("$cmp", Arrays.asList("$$estatus1", "$$estatus2")))))), 
					    new Document("$match", 
					    new Document("diferencia", 
					    new Document("$ne", 0L))), 
					    new Document("$project", 
					    new Document("id_ejecucion", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.id_ejecucion", 1L)))
					            .append("numero_serie", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.numero_serie", 1L)))
					            .append("id_olt", "$_id.id_olt")
					            .append("estatus", 
					    new Document("$switch", 
					    new Document("branches", Arrays.asList(new Document("case", 
					                        new Document("$eq", Arrays.asList("$diferencia", 0L)))
					                            .append("then", 
					                        new Document("$multiply", Arrays.asList(new Document("$sum", Arrays.asList(new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 0L)), 
					                                            new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 1L)))), 0L))), 
					                        new Document("case", 
					                        new Document("$eq", Arrays.asList("$diferencia", 1L)))
					                            .append("then", 
					                        new Document("$multiply", Arrays.asList(new Document("$sum", Arrays.asList(new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 0L)), 
					                                            new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 1L)))), 1L))), 
					                        new Document("case", 
					                        new Document("$eq", Arrays.asList("$diferencia", -1L)))
					                            .append("then", 
					                        new Document("$multiply", Arrays.asList(new Document("$sum", Arrays.asList(new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 0L)), 
					                                            new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 1L)))), -1L)))))))
					            .append("oid", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.oid", 1L)))
					            .append("fecha_descubrimiento", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.fecha_descubrimiento", 1L)))
					            .append("estatus_actual", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 1L)))
					            .append("estatus_anterior", 
					    new Document("$arrayElemAt", Arrays.asList("$datos.estatus", 0L)))), 
					    new Document("$unset", Arrays.asList("metricas", "_id")), 
					    new Document("$merge", 
					    new Document("into", "tb_historico_diferencias")
					            .append("on", "_id")))).forEach(doc -> System.out.println(doc.toJson()));
		MongoCollection<Document> collection2 = database.getCollection("tb_inventario_onts");
		collection2
		.aggregate(Arrays.asList(new Document("$out", "tb_estado_onts"))).forEach(doc -> System.out.println(doc.toJson()));
		System.out.println("------------------------FIN DEL PROCESO DE HISTORIAL DE CAMBIOS----------------------------------------");
		return response;
	}
	
	

}
