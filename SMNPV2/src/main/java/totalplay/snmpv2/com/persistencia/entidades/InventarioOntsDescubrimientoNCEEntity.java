package totalplay.snmpv2.com.persistencia.entidades;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_inventario_onts_descubrimiento_nce")
@Data
@NoArgsConstructor
public class InventarioOntsDescubrimientoNCEEntity extends InventarioOntsEntity {
	
	
   
    
}
