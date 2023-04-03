package totalplay.snmpv2.com.negocio.dto;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Data
@ToString
public class RequestPostMetrica implements Serializable {
    private String numero_serie;
    private Integer idMetrica;
}
