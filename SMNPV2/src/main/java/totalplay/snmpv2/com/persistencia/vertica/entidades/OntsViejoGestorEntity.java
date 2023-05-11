package totalplay.snmpv2.com.persistencia.vertica.entidades;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "mod_adhoc_itsm_1.valor_metricas_onts_actual")
public class OntsViejoGestorEntity {
	@Id
	@Column(name = "id_ont")
	private Integer id_ont;
	
	@Column(name = "num_serie")
	private String numero_serie;

	@Column(name = "ip_olt")
	private String ip_olt;
	
	@Column(name = "frame")
	private String frame;

	@Column(name = "slot")
	private String slot;
	
	@Column(name = "puerto")
	private String port;
	
	@Column(name = "run_status")
	private String estatus;
	
	@Column(name = "last_down_cause")
	private String last_down_cause;
	
	@Column(name = "last_down_time")
	private String last_down_time;
	
}
