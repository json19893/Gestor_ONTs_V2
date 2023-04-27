package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_poleos_time_out")
@Data
@NoArgsConstructor

public class PoleosTimeOutEntity extends GenericPoleosDto {
	
}
