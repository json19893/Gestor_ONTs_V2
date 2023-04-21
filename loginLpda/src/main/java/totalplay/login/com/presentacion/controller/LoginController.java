package totalplay.login.com.presentacion.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import totalplay.login.com.negocio.Dto.RequestDto;
import totalplay.login.com.negocio.Dto.ResponseDto;
import totalplay.login.com.negocio.Dto.ResponseGenericoDto;
import totalplay.login.com.negocio.Dto.UsuariosRequestDto;
import totalplay.login.com.negocio.service.IloginService;
@RestController
public class LoginController {
	@Autowired
	IloginService loginService;

	 @PostMapping("/login")
	  public ResponseDto  login(@RequestBody (required = true) RequestDto request ) throws Exception { 
		    return loginService.loginLpda(request);
		  }
	
		@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST })
	 @PostMapping("/putUsuarios")
	  public ResponseGenericoDto  putUsuarios(@RequestBody(required = true) UsuariosRequestDto request ) throws Exception { 
		    return loginService.putUsuarios(request);
		  }

}
