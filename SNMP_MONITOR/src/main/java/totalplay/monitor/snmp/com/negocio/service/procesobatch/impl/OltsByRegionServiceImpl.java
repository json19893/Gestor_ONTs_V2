package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.*;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IOltsByRegionService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioGetOltsByRegionEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioGetOltsByRegionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;

/**
 * Este proceso batch actualiza los totales de regiones, ols por tecnologia y sus onts.
 */
@Service
public class OltsByRegionServiceImpl implements IOltsByRegionService {
    @Autowired
    ImonitorService negocio;
    @Autowired
    IEnvoltorioGetOltsByRegionRepository repositorio;

    @Override
    @Scheduled(fixedDelay = 60000)
    public void process() throws Exception {
        System.out.printf("Inicia el proceso consulta estatus regiones onts");
        try {
            List<EnvoltorioTopLevelRegionAuxiliarDto> result = obtenerOltsPorRegion();

            List<EnvoltorioGetOltsByRegionEntidad> entityList = new ArrayList<>();
            EnvoltorioGetOltsByRegionEntidad entity;

            for (EnvoltorioTopLevelRegionAuxiliarDto regionResumen : result) {
                entity = new EnvoltorioGetOltsByRegionEntidad();
                switch (regionResumen.getIdRegion()) {
                    case 1:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 2:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 3:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 4:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 5:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 6:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 7:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 8:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 9:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 10:
                        entity = adapterObjeto(regionResumen);
                        break;
                    case 11:
                        entity = adapterObjeto(regionResumen);
                        break;
                }
                //Agregar a una lista
                entityList.add(entity);
            }
            persistirDocumentos(entityList);
            System.out.printf("Finalizo el proceso consulta estatus regiones onts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void persistirDocumentos(List<EnvoltorioGetOltsByRegionEntidad> entityList) {
        for (EnvoltorioGetOltsByRegionEntidad entity : entityList) {
            //Pregunta si existe la entidad:
            EnvoltorioGetOltsByRegionEntidad existe = repositorio.obtenerEntidad(entity.getIdRegion());
            if (existe != null) {
                entity.setId(existe.getId());
            }
            repositorio.save(entity);
        }
    }

    private EnvoltorioGetOltsByRegionEntidad adapterObjeto(EnvoltorioTopLevelRegionAuxiliarDto regionResumen) {
        EnvoltorioGetOltsByRegionEntidad entity = new EnvoltorioGetOltsByRegionEntidad();
        entity.setIdRegion(regionResumen.getIdRegion());
        entity.setFechaPoleo(regionResumen.getFechaPoleo());
        entity.setNombreRegion(regionResumen.getNombreRegion());
        entity.setDescripcion(regionResumen.getDescripcion());
        entity.setRegionOntTodoEstatus(regionResumen.getRegionOntTodoEstatus());
        entity.setRegionOntEmpresarialesEstatus(regionResumen.getRegionOntEmpresarialesEstatus());
        entity.setRegionOntVipsEstatus(regionResumen.getRegionOntVipsEstatus());
        entity.setRegionOntSAEstatus(regionResumen.getRegionOntSAEstatus());
        
        return entity;
    }

    //Mete otro nivel
    public List<EnvoltorioTopLevelRegionAuxiliarDto> obtenerOltsPorRegion() throws Exception {
        //Catalaogo:
        List<String> tipoOnt = new ArrayList<>();
        tipoOnt.add(ONT_TOTALES);
        tipoOnt.add(ONT_EMPRESARIALES);
        tipoOnt.add(ONT_VIP);
        tipoOnt.add(ONT_SERVICIOS_ADMINISTRADOS);

        List<EnvoltorioTopLevelRegionAuxiliarDto> listRegiones = new ArrayList<>();
        EnvoltorioTopLevelRegionAuxiliarDto regionAux;

        List<CompletableFuture<EnvoltorioResumenOltsbyRegionEstatus>> listaAsyncrona = new ArrayList<>();
        CompletableFuture<EnvoltorioResumenOltsbyRegionEstatus> future = new CompletableFuture<>();

        for (int region = 0; region < 11; region++) {
            int regionOne = region + 1;
            //Necesito otro nivel- segundo nivel detalle region:
            regionAux = new EnvoltorioTopLevelRegionAuxiliarDto();

            regionAux.setIdRegion(regionOne);
            regionAux.setFechaPoleo(LocalDateTime.now());
            regionAux.setNombreRegion("Region" + regionOne);
            regionAux.setDescripcion("Resumen del estado de las onts de una region");

            for (String auxTipo : tipoOnt) {
                future = subproceso(regionOne, auxTipo, false);
                listaAsyncrona.add(future);
            }

            //Aqui arma los procesos asyncronos en la capa superior los detienes:
            CompletableFuture.allOf(listaAsyncrona.toArray(new CompletableFuture[listaAsyncrona.size()])).join();
            for (CompletableFuture<EnvoltorioResumenOltsbyRegionEstatus> futureResponse : listaAsyncrona) {
                EnvoltorioResumenOltsbyRegionEstatus tmp = futureResponse.get();
                switch (tmp.getTipo()) {
                    case "T":
                        regionAux.setRegionOntTodoEstatus(tmp);
                        break;
                    case "E":
                        regionAux.setRegionOntEmpresarialesEstatus(tmp);
                        break;
                    case "V":
                        regionAux.setRegionOntVipsEstatus(tmp);
                        break;
                    case "S":
                        regionAux.setRegionOntSAEstatus(tmp);
                        break;
                }
            }
            listRegiones.add(regionAux);
        }
        return listRegiones;
    }

    //Esta parte necesita asyncronismo:
    public CompletableFuture<EnvoltorioResumenOltsbyRegionEstatus> subproceso(int idRegion, String tipo, boolean headers) throws Exception {
        //Constante: region
        String consultar = "";
        String descripcion_corta = "";
        String descripcion_larga = "";

        //Settea los parametros:
        switch (tipo) {
            //Opcion por defecto:
            case ONT_TOTALES:
                //Agregar estos valores a constantes:
                consultar = "T";
                descripcion_corta = "[Vip, Empresariales, Residenciales]";
                descripcion_larga = "Resumen del estado de las onts de todas las regiones";
                break;
            case ONT_EMPRESARIALES:
                consultar = "E";
                descripcion_corta = "Empresariales";
                descripcion_larga = "Resumen del estado de las onts empresariales";
                break;
            case ONT_VIP:
                //Settea los datos:
                consultar = "V";
                descripcion_corta = "Vip";
                descripcion_larga = "Resumen del estado de las onts Vip";
                break;
            case ONT_SERVICIOS_ADMINISTRADOS:
                //Settea los datos:
                consultar = "S";
                descripcion_corta = "Servicios Administrados";
                descripcion_larga = "Resumen del estado de las onts de Servicios Administrados ";
                break;
        }

        //Estructura Segundaria: Segundo nivel.
        EnvoltorioResumenOltsbyRegionEstatus auxiliar = new EnvoltorioResumenOltsbyRegionEstatus();

        auxiliar.setTipo(consultar);
        auxiliar.setDescripcionCorta(descripcion_corta);
        auxiliar.setDescripcionLarga(descripcion_larga);

        //Parte del negocio
        responseRegionDto estatusOnts = negocio.getOltsByRegion(idRegion, consultar, headers);
        auxiliar.setResumenStatusOnts(estatusOnts);
        return CompletableFuture.completedFuture(auxiliar);
    }
}
