package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarOntsByTecnologiaDto;
import totalplay.monitor.snmp.com.negocio.dto.datosRegionDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IObtenerOntsByTecnologiaService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.entidad.TotalesByTecnologiaEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.ITotalesByTecnologiaRepository;
import totalplay.monitor.snmp.com.persistencia.repository.IinventarioOntsRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;

@Service
public class ObtenerOntsByTecnologiaServiceImpl implements IObtenerOntsByTecnologiaService {
    @Autowired
    ITotalesByTecnologiaRepository repository;
    @Autowired
    IinventarioOntsRepositorio inventario;

    @Autowired
    ImonitorService negocio;

    //process_negocio - target : negocio.getTotalesByTecnologia(T or E or V)
    @Override
    @Scheduled(fixedDelay = 60000, initialDelay = 5000)
    public synchronized void process() {
        System.out.println("Ejecutando proceso para consultar los totales de las onts por tecnologia");
        //Meter el tiempo que tomo para actualizar:+
        long time1 = System.currentTimeMillis();

        try {
            //Estructura Segundaria: Segundo nivel.
            CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> ontsTotales = obtenerResumenOntsByTecnologia(ONT_TOTALES);
            CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> ontsEmpresariales = obtenerResumenOntsByTecnologia(ONT_EMPRESARIALES);
            CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> ontsVips = obtenerResumenOntsByTecnologia(ONT_VIP);

            // Wait until they are all done
            CompletableFuture.allOf(ontsTotales, ontsEmpresariales, ontsVips).join();

            EnvoltorioAuxiliarOntsByTecnologiaDto resumenEstadoOntTotales = ontsTotales.get();  // ONT_TOTALES
            EnvoltorioAuxiliarOntsByTecnologiaDto resumenEstadoOntEmpresariales = ontsEmpresariales.get();  // ONT_EMPRESARIALES
            EnvoltorioAuxiliarOntsByTecnologiaDto resumenEstadoOntVip = ontsVips.get();  // ONT_VIP

            persistirInformacion(adapterEntidad(resumenEstadoOntTotales));
            persistirInformacion(adapterEntidad(resumenEstadoOntEmpresariales));
            persistirInformacion(adapterEntidad(resumenEstadoOntVip));

            //System.out.println("El proceso actualizacion del resumen: estatus de onts tomo: " +  df.format(minutos)  + " segundos en terminar la ejecuccion.");
            System.out.println("Finalizo el proceso para consultar los totales de las onts por tecnologia");
        } catch (Exception ex) {
            System.out.println("Error en el proceso para crear los resumenes de estatus para las onts. Reintentando... en 5 segundos");
        }
    }
    public TotalesByTecnologiaEntidad adapterEntidad(EnvoltorioAuxiliarOntsByTecnologiaDto dto){
        TotalesByTecnologiaEntidad entity = new TotalesByTecnologiaEntidad();
        entity.setDateTime(dto.getDate());
        entity.setTipo(dto.getTipo());
        entity.setDescripcionCorta(dto.getDescripcionCorta());
        entity.setDescripcionLarga(dto.getDescripcionLarga());
        entity.setResumenStatusOnts(dto.getResumenStatusOnts());
        return entity;
    }

    public void persistirInformacion(TotalesByTecnologiaEntidad entidad) {
        //Buscar entidad
        TotalesByTecnologiaEntidad existe = repository.getEntity(entidad.getTipo());
        if(existe != null){
            entidad.setId(existe.getId());
        }
        repository.save(entidad);
    }

    @Async("taskExecutor")
    private CompletableFuture<EnvoltorioAuxiliarOntsByTecnologiaDto> obtenerResumenOntsByTecnologia(String tipo) throws Exception {
        List<datosRegionDto> lista;
        String consultar = "";
        String descripcion_corta = "";
        String descripcion_larga = "";

        //Settea los parametros:
        switch (tipo) {
            //Opcion por defecto:
            case ONT_TOTALES:
                //Agregar estos valores a constantes:
                consultar = "T";
                descripcion_corta = "{Vip, Empresariales, residenciales}";
                descripcion_larga = "Resumen del estado de todas las onts agrupada por: tipo y tecnologia";
                break;
            case ONT_EMPRESARIALES:
                consultar = "E";
                descripcion_corta = "Empresariales";
                descripcion_larga = "Resumen del estado de las onts Empresariales agrupada por: tipo y tecnologia";
                break;
            case ONT_VIP:
                //Settea los datos:
                consultar = "V";
                descripcion_corta = "Vip";
                descripcion_larga = "Resumen del estado de las onts Vips agrupada por: tipo y tecnologia";
                break;
        }

        //Estructura Segundaria: Segundo nivel.
        EnvoltorioAuxiliarOntsByTecnologiaDto auxiliar = new EnvoltorioAuxiliarOntsByTecnologiaDto();
        auxiliar.setTipo(consultar);
        auxiliar.setDate(LocalDateTime.now());
        auxiliar.setDescripcionCorta(descripcion_corta);
        auxiliar.setDescripcionLarga(descripcion_larga);

        List<datosRegionDto> totales = negocio.getTotalesByTecnologia(consultar);
        auxiliar.setResumenStatusOnts(totales);

        //return auxiliar;
        return CompletableFuture.completedFuture(auxiliar);
    }
}
