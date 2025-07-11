package totalplay.snmpv2.com.persistencia.entidades;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "monitor_poleo_olt_metrica")
@Data
@NoArgsConstructor

public class MonitorPoleoOltMetricaEntity {
	@Id
	private String id;
	private Integer id_olt;
	private Integer id_metrica;
	private Date fecha_inicio;
	private Date fecha_fin;
	private String id_poleo;
	private String resultado;
	private boolean error;

	public MonitorPoleoOltMetricaEntity(Integer id_olt, Integer id_metrica, Date fecha_inicio, String id_poleo) {
		super();
		this.id_olt = id_olt;
		this.id_metrica = id_metrica;
		this.fecha_inicio = fecha_inicio;
		this.id_poleo =id_poleo;
		
	}
	
	
	
	
	
	
}
