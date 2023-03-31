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
    public static final int RESOURCE_NOT_FOUND = 5;
    public static final int SNMP_COMMAND_ERROR = 1;
    @Autowired
    IpoleoMetricasService service;

    /**
     * Este metodo expone un punto de acceso para polear una metrica asociada a una ont.
     *
     * @param request
     * @return ResponseEntity<?> Respuesta: Es una envoltura donde contiene un codigo de estatus http junto con la respuesta del servidor
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @GetMapping(value = "/metrica/poleo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMetricaByNum_serial(@RequestBody RequestPostMetrica request) {
        ResponseEntity<PostMetricaResponse> responseWrapperServer = null;
        PostMetricaResponse response = null;
        try {
            response = service.getPoleoOntMetrica(request);
            if (response.getCod().intValue() == 0) {
                return responseWrapperServer = new ResponseEntity(response, HttpStatus.OK);
            } else {
                //Errores del negocio
                //Errores del cliente
                if (response.getCod().intValue() == RESOURCE_NOT_FOUND) {
                    return responseWrapperServer = new ResponseEntity(response, HttpStatus.NOT_FOUND);
                }

                if (response.getCod().intValue() == SNMP_COMMAND_ERROR) {
                    return responseWrapperServer = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e) {
            return responseWrapperServer = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseWrapperServer;
    }
}
