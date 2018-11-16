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
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import gov.va.vasi.etl.cmdb.model.BusinessService;
import gov.va.vasi.etl.cmdb.utils.GlobalValues;

@Component
public class VASIFileLoader {

    public List<BusinessService> loadVASIData() throws IOException {

	List<BusinessService> sList = new ArrayList<BusinessService>();

	String vasiFile = getVASIFilePath();
	System.out.println("VASI Input File : " + vasiFile);
	FileInputStream file = new FileInputStream(new File(vasiFile));
	// Get the workbook instance for XLS file
	XSSFWorkbook workbook = new XSSFWorkbook(file);
	// Get first sheet from the workbook
	XSSFSheet sheet = workbook.getSheetAt(0);
	// Iterate through each rows from first sheet
	Iterator<Row> rowIterator = sheet.iterator();
	Row row;
	// Ignore the Header
	if (rowIterator.hasNext()) {
	    row = rowIterator.next();
	}

	DataFormatter df = new DataFormatter();
	while (rowIterator.hasNext()) {
	    BusinessService etlBusinessService = new BusinessService();
	    row = rowIterator.next();

	    etlBusinessService.setSystemID(new BigDecimal(df.formatCellValue(row.getCell(0)).trim()));
	    if (!df.formatCellValue(row.getCell(1)).trim().isEmpty())
		etlBusinessService.setAltCIID(df.formatCellValue(row.getCell(1)).trim());
	    if (!df.formatCellValue(row.getCell(2)).trim().isEmpty())
		etlBusinessService.setName(df.formatCellValue(row.getCell(2)).trim());
	    if (!df.formatCellValue(row.getCell(3)).trim().isEmpty())
		etlBusinessService.setBusinessUnit(df.formatCellValue(row.getCell(3)).trim());
	    if (!df.formatCellValue(row.getCell(4)).trim().isEmpty())
		etlBusinessService.setStatus(df.formatCellValue(row.getCell(4)).trim());
	    if (!df.formatCellValue(row.getCell(5)).trim().isEmpty())
		etlBusinessService.setBusinessImpact(df.formatCellValue(row.getCell(5)).trim());
	    /*
	     * if (!df.formatCellValue(row.getCell(8)).trim().isEmpty())
	     * etlBusinessService.setResponsibleOrganization(df.formatCellValue(row.getCell(
	     * 8)).trim());
	     * 
	     * 
	     * if (!df.formatCellValue(row.getCell(6)).trim().isEmpty())
	     * etlBusinessService.setDescription(df.formatCellValue(row.getCell(6)).trim());
	     * if (!df.formatCellValue(row.getCell(7)).trim().isEmpty())
	     * etlBusinessService.setAccreditationBoundary(df.formatCellValue(row.getCell(7)
	     * ).trim()); if (!df.formatCellValue(row.getCell(9)).trim().isEmpty())
	     * etlBusinessService.setBusinessRisk(df.formatCellValue(row.getCell(9)).trim())
	     * ; if (!df.formatCellValue(row.getCell(10)).trim().isEmpty())
	     * etlBusinessService.setPortfolio(df.formatCellValue(row.getCell(10)).trim());
	     * if (!df.formatCellValue(row.getCell(11)).trim().isEmpty())
	     * etlBusinessService.setServiceCategory(df.formatCellValue(row.getCell(11)).
	     * trim());
	     */
	    sList.add(etlBusinessService);
	}
	workbook.close();
	file.close();

	return sList;

    }

    private String getVASIFilePath() throws FileNotFoundException {
	List<Path> files = new ArrayList<>();
	try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(GlobalValues.FILE_PATH),
		"VASI_Extract*.{xlsx,xls}")) {
	    for (Path p : stream) {
		files.add(p);
	    }
	} catch (IOException ex) {
	    System.err.println(ex);
	}
	if (files.size() == 0) {
	    throw new FileNotFoundException("ERROR: Sorry ... I could not find the VASI Input file in the folder "
		    + GlobalValues.FILE_PATH
		    + " . I was told to look for a excel file with a name starting 'VASI_Extract'. Aborting ETL process ....");
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
