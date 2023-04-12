package totalplay.snmpv2.com.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import totalplay.snmpv2.com.negocio.dto.OntsRepetidasPorOltPostRequest;
import totalplay.snmpv2.com.negocio.dto.OntsRepetidasPorOltPostResponse;
import totalplay.snmpv2.com.negocio.dto.PostMetricaResponse;
import totalplay.snmpv2.com.negocio.services.IDiferenciaCargaManualService;

@RestController
@RequestMapping("/api/v2")
public class OntController {
    @Autowired
    IDiferenciaCargaManualService service;

    /**
     * Este proceso devuelve un arreglo con objetos onts. En cada objeto ont tiene asignada una lista con las olts donde estan repetidas para el proceso de limpieza.
     *
     * @param request
     * @return
     */
    @CrossOrigin(origins = "*", methods = RequestMethod.POST)
    @RequestMapping(value = "/limpieza/onts/repetidas", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<PostMetricaResponse> obtenerOntsRepetidas(@RequestBody OntsRepetidasPorOltPostRequest request) {
        OntsRepetidasPorOltPostResponse response = new OntsRepetidasPorOltPostResponse();
        try {
            response.setOnt(service.consultarOntsRepetidasPorOlt(request));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
