package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RequestPostMetrica implements Serializable {
    private String numero_serie;
    private Integer idMetrica;
}
