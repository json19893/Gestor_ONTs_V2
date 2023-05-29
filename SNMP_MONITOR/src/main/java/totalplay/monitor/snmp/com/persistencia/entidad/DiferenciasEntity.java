package totalplay.monitor.snmp.com.persistencia.entidad;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.monitor.snmp.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_diferencias")
@Data
@NoArgsConstructor
public class DiferenciasEntity extends GenericPoleosDto {
   
    
}
