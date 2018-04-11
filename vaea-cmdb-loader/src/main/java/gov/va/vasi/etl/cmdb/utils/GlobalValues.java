package gov.va.vasi.etl.cmdb.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.va.vasi.etl.cmdb.model.BusinessService;

public final class GlobalValues {

	private GlobalValues() {
		}

	public static int EXAMPLE_VASI_ID = 1006;
	public static List<String> ERROR_MESSAGES = new ArrayList<String>();
	public static List<BusinessService> output = new ArrayList<BusinessService>();

	public static int entServiceCount = 0;
	public static int validBusServiceCount = 0;
	public static int addCount = 0;
	public static int updateCount = 0;
	public static int matchCount = 0;
	
	public static String FILE_PATH = "." + File.separator + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + File.separator;
}
