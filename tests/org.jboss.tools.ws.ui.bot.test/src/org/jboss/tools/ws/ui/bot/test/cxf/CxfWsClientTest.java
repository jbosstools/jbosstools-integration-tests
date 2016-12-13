package org.jboss.tools.ws.ui.bot.test.cxf;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.ws.reddeer.ui.preferences.WsCxf2xPreferencePage;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceRuntime;
import org.jboss.tools.ws.ui.bot.test.wsclient.WSClientTestTemplate;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * Test Web Service Client with CXF Service Runtime, tests inherited from
 * {@link WSClientTestTemplate}
 * 
 * @author Radoslav Rabara
 * 
 */
@RunWith(RedDeerSuite.class)
public class CxfWsClientTest extends WSClientTestTemplate {

	private static final String CXF_HOME_LOCATION;

	static {
		CXF_HOME_LOCATION = System.getProperty("apache-cxf-2.x");
	}

	public CxfWsClientTest() {
		super(WebServiceRuntime.APACHE_CXF2);
	}

	@Override
	protected String getWsProjectName() {
		return "cxfclient";
	}

	@Override
	protected String getWsPackage() {
		return "cxfclient." + getLevel().toString().toLowerCase();
	}

	@Override
	protected String getEarProjectName() {
		return "cxfclientEAR";
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
