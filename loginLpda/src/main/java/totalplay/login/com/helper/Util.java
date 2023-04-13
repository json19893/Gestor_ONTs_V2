package totalplay.login.com.helper;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Util {
    
    

    public TokenResponse token(String u,String p){
        
      
        TokenResponse tokenResponse = new TokenResponse();
        try {
            ResourceOwnerPasswordResourceDetails resourceDetails = new ResourceOwnerPasswordResourceDetails();
            resourceDetails.setUsername(u);
            resourceDetails.setPassword(p);
            resourceDetails.setClientId("jsalgadom");
            resourceDetails.setClientSecret("hola");
            resourceDetails.setGrantType("password");
            resourceDetails.setAccessTokenUri("http://localhost:8081/oauth/token");
            resourceDetails.setScope(Arrays.asList("read", "write"));
     
            OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
            restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter()));
     
            OAuth2AccessToken token = restTemplate.getAccessToken();
     
           
            tokenResponse.setAccessToken(token.getValue());
            tokenResponse.setTokenType(token.getTokenType());
            tokenResponse.setExpiresIn(token.getExpiresIn());
            //tokenResponse.setScope(token.getScope());
    
    } catch (Exception e) {
      log.error("error::: ", e);
    }
        return tokenResponse;
    }


@Data
@NoArgsConstructor
public static class TokenResponse {
    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private List<String> scope;

    // Getters and setters
}
}
