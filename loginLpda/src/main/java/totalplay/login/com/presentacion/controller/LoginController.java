package totalplay.login.com.presentacion.controller;

import javax.servlet.http.HttpServletRequest;

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
		ResponseDto response=new ResponseDto(); 
		try {
			response=	loginService.loginLpda(request);
		} catch (Exception e) {
		
		}
		return response;
		  }
	
	
	 @PostMapping("/putUsuarios")
	  public ResponseGenericoDto  putUsuarios(@RequestBody(required = true) UsuariosRequestDto request ) throws Exception { 
		    return loginService.putUsuarios(request);
		  }

	
	 @GetMapping(value = "/logout/{u}")
    public ResponseGenericoDto logout(@PathVariable("u") String u) throws Exception {
		    return loginService.logout(u);
		  }
		  @GetMapping("/getIp")
		  public ResponseGenericoDto handleRequest(HttpServletRequest request) {
			ResponseGenericoDto response = new ResponseGenericoDto();
			response.setCod(0);
			response.setSms(request.getRemoteAddr().toString());
	
			  return response;
		  }

}
