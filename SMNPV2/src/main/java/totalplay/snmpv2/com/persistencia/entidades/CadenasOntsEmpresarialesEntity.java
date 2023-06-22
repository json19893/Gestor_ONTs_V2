package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_cadenas_onts_empresariales")
@Data
@NoArgsConstructor
public class CadenasOntsEmpresarialesEntity  {
	@Id
	private String _id;
	private String numero_serie;
	private Integer id_olt;
	private Integer configuracion;
	private String tecnologia;
	private String metrica_1;
	private String metrica_2;
	private String metrica_3;
	private String metrica_4;
	private String metrica_5;
	private String metrica_6;
	private String metrica_7;
	private String metrica_8;
	private String metrica_9;
	private String metrica_10;
	private String metrica_11;
	private String metrica_12;
	private String metrica_13;
	private String metrica_14;
	private String metrica_15;
	private String metrica_16;
}
