package org.jboss.tools.ws.ui.bot.test.cxf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.ws.reddeer.ui.preferences.WsCxf2xPreferencePage;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.jboss.tools.ws.ui.bot.test.wsclient.WSClientTestTemplate;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Test Web Service Client with CXF Service Runtime, tests inherited
 * from {@link WSClientTestTemplate}
 * 
 * @author Radoslav Rabara
 * 
 */
public class CxfWsClientTest extends WSClientTestTemplate {

	private static final String CXF_HOME_LOCATION;

	static {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(new File("properties/ws.properties")));

			CXF_HOME_LOCATION = properties.getProperty("apache-cxf-2.x");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public CxfWsClientTest() {
		super(WebServiceRuntime.APACHE_CXF2);
	}

	@Override
	protected String getSampleClientFileName() {
		return "ICalculator_ICalculator_Client.java";
	}

	@BeforeClass
	public static void setupCxfRuntime() {
		WsCxf2xPreferencePage cxfPreferencePage = new WsCxf2xPreferencePage();
		new WorkbenchPreferenceDialog().open();
		new WorkbenchPreferenceDialog().select(cxfPreferencePage);
		cxfPreferencePage.add(CXF_HOME_LOCATION);
		cxfPreferencePage.select(CXF_HOME_LOCATION);
		new WorkbenchPreferenceDialog().ok();
	}

	@AfterClass
	public static void removeCxfRuntime() {
		WsCxf2xPreferencePage cxfPreferencePage = new WsCxf2xPreferencePage();
		new WorkbenchPreferenceDialog().open();
		new WorkbenchPreferenceDialog().select(cxfPreferencePage);
		cxfPreferencePage.remove(CXF_HOME_LOCATION);
	}

	/*
	 * All tests are inherited from WSClientTestTemplate
	 */
}
