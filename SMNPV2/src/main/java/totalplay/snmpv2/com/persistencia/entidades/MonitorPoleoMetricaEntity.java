package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "monitor_poleo_metrica")
@Getter
@Setter
@ToString
public class MonitorPoleoMetricaEntity {
	@Id
	private String id;
	private Integer id_metrica;
	private String fecha_inicio;
	private String fecha_fin;
	private String id_poleo;
	private Integer ontsSnmp;

	

	public MonitorPoleoMetricaEntity(Integer id_metrica, String fecha_inicio, String id_poleo) {
		super();
		this.id_metrica = id_metrica;
		this.fecha_inicio = fecha_inicio;
		this.id_poleo = id_poleo;
	}
	
	
	
	
}
