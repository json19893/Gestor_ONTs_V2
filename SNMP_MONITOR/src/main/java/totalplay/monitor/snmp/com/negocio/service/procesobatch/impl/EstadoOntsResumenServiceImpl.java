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

import totalplay.monitor.snmp.com.persistencia.entidad.TotalesByTecnologiaEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioOntsTotalesActivoRepositorio;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;
import static totalplay.monitor.snmp.com.negocio.util.constantes.ONT_VIP;

/**
 *
 */
@Service
public class EstadoOntsResumenServiceImpl implements IEstadoOntsResumenService {
    @Autowired
    IEnvoltorioOntsTotalesActivoRepositorio repository;
    @Autowired
    ImonitorService negocio;
    /*
        Target - Process: ImonitorService.getTotalesActivo(tipo);
        El cron se ejecuta cada dos minutos:
    */
    @Scheduled(fixedDelay = 120000, initialDelay = 1000)
    public void process() {
        System.out.println("Ejecutando proceso para obtener el estatus los totales de todas las onts");
        //Meter el tiempo que tomo para actualizar:+ 
        long time1 = System.currentTimeMillis();
        try {
            //Estructura Segundaria: Segundo nivel.
            CompletableFuture<EnvoltorioAuxiliarDto> estatusTotales = obtenerResumenAltasOnts(ONT_TOTALES);
            CompletableFuture<EnvoltorioAuxiliarDto> estatusEmpresariales = obtenerResumenAltasOnts(ONT_EMPRESARIALES);
            CompletableFuture<EnvoltorioAuxiliarDto> estatusVips = obtenerResumenAltasOnts(ONT_VIP);
            CompletableFuture<EnvoltorioAuxiliarDto> estatusSA = obtenerResumenAltasOnts(ONT_SA);

            // Wait until they are all done
            CompletableFuture.allOf(estatusTotales, estatusEmpresariales, estatusVips, estatusSA).join();

            EnvoltorioAuxiliarDto resumenEstadoOntTotales = estatusTotales.get();
            EnvoltorioAuxiliarDto resumenEstadoOntEmpresariales = estatusEmpresariales.get();
            EnvoltorioAuxiliarDto resumenEstadoOntVip = estatusVips.get();
            EnvoltorioAuxiliarDto resumenEstadoOntSA = estatusSA.get();

            persistirInformacion(adapterEntidad(resumenEstadoOntTotales));
            persistirInformacion(adapterEntidad(resumenEstadoOntEmpresariales));
            persistirInformacion(adapterEntidad(resumenEstadoOntVip));
            persistirInformacion(adapterEntidad(resumenEstadoOntSA));

            System.out.println("Finalizo proceso para obtener el estatus los totales de todas las onts");
        } catch (Exception ex) {
            System.out.println("Error en el proceso para crear los resumenes de estatus para las onts. Reintentando... en 5 segundos");
        }
    }
    public EnvoltorioOntsTotalesActivoEntidad adapterEntidad(EnvoltorioAuxiliarDto dto){
        EnvoltorioOntsTotalesActivoEntidad entity = new EnvoltorioOntsTotalesActivoEntidad();
        entity.setDate(LocalDateTime.now());
        entity.setTipo(dto.getTipo());
        entity.setDescripcionCorta(dto.getDescripcionCorta());
        entity.setDescripcionLarga(dto.getDescripcionLarga());
        entity.setTotalesOntsActivas (dto.getResumenStatusOnts());
        return entity;
    }
    private void persistirInformacion(EnvoltorioOntsTotalesActivoEntidad entidad) {
        //Buscar entidad
        EnvoltorioOntsTotalesActivoEntidad existe = repository.getEntity(entidad.getTipo());
        if(existe != null){
            entidad.setId(existe.getId());
        }
        repository.save(entidad);
    }

    /**
     * Devuelve un objeto asincrono encapsulando la ejecuccion de la logica del negocio para relizar el conteo del estado de las puntas.
     * @param tipo - Recibe el tipo de Onts que se requiere consultar para realizar un conteo agrupado por tipo:{Empresarial, Vip o Ambas}
     * @return - CompletableFuture Objeto asyncrono que se ejecutara en un hilo.
     * @throws Exception
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
                descripcion_corta = "Todas las Ont's: {Vip, Empresarial, Residencial}";
                descripcion_larga = "Resumen Conteo: Estado Actual de todas las Ont's ";
                break;
            case ONT_EMPRESARIALES:
                consultar = "E";
                descripcion_corta = "Empresariales";
                descripcion_larga = "Resumen - Conteo: Estado de las Ont's Empresariales (Agrupadas por Tipo Empresarial)";
                break;
            case ONT_VIP:
                //Settea los datos:
                consultar = "V";
                descripcion_corta = "Vip";
                descripcion_larga = "Resumen - Conteo: Estado de las Ont's Vip (Agrupadas por Tipo Vip)";
                break;
            case ONT_SA:
                //Settea los datos:
                consultar = "S";
                descripcion_corta = "SA";
                descripcion_larga = "Resumen - Conteo: Estado de las Ont's Sistemas Administrados (Agrupadas por Tipo S)";
                break;
        }
        //Estructura de datos para almacenar datos resultantes del proceso:
        EnvoltorioAuxiliarDto auxiliar = new EnvoltorioAuxiliarDto();

        auxiliar.setTipo(consultar);
        auxiliar.setDescripcionCorta(descripcion_corta);
        auxiliar.setDescripcionLarga(descripcion_larga);

        //Se ejecuta el negocio para consultar el estado de la infraestrutura:
        totalesActivoDto estatusOnts = negocio.getTotalesActivo(consultar);
        auxiliar.setResumenStatusOnts(estatusOnts);
        return CompletableFuture.completedFuture(auxiliar);
    }
}
