
package totalplay.monitor.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import totalplay.monitor.snmp.com.negocio.dto.ResponsePostBlockMetricDto;
import totalplay.monitor.snmp.com.negocio.dto.responseDto;
import totalplay.monitor.snmp.com.negocio.service.IBlockMetricService;
import totalplay.monitor.snmp.com.persistencia.entidad.ConfiguracionMetricaEntity;
import totalplay.monitor.snmp.com.persistencia.repository.ITConfigMetricsRepository;

import java.util.ArrayList;
import java.util.List;



@Service
@Slf4j
public class BlockMetricServiceImpl implements IBlockMetricService {
    private String BLOCK1 = "block1";
    private List<GroupBlockMetric> blockMetric1;


    private List<Metrica> metricaList1;
    private List<Metrica> metricaList2;
    private List<Metrica> metricaList3;
    private List<Metrica> metricaList4;

    @Data
    @NoArgsConstructor
    class Metrica {
        private int idMetrica;
        private String metrica;

        public Metrica(int idMetrica, String metrica) {
            this.idMetrica = idMetrica;
            this.metrica = metrica;
        }

        
        @Override
        public String toString() {
            return "Metrica{" +
                    "idMetrica=" + idMetrica +
                    ", metrica='" + metrica + '\'' +
                    '}';
        }
    }
    @Data
    @NoArgsConstructor
    class GroupBlockMetric {
        private int idBlock;
        private List<ConfiguracionMetricaEntity> metricList;

     
        public GroupBlockMetric(int idBlock, List<ConfiguracionMetricaEntity> metricList) {
            this.idBlock = idBlock;
            this.metricList = metricList;
        }


        @Override
        public String toString() {
            return "GroupBlockMetric{" +
                    "idBlock=" + idBlock +
                    ", metricList=" + metricList +
                    '}';
        }
    }


    //Objeto de puras llaves
    @Data
    @NoArgsConstructor
    class BlockMetricRelationship {
        private int idblock;
        private List<Integer> metricids;

        public BlockMetricRelationship(int idblock, List<Integer> metricids) {
            this.idblock = idblock;
            this.metricids = metricids;
        }

        @Override
        public String toString() {
            return "BlockMetricRelationship{" +
                    "idblock=" + idblock +
                    ", metricids=" + metricids +
                    '}';
        }
    }

    //Esta funcion es util cuando ya estan llenos los bloques
    //searchCriteria
    public boolean isExistsMetricIntoBlock(int searchMetrica, GroupBlockMetric block) {
        boolean isExistsMetric = false;
        for (ConfiguracionMetricaEntity metrica : block.getMetricList()) {
            if (metrica.getId_metrica() == searchMetrica) {
                isExistsMetric = true;
                break;
            }
        }
        return isExistsMetric;
    }

    @Autowired
    ITConfigMetricsRepository repository;

    /**
     * Este metodo cambia asigna a un bloqye la metrica mandada como parametro:
     */
    @Override
    public responseDto changeMetricBlock(int id_metrica, int id_bloque) {
        ResponsePostBlockMetricDto response = new ResponsePostBlockMetricDto();

        ConfiguracionMetricaEntity config = repository.findByidMetrica(id_metrica);
        if (config == null) {
            response.setCod(1);
            response.setSms("No existe la metrica");
            return response;
        }

        //Sino existe el array crealo
        if (config.getBloque() == null) {
            config.getBloque().add(id_bloque);
            List<Integer> blocks = new ArrayList<>();
            blocks.add(id_metrica);
            config.setBloque(blocks);
            repository.save(config);
            response.setCod(0);
            response.setSms("Se creo el bloque y se agrego la metrica");
            response.setEntity(repository.findAll());
            return response;
        }

        //Buscar si ya existe el id en el bloque
        for (Integer bloque : config.getBloque()) {
            if (bloque.intValue() == id_bloque) {
                //Si ya esta asignado en el bloque notificar al usuario
                response.setCod(1);
                response.setSms("La metrica ya esta asignada en el bloque");
                response.setEntity(repository.findAll());
                return response;
            }
        }
        //Cambiar la metrica de bloque
        config.getBloque().add(id_bloque);
        repository.save(config);
        response.setCod(0);
        response.setSms("Success la metrica se asigno al bloque " + id_bloque);
        //response.setEntity(repository.findAll());
        return response;
    }

    @Override
    public responseDto getAllConfigMetrics() {
        ResponsePostBlockMetricDto response = new ResponsePostBlockMetricDto();
        response.setCod(0);
        response.setSms("Exito");

        try {
            List<ConfiguracionMetricaEntity> configMetricaList = repository.findAll();
            response.setEntity(configMetricaList);
        } catch (Exception ex) {
            log.error("error", ex);
            response.setCod(1);
            response.setSms("Error");
        }

        return response;
    }

    @Override
    public responseDto removeMetricBlock(int id_metrica, int id_bloque) {
        ResponsePostBlockMetricDto response = new ResponsePostBlockMetricDto();
        List<ConfiguracionMetricaEntity> listConfigMetrica = repository.findAll();

        ConfiguracionMetricaEntity config = repository.findByidMetrica(id_metrica);
        if (config == null) {
            response.setCod(1);
            response.setSms("No existe la configuracion de la metrica");
            response.setEntity(listConfigMetrica);
            return response;
        }

        if (config.getBloque().contains(id_bloque)) {
            //Como obtengo el indice
            int index = config.getBloque().indexOf(new Integer(id_bloque));
            config.getBloque().remove(index);

            repository.save(config);
            response.setEntity(listConfigMetrica);
            response.setCod(0);
            response.setSms("Exito - la metrica: " + id_metrica + " fue removida del bloque: " + id_bloque);
            return response;
        } else {
            response.setCod(1);
            response.setSms("No esta asignado la metrica en el bloque");
            //response.setEntity(listConfigMetrica);
            return response;
        }
    }
}
