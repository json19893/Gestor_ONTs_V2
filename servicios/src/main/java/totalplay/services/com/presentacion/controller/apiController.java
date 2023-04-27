package totalplay.services.com.presentacion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import totalplay.services.com.negocio.dto.requestAltaOnts;
import totalplay.services.com.negocio.dto.requestDto;
import totalplay.services.com.negocio.dto.requestEstatusDto;
import totalplay.services.com.negocio.dto.responseDto;
import totalplay.services.com.negocio.dto.respuestaDto;
import totalplay.services.com.negocio.dto.respuestaStatusDto;
import totalplay.services.com.negocio.service.IapiService;

@RestController
public class apiController {
	@Autowired
	IapiService apiService;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@PostMapping(value = "/getNumeroSerie", produces = MediaType.APPLICATION_JSON_VALUE)
	public respuestaDto getNumeroSerie(@RequestBody requestDto datos) throws Exception {
		respuestaDto response = apiService.getNumeroSerie(datos);

		return response;
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@PutMapping(value = "/putStatusOnt", produces = MediaType.APPLICATION_JSON_VALUE)
	public respuestaStatusDto putStatusOnt(@RequestBody List<requestEstatusDto> datos) throws Exception {
		respuestaStatusDto response = apiService.putStatusOnt(datos);

		return response;
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@GetMapping(value = "/getConfiguracionOlt/{tecnologia}", produces = MediaType.APPLICATION_JSON_VALUE)
	public responseDto getConfiguracionOlt(@PathVariable("tecnologia") String tecnologia) throws Exception {
		responseDto response = apiService.getConfiguracionOlt(tecnologia);

		return response;
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@PostMapping(value = "/altas/altaOnts", produces = MediaType.APPLICATION_JSON_VALUE)
	public respuestaDto altaOnts(@RequestBody requestAltaOnts datos) throws Exception {
		
		respuestaDto response = apiService.altaOnts(datos);
		return response;
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	@PostMapping(value = "/altas/validaEstatus", produces = MediaType.APPLICATION_JSON_VALUE)
	public respuestaDto validaONT(@RequestBody requestAltaOnts datos) throws Exception {
		respuestaDto response = apiService.validaONT(datos);

		return response;
	}

}
