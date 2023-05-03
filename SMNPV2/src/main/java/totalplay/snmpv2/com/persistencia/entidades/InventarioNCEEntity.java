package totalplay.snmpv2.com.persistencia.entidades;


import javax.persistence.Column;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_inventario_onts_nce")
@Data
@NoArgsConstructor
public class InventarioNCEEntity  {
	@Id
	private String _id;
	private Integer id_ont;
	private String CIA;
	private String nombre_ont;
	private String user_vlan;
	private Integer frame;
	private String nombre_olt;
	private String etiqueta_ont;
	private String equipment_id;
	private Integer slot;
	private String ip_olt;
	private Integer status;
	private String create_time;
	private Integer port;	
	private String sn;

	
}
