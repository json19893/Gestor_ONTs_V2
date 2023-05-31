package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "monitor_poleo_metrica")
@Data
@NoArgsConstructor

public class MonitorPoleoMetricaEntity {
	@Id
	private String id;
	private Integer id_metrica;
	private Date fecha_inicio;
	private Date fecha_fin;
	private String id_poleo;
	private Integer ontsSnmp;
	private String stop;
	private Date fecha_corte;

	

	public MonitorPoleoMetricaEntity(Integer id_metrica, Date fecha_inicio, String id_poleo) {
		super();
		this.id_metrica = id_metrica;
		this.fecha_inicio = fecha_inicio;
		this.id_poleo = id_poleo;
	}
	
	
	
	
}
