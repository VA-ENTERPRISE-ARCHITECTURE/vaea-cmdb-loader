package gov.va.vasi.etl.cmdb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import gov.va.vasi.etl.cmdb.model.BusinessService;
import gov.va.vasi.etl.cmdb.utils.GlobalValues;

@Component
public class ETLOutputWriter {

    public void writeOutput() throws IOException {

	String sheetName = "VASI_CMDB_ETL";// name of sheet

	XSSFWorkbook wb = new XSSFWorkbook();
	XSSFSheet sheet = wb.createSheet(sheetName);

	/*
	 * VASI ID Name Owned By System Acronym Sponsor Organization Used for Support
	 * group Service classification
	 */
	// Add Header Row
	XSSFRow header = sheet.createRow(0);
	header.createCell(0).setCellValue("VASI ID");
	header.createCell(1).setCellValue("Name");
	header.createCell(2).setCellValue("Owned By");
	header.createCell(3).setCellValue("System Acronym");
	header.createCell(4).setCellValue("Sponsor Organization");
	header.createCell(5).setCellValue("Used for");
	header.createCell(6).setCellValue("Support group");
	header.createCell(7).setCellValue("Service classification");
	header.createCell(8).setCellValue("Project Code");
	// header.createCell(9).setCellValue("Class");
	// header.createCell(10).setCellValue("Family");
	// header.createCell(11).setCellValue("FISMA");
	// header.createCell(12).setCellValue("FIPS Category ");
	// header.createCell(13).setCellValue("VASI System ID");
	// header.createCell(14).setCellValue("Service Manager");
	// header.createCell(15).setCellValue("Business Unit");
	// header.createCell(16).setCellValue("VASI Criticality");
	// header.createCell(17).setCellValue("Accreditation Boundary");
	// header.createCell(18).setCellValue("Responsible Organization");
	// header.createCell(19).setCellValue("Maintenance Organization");
	// header.createCell(20).setCellValue("Primary Contact");
	// header.createCell(21).setCellValue("Application Support");
	// header.createCell(22).setCellValue("Database Support");
	// header.createCell(23).setCellValue("Hardware/OS");
	// header.createCell(24).setCellValue("Disaster Recovery Support");
	// header.createCell(25).setCellValue("Storage Support");
	// header.createCell(26).setCellValue("Backup Services Support");
	// header.createCell(27).setCellValue("Network Operations Support");
	// header.createCell(28).setCellValue("Service Desk");
	// header.createCell(29).setCellValue("Security Support");
	// header.createCell(30).setCellValue("Business Risk");

	applyStyles(header, getHeaderStyle(wb));

	// // Add Sub-header Row
	// XSSFRow subHeader = sheet.createRow(1);
	//
	// subHeader.createCell(0).setCellValue("objecttype");
	// subHeader.createCell(1).setCellValue("tgt_id");
	// subHeader.createCell(2).setCellValue("name");
	// subHeader.createCell(3).setCellValue("asset_num");
	// subHeader.createCell(4).setCellValue("serial_number");
	// subHeader.createCell(5).setCellValue("system_name");
	// subHeader.createCell(6).setCellValue("description");
	// subHeader.createCell(7).setCellValue("status");
	// subHeader.createCell(8).setCellValue("dns_name");
	// subHeader.createCell(9).setCellValue("class");
	// subHeader.createCell(10).setCellValue("family");
	// subHeader.createCell(11).setCellValue("portfolio");
	// subHeader.createCell(12).setCellValue("category");
	// subHeader.createCell(13).setCellValue("version");
	// subHeader.createCell(14).setCellValue("service_manager");
	// subHeader.createCell(15).setCellValue("business_unit");
	// subHeader.createCell(16).setCellValue("business_impact");
	// subHeader.createCell(17).setCellValue("zaccreditation_boundary");
	// subHeader.createCell(18).setCellValue("service_org");
	// subHeader.createCell(19).setCellValue("repair_org");
	// subHeader.createCell(20).setCellValue("resource_contact");
	// subHeader.createCell(21).setCellValue("support_contact1_uuid");
	// subHeader.createCell(22).setCellValue("support_contact2_uuid");
	// subHeader.createCell(23).setCellValue("support_contact3_uuid");
	// subHeader.createCell(24).setCellValue("disaster_recovery_contact_uuid");
	// subHeader.createCell(25).setCellValue("contact_1");
	// subHeader.createCell(26).setCellValue("backup_services_contact_uuid");
	// subHeader.createCell(27).setCellValue("network_contact_uuid");
	// subHeader.createCell(28).setCellValue("zsupport_servicedesk");
	// subHeader.createCell(29).setCellValue("zsecurity_support");
	// subHeader.createCell(30).setCellValue("business_risk");

	// applyStyles(subHeader, getSubHeaderStyle(wb));

	// Write Data
	BusinessService outRecord = null;
	Iterator<BusinessService> outIterator = GlobalValues.output.iterator();
	int rowNumb = 1;
	while (outIterator.hasNext()) {
	    outRecord = outIterator.next();
	    XSSFRow row = sheet.createRow(rowNumb++);
	    row.createCell(0).setCellValue(outRecord.getSystemID().intValue());
	    row.createCell(1).setCellValue(outRecord.getName());
	    row.createCell(2).setCellValue(outRecord.getOwnedBy());
	    row.createCell(3).setCellValue(outRecord.getSystemAcronym());
	    row.createCell(4).setCellValue(outRecord.getSponsorOrganization());
	    row.createCell(5).setCellValue(outRecord.getUsedFor());
	    row.createCell(6).setCellValue(outRecord.getSupportGroup());
	    row.createCell(7).setCellValue(outRecord.getServiceClassification());
	    row.createCell(8).setCellValue(outRecord.getAppCode());
	    // row.createCell(9).setCellValue("Business Service");
	    // row.createCell(10).setCellValue("Enterprise Service");
	    // row.createCell(11).setCellValue(outRecord.getPortfolio());
	    // row.createCell(12).setCellValue(outRecord.getServiceCategory());
	    // row.createCell(13).setCellValue(outRecord.getSystemID().intValue());
	    // // row.createCell(14).setCellValue(outRecord.getResponsibleOrganization());
	    // row.createCell(15).setCellValue(outRecord.getBusinessUnit());
	    // row.createCell(16).setCellValue(outRecord.getBusinessImpact());
	    // row.createCell(17).setCellValue(outRecord.getAccreditationBoundary());
	    // row.createCell(18).setCellValue(outRecord.getResponsibleOrganization());
	    // // row.createCell(19).setCellValue("Maintenance Organization");
	    // // row.createCell(20).setCellValue("Primary Contact");
	    // // row.createCell(21).setCellValue("Application Support");
	    // // row.createCell(22).setCellValue("Database Support");
	    // // row.createCell(23).setCellValue("Hardware/OS");
	    // // row.createCell(24).setCellValue("Disaster Recovery Support");
	    // // row.createCell(25).setCellValue("Storage Support");
	    // // row.createCell(26).setCellValue("Backup Services Support");
	    // // row.createCell(27).setCellValue("Network Operations Support");
	    // // row.createCell(28).setCellValue("Service Desk");
	    // // row.createCell(29).setCellValue("Security Support");
	    // row.createCell(30).setCellValue(outRecord.getBusinessRisk());
	}

	// write this workbook to an Output stream.
	FileOutputStream fileOut = new FileOutputStream(getOutFileName());

	wb.write(fileOut);
	fileOut.flush();
	fileOut.close();
    }

    private CellStyle getHeaderStyle(XSSFWorkbook wb) {
	XSSFFont font = wb.createFont();
	font.setFontHeightInPoints((short) 12);
	font.setFontName("Calibri");
	font.setColor(IndexedColors.BLACK.getIndex());
	font.setBold(false);
	font.setItalic(false);

	CellStyle style = wb.createCellStyle();
	style.setFont(font);

	return style;

    }

    private CellStyle getSubHeaderStyle(XSSFWorkbook wb) {
	XSSFFont font = wb.createFont();
	font.setFontHeightInPoints((short) 12);
	font.setFontName("Calibri");
	font.setColor(IndexedColors.BLUE.getIndex());
	font.setBold(true);
	font.setItalic(false);

	CellStyle style = wb.createCellStyle();
	style.setFont(font);

	return style;
    }

    private void applyStyles(XSSFRow row, CellStyle style) {
	row.getCell(0).setCellStyle(style);
	row.getCell(1).setCellStyle(style);
	row.getCell(2).setCellStyle(style);
	row.getCell(3).setCellStyle(style);
	row.getCell(4).setCellStyle(style);
	row.getCell(5).setCellStyle(style);
	row.getCell(6).setCellStyle(style);
	row.getCell(7).setCellStyle(style);
	row.getCell(8).setCellStyle(style);
	// row.getCell(9).setCellStyle(style);
	// row.getCell(10).setCellStyle(style);
	// row.getCell(11).setCellStyle(style);
	// row.getCell(12).setCellStyle(style);
	// row.getCell(13).setCellStyle(style);
	// row.getCell(14).setCellStyle(style);
	// row.getCell(15).setCellStyle(style);
	// row.getCell(16).setCellStyle(style);
	// row.getCell(17).setCellStyle(style);
	// row.getCell(18).setCellStyle(style);
	// row.getCell(19).setCellStyle(style);
	// row.getCell(20).setCellStyle(style);
	// row.getCell(21).setCellStyle(style);
	// row.getCell(22).setCellStyle(style);
	// row.getCell(23).setCellStyle(style);
	// row.getCell(24).setCellStyle(style);
	// row.getCell(25).setCellStyle(style);
	// row.getCell(26).setCellStyle(style);
	// row.getCell(27).setCellStyle(style);
	// row.getCell(28).setCellStyle(style);
	// row.getCell(29).setCellStyle(style);
	// row.getCell(30).setCellStyle(style);
    }

    private String getOutFileName() {
	DateTimeFormatter timeStampPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	String dateStr = timeStampPattern.format(java.time.LocalDateTime.now());
	return GlobalValues.FILE_PATH + "VASI_ECMDB_ETL_" + dateStr + ".xlsx";
    }

}
