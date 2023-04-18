package totalplay.services.com.negocio.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.extern.slf4j.Slf4j;
import totalplay.services.com.negocio.dto.ejecucionDto;


@Slf4j
public class util {
	


	public ejecucionDto execBash(String comando, String ruta) throws IOException {
        ejecucionDto response = new ejecucionDto();
       
        Process p;
        try {
            String shell = ruta + "pdm"+Thread.currentThread().getId() + ".sh";

            File file = new File(shell);
            
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(comando);
            bw.close();
            ProcessBuilder pb = new ProcessBuilder(new String[]{"sh", "-c", "chmod 777 " + shell + ";" + shell});
            p = pb.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            response.setBuffer(br);
            response.setProceso(p);
            
        } catch (Exception e) {
			response.setError(e.toString());
			log.error("Error: " + e);

        }

        return response;

    }
	
	public static boolean isBlankOrNull(String str) {
	    return (str == null || "".equals(str.trim()));
	}

}
