package totalplay.oauth2.com.cofiguracion;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest loginDto) {
      try {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = null;
        OAuth2AccessToken accessToken = createAccessToken(authentication, userDetails);
        return ResponseEntity.ok(accessToken);
      } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
    }

    private OAuth2AccessToken createAccessToken(Authentication authentication, UserDetails userDetails) {
      Map<String, Object> additionalInfo = new HashMap<>();
      additionalInfo.put("user_details", userDetails);
      OAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(UUID.randomUUID().toString());
      ((DefaultOAuth2AccessToken) accessToken).setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(3600))));
      ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
      return accessToken;
    }

}




