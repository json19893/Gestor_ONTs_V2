package totalplay.snmpv2.com.presentacion;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.dto.RequestPostMetrica;
import totalplay.snmpv2.com.negocio.services.IpoleoMetricasService;

@RestController
@Slf4j

public class MetricaController {
    public static final int RESOURCE_NOT_FOUND = 5;
    public static final int SNMP_COMMAND_ERROR = 1;
    public static final int METRIC_NOT_SUPPORTED = 6;
    public static final int TECHNOLOGY_NOT_SUPPORTED = 7;
    public static final int RESOURCE_NOT_AVAILABLE = 8;
    @Autowired
    IpoleoMetricasService service;

    static class Message {
        //Significa que no existe una instancia asociado a ese OID
        public static final String INSTANCE_NOT_EXIST_WITH_OID = "No Such Instance currently exists at this OID";
        //Significa que el objeto no esta incluido en el MIB de Huawei
        public static final String IDENTIFIED_OBJECT_UNKNOW = "Unknown Object Identifier (Index out of range:  (ifIndex))";
        //Estado de la metrica
        public static final int RUN_STATUS = 1;
    }

    @Getter
    @Setter
    public static class MetricaPoleo {
        private String nombre;
        private String valor;
    }

    /**
     * Este metodo expone un punto de acceso para polear una metrica asociada a una ont.
     *
     * @param request
     * @return ResponseEntity<?> Respuesta: Es una envoltura donde contiene un codigo de estatus http junto con la respuesta del servidor
     */
 
     @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
     @PostMapping("/metrica/poleo")
    public ResponseEntity<?> getMetricaByNum_serial(@RequestBody RequestPostMetrica request) {
        ResponseEntity<GenericResponseDto> responseWrapperServer = null;
        GenericResponseDto response = new GenericResponseDto();

        try {
            response = service.getPoleoOntMetrica(request);
            if (response.getCod().intValue() == 0) {
                return responseWrapperServer = new ResponseEntity(response, HttpStatus.OK);
            } else {
                if (response.getCod().intValue() == RESOURCE_NOT_FOUND) {
                    return responseWrapperServer = new ResponseEntity(response, HttpStatus.NOT_FOUND);
                }

                if (response.getCod().intValue() == SNMP_COMMAND_ERROR) {
                    return responseWrapperServer = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } catch (Exception e) {
            response.setCod(1);
            response.setSms("Internal Server Error::: "+e);
            return responseWrapperServer = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseWrapperServer;
    }
}
