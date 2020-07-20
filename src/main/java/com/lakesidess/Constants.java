package com.lakesidess;

import java.io.File;

public interface Constants {
	String MAP_DATA_CORRECT = "mapDataCorrect";
	String LIST_DATA_NOT_CORRECT = "listDataNotCorrect";
	String PDF_EXTENSIONS = ".pdf" ;
	String USER_HOME = System.getProperty("user.home");
	File TMP_FILE_DIRECTORY = new File(Constants.USER_HOME+"/tmp");
	String TMP_ABSOULTE_DIRECTORY = TMP_FILE_DIRECTORY.getAbsolutePath();
	String APPLICATION_PROPERTIES_NAME = "application.properties";
}
