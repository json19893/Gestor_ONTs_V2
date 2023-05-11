package totalplay.monitor.snmp.com;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IObtenerOntsByTecnologiaService;

@SpringBootTest
public class GetOntsByTecnologiaTest {
    @Autowired
    IObtenerOntsByTecnologiaService service;
    @Test
    void process(){
        service.process();
    }
}
