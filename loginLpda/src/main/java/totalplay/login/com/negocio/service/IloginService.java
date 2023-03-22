package totalplay.login.com.negocio.service;

import totalplay.login.com.negocio.Dto.RequestDto;
import totalplay.login.com.negocio.Dto.ResponseDto;
import totalplay.login.com.negocio.Dto.ResponseGenericoDto;
import totalplay.login.com.negocio.Dto.UsuariosRequestDto;

public interface IloginService {

	ResponseDto loginLpda(RequestDto request) throws Exception;

	ResponseGenericoDto putUsuarios(UsuariosRequestDto request) throws Exception;;

}
