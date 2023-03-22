package totalplay.snmpv2.com.presentacion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IUpdateTotalOntsService;

@Slf4j
@RestController
public class OltsController {
    @Autowired
    IUpdateTotalOntsService service;

    @CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
    @GetMapping(value = "/olts/actualizartotales", produces = MediaType.APPLICATION_JSON_VALUE)
    public GenericResponseDto actualizarTotalOntsPorOlt() {
        return service.updateTotalOntsFromOlts();
    }
}
