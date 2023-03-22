package totalplay.snmpv2.com.persistencia.entidades;


import org.springframework.data.mongodb.core.mapping.Document;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_inventario_onts_erroneas")
@Data
@NoArgsConstructor
public class inventarioOntsErroneas extends GenericPoleosDto {
   
    
}
