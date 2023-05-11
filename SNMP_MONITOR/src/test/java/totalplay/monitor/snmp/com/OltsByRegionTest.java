package totalplay.monitor.snmp.com;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import totalplay.monitor.snmp.com.negocio.service.procesobatch.IOltsByRegionService;

@SpringBootTest
public class OltsByRegionTest {
    @Autowired
    IOltsByRegionService service;

    @Test
    public void process() throws Exception {
        service.process();
    }
}
