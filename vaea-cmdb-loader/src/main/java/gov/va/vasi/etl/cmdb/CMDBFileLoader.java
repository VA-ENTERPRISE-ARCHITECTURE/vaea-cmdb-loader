package gov.va.vasi.etl.cmdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import gov.va.vasi.etl.cmdb.model.BusinessService;
import gov.va.vasi.etl.cmdb.utils.GlobalValues;

@Component
public class CMDBFileLoader {
    private static final Logger LOG = Logger.getLogger(CMDBFileLoader.class.getName());

    public List<BusinessService> loadCMDBData() throws IOException {
	List<BusinessService> sList = new ArrayList<BusinessService>();
	String cmdbFile = getCMDBFile();
	LOG.log(Level.INFO, "CMDB Input File : " + cmdbFile);
	FileInputStream file = new FileInputStream(new File(cmdbFile));
	// Get the workbook instance for XLS file
	HSSFWorkbook workbook = new HSSFWorkbook(file);
	// Get first sheet from the workbook
	HSSFSheet sheet = workbook.getSheetAt(0);
	// Iterate through each rows from first sheet
	Iterator<Row> rowIterator = sheet.iterator();
	Row row = null;
	// Ignore the Header
	if (rowIterator.hasNext()) {
	    row = rowIterator.next();
	}

	DataFormatter df = new DataFormatter();
	while (rowIterator.hasNext()) {
	    BusinessService etlBusinessService = new BusinessService();
	    GlobalValues.entServiceCount++;
	    row = rowIterator.next();

	    if (!df.formatCellValue(row.getCell(0)).trim().isEmpty())
		etlBusinessService.setSystemID(new BigDecimal(df.formatCellValue(row.getCell(0)).trim()));
	    if (!df.formatCellValue(row.getCell(1)).trim().isEmpty())
		etlBusinessService.setName(df.formatCellValue(row.getCell(1)).trim());
	    if (!df.formatCellValue(row.getCell(2)).trim().isEmpty())
		etlBusinessService.setOwnedBy(df.formatCellValue(row.getCell(2)).trim());
	    if (!df.formatCellValue(row.getCell(3)).trim().isEmpty())
		etlBusinessService.setSystemAcronym(df.formatCellValue(row.getCell(3)).trim());
	    if (!df.formatCellValue(row.getCell(4)).trim().isEmpty())
		etlBusinessService.setSponsorOrganization(df.formatCellValue(row.getCell(4)).trim());
	    if (!df.formatCellValue(row.getCell(5)).trim().isEmpty())
		etlBusinessService.setUsedFor(df.formatCellValue(row.getCell(5)).trim());
	    if (!df.formatCellValue(row.getCell(6)).trim().isEmpty())
		etlBusinessService.setSupportGroup(df.formatCellValue(row.getCell(6)).trim());
	    if (!df.formatCellValue(row.getCell(7)).trim().isEmpty())
		etlBusinessService.setServiceClassification(df.formatCellValue(row.getCell(7)).trim());
	    if (!df.formatCellValue(row.getCell(8)).trim().isEmpty())
		etlBusinessService.setAppCode(df.formatCellValue(row.getCell(8)).trim());
	    sList.add(etlBusinessService);

	}
	workbook.close();
	file.close();

	return getValidBusinessServiceList(sList);

    }

    private List<BusinessService> getValidBusinessServiceList(List<BusinessService> services) {
	// Check for the presence of duplicates and create a list of valid
	// Business Services
	Set<BigDecimal> dupVerifySet = new HashSet<BigDecimal>();
	List<BusinessService> output = new ArrayList<BusinessService>();

	for (BusinessService service : services) {
	    if (isValidBusinessService(service)) {
		if (dupVerifySet.add(service.getSystemID())) {
		    output.add(service);
		} else {
		    GlobalValues.ERROR_MESSAGES.add("Duplicate VASI Id {" + service.getSystemID().intValue()
			    + "} found for CI, Name {" + service.getName() + "}");
		}
	    }
	}

	// Sort the CMDB List by VASI Id in ascending order
	Collections.sort(output, new Comparator<BusinessService>() {
	    public int compare(BusinessService o1, BusinessService o2) {
		return (o1.getSystemID()).compareTo(o2.getSystemID());

	    }
	});
	GlobalValues.validBusServiceCount = output.size();
	return output;
    }

    private boolean isValidBusinessService(BusinessService service) {
	if (service.getServiceClassification().equalsIgnoreCase("Business Service") && service.getSystemID() != null) {
	    return true;
	}

	if (!service.getServiceClassification().equalsIgnoreCase("Business Service") && service.getSystemID() != null) {
	    GlobalValues.ERROR_MESSAGES.add("VASI Id {" + service.getSystemID().intValue()
		    + "} populated on non-buisness service CI, Name {" + service.getName() + "}");

	} else if (service.getServiceClassification().equalsIgnoreCase("Business Service")
		&& service.getSystemID() == null) {
	    GlobalValues.ERROR_MESSAGES
		    .add("Buisness service CI, Name {" + service.getName() + "} exists without a VASI Id");

	}

	return false;
    }

    private String getCMDBFile() throws FileNotFoundException {
	List<Path> files = new ArrayList<>();
	try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(GlobalValues.FILE_PATH),
		"VASI ETL*.{xlsx,xls}")) {
	    for (Path p : stream) {
		files.add(p);
	    }
	} catch (IOException ex) {
	    System.err.println(ex);
	}
	if (files.size() == 0) {
	    throw new FileNotFoundException("ERROR: Sorry ... I could not find the CMDB Input file in the folder "
		    + GlobalValues.FILE_PATH
		    + " . I was told to look for a excel file with a name starting 'VASI ETL'. Aborting ETL process ....");
	} else if (files.size() == 1) {
	    return files.get(0).toString();

	} else {
	    Collections.sort(files, new Comparator<Path>() {
		public int compare(Path o1, Path o2) {
		    return (o2.getFileName().compareTo(o1.getFileName()));

		}
	    });

	}
	return files.get(0).toString();
    }
}
