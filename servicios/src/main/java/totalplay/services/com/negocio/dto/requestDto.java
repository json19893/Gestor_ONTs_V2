package totalplay.services.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class requestDto {

	private Integer frame;
	private Integer slot;
	private Integer port;
	private String uid;
	private String ip;



}
