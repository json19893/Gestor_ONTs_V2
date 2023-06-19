package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "auxiliar_olts_nce_estatus")
@Data
@NoArgsConstructor

public class PoleoEstatusOltsNCEEntity extends GenericPoleosDto {
	
	
	
}
