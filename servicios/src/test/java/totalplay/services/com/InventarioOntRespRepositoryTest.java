package totalplay.services.com;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import totalplay.services.com.negocio.dto.requestEstatusDto;
import totalplay.services.com.negocio.service.IapiService;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InventarioOntRespRepositoryTest {
    @Autowired
    IapiService service;

    @Test
    public void probarServicio() {
        requestEstatusDto tmp = new requestEstatusDto();
        tmp.setFrame(0);
        tmp.setSlot(1);
        tmp.setPort(0);
        tmp.setUid("0");
        tmp.setIp("10.71.59.238");
        tmp.setEstatus("1");
        tmp.setDescripcionAlarma("Cambio de estado de la ont: up or down");
        List<requestEstatusDto> datos = new ArrayList<>();
        datos.add(tmp);
        try {
            service.putStatusOnt(datos);
            System.out.println("Termina el flujo del proceso");
        } catch (Exception e) {

        }
    }
}
