package totalplay.services.com;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import totalplay.services.com.negocio.dto.requestEstatusDto;
import totalplay.services.com.negocio.service.IapiService;
import totalplay.services.com.persistencia.entidad.InventarioOntResp;
import totalplay.services.com.persistencia.entidad.inventarioOntsEntidad;
import totalplay.services.com.persistencia.repositorio.InventarioOntRespRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InventarioOntRespRepositoryTest {
    @Autowired
    InventarioOntRespRepository repository;
    @Autowired
    IapiService service;

    @Test
    public void probarFuncion() {
        List<InventarioOntResp> response = repository.getOntsRespaldo(394, "0", 0, 0, 1);
        System.out.println("Hola mundo desde services");
    }

    @Test
    public void probarServicio() {
        requestEstatusDto tmp = new requestEstatusDto();
        tmp.setFrame(0);
        tmp.setSlot(1);
        tmp.setPort(0);
        tmp.setUid("0");
        tmp.setIp("10.71.59.238");
        tmp.setEstatus("1");
        tmp.setDescripcionAlarma("test");

        List<requestEstatusDto> datos = new ArrayList<>();
        datos.add(tmp);
        try {
            service.putStatusOnt(datos);
            System.out.println("Hola mundo desde services");
        } catch (Exception e) {

        }


    }
}
