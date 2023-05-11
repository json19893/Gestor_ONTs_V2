package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IEstadoOntsResumenService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioOntsTotalesActivoRepositorio;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;
import static totalplay.monitor.snmp.com.negocio.util.constantes.ONT_VIP;

/**
 *
 */
@Service
public class EstadoOntsResumenServiceImpl implements IEstadoOntsResumenService {
    @Autowired
    IEnvoltorioOntsTotalesActivoRepositorio repositorio;
    @Autowired
    ImonitorService negocio;

    //Agrega un cron para ejecutar cada dos minutos
    @Scheduled(fixedDelay = 2000)
    public void process() {
        //System.out.println("Ejecutando proceso");
        //Meter el tiempo que tomo para actualizar:
        long time1 = System.currentTimeMillis();

        //Estructura principal: top-level
        EnvoltorioOntsTotalesActivoEntidad envoltura = new EnvoltorioOntsTotalesActivoEntidad();
        envoltura.setDate(LocalDateTime.now());

        try {
            //Estructura Segundaria: Segundo nivel.
            CompletableFuture<EnvoltorioAuxiliarDto> alta1 = obtenerResumenAltasOnts(ONT_TOTALES);
            CompletableFuture<EnvoltorioAuxiliarDto> alta2 = obtenerResumenAltasOnts(ONT_EMPRESARIALES);
            CompletableFuture<EnvoltorioAuxiliarDto> alta3 = obtenerResumenAltasOnts(ONT_VIP);

            // Wait until they are all done
            CompletableFuture.allOf(alta1, alta2, alta3).join();

            EnvoltorioAuxiliarDto resumenEstadoOntTotales = alta1.get();  // ONT_TOTALES
            EnvoltorioAuxiliarDto resumenEstadoOntEmpresariales = alta2.get();  // ONT_EMPRESARIALES
            EnvoltorioAuxiliarDto resumenEstadoOntVip = alta3.get();  // ONT_VIP

            envoltura.setTotalesOntsActivas(resumenEstadoOntTotales);
            envoltura.setTotalesOnstsActivasEmpresariales(resumenEstadoOntEmpresariales);
            envoltura.setTotalesOntsActivasVips(resumenEstadoOntVip);

            //Finalmente se persiste la informacion procesada:
            List<EnvoltorioOntsTotalesActivoEntidad> document = repositorio.findAll();
            if(document.size() == 0){
                EnvoltorioOntsTotalesActivoEntidad persistence = repositorio.save(envoltura);
                if (persistence != null) {
                    //System.out.println("Resumen persistido en el repositorio");
                }
            }

            EnvoltorioOntsTotalesActivoEntidad res = document.get(0);
            res.setDate(LocalDateTime.now());
            res.setTotalesOntsActivas(resumenEstadoOntTotales);
            res.setTotalesOnstsActivasEmpresariales(resumenEstadoOntEmpresariales);
            res.setTotalesOntsActivasVips(resumenEstadoOntVip);
            EnvoltorioOntsTotalesActivoEntidad updated = repositorio.save(res);

            if (updated != null) {
                //System.out.println("Resumen actualizado en el repositorio");
            }
            long time2 = System.currentTimeMillis();
            long totalTime = time2 - time1;
            double minutos = (totalTime / (double) 1000);
            DecimalFormat df = new DecimalFormat("#.##");

            //System.out.println("El proceso actualizacion del resumen: estatus de onts tomo: " +  df.format(minutos)  + " segundos en terminar la ejecuccion.");
        } catch (Exception ex) {
                System.out.println("Error en el proceso para crear los resumenes de estatus para las onts. Reintentando... en 5 segundos");
        }
    }

    /**
     * Consulta el estado de las puntas en funcion del parametro
     *
     * @param tipo representa el resumen del estado de las puntas
     * @return
     */
    @Async("taskExecutor")
    CompletableFuture<EnvoltorioAuxiliarDto> obtenerResumenAltasOnts(String tipo) throws Exception {
        String consultar = "";
        String descripcion_corta = "";
        String descripcion_larga = "";

        //Settea los parametros:
        switch (tipo) {
            //Opcion por defecto:
            case ONT_TOTALES:
                //Agregar estos valores a constantes:
                consultar = "T";
                descripcion_corta = "[Vip, Empresariales, residenciales]";
                descripcion_larga = "Resumen del estado de todas las onts";
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
        EnvoltorioAuxiliarDto auxiliar = new EnvoltorioAuxiliarDto();

        auxiliar.setTipo(consultar);
        auxiliar.setDescripcionCorta(descripcion_corta);
        auxiliar.setDescripcionLarga(descripcion_larga);

        //Parte del negocio
        totalesActivoDto estatusOnts = negocio.getTotalesActivo(consultar);
        auxiliar.setResumenStatusOnts(estatusOnts);
        return CompletableFuture.completedFuture(auxiliar);
    }
}
