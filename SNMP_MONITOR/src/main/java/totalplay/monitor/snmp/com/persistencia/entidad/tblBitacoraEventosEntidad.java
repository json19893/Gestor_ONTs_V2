package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tbl_bitacora_eventos")
@Data
@NoArgsConstructor
public class tblBitacoraEventosEntidad {
	@Id
	private String id;
	private String fecha;
	private String usuario;
	private String modulo;
	private String descripcion;
	
	public tblBitacoraEventosEntidad(String fecha,String usuario,String modulo,String descripcion) {
		this.fecha = fecha;
		this.usuario = usuario;
		this.modulo = modulo;
		this.descripcion=descripcion;
		
	}
	
}
