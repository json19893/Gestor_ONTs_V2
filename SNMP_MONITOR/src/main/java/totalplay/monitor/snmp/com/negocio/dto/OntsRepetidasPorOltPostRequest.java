package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class OntsRepetidasPorOltPostRequest implements Serializable {
    //Agregar el UUID para la serializacion
    private Integer idOlt;
}
