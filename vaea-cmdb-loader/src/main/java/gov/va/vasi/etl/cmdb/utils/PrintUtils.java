package gov.va.vasi.etl.cmdb.utils;

import gov.va.vasi.etl.cmdb.model.BusinessService;

public final class PrintUtils {

    private PrintUtils() {
    }

    public static void printSummaryReport() {
	System.out.println("");
	System.out.println("CMDB Input");
	System.out.println("==========");
	System.out.println("Total Enterprise Service Family CIs received = " + GlobalValues.entServiceCount);
	System.out.println("Total valid Business Service CIs received = " + GlobalValues.validBusServiceCount);
	System.out.println("");
	System.out.println("Input Errors");
	System.out.println("============");
	if (GlobalValues.ERROR_MESSAGES.size() == 0) {
	    System.out.println("No Errors found in CMDB input");
	} else {
	    for (String errMessage : GlobalValues.ERROR_MESSAGES) {
		System.out.println(errMessage);
	    }
	}
	System.out.println("");
	System.out.println("ETL Output");
	System.out.println("==========");
	System.out.println("New Business Service CIs to be added = " + GlobalValues.addCount);
	System.out.println("Business Service CIs that need to be updated with VASI Info = " + GlobalValues.updateCount);
	System.out.println("Business Service CIs in Sync with VASI = " + GlobalValues.matchCount);
    }

    public static void printBusService(BusinessService bservice) {
	if (bservice != null) {
	    System.out.println("VASI ID: " + bservice.getSystemID());
	    // System.out.println("CMDB ID: " + bservice.getCmdbID());
	    System.out.println("Name: " + bservice.getName());
	    System.out.println("System Acronym: " + bservice.getSystemAcronym());
	    System.out.println("Used for: " + bservice.getUsedFor());
	    // System.out.println("Business Unit: " + bservice.getBusinessUnit());
	    System.out.println("Sponsor Organization: " + bservice.getSponsorOrganization());
	    /*
	     * System.out.println("Business Impact: " + bservice.getBusinessImpact());
	     * System.out.println("Service Category: " + bservice.getServiceCategory());
	     * System.out.println("Portfolio: " + bservice.getPortfolio());
	     * System.out.println("Business Risk: " + bservice.getBusinessRisk());
	     * System.out.println("Accreditaion Boundary: " +
	     * bservice.getAccreditationBoundary()); System.out.println("Description: " +
	     * bservice.getDescription());
	     */
	    System.out.println("");
	}
    }

}
