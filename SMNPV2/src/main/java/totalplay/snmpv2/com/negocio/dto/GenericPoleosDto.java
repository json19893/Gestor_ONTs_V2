package totalplay.snmpv2.com.negocio.dto;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericPoleosDto {
	@Id
	private String _id;
	private String oid;
	private String uid;
	private String valor;
	private Integer id_olt;
	private Integer id_metrica;
	private Date fecha_poleo;
	private Date fecha_modificacion;
	private Integer estatus;
	private String id_ejecucion;
	private Integer id_region;
	private Integer frame;
	private Integer slot;
	private Integer port;
	private String id_puerto;
	private String numero_serie;
	private String tecnologia;
	private String index;
	private String indexFSP;
	private String descripcion;
	

}
