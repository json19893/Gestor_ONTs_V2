package totalplay.monitor.snmp.com.negocio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import totalplay.monitor.snmp.com.negocio.service.ICargaOntSaService;
import totalplay.monitor.snmp.com.persistencia.entidad.CargaOntSaEntity;
import totalplay.monitor.snmp.com.persistencia.repository.ICargaOntSaRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CargaOntSaServiceImpl implements ICargaOntSaService {

    @Autowired
    ICargaOntSaRepository cargaOntSaRepository;


    @Override
    public boolean archivoSA(MultipartFile fileSA) {
        try {
            //se eliminan datos anteriores
            cargaOntSaRepository.deleteAll();

            List<String> listSeries = new ArrayList<>();


            //guardamos nuevo registros
            List<CargaOntSaEntity> listaActualizar = guardarListaSerieDeArchivo(fileSA);

            cargaOntSaRepository.updateSAporSerieOnt();

        } catch (Exception e) {
            return false;
        }




        return true;
    }

    private List<CargaOntSaEntity> guardarListaSerieDeArchivo(MultipartFile fileSA) {

        List<CargaOntSaEntity> listRes = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(fileSA.getInputStream()));
            CargaOntSaEntity objOnt = null;
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {

                String serie = line.split(",")[0];

                if (!serie.equals("serie")) {
                    objOnt = new CargaOntSaEntity();

                    objOnt.setSerie(serie);

                 //   System.out.println("__________ "+serie);

                    listRes.add(objOnt);

             //       cargaOntSaRepository.save(objOnt);
                }

            }
            br.close();
            cargaOntSaRepository.saveAll(listRes);

        } catch (Exception e) {

            e.printStackTrace();
            // Manejar cualquier excepción que ocurra durante el proceso
            // return "Error al procesar el archivo: " + e.getMessage();
        }
        return listRes;
    }
}
