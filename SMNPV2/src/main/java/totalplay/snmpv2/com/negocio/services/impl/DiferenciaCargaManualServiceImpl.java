package totalplay.snmpv2.com.negocio.services.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import totalplay.snmpv2.com.negocio.dto.OntsRepetidasPorOltPostRequest;
import totalplay.snmpv2.com.negocio.dto.EnvoltorioOntRepetidasOltsDto;
import totalplay.snmpv2.com.negocio.services.IDiferenciaCargaManualService;
import totalplay.snmpv2.com.persistencia.entidades.DiferenciasManualEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IdiferenciasManualRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta clase contiene funciones para consultar las olts...
 * Condicion: Una ont no puede existir en varias olts al mismo tiempo.
 */
@Service
public class DiferenciaCargaManualServiceImpl implements IDiferenciaCargaManualService {
    //private List<EnvoltorioOntRepetidasOltsDto> adapterList;
    @Autowired
    IinventarioOntsRepository inventarioOntsRepository;
    @Autowired
    IdiferenciasManualRepository diferenciasManualRepository;

    /**
     * Este metodo devuelve la coleccion de documentos. Este documento realizara una comparativa para consultar las onts repetidas por el id olt.
     *
     * @return
     */
    @Override
    public List<EnvoltorioOntRepetidasOltsDto> consultarOntsRepetidasPorOlt(final OntsRepetidasPorOltPostRequest request) {
        //int idOlt = request.getIdOlt();
        Integer idOlt = new Integer(1);
        List<InventarioOntsEntity> ontList = inventarioOntsRepository.getOntsListByIdOlt(idOlt);
        List<EnvoltorioOntRepetidasOltsDto> adapterList = adapterList(ontList);
        return consultarOntsRepetidas(adapterList);
    }

    //Patron-Adapter: Se usa para mappear ciertos campos del documento
    @Setter
    @Getter
    public class AuxTemplateOlt {
        private String ip;
        private Integer id_olt;
        private String oid;
    }

    @Setter
    @Getter
    public class AuxTemplateOnts {
        private String _id;
        private String oid;
        private String uid;
        private String valor;
        private Integer id_olt;
        private Integer id_metrica;
        private Date fecha_poleo;
        private Date fecha_modificacion;
        private Integer estatus;
        private String id_ejecucion;
        private Integer id_region;
        private Integer frame;
        private Integer slot;
        private Integer port;
        private String id_puerto;
        private String numero_serie;
        private String tecnologia;
        private String index;
        private String indexFSP;
        private String descripcion;
        private boolean error;
    }

    //Adapter pattern: convierte una lista de objetos List<InventarioOntsEntity> a: una lista de: List<EnvoltorioOntsRepetidasDto>.
    //Devuelve un Dto
    List<EnvoltorioOntRepetidasOltsDto> adapterList(List<InventarioOntsEntity> ontList) {
        List<EnvoltorioOntRepetidasOltsDto> auxiliarList = new ArrayList<>();
        EnvoltorioOntRepetidasOltsDto tmpOnt;

        for (InventarioOntsEntity ont : ontList) {
            //Atributos del GenericPoleosDto
            tmpOnt = new EnvoltorioOntRepetidasOltsDto();
            tmpOnt.set_id(ont.get_id());
            tmpOnt.setOid(ont.getOid());
            tmpOnt.setUid(ont.getUid());
            tmpOnt.setValor(ont.getValor());
            tmpOnt.setTecnologia(ont.getTecnologia());
            tmpOnt.setId_olt(ont.getId_olt());
            tmpOnt.setId_metrica(ont.getId_metrica());
            tmpOnt.setFecha_poleo(ont.getFecha_poleo());
            tmpOnt.setFecha_modificacion(ont.getFecha_modificacion());
            tmpOnt.setEstatus(ont.getEstatus());
            tmpOnt.setId_ejecucion(ont.getId_ejecucion());
            //Atributos de la ont
            tmpOnt.setId_ejecucion(ont.getAlias());
            tmpOnt.setTipo(ont.getTipo());
            tmpOnt.setLastDownTime(ont.getLastDownTime());
            tmpOnt.setDescripcionAlarma(ont.getDescripcionAlarma());
            tmpOnt.setActualizacion(ont.getActualizacion());
            tmpOnt.setActualizacion(ont.getActualizacion());
            tmpOnt.setVip(ont.getVip());
            tmpOnt.setFecha_actualizacion(ont.getFecha_actualizacion());
            tmpOnt.setNumero_serie(ont.getNumero_serie());
            //Inicializa el espacio de memoria en donde viviran las referencias a las olts en donde se repite las onts
            tmpOnt.setOltList(new ArrayList<>());
            auxiliarList.add(tmpOnt);
        }
        return auxiliarList;
    }

    /**
     * Proceso para determinar si una ont esta en una o mas olts.
     */
    public List<EnvoltorioOntRepetidasOltsDto> consultarOntsRepetidas(List<EnvoltorioOntRepetidasOltsDto> adapterList) {
        //Prueba: {numero_serie: "485754435D4448A5"} Estan tanto en la id_olt:1 y id_olt: 338.
        List<DiferenciasManualEntity> diferenciasManualList = diferenciasManualRepository.findAll();

        //Test
        List<AuxTemplateOlt> auxArray = new ArrayList<>();
        List<InventarioOntsEntity> wrapperTest = new ArrayList<>();
        InventarioOntsEntity auxTest = inventarioOntsRepository.getOntBySerialNumber("485754435D4448A5");
        wrapperTest.add(auxTest);
        //Test Final
        List<EnvoltorioOntRepetidasOltsDto> isFinded = new ArrayList<>();
        isFinded
                = adapterList
                .stream()
                .filter(ont -> ont.getNumero_serie().equals("485754435D4448A5"))
                .collect(Collectors.toList());

        //for (EnvoltorioOntRepetidasOltsDto ont : adapterList) {
        for (EnvoltorioOntRepetidasOltsDto ont : isFinded) {
            //Aqui se va a meter tantas veces como se resetee la variables
            for (DiferenciasManualEntity diferenciasManualEntity : diferenciasManualList) {
                if (auxTest.getNumero_serie().equals(diferenciasManualEntity.getNumero_serie())) {
                    //Volcar informacion:
                    AuxTemplateOlt tmpOlt = new AuxTemplateOlt();
                    tmpOlt.setId_olt(diferenciasManualEntity.getId_olt());
                    tmpOlt.setOid(diferenciasManualEntity.getOid());
                    auxArray.add(tmpOlt);
                }
            }
            ont.setOltList(auxArray);
        }
        //auxArray.size();
        System.out.println("resultado del volcado");
        //Aqui vas a volcar la informacion en el objeto.
        //Crea una estructura intermedia para almacenar en cache el resultado de los datos ya procesados:
        return isFinded;
    }
}
