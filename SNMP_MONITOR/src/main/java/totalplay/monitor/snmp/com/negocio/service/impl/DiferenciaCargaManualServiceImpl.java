package totalplay.monitor.snmp.com.negocio.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.OntsRepetidasPorOltPostRequest;
import totalplay.monitor.snmp.com.negocio.service.IDiferenciaCargaManualService;
import totalplay.monitor.snmp.com.persistencia.entidad.DiferenciasManualEntity;
import totalplay.monitor.snmp.com.persistencia.entidad.catOltsEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IcatOltsRepositorio;
import totalplay.monitor.snmp.com.persistencia.repository.IdiferenciasManualRepository;


import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase contiene funciones para consultar las olts...
 * Condicion: Una ont no puede existir en varias olts al mismo tiempo.
 */
@Service
public class DiferenciaCargaManualServiceImpl implements IDiferenciaCargaManualService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    IdiferenciasManualRepository diferenciasManualRepository;
    @Autowired
    IcatOltsRepositorio catOltsRepository;

    /**
     * Este metodo devuelve la coleccion de documentos. Este documento realizara una comparativa para consultar las onts repetidas por el id olt.
     *
     * @return
     */
    @Override
    public List<AuxOntsAdapter> consultarCatalogoOntsRepetidas(final OntsRepetidasPorOltPostRequest request) {
        List<DiferenciasManualEntity> lista = obtenerOntsOlt(request.getIdOlt());
        List<AuxOntsAdapter> adapterOntsList = adapterList(lista);
        List<AuxOntsAdapter> repetidas = consultarOntsRepetidas(adapterOntsList);
        return repetidas;
    }

    List<AuxOntsAdapter> adapterList(List<DiferenciasManualEntity> ontList) {

        List<AuxOntsAdapter> auxiliarList = new ArrayList<>();
        AuxOntsAdapter tmpOnt;

        for (DiferenciasManualEntity ont : ontList) {
            tmpOnt = new AuxOntsAdapter();
            //Atributos del GenericPoleosDto
            tmpOnt.setOid(ont.getOid());
            tmpOnt.setNumero_serie(ont.getNumero_serie());
            tmpOnt.setUid(ont.getUid());
            tmpOnt.setTecnologia(ont.getTecnologia());
            tmpOnt.setId_olt(ont.getId_olt());
            //tmpOnt.setId_metrica(ont.getId_metrica());
            //tmpOnt.setFecha_poleo(ont.getFecha_poleo());
            tmpOnt.setId_region(ont.getId_region());
            //tmpOnt.setFecha_modificacion(ont.getFecha_modificacion());
            tmpOnt.setFrame(ont.getFrame());
            tmpOnt.setSlot(ont.getSlot());
            tmpOnt.setPort(ont.getPort());
            tmpOnt.setId_puerto(ont.getId_puerto());
            tmpOnt.setEstatus(ont.getEstatus());
            tmpOnt.setId_ejecucion(ont.getId_ejecucion());
            //Inicializa el espacio de memoria en donde viviran las referencias a las olts en donde se repite las onts
            tmpOnt.setOltList(new ArrayList<>());
            auxiliarList.add(tmpOnt);
        }
        return auxiliarList;
    }

    /**
     * Proceso para determinar si una ont esta en una o mas olts.
     */
    public List<AuxOntsAdapter> consultarOntsRepetidas(List<AuxOntsAdapter> adapterList) {
        List<DiferenciasManualEntity> diferenciasManualList = diferenciasManualRepository.findAll();
        List<AuxOltAdapter> auxArray = new ArrayList<>();

        for (AuxOntsAdapter ont : adapterList) {
            auxArray = new ArrayList<>();
            for (DiferenciasManualEntity diferenciasManualEntity : diferenciasManualList) {
                if (ont.getNumero_serie().equals(diferenciasManualEntity.getNumero_serie())) {
                    AuxOltAdapter tmpOlt = new AuxOltAdapter();
                    tmpOlt.setId_olt(diferenciasManualEntity.getId_olt());
                    setIp(tmpOlt);
                    tmpOlt.setOid(diferenciasManualEntity.getOid());
                    auxArray.add(tmpOlt);
                }
            }
            ont.setOltList(auxArray);
        }
        return adapterList;
    }

    private void setIp(AuxOltAdapter auxArray) {
        List<catOltsEntidad> oltList = catOltsRepository.findAll();
        oltList.stream().forEach(olt -> {
            if (auxArray.id_olt.intValue() == olt.getId_olt().intValue()) {
                auxArray.setIp(olt.getIp());
            }
        });
    }

    //Patron-Adapter: Se usa para mappear ciertos campos del documento
    @Setter
    @Getter
    public class AuxOltAdapter {
        private String ip;
        private Integer id_olt;
        private String oid;
    }

    @Setter
    @Getter
    public class AuxOntsAdapter {
        //private String _id;
        private String oid;
        private String uid;
        //private String valor;
        private Integer id_olt;
        //private Integer id_metrica;
        //private Date fecha_poleo;
        //private Date fecha_modificacion;
        private Integer estatus;
        private String id_ejecucion;
        private Integer id_region;
        private Integer frame;
        private Integer slot;
        private Integer port;
        private String id_puerto;
        private String numero_serie;
        private String tecnologia;
        //private String index;
        //private String indexFSP;
        //private String descripcion;
        private boolean error;
        List<AuxOltAdapter> oltList;
    }

    List<DiferenciasManualEntity> obtenerOntsOlt(Integer idOlt) {
        AggregationOperation match = Aggregation.match(Criteria.where("id_olt").is(idOlt.intValue()));
        AggregationOperation lookup = Aggregation.lookup("tb_inventario_onts", "numero_serie",
                "numero_serie", "onts");
        Aggregation aggregation = Aggregation.newAggregation(match, lookup);
        AggregationResults<DiferenciasManualEntity> out
                = mongoTemplate.aggregate(aggregation, "tb_diferencias_carga_manual", DiferenciasManualEntity.class);
        List<DiferenciasManualEntity> list = out.getMappedResults();
        return list;
    }
}
