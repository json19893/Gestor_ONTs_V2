package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarOntsByTecnologiaDto;
import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IObtenerOntsByTecnologiaService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.TotalesByTecnologiaEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.ITotalesByTecnologiaRepository;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;

@Service
public class ObtenerOntsByTecnologiaServiceImpl implements IObtenerOntsByTecnologiaService {
    @Autowired
    ITotalesByTecnologiaRepository repository;
    @Autowired
    IinventarioOntsRepositorio inventario;

    @Override
     @Scheduled(fixedDelay = 60000)
    public void process() {
        System.out.println("Ejecutando proceso para consultar los totales de las onts por tecnologia");
        //Meter el tiempo que tomo para actualizar:+
        long time1 = System.currentTimeMillis();

        //Estructura principal: top-level
        EnvoltorioOntsTotalesActivoEntidad envoltura = new EnvoltorioOntsTotalesActivoEntidad();
        envoltura.setDate(LocalDateTime.now());

        try {
            //Estructura Segundaria: Segundo nivel.
            CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> alta1 = obtenerResumenOntsByTecnologia(ONT_TOTALES);
            CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> alta2 = obtenerResumenOntsByTecnologia(ONT_EMPRESARIALES);
            CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> alta3 = obtenerResumenOntsByTecnologia(ONT_VIP);

            // Wait until they are all done
            CompletableFuture.allOf(alta1, alta2, alta3).join();

            EnvoltorioAuxiliarOntsByTecnologiaDto resumenEstadoOntTotales = alta1.get();  // ONT_TOTALES
            EnvoltorioAuxiliarOntsByTecnologiaDto resumenEstadoOntEmpresariales = alta2.get();  // ONT_EMPRESARIALES
            EnvoltorioAuxiliarOntsByTecnologiaDto resumenEstadoOntVip = alta3.get();  // ONT_VIP

            persistirInformacion(adapterEntidad(resumenEstadoOntTotales));
            persistirInformacion(adapterEntidad(resumenEstadoOntEmpresariales));
            persistirInformacion(adapterEntidad(resumenEstadoOntVip));
            //System.out.println("El proceso actualizacion del resumen: estatus de onts tomo: " +  df.format(minutos)  + " segundos en terminar la ejecuccion.");
        } catch (Exception ex) {
            System.out.println("Error en el proceso para crear los resumenes de estatus para las onts. Reintentando... en 5 segundos");
        }
    }

    public void persistirInformacion(TotalesByTecnologiaEntidad entidad) {
        //Buscar entidad
        TotalesByTecnologiaEntidad existe = repository.getEntity(entidad.getTipo());
        if(existe != null){
            entidad.setId(existe.getId());
        }
        repository.save(entidad);
    }

    public TotalesByTecnologiaEntidad adapterEntidad(EnvoltorioAuxiliarOntsByTecnologiaDto dto){
        TotalesByTecnologiaEntidad entity = new TotalesByTecnologiaEntidad();
        entity.setDateTime(dto.getDateTime());
        entity.setTipo(dto.getTipo());
        entity.setDescripcionCorta(dto.getDescripcionCorta());
        entity.setDescripcionLarga(dto.getDescripcionLarga());
        entity.setResumenStatusOnts(dto.getResumenStatusOnts());
        return entity;
    }

    @Async("taskExecutor")
    CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> obtenerResumenOntsByTecnologia(String tipo) throws Exception {
        List<datosRegionDto> lista;
        String consultar = "";
        String descripcion_corta = "";
        String descripcion_larga = "";

        //Estructura Segundaria: Segundo nivel.
        EnvoltorioAuxiliarOntsByTecnologiaDto auxiliar = new EnvoltorioAuxiliarOntsByTecnologiaDto();

        //Settea los parametros:
        switch (tipo) {
            //Opcion por defecto:
            case ONT_TOTALES:
                //Agregar estos valores a constantes:
                consultar = "T";
                descripcion_corta = "[Vip, Empresariales, residenciales]";
                descripcion_larga = "Resumen del estado de todas las onts";
                auxiliar.setTipo(consultar);
                auxiliar.setDateTime(LocalDateTime.now());
                auxiliar.setDescripcionCorta(descripcion_corta);
                auxiliar.setDescripcionLarga(descripcion_larga);
                List<datosRegionDto> datosTotales = inventario.getTotalesRegion();
                auxiliar.setResumenStatusOnts(datosTotales);
                break;
            case ONT_EMPRESARIALES:
                consultar = "E";
                descripcion_corta = "Empresariales";
                descripcion_larga = "Resumen del estado de las onts empresariales";
                auxiliar.setTipo(consultar);
                auxiliar.setDateTime(LocalDateTime.now());
                auxiliar.setDescripcionCorta(descripcion_corta);
                auxiliar.setDescripcionLarga(descripcion_larga);
                List<datosRegionDto> datosEmpresariales = inventario.getTotalesEmpresariales();
                auxiliar.setResumenStatusOnts(datosEmpresariales);
                break;
            case ONT_VIP:
                //Settea los datos:
                consultar = "V";
                descripcion_corta = "Vip";
                descripcion_larga = "Resumen del estado de las onts Vip";
                auxiliar.setTipo(consultar);
                auxiliar.setDateTime(LocalDateTime.now());
                auxiliar.setDescripcionCorta(descripcion_corta);
                auxiliar.setDescripcionLarga(descripcion_larga);
                List<datosRegionDto> datosVip = inventario.getTotalesRegionesVips();
                auxiliar.setResumenStatusOnts(datosVip);
                break;
        }

        return CompletableFuture.completedFuture(auxiliar);
    }
}
