package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tb_inventario_onts_puertos")
@Data
@NoArgsConstructor
public class InventarioPuertosEntity {
	@Id
	private String _id;
	private String oid;
	private String uid;
	private String puerto;
	private Integer id_olt;
	private Integer id_region;
	private String fecha_poleo;
	private String index;
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof InventarioPuertosEntity) {
			return ((InventarioPuertosEntity)obj).getOid().equals(this.getOid());
		}
		return false;
	}

}
