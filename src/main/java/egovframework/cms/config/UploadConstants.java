package egovframework.cms.config;

public class UploadConstants {
	 //public static final String UPLOAD_PATH = "D:/eGov4.3.0/upload-file";
	 //public static final String UPLOAD_PATH = "/home/bluemedia/upload-file";	 
	public static final String UPLOAD_PATH;

    static {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            UPLOAD_PATH = "D:/eGov4.3.0/upload-file";
        } else {
            UPLOAD_PATH = "/home/bluemedia/upload-file";
        }
    }
}
