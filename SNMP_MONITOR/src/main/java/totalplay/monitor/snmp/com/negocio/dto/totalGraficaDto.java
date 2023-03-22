package totalplay.monitor.snmp.com.negocio.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class totalGraficaDto {
	public String  category;
	public Integer value;
	public Integer full;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getFull() {
		return full;
	}
	public void setFull(Integer full) {
		this.full = full;
	}
	

}
