package totalplay.snmpv2.com.presentacion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import totalplay.snmpv2.com.negocio.dto.CambioManualOntOltRequest;
import totalplay.snmpv2.com.negocio.dto.CambioManualOntOltResponse;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IUpdateTotalOntsService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.InventarioOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IinventarioOntsRepository;

@Slf4j
@RestController
public class OltsController {

    class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Autowired
    IUpdateTotalOntsService service;

    @Autowired
    IcatOltsRepository oltsRepository;

    @Autowired
    IinventarioOntsRepository ontsRepository;

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @GetMapping(value = "/olts/actualizartotales", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDto actualizarTotalOntsPorOlt() {
        return service.updateTotalOntsFromOlts();
    }

    /**
     * Este servicio cambia una ont de olt.
     */
    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @PostMapping(value = "/ont/cambiar/olt", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cambiarOntdeOlt(@RequestBody CambioManualOntOltRequest request) {
        CambioManualOntOltResponse response = new CambioManualOntOltResponse();
        ResponseEntity<CambioManualOntOltResponse> envoltorioRespuestaServidor;
        //Logica: Aqui agrupas los services para resolver necesidades del negocio
        try {
            //Manejo de errores del negocio:
            //Busca la olt por su id:
            CatOltsEntity olt = oltsRepository.getOlt(request.getIdOlt());
            if (olt == null || !olt.getEstatus().equals(1)) {
                response.setCod(1);
                response.setSms("Error en el negocio: Recurso no Disponible");
                return envoltorioRespuestaServidor = new ResponseEntity(response, HttpStatus.NOT_FOUND);
            }

            //Busca la ont por el numero de serie:
            InventarioOntsEntity ont = ontsRepository.getOntBySerialNumber(request.getNumeroSerie());
            if (ont == null) {
                response.setCod(1);
                response.setSms("Error en el negocio: Recurso no Disponible");
                return envoltorioRespuestaServidor = new ResponseEntity(response, HttpStatus.NOT_FOUND);
            }

            if (ont.getId_olt().equals(request.getIdOlt())) {
                response.setCod(1);
                response.setSms("Error en el negocio: la ont actualmente se encuentra asignada a la olt pasada como parametro");
                return envoltorioRespuestaServidor = new ResponseEntity(response, HttpStatus.OK);
            }

            //Se settea el nuevo idOlt de la ont
            ont.setId_olt(request.getIdOlt());
            //Se settean los demas valores en el objeto para actualizarlo
            ont.setFrame(request.getFrame());
            ont.setSlot(request.getSlot());
            ont.setPort(request.getPort());

            //Se empieza a ejecutar la logica del negocio (Service):
            InventarioOntsEntity ontActualizada = service.CambioManualOntdeOlt(ont);
            if (ontActualizada == null
                    && !(ontActualizada.getId_olt().equals(request.getIdOlt()))) {
                //Error no se pudo actualizar la ont
                response.setCod(1);
                response.setSms("Error en el negocio: No se pudo cambiar la ont de olt");
                return envoltorioRespuestaServidor = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

            }
            response.setCod(0);
            response.setSms("Peticion Exitosa: La ont fue cambiada de olt de forma correcta!");
            return envoltorioRespuestaServidor = new ResponseEntity(response, HttpStatus.OK);
        } catch (Exception ex) {
            //Manejo de errores del servidor:
            response.setCod(1);
            response.setSms("Error en el servidor");
            return envoltorioRespuestaServidor = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
