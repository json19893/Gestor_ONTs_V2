package totalplay.services.com.negocio.service;

import java.util.List;

import totalplay.services.com.negocio.dto.requestAltaOnts;
import totalplay.services.com.negocio.dto.requestDto;
import totalplay.services.com.negocio.dto.requestEstatusDto;
import totalplay.services.com.negocio.dto.requestEstatusOltDto;
import totalplay.services.com.negocio.dto.responseDto;
import totalplay.services.com.negocio.dto.responseEstatusOltDto;
import totalplay.services.com.negocio.dto.respuestaDto;
import totalplay.services.com.negocio.dto.respuestaStatusDto;

public interface IapiService {

	respuestaDto getNumeroSerie(requestDto datos) throws Exception;

	respuestaStatusDto putStatusOnt(List<requestEstatusDto> datos) throws Exception;

	responseDto getConfiguracionOlt(String tecnologia) throws Exception;
	
	respuestaDto altaOnts(requestAltaOnts datos) throws Exception;

	respuestaDto validaONT(requestAltaOnts datos) throws Exception;

    responseEstatusOltDto putStatusOlt(List<requestEstatusOltDto> datos) throws Exception;

}
