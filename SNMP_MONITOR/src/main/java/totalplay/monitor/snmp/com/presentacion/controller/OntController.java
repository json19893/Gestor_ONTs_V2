package totalplay.monitor.snmp.com.presentacion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import totalplay.monitor.snmp.com.negocio.dto.OntsRepetidasPorOltPostRequest;
import totalplay.monitor.snmp.com.negocio.dto.OntsRepetidasPorOltPostResponse;
import totalplay.monitor.snmp.com.negocio.service.IDiferenciaCargaManualService;
import totalplay.monitor.snmp.com.negocio.service.impl.DiferenciaCargaManualServiceImpl;

import java.util.List;

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
    @CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
    @RequestMapping(value = "/limpieza/onts/repetidas/{idOlt}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public ResponseEntity<OntsRepetidasPorOltPostResponse> obtenerOntsRepetidas(@PathVariable("idOlt") Integer idOlt) {
        ResponseEntity wrapperServerHttp = new ResponseEntity("", HttpStatus.OK);
        OntsRepetidasPorOltPostResponse response = new OntsRepetidasPorOltPostResponse();
        List<DiferenciaCargaManualServiceImpl.AuxOntsAdapter> coleccion;
        try {
            coleccion = service.consultarCatalogoOntsRepetidas(idOlt);
            response.setOnts(coleccion);
            if (response.getOnts().isEmpty()) {
                response.setOnts(coleccion);
                wrapperServerHttp = new ResponseEntity(response, HttpStatus.NOT_FOUND);
            } else {
                wrapperServerHttp = new ResponseEntity(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            wrapperServerHttp = new ResponseEntity("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return wrapperServerHttp;
    }
}
