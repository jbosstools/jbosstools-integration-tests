package org.jboss.tools.ws.ui.bot.test.cxf;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ws.reddeer.ws.ui.WsCxf2xPreferencePage;
import org.jboss.tools.ws.ui.bot.test.Activator;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.jboss.tools.ws.ui.bot.test.wsclient.WSClientTestTemplate;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Test Web Service Client with Web Service Runtime
 * 
 * @author Radoslav Rabara
 * 
 */
public class CxfWsClientTest extends WSClientTestTemplate {
	private static final String CXF_HOME_LOCATION;
	static {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(SWTUtilExt.getResourceFile(
					Activator.PLUGIN_ID, "/properties/ws.properties")));
			
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
		return "InfoSoapType_InfoSoap_Client.java";	
	}
	
	@BeforeClass
	public static void setupCxfRuntime() {
		WsCxf2xPreferencePage cxfPreferencePage = new WsCxf2xPreferencePage();
		cxfPreferencePage.open();
		cxfPreferencePage.add(CXF_HOME_LOCATION);
		cxfPreferencePage.select(CXF_HOME_LOCATION);
		cxfPreferencePage.ok();
	}
	
	@AfterClass
	public static void removeCxfRuntime() {
		WsCxf2xPreferencePage cxfPreferencePage = new WsCxf2xPreferencePage();
		cxfPreferencePage.open();
		cxfPreferencePage.remove(CXF_HOME_LOCATION);
	}
	
	/*
	 * All tests are inherited from WSClientTestTemplate
	 */
}
