package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class OntsRepetidasPorOltPostResponse implements Serializable {
    List<EnvoltorioOntRepetidasOltsDto> ont;
}
