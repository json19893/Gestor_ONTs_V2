package totalplay.snmpv2.com.persistencia.entidades;


import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_inventario_onts_tmp")
@Data
@NoArgsConstructor
public class InventarioOntsTmpEntity extends GenericPoleosDto {
	//@DateTimeFormat(style="M-")//pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX")
	//@CreatedDate
	//private Date fecha_prueba;
   
    
}
