package totalplay.snmpv2.com.persistencia.entidades;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import totalplay.snmpv2.com.negocio.dto.GenericPoleosDto;

@Document(collection = "tb_poleos_frame_slot_port")
@Data
@NoArgsConstructor

public class PoleosFrameSlotPortEntity extends GenericPoleosDto {
	
}
