package totalplay.login.com.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import totalplay.login.com.persistencia.entidad.usuariosEntity;
import totalplay.login.com.persistencia.repositorio.IUsuariosPermitidosRepository;

@Component
public class InfoAdicionalToken implements TokenEnhancer{

	@Autowired
	IUsuariosPermitidosRepository usuarios;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<String, Object>();
		usuariosEntity usuario=	usuarios.findByNombreUsuario(authentication.getName());
		info.put("correo", usuario.getCorreo());
		info.put("fechaConexion", usuario.getFechaConexion());
		info.put("nombre", usuario.getNombreCompleto());
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
		
		return accessToken;
	}

    

}