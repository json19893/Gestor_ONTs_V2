package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;
import totalplay.snmpv2.com.negocio.dto.OntsConfiguracionDto;

@Document(collection = "faltantes_metricas_manual")
@Data
@NoArgsConstructor
public class FaltantesMetricasManualEntity extends OntsConfiguracionDto {	 
	
}
