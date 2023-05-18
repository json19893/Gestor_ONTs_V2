package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;
import totalplay.monitor.snmp.com.negocio.dto.totalGraficaDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IEstadoOntsResumenService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioOntsTotalesActivoRepositorio;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
        //Meter el tiempo que tomo para actualizar:+ 
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
            //A future cmabiarlo por la logica del negocio:
            envoltura.setTotalesOntsActivasServiciosAdministrados(getDummyResumen());

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
            res.setTotalesOntsActivasServiciosAdministrados(getDummyResumen());

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
            ex.printStackTrace();
                System.out.println("Error en el proceso para crear los resumenes de estatus para las onts. Reintentando... en 5 segundos");
        }
    }

    private EnvoltorioAuxiliarDto getDummyResumen() {
        EnvoltorioAuxiliarDto dummyObject = new EnvoltorioAuxiliarDto();
        totalesActivoDto dto = new totalesActivoDto();
        List<totalGraficaDto> graficaList = new ArrayList<>();
        totalGraficaDto graficaEmp = new totalGraficaDto();
        totalGraficaDto graficaONTs = new totalGraficaDto();
        totalGraficaDto graficaOLTs = new totalGraficaDto();

        dummyObject.setTipo("S");
        dummyObject.setDescripcionCorta("Servicios Administrados");
        dummyObject.setDescripcionLarga("Resumen del estado de las onts: Servicios Administrados");

        dto.setTotalHuawei(0);

        dto.setTotalArribaZte(0);
        dto.setTotalArribaFh(0);
        dto.setTotalAbajoZte(0);
        dto.setTotalAbajoFh(0);

        dto.setTotalAbajoHuawei(0);
        dto.setTotalZte(0);
        dto.setTotalFh(0);
        dto.setTotalArribaHuawei(0);
        dto.setTotalAbajoHuawei(0);
        dto.setUltimaActualizacion(Date.from(Instant.now()));
        dto.setProximoDescubrimiento(Date.from(Instant.now().plusMillis(10000)));
        dto.setConteoPdmOnts(0);
        dto.setTotalHuaweiEmp(0);
        dto.setTotalZteEmp(0);
        dto.setTotalFhEmp(0);
        dto.setTotalArribaZteEmp(0);
        dto.setTotalArribaHuaweiEmp(0);
        dto.setTotalArribaFhEmp(0);
        dto.setTotalAbajoHuaweiEmp(0);
        dto.setTotalAbajoZteEmp(0);
        dto.setTotalAbajoFhEmp(0);

        graficaEmp.setCategory("Emp");
        graficaEmp.setValue(0);
        graficaEmp.setFull(100);

        graficaONTs.setCategory("ONTs");
        graficaONTs.setValue(0);
        graficaONTs.setFull(100);

        graficaOLTs.setCategory("OLTs");
        graficaOLTs.setValue(0);
        graficaOLTs.setFull(100);

        graficaList.add(graficaEmp);
        graficaList.add(graficaONTs);
        graficaList.add(graficaOLTs);
        dto.setGrafica(graficaList);
        dummyObject.setResumenStatusOnts(dto);
        return dummyObject;
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
