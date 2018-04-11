package gov.va.vasi.etl.cmdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import gov.va.vasi.etl.cmdb.model.BusinessService;

@Repository
public class VASIDBLoader {

	ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
	JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");

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

		if (i == 1){
			System.out.println("Successfully connected to VASI Database.");
			return true;
		}
		return false;
	}

	private static final class BusinessServiceMapper implements RowMapper<BusinessService> {
		public BusinessService mapRow(ResultSet rs, int rowNumber) throws SQLException {
			BusinessService busService = new BusinessService();
			busService.setSystemID(rs.getBigDecimal("system_id"));
			busService.setAltCIID(rs.getString("system_acronym"));
			busService.setName(rs.getString("system_name"));
			busService.setBusinessUnit(rs.getString("business_unit"));
			busService.setStatus(rs.getString("status"));
			busService.setResponsibleOrganization(rs.getString("responsible_org"));

			return busService;
		}
	}

	private static String BUS_SERVICE_QUERY = "SELECT system_id, system_acronym, system_name"
			+ " ,CASE WHEN org_level < 2 THEN organization_name ELSE nvl(parent_org, organization_acronym) END business_unit"
			+ " ,CASE WHEN system_status_pick_list = 715 then 'Production' WHEN system_status_pick_list = 716 THEN 'Development'	ELSE 'Inactive' END Status"
			+ " ,oit_office_of_responsibility" + " , CASE"
			+ " WHEN oit_office_of_responsibility = 1020 THEN  'Enterprise Operations'"
			+ " WHEN oit_office_of_responsibility = 1021 THEN  'Field Operations'"
			+ " WHEN oit_office_of_responsibility = 1022 THEN  'Enterprise Systems Engineering'"
			+ " WHEN oit_office_of_responsibility = 1023 THEN  'Enterprise Service Desk'"
			+ " WHEN oit_office_of_responsibility = 1062 THEN  'Financial Services Center'"
			+ " WHEN oit_office_of_responsibility = 1064 THEN  'Product Development'"
			+ " WHEN oit_office_of_responsibility = 1065 THEN  'Network Security Operations Center'"
			+ " WHEN oit_office_of_responsibility = 1066 THEN  'Office of Information Security'"
			+ " WHEN oit_office_of_responsibility = 1067 THEN  'Office of Human Resources and Administration'"
			+ " WHEN oit_office_of_responsibility = 1068 THEN  'Information Technology Resource Management'"
			+ " WHEN oit_office_of_responsibility = 1069 THEN  'Office of Acquisition, Logistics and Construction'"
			+ " WHEN oit_office_of_responsibility = 1070 THEN  'VA Office of Operations, Security, and Preparedness'"
			+ " WHEN oit_office_of_responsibility = 1071 THEN  null"
			+ " WHEN oit_office_of_responsibility = 1072 THEN  'Veterans Benefits Administration'"
			+ " WHEN oit_office_of_responsibility = 1073 THEN  'Healthcare Talent Management'"
			+ " WHEN oit_office_of_responsibility = 1074 THEN  'VHA Office of Informatics and Analytics'"
			+ " WHEN oit_office_of_responsibility = 1075 THEN  'VHA Chief Business Office'"
			+ " WHEN oit_office_of_responsibility = 1076 THEN  'Enterprise Operations'"
			+ " ELSE null  END responsible_org" + " FROM ee.element_attr_c56 system"
			+ " LEFT OUTER JOIN (SELECT org.organization_id, org.organization_name, org.organization_acronym, org.org_level, porg.organization_name parent_org FROM ee.element_attr_c53 org,  ee.element_attr_c53 porg WHERE org.parent_0_organization = porg.organization_id) o ON system.organization_id = o.organization_id"
			+ " ORDER BY system_id";
}
