package totalplay.monitor.snmp.com.negocio.service.procesobatch.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import totalplay.monitor.snmp.com.persistencia.repository.IEnvoltorioOntsTotalesActivoRepositorio;

@Service
public class Task {
    @Autowired
    IEnvoltorioOntsTotalesActivoRepositorio repositorio;

    //Que se borre la tabla cada 10 minutos
    @Scheduled(fixedDelay = 600000)
    public void dropCollectionEstatusHistoryTmp() {
        if(repositorio.count() > 0){
            repositorio.deleteAll();
        }
    }
}
