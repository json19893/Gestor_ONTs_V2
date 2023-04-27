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
public class BmsGestorOraVerticaEntity {
	
	@Column(name = "ONTID")
	private Integer id_ont;

	@Column(name = "CIA")
	private String CIA;
	
	@Column(name = "NOMBREONT")
	private String nombre_ont;

	@Column(name = "USERVLAN")
	private String user_vlan;
	
	@Column(name = "FRAME")
	private Integer frame;
	
	@Column(name = "NOMBREOLT")
	private String nombre_olt;
	
	@Column(name = "ETIQUETAONT")
	private String etiqueta_ont;
	
	@Column(name = "EQUIPMENTID")
	private String equipment_id;
	
	@Column(name = "SLOT")
	private Integer slot;
		
	@Column(name = "IPOLT")
	private String ip_olt;
	
	@Column(name = "STATUS")
	private Integer status;
	
	@Column(name = "CREATETIME")
	private String create_time;
	
	@Column(name = "PORT")
	private Integer port;	
	
	@Id
	@Column(name = "SN")
	private String sn;
}
