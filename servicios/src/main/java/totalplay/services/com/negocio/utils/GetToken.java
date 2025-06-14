package totalplay.services.com.negocio.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GetToken {

	public String getToken() {
		//Properties properties = Utils.properties();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut tokenRequest = new HttpPut("http://10.180.199.203:8080/ra/token/");
		StringEntity tokenParams;
		try {
			tokenParams = new StringEntity("{\"App\":\"app\"}");
			tokenRequest.addHeader("content-type", "application/json");
			tokenRequest.setEntity(tokenParams);

			HttpResponse tokenResponseHttp = httpClient.execute(tokenRequest);

			HttpEntity tokenEntity = tokenResponseHttp.getEntity();
			String tokenResult = EntityUtils.toString(tokenEntity);

			JSONObject jsonObject1 = new JSONObject(tokenResult);
			String token = jsonObject1.getString("token");

			return token;
		} catch (UnsupportedEncodingException e) {
			log.error("Error: "+e);
		} catch (ClientProtocolException e) {
			log.error("Error: "+e);
		} catch (IOException e) {
			log.error("Error: "+e);
		}
		return null;

	}

}