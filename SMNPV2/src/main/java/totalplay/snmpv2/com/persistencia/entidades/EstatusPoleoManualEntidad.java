package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_descubrimiento_manual")
@Data
@NoArgsConstructor
public class EstatusPoleoManualEntidad {
    @Id
	public String id;
	public Integer idOlt;
	public String ip;
	public String nombre;
	public String fecha;
	public Integer estatus;
	public String descripcion;
	public Integer onts;
	public String usuario;

	public EstatusPoleoManualEntidad(Integer idOlt,String ip,String nombre,
	String fecha,Integer estatus,String descripcion,Integer onts,String usuario) {
		this.idOlt = idOlt;
		this.ip = ip;
		this.nombre = nombre;
		this.fecha = fecha;
		this.estatus = estatus;
		this.descripcion = descripcion;
		this.onts = onts;
		this.usuario = usuario;
	}
}
