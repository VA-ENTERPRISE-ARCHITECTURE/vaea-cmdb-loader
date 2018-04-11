package gov.va.vasi.etl.cmdb;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.va.vasi.etl.cmdb.model.BusinessService;
import gov.va.vasi.etl.cmdb.utils.GlobalValues;
import gov.va.vasi.etl.cmdb.utils.PrintUtils;

@Component
public class ETLMainProcessor {
	@Autowired
	VASIFileLoader vasiFileLoader;
	@Autowired
	VASIDBLoader vasiDBLoader;
	@Autowired
	CMDBFileLoader cmdbFileLoader;
	@Autowired
	ETLOutputWriter etlOutputWriter;

	public void process() {
		System.out.println("Starting the VASI CMDB ETL process .....");
		// Make Sure proper directory structure is place.
		if (Files.exists(Paths.get(GlobalValues.FILE_PATH))) {
			try {
				//Load CMDB Input File
				List<BusinessService> cmdbList = cmdbFileLoader.loadCMDBData();

				System.out.println("");
				System.out.println("Printing sample record from CMDB Input");
				System.out.println("======================================");
				PrintUtils.printBusService(
						getBusinessService(BigDecimal.valueOf(GlobalValues.EXAMPLE_VASI_ID), cmdbList));
				
				//Load VASI Data. First try to connect to VASI Database. If DB is not accessible, then look for a VASI input file
				List<BusinessService> vasiList;
				if (vasiDBLoader.isVASIDBAccessible()) {
					vasiList = vasiDBLoader.loadVASIData();
					System.out.println("Loaded " + vasiList.size() + " IT Systems from the VASI Database .....");
				} else {
					vasiList = vasiFileLoader.loadVASIData();
				}
				
				findAdds(vasiList, cmdbList);
				findChanges(cmdbList, vasiList);
				etlOutputWriter.writeOutput();
				PrintUtils.printSummaryReport();
				System.out.println("");
				System.out.println("All done. ETL Output written to the folder " + GlobalValues.FILE_PATH);
			} catch (FileNotFoundException fn) {
				System.out.println(fn.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println(
					"ERROR: Sorry... I was told to find the ETL Input files a folder named with today's date  in YYYY-mm-dd format. I could not find the directory"
							+ GlobalValues.FILE_PATH);
			System.out.println("Aborting the VASI CMDB ETL process .....");
		}
	}

	private void findAdds(List<BusinessService> vasiList, List<BusinessService> cmdbList) {
		BusinessService vasiRecord = null;
		Iterator<BusinessService> vasiIterator = vasiList.iterator();
		while (vasiIterator.hasNext()) {
			vasiRecord = vasiIterator.next();
			if (getBusinessService(vasiRecord.getSystemID(), cmdbList) == null) {
				if (isSystemActive(vasiRecord)) {
					GlobalValues.output.add(vasiRecord);
					GlobalValues.addCount++;
				}
			}
		}

	}

	private void findChanges(List<BusinessService> cmdbList, List<BusinessService> vasiList) {
		for (BusinessService cmdbRecord : cmdbList) {
			BusinessService vasiRecord = getBusinessService(cmdbRecord.getSystemID(), vasiList);
			if (vasiRecord == null) {
				BusinessService delRec = new BusinessService();
				delRec.setStatus("Retired");
				delRec.setSystemID(cmdbRecord.getSystemID());
				GlobalValues.output.add(delRec);
				System.out.println("DELETE found " + delRec.getSystemID());
			} else {
				setUpdate(cmdbRecord, vasiRecord);
			}
		}
	}

	private void setUpdate(BusinessService cmdbRecord, BusinessService vasiRecord) {
		BusinessService updatedRec = new BusinessService();
		boolean updateFlag = false;

		// Compare Status
		String status = compareValues(vasiRecord.getStatus(), cmdbRecord.getStatus());
		if (status != null) {
			updateFlag = true;
			updatedRec.setStatus(status);
		}
		// Compare Name
		String name = compareValues(vasiRecord.getName(), cmdbRecord.getName());
		if (name != null) {
			updateFlag = true;
			updatedRec.setName(name);
		}
		// Compare AltCIId
		String altCIID = compareValues(vasiRecord.getAltCIID(), cmdbRecord.getAltCIID());
		if (altCIID != null) {
			updateFlag = true;
			updatedRec.setAltCIID(altCIID);
		}

		// Compare BusinessUnit
		String businessUnit = compareValues(vasiRecord.getBusinessUnit(), cmdbRecord.getBusinessUnit());
		if (businessUnit != null) {
			updateFlag = true;
			updatedRec.setBusinessUnit(businessUnit);
		}

		// Compare ResponsibleOrganization
		String responsibleOrganization = compareValues(vasiRecord.getResponsibleOrganization(),
				cmdbRecord.getResponsibleOrganization());
		if (responsibleOrganization != null) {
			updateFlag = true;
			updatedRec.setResponsibleOrganization(responsibleOrganization);
		}
		
		/*
		// Compare Description
		String description = compareValues(vasiRecord.getDescription(), cmdbRecord.getDescription());
		if (description != null) {
			updateFlag = true;
			updatedRec.setDescription(description);
			if (description != null && description.length() > 255) {
				System.out.println(
						vasiRecord.getSystemID().intValue() + " has a description length of " + description.length());
			}
		}

		// Compare Portfolio
		String portfolio = compareValues(vasiRecord.getPortfolio(), cmdbRecord.getPortfolio());
		if (portfolio != null) {
			updateFlag = true;
			updatedRec.setPortfolio(portfolio);
		}

		// Compare ServiceCategory
		String serviceCategory = compareValues(vasiRecord.getServiceCategory(), cmdbRecord.getServiceCategory());
		if (serviceCategory != null) {
			updateFlag = true;
			updatedRec.setServiceCategory(serviceCategory);
		}

		// Compare BusinessImpact
		String businessImpact = compareValues(vasiRecord.getBusinessImpact(), cmdbRecord.getBusinessImpact());
		if (businessImpact != null) {
			updateFlag = true;
			updatedRec.setBusinessImpact(businessImpact);
		}

		// Compare AccreditationBoundary
		String accreditationBoundary = compareValues(vasiRecord.getAccreditationBoundary(),
				cmdbRecord.getAccreditationBoundary());
		if (accreditationBoundary != null) {
			updateFlag = true;
			updatedRec.setAccreditationBoundary(accreditationBoundary);
		}

		// Compare BusinessRisk
		String businessRisk = compareValues(vasiRecord.getBusinessRisk(), cmdbRecord.getBusinessRisk());
		if (businessRisk != null) {
			updateFlag = true;
			updatedRec.setBusinessRisk(businessRisk);
		}
		*/

		if (updateFlag) {
			updatedRec.setSystemID(vasiRecord.getSystemID());
			updatedRec.setCmdbID(cmdbRecord.getCmdbID());
			GlobalValues.output.add(updatedRec);
			GlobalValues.updateCount++;
		} else {
			// System.out.println("No updates found for " +
			// vasiRecord.getSystemID());
			GlobalValues.matchCount++;
		}
	}

	private boolean isSystemActive(BusinessService vasiRecord) {
		String status = vasiRecord.getStatus().trim();
		if (status.equalsIgnoreCase("Production") || status.equalsIgnoreCase("Development")) {
			return true;
		}
		return false;
	}

	private String compareValues(String vasiField, String cmdbField) {

		if (cleanUp(vasiField).equals("") || cleanUp(vasiField).equalsIgnoreCase(cleanUp(cmdbField))) {
			return null;
		}
		return vasiField;
	}

	private BusinessService getBusinessService(BigDecimal systemId, List<BusinessService> lookupList) {
		for (BusinessService busService : lookupList) {
			if (busService.getSystemID().intValue() == systemId.intValue()) {
				return busService;
			}
		}
		return null;
	}
	
	private String cleanUp(String str1) {
        if (str1 == null || str1.trim().isEmpty()) {
        	return "";        	
        } else {
               str1 = str1.replaceAll("[^\\w\\s]", " ");
               str1 = str1.replaceAll("  ", " ");
               str1 = str1.trim().toUpperCase();
        }
        return str1;
  }

}
