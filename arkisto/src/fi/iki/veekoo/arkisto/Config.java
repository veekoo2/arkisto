package fi.iki.veekoo.arkisto;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.vaadin.server.VaadinService;

public class Config {

	private static String servletBase = null;

	private static Properties confProps = new Properties();

	Config() {

		// Configuration file.
		
		servletBase = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		
		System.out.println("Config.servletBase: " + servletBase);
		
		try {
			FileInputStream in = new FileInputStream(servletBase + File.separator + "WEB-INF" + File.separator + "conf"
					+ File.separator + "arkisto.conf");
			
			System.out.println("Config: " + in.toString());
			
			confProps.load(in);
			in.close();
		} catch (Exception ex) {
			System.out.println("Config.Exception: confProps: " + ex);
		}

	}

	public String getServletBase() {

		return servletBase;

	}

	public String get(String parameter) {
		
		System.out.println("Config.parameter: " + parameter);

		return confProps.getProperty(parameter).trim();

	}

}
