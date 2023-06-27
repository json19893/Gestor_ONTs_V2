package totalplay.services.com.persistencia.entidad;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "tbl_olts_polear_estatus")
public class oltsNcePolearEntidad {
    private String id;
    private String nombre;
    private Integer estatus_poleo;//0 sin polear,1-ejecutando,2-terminado
    private Date fecha_registro;
    private Date fecha_poleo;
    private Integer id_olt; 
}
