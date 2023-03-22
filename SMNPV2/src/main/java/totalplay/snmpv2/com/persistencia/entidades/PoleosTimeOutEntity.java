package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_poleos_time_out")
@Getter
@Setter
@ToString
public class PoleosTimeOutEntity extends GenericPoleosDto {
	
}
