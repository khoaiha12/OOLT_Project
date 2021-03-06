
package projectCK;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Language {
	
	private final static Properties lang = new Properties();
	
	private Language() {
	}
	
	public static void init() throws IOException {
		FileInputStream fis = new FileInputStream("lang/"+Configuration.getLanguageFile());
		lang.load(fis);
		fis.close();
	}
	
	public static String getText(String key) {
		return lang.getProperty(key, "");
	}
	
}
