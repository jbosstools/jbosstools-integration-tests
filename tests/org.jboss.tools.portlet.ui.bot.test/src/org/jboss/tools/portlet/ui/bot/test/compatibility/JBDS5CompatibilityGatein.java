package org.jboss.tools.portlet.ui.bot.test.compatibility;

import org.jboss.tools.portlet.ui.bot.test.template.JBDSCompatibilityGateinTemplate;

/**
 * Tests that project from JBDS 5 can work in JBDS 6/7/8 without any problems.
 * 
 * @author Lucia Jelinkova
 * 
 */
public class JBDS5CompatibilityGatein extends JBDSCompatibilityGateinTemplate {

	private static final String ZIP_FILE = "resources/compatibility/jbds5-compatibility.zip";
	
	private static final String CORE_PROJECT_NAME = "jbds-5-core";

	private static final String JSF_PROJECT_NAME = "jbds-5-jsf";

	private static final String SEAM_PROJECT_NAME = "jbds-5-seam";

	@Override
	protected String[] getProjectNames() {
		String[] projectNames = {CORE_PROJECT_NAME,JSF_PROJECT_NAME,SEAM_PROJECT_NAME}; 
		return projectNames;
	}

	@Override
	protected String getZipFilePath() {
		return ZIP_FILE;
	}
}
