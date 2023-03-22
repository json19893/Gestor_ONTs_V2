package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
    
    public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getErrorlevel() {
		return errorlevel;
	}
	public void setErrorlevel(String errorlevel) {
		this.errorlevel = errorlevel;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOutfileb64() {
		return outfileb64;
	}
	public void setOutfileb64(String outfileb64) {
		this.outfileb64 = outfileb64;
	}
	public Integer getSend() {
		return Send;
	}
	public void setSend(Integer Send) {
		this.Send = Send;
	}
	public Integer getReceived() {
		return Received;
	}
	public void setReceived(Integer Received) {
		this.Received = Received;
	}
	public Integer getPacked_Loss() {
		return Packed_Loss;
	}
	public void setPacked_Loss(Integer Packed_Loss) {
		this.Packed_Loss = Packed_Loss;
	}
	public Double getTotal_Time() {
		return Total_Time;
	}
	public void setTotal_Time(Double Total_Time) {
		this.Total_Time = Total_Time;
	}
	public Double getMin() {
		return Min;
	}
	public void setMin(Double Min) {
		this.Min = Min;
	}
	public Double getAvg() {
		return Avg;
	}
	public void setAvg(Double Avg) {
		this.Avg = Avg;
	}
	public Double getMax() {
		return Max;
	}
	public void setMax(Double Max) {
		this.Max = Max;
	}
	public Double getmDev() {
		return mDev;
	}
	public void setmDev(Double mDev) {
		this.mDev = mDev;
	}    
}


