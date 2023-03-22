package totalplay.services.com.negocio.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import totalplay.services.com.negocio.dto.ejecucionDto;



public class util {
	
	public  ejecucionDto ejecutaComando(String comando) throws IOException {
		Process p;
		ejecucionDto response = new ejecucionDto();

		System.out.println(comando.toString());
		try {
			p = Runtime.getRuntime().exec(comando);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			response.setBuffer(br);
			response.setProceso(p);

		} catch (IOException e) {
			response.setError(e.toString());
			e.printStackTrace();
			System.out.println("Error: " + e);

		}

		return response;
	}
	
	public static boolean isBlankOrNull(String str) {
	    return (str == null || "".equals(str.trim()));
	}

}
