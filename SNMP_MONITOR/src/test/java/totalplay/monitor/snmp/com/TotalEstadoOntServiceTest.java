package totalplay.monitor.snmp.com;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import totalplay.monitor.snmp.com.negocio.dto.totalesActivoDto;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IEstadoOntsResumenService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioOntsTotalesActivoEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioOntsTotalesActivoRepositorio;
import totalplay.monitor.snmp.com.presentacion.controller.monitorController;

@SpringBootTest
public class TotalEstadoOntServiceTest {
    @Autowired
    IEnvoltorioOntsTotalesActivoRepositorio repositorio;
    @Autowired
    IEstadoOntsResumenService repository;


    @Test
    public void endpointTest(){
        try{
            new monitorController().getTotalesActivo("T");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void probarFlujo() {
        totalesActivoDto response1 = getTotalesActivo("E");
        totalesActivoDto response2 = getTotalesActivo("V");
        totalesActivoDto response3 = getTotalesActivo("T");

        System.out.println("response");
    }

    public totalesActivoDto getTotalesActivo(String tipo) {
        //Tiempo diferencia
        //Apunta al objeto generado

        //Proceso:
        //repository.process(); //14:41
        EnvoltorioOntsTotalesActivoEntidad resumenOnts = repositorio.findAll(Sort.by(Sort.Direction.DESC, "id")).get(0);
        //Destructuramos el objeto
        if (tipo.equals(resumenOnts.getTotalesOntsActivas().getTipo())) {
            return resumenOnts.getTotalesOntsActivas().getResumenStatusOnts();
        }

        if (tipo.equals(resumenOnts.getTotalesOntsActivasVips().getTipo())) {
            return resumenOnts.getTotalesOntsActivasVips().getResumenStatusOnts();
        }

        if (tipo.equals(resumenOnts.getTotalesOnstsActivasEmpresariales().getTipo())) {
            return resumenOnts.getTotalesOnstsActivasEmpresariales().getResumenStatusOnts();
        }
        return null;
    }
}
