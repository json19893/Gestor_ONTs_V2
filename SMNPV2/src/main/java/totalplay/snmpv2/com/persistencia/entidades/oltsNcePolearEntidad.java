package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tbl_olts_polear_estatus")
@Data
@NoArgsConstructor

public class oltsNcePolearEntidad{
	@Id
	private String id;
    private String nombre;
    private Integer estatus_poleo;//0 sin polear,1-ejecutando,2-terminado
    private Date fecha_registro;
    private Date fecha_poleo;
    private Integer id_olt; 
	
}
