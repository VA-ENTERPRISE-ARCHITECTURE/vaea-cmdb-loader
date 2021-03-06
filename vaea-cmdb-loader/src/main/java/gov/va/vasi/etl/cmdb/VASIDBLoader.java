package gov.va.vasi.etl.cmdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import gov.va.vasi.etl.cmdb.model.BusinessService;

@Repository
public class VASIDBLoader {

    private static final Logger LOG = Logger.getLogger(VASIDBLoader.class.getName());

    protected static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    protected static JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");

    public List<BusinessService> loadVASIData() {
	return jdbcTemplate.query(BUS_SERVICE_QUERY, new BusinessServiceMapper());
    }

    public boolean isVASIDBAccessible() {
	int i = 0;
	try {
	    i = jdbcTemplate.queryForObject("SELECT 1 FROM DUAL", Integer.class);
	} catch (Exception e) {
	    System.out.println("Not able to establish connection to VASI Database.");
	    return false;
	}

	if (i == 1) {
	    System.out.println("Successfully connected to VASI Database.");
	    return true;
	}
	return false;
    }

    private static final class BusinessServiceMapper implements RowMapper<BusinessService> {
	public BusinessService mapRow(ResultSet rs, int rowNumber) throws SQLException {
	    BusinessService busService = new BusinessService();
	    busService.setSystemID(rs.getBigDecimal("system_id"));
	    busService.setSystemAcronym(rs.getString("system_acronym"));
	    busService.setName(rs.getString("system_name"));
	    busService.setUsedFor(rs.getString("status"));
	    busService.setSponsorOrganization(rs.getString("sponsor_organization"));
	    busService.setAppCode(rs.getString("APPCODE"));
	    return busService;
	}
    }

    private static String BUS_SERVICE_QUERY = "SELECT system.system_id, system.system_acronym, system.system_name\r\n"
	    + "    , CASE WHEN org_level < 2 THEN organization_name ELSE nvl(parent_org, organization_acronym) END sponsor_organization\r\n"
	    + "    , CASE WHEN system_status_pick_list = 715 then 'Production' WHEN system_status_pick_list = 716 THEN 'Development' ELSE 'Inactive' END status\r\n"
	    + "    , lo3.description As responsible_org, apc.APPCODE\r\n"
	    + "FROM ee.element_attr_c56 system, ee.list_option lo3\r\n"
	    + "    ,(SELECT org.organization_id, org.organization_name, org.organization_acronym, org.org_level, porg.organization_name parent_org \r\n"
	    + "        FROM ee.element_attr_c53 org,  ee.element_attr_c53 porg WHERE org.parent_0_organization = porg.organization_id) o\r\n"
	    + "    , (select ac.APPCODE, sac.SYSTEM_ID from ee.element_attr_c2134 sac, ee.element_attr_c2133 ac \r\n"
	    + "        where sac.APPCODE_ELEMENTID = ac.ELEMENT_ID) apc\r\n"
	    + "where system.organization_id = o.organization_id(+) AND lo3.option_id(+) = system.OIT_OFFICE_OF_RESPONSIBILITY AND system.system_id = apc.system_id(+)\r\n"
	    + "ORDER BY system.system_id";
}
