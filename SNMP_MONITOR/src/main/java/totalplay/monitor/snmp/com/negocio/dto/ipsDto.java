package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ipsDto {	
    private String ip;
    private String errorlevel;
    private String desc;
    private String outfileb64;
    public Integer Send;
    public Integer Received;
    public Integer Packed_Loss;
    public Double Total_Time;
    public Double Min;
    public Double Avg;
    public Double Max;
    public Double mDev;
    
    
}


