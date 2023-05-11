package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.stream.Collectors;

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
    public void process() throws Exception {
        System.out.printf("Inicia el proceso");
        try {
            List<EnvoltorioTopLevelRegionAuxiliarDto> result = obtenerOltsPorRegion();

            EnvoltorioGetOltsByRegionEntidad entity = new EnvoltorioGetOltsByRegionEntidad();
            for (EnvoltorioTopLevelRegionAuxiliarDto regionResumen : result) {
                int idRegion = regionResumen.getIdRegion();
                switch (idRegion) {
                    case 1: entity.setRegion1(regionResumen);
                        break;
                    case 2: entity.setRegion2(regionResumen);
                        break;
                    case 3: entity.setRegion3(regionResumen);
                        break;
                }
            }

            repositorio.save(entity);
            System.out.println("pp /n");
        } catch (Exception e) {

        }
    }

    private EnvoltorioGetOltsByRegionEntidad adaptarEntity(){
        return null;
    }

    //Mete otro nivel
    public List<EnvoltorioTopLevelRegionAuxiliarDto> obtenerOltsPorRegion() throws Exception {
        //Catalaogo:
        List<String> tipoOnt = new ArrayList<>();
        tipoOnt.add(ONT_TOTALES);
        tipoOnt.add(ONT_EMPRESARIALES);
        tipoOnt.add(ONT_VIP);

        List<EnvoltorioTopLevelRegionAuxiliarDto> listRegiones = new ArrayList<>();
        EnvoltorioTopLevelRegionAuxiliarDto regionAux;

        List<CompletableFuture<EnvoltorioResumenOltsbyRegionEstatus>> listaAsyncrona = new ArrayList<>();
        CompletableFuture<EnvoltorioResumenOltsbyRegionEstatus> future = new CompletableFuture<>();

        for (int region = 0; region <= 11; region++) {
            //Necesito otro nivel- segundo nivel detalle region:
            regionAux = new EnvoltorioTopLevelRegionAuxiliarDto();

            regionAux.setIdRegion(region+1);
            regionAux.setNombreRegion("Region" + region);
            regionAux.setDescripcion("Resumen del estado de las onts de una region");

            for (String auxTipo : tipoOnt) {
                future = subproceso(region, auxTipo, false);
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
