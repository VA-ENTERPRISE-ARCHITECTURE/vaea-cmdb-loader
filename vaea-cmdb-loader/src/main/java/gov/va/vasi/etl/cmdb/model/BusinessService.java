package gov.va.vasi.etl.cmdb.model;


import java.math.BigDecimal;

public class BusinessService {
	private String cmdbID;
	private	BigDecimal	systemID;
	private	String	name;
	private	String	altCIID;
	private	String	businessUnit;
	private	String	status;
	private	String	businessImpact;
	private	String	description;
	private	String	accreditationBoundary;
	private	String	responsibleOrganization;
	private	String	businessRisk;
	private	String	portfolio;
	private	String	serviceCategory;
	private String  ciClass;
	public String getCiClass() {
		return ciClass;
	}
	public void setCiClass(String ciClass) {
		this.ciClass = ciClass;
	}
	public String getCiFamily() {
		return ciFamily;
	}
	public void setCiFamily(String ciFamily) {
		this.ciFamily = ciFamily;
	}
	private String  ciFamily;
	
	
	public String getCmdbID() {
		return cmdbID;
	}
	public void setCmdbID(String cmdbID) {
		this.cmdbID = cmdbID;
	}
	public BigDecimal getSystemID() {
		return systemID;
	}
	public void setSystemID(BigDecimal systemID) {
		this.systemID = systemID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAltCIID() {
		return altCIID;
	}
	public void setAltCIID(String altCIID) {
		this.altCIID = altCIID;
	}
	public String getBusinessUnit() {
		return businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBusinessImpact() {
		return businessImpact;
	}
	public void setBusinessImpact(String businessImpact) {
		this.businessImpact = businessImpact;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAccreditationBoundary() {
		return accreditationBoundary;
	}
	public void setAccreditationBoundary(String accreditationBoundary) {
		this.accreditationBoundary = accreditationBoundary;
	}
	public String getResponsibleOrganization() {
		return responsibleOrganization;
	}
	public void setResponsibleOrganization(String responsibleOrganization) {
		this.responsibleOrganization = responsibleOrganization;
	}
	public String getBusinessRisk() {
		return businessRisk;
	}
	public void setBusinessRisk(String businessRisk) {
		this.businessRisk = businessRisk;
	}
	public String getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(String portfolio) {
		this.portfolio = portfolio;
	}
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	
}
