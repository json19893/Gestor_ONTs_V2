package totalplay.monitor.snmp.com.persistencia.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import totalplay.monitor.snmp.com.negocio.dto.oltsCMDBDto;
import totalplay.monitor.snmp.com.persistencia.dao.IOltsCMDBDAO;

@Component
public class OltsCMDBDAOImpl implements IOltsCMDBDAO<oltsCMDBDto> {

	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public OltsCMDBDAOImpl(JdbcTemplate jdbcTemplate) {
		
		this.jdbcTemplate = jdbcTemplate;
	}



	@Override
	public List<oltsCMDBDto> getOlts() {
		String sql="SELECT ow.resource_name, ow.host_name, ow.ip_address, us.zregion\n"
				+ "from sdm_ca_owned_resource ow\n"
				+ "left join sdm_ca_resource_class clas on ow.resource_class = clas.id\n"
				+ "left join sdm_usp_owned_resource us on ow.own_resource_uuid = us.owned_resource_uuid\n"
				+ "where ow.inactive = 0\n"
				+ "and ow.resource_class = 1000915\n"
				+ "order by ow.own_resource_id;";
		return jdbcTemplate.query(sql, (rs, rowNum)->{
			oltsCMDBDto olt = new oltsCMDBDto();
			olt.setResource_name(rs.getString("resource_name"));
			olt.setHost_name(rs.getString("host_name"));
			olt.setIp_address(rs.getString("ip_address"));
			olt.setZregion(rs.getString("zregion"));
			return olt;			
		});
		
	}

}
