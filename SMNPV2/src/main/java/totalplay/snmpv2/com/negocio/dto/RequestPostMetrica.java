package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class RequestPostMetrica implements Serializable {
    private String numero_serie;
    private Integer idMetrica;
}
