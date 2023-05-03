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
//@Document(collection = "tb_inventario_onts_nce")
@Table(name = "adquisicion.bms_gestor_ora")//, schema = "adquisicion")
public class CatOltsVerticaEntity {
	
	@Column(name = "NOMBREOLT")
	private String nombre_olt;
	
	@Id
	@Column(name = "IPOLT")
	private String ip_olt;
	

}
