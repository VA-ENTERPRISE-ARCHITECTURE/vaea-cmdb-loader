package gov.va.vasi.etl.cmdb.model;

import java.math.BigDecimal;

public class BusinessService {

    private BigDecimal systemID;
    private String name;
    private String ownedBy;
    private String systemAcronym;
    private String sponsorOrganization;
    private String usedFor;
    private String supportGroup;
    private String serviceClassification;
    private String appCode;

    // private String altCIID;
    // private String businessUnit;
    // private String status;
    // private String businessImpact;
    // private String description;
    // private String accreditationBoundary;
    // private String responsibleOrganization;
    // private String businessRisk;
    // private String portfolio;
    // private String serviceCategory;
    // private String ciClass;

    public String getAppCode() {
	return appCode;
    }

    public void setAppCode(String appCode) {
	this.appCode = appCode;
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

    public String getOwnedBy() {
	return ownedBy;
    }

    public void setOwnedBy(String ownedBy) {
	this.ownedBy = ownedBy;
    }

    public String getSystemAcronym() {
	return systemAcronym;
    }

    public void setSystemAcronym(String systemAcronym) {
	this.systemAcronym = systemAcronym;
    }

    public String getSponsorOrganization() {
	return sponsorOrganization;
    }

    public void setSponsorOrganization(String sponsorOrganization) {
	this.sponsorOrganization = sponsorOrganization;
    }

    public String getUsedFor() {
	return usedFor;
    }

    public void setUsedFor(String usedFor) {
	this.usedFor = usedFor;
    }

    public String getSupportGroup() {
	return supportGroup;
    }

    public void setSupportGroup(String supportGroup) {
	this.supportGroup = supportGroup;
    }

    public String getServiceClassification() {
	return serviceClassification;
    }

    public void setServiceClassification(String serviceClassification) {
	this.serviceClassification = serviceClassification;
    }
}
