package totalplay.snmpv2.com.persistencia.entidades;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "cat_olts_nce")
public class CatOltsNCEEntity {
	@Id
	private String _id;
	private String nombre_olt;
	private String ip_olt;
	
}
