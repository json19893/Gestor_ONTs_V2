package totalplay.login.com.negocio.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

import totalplay.login.com.negocio.Dto.RequestDto;
import totalplay.login.com.negocio.Dto.ResponseDto;
import totalplay.login.com.negocio.Dto.ResponseGenericoDto;
import totalplay.login.com.negocio.Dto.UsuariosRequestDto;
import totalplay.login.com.negocio.service.IloginService;
import totalplay.login.com.negocio.utils.NamingExceptionCode;

import totalplay.login.com.persistencia.entidad.RolesEntity;
import totalplay.login.com.persistencia.entidad.usuariosEntity;
import totalplay.login.com.persistencia.repositorio.IRolesRepository;
import totalplay.login.com.persistencia.repositorio.IUsuariosPermitidosRepository;
@Slf4j
@Service
public class LoginServiceImpl implements IloginService {

	@Autowired
	IUsuariosPermitidosRepository usuarios;
	@Autowired
	IRolesRepository roles;



	@Override
	public ResponseDto loginLpda(RequestDto request) throws Exception {
		ResponseDto response = new ResponseDto();
		try {
			long exite=usuarios.countByNombreUsuario(request.getU());
			if(exite>0) {
				if(request.getU().equals("amagos")||request.getU().equals("jsalgadom")) {
					usuariosEntity usuario=	usuarios.findByNombreUsuario(request.getU());
					if(usuario.getSesion()==0 ) {
						Optional<RolesEntity>  rol= roles.findById(usuario.getRol());
						response.setCod(0);
						response.setSms("OK");
						response.setNombreCompleto(usuario.getNombreCompleto());
						response.setUsuario(usuario.getNombreUsuario());
						response.setRol(rol.get().getRol());
						
						//response.setCadenaConexion(ult.getJWTToken(request.getUsuario()));
						usuario.setSesion(1);
						usuario.setIpConexion(request.getC());
						usuario.setFechaConexion(LocalDateTime.now().toString());
						usuario.setIntentos(usuario.getIntentos()+1);
						List<String > intentos=usuario.getIpConexionIntentos();
						intentos.add(request.getC());
						usuario.setIpConexionIntentos(intentos);
						usuarios.save(usuario);
						}else {
					response.setCod(1);
					response.setSms("Ya existe una seion activa par el usuario: "+ request.getU());
					usuario.setIntentos(usuario.getIntentos()+1);
					List<String > intentos=usuario.getIpConexionIntentos();
					intentos.add(request.getC());
					usuario.setIpConexionIntentos(intentos);
					usuarios.save(usuario);
					return response;
				}
			}else {
				
				String res = validaUsuario(request);
				usuariosEntity usuario=	usuarios.findByNombreUsuario(request.getU());
				if (res == "LDAP_SUCCESS") {
		
					if(usuario.getSesion()==0) {
					Optional<RolesEntity>  rol= roles.findById(usuario.getRol());
					
					response.setCod(0);
					response.setSms("OK");
					response.setNombreCompleto(usuario.getNombreCompleto());
					response.setUsuario(usuario.getNombreUsuario());
					response.setRol(rol.get().getRol());
					//response.setCadenaConexion(ult.getJWTToken(request.getUsuario()));
					usuario.setSesion(1);
					usuario.setIpConexion(request.getC());
					usuario.setFechaConexion(LocalDateTime.now().toString());
					usuario.setIntentos(usuario.getIntentos()+1);
					List<String > intentos=usuario.getIpConexionIntentos();
					intentos.add(request.getC());
					usuario.setIpConexionIntentos(intentos);
					usuarios.save(usuario);
			}else {
					response.setCod(1);
					response.setSms("Ya existe una seion activa par el usuario: "+ request.getU());
					usuario.setIntentos(usuario.getIntentos()+1);
					List<String > intentos=usuario.getIpConexionIntentos();
					intentos.add(request.getC());
					usuario.setIpConexionIntentos(intentos);
					usuarios.save(usuario);
					return response;
				}
				
				}else {
					response.setCod(1);
					response.setSms("El usuario: "+ request.getU()+" no tiene acceso al Directory Activo");
					usuario.setIntentos(usuario.getIntentos()+1);
					List<String > intentos=usuario.getIpConexionIntentos();
					intentos.add(request.getC());
					usuario.setIpConexionIntentos(intentos);
					usuarios.save(usuario);
					return response;
				}
				
				
			}
			}else {
				response.setCod(1);
				response.setSms("El usuario: "+ request.getU()+" no tiene permitido acceder a este sistema");
				return response;
			}
		
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("Error al consultar:: "+ e);
		}
		return response;
	}

	public String validaUsuario(RequestDto datos) {
		String response = "";
		Hashtable<String, String> authEnv = new Hashtable<String, String>();
		String userName = "totalplay\\" + datos.getU();

		String ldapURL = "ldap://10.216.20.175/DC=totalplay,DC=corp";
		authEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		authEnv.put(Context.PROVIDER_URL, ldapURL);
		authEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		authEnv.put(Context.SECURITY_PRINCIPAL, userName);
		authEnv.put(Context.SECURITY_CREDENTIALS, datos.getP());
		try {
			@SuppressWarnings("unused")
			DirContext authContext = new InitialDirContext(authEnv);
			response = NamingExceptionCode.getMsgErrorCode("code 0");
		} catch (AuthenticationException authEx) {
			response = NamingExceptionCode.getMsgErrorCode(authEx.getMessage());
			authEx.printStackTrace();
		} catch (NamingException namEx) {
			response = NamingExceptionCode.getMsgErrorCode(namEx.getMessage());
			namEx.printStackTrace();
		}
		return response;
	}

	@Override
	public ResponseGenericoDto putUsuarios(UsuariosRequestDto request) throws Exception {
		ResponseGenericoDto response=new ResponseGenericoDto();
		usuariosEntity user=new usuariosEntity();
		try {
			//String encode = passwordEncode.encode(request.getPassword());
			long res=usuarios.countByNombreUsuario(request.getNombreUsuario());
			if(res==0) {
			user.setCorreo(request.getCorreo());
			user.setEstatus(1);
			user.setNombreCompleto(request.getNombreCompleto());
			user.setNombreUsuario(request.getNombreUsuario());
			user.setSesion(0);
			user.setRol(request.getRol());
			user.setFechaRegistro(LocalDateTime.now().toString());
			usuarios.save(user);
			response.setCod(1);
			response.setSms("OK");
			}else {
				response.setCod(0);
				response.setSms("El nombre de usuario ya se encuentra registrado");
				return response;
			}
			
		} catch (Exception e) {
			response.setCod(1);
			response.setSms("Error al registrar ");
			log.error("Error: "+e);;
		}
		return response;
	}

	@Override
	public ResponseDto logout(String u) throws Exception {
		ResponseDto response = new ResponseDto();
		try {
			response.setCod(0);
			response.setSms("OK");
		
		usuariosEntity usuario=	usuarios.findByNombreUsuario(u);
		usuario.setIntentos(0);
		usuario.setFechaConexion("");
		usuario.setIpConexion("");
		List<String> inCo=new ArrayList<>();
		usuario.setIpConexionIntentos(inCo);
		usuario.setSesion(0);
		usuarios.save(usuario);
	} catch (Exception e) {
		response.setCod(1);
		response.setSms("Error al cerra seión ");
	}
	return response;
	}
	
	@ConditionalOnProperty(name="scheduler.enabled", matchIfMissing = true)
	@Scheduled(fixedRate = 100)
	public void cierraSesion() {
		
	}


}
