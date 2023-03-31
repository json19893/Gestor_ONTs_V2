package totalplay.snmpv2.com.presentacion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import totalplay.snmpv2.com.negocio.dto.PostMetricaResponse;
import totalplay.snmpv2.com.negocio.dto.RequestPostMetrica;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;

@RestController
@Slf4j
public class MetricaController {
    @Autowired
    IpoleoMetricasService service;

    /**
     * Este metodo exponse un punto de acceso para consultar el poleo de una metrica especifica indicada por el cliente
     *
     * @param request
     * @return ResponseEntity<?> Respuesta del servidor hacia el cliente
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @GetMapping(value = "/metrica/poleo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMetricaByNum_serial(@RequestBody RequestPostMetrica request) {
        PostMetricaResponse responseProcess = service.getPoleoOntMetrica(request);
        ResponseEntity<PostMetricaResponse> responseServer;

        if (responseProcess.getCod() == 0) {
            responseServer = new ResponseEntity("Success: la metrica ha sido consultada exitosamente", HttpStatus.OK);
        } else {
            //Errores del negocio
            //Errores del cliente
            responseServer = new ResponseEntity("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseServer;
    }
}
