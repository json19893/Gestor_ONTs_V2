package totalplay.monitor.snmp.com.persistencia.entidad;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "totalesRegion")
@Getter
@Setter
@ToString
public class vwTotalOltsEntidad {
	public Integer id_region;
	public Integer totalOlts;

	public Integer getId_region() {
		return id_region;
	}

	public void setId_region(Integer id_region) {
		this.id_region = id_region;
	}

	public Integer getTotalOlts() {
		return totalOlts;
	}

	public void setTotalOlts(Integer totalOlts) {
		this.totalOlts = totalOlts;
	}

}
