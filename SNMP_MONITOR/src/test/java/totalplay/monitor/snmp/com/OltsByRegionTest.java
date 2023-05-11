package totalplay.monitor.snmp.com;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IOltsByRegionService;
import totalplay.monitor.snmp.com.persistencia.entidad.EnvoltorioGetOltsByRegionEntidad;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioGetOltsByRegionRepository;

@SpringBootTest
public class OltsByRegionTest {
    @Autowired
    IOltsByRegionService service;
    @Autowired
    IEnvoltorioGetOltsByRegionRepository repository;

    @Test
    public void process() throws Exception {
        //EnvoltorioGetOltsByRegionEntidad entidad = repository.obtenerEntidad(new Integer(100));
        //System.out.println("");
        service.process();
    }
}
