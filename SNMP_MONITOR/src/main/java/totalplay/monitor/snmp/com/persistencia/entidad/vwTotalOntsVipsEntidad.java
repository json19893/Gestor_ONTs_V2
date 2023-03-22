package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "vw_total_onts_vips")
@Getter
@Setter
@ToString
public class vwTotalOntsVipsEntidad {
	@Id
	private String	_id;	
	private Integer id_olt;
	private String nombre;
	private String ip;
	private String 	descricion;
	private Integer 	id_region;
	private Integer total_onts;
	private String tecnologia;
	
	
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Integer getId_olt() {
		return id_olt;
	}
	public void setId_olt(Integer id_olt) {
		this.id_olt = id_olt;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDescricion() {
		return descricion;
	}
	public void setDescricion(String descricion) {
		this.descricion = descricion;
	}
	public Integer getId_region() {
		return id_region;
	}
	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}
	public Integer getTotal_onts() {
		return total_onts;
	}
	public void setTotal_onts(Integer total_onts) {
		this.total_onts = total_onts;
	}
	public String getTecnologia() {
		return tecnologia;
	}
	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof vwTotalOntsVipsEntidad) {
			return ((vwTotalOntsVipsEntidad) obj).getIp().equals(this.getIp());
		}
		return false;
	}
	

}
