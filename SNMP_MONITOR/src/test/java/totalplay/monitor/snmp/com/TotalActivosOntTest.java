package totalplay.monitor.snmp.com;

/*import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.service.ImonitorService;

import totalplay.monitor.snmp.com.negocio.dto.EnvoltorioAuxiliarDto;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioOntsTotalesActivoRepositorio;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static totalplay.monitor.snmp.com.negocio.util.constantes.*;


@SpringBootTest
public class TotalActivosOntTest {
    @Autowired
    IEnvoltorioOntsTotalesActivoRepositorio repositorio;
    @Autowired
    ImonitorService negocio;



    @Test
    void testRepository() {
        //List<EnvoltorioOntsTotalesActivoEntidad> totales = repositorio.findAll();
        System.out.println("Hola mundo");
    }

    //Agrega un cron para ejecutar cada dos minutos
    @Test
    @Scheduled(fixedDelay = 120000)
    public void process() {
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
            System.out.println("");
            //Finalmente se persiste la informacion procesada:
            EnvoltorioOntsTotalesActivoEntidad persistence = repositorio.save(envoltura);
            if (persistence != null) {
                System.out.println("Resumen persistido en el repositorio");
            }
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }

    /** Consulta el estado de las puntas en funcion del parametro
     * @param tipo representa el resumen del estado de las puntas
     * @return
     */
   /*  @Test
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
*/