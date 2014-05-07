package org.jboss.tools.portlet.ui.bot.test.compatibility;

import org.jboss.tools.portlet.ui.bot.test.template.JBDSCompatibilityGateinTemplate;

/**
 * Tests that project from JBDS 7 can work in JBDS 8 without any problems.
 * 
 * @author Radoslav Rabara
 *
 */
public class JBDS7CompatibilityGatein extends JBDSCompatibilityGateinTemplate {

	private static final String ZIP_FILE = "resources/compatibility/jbds7-compatibility.zip";
	
	private static final String CORE_PROJECT_NAME = "jbds-7-core";

	private static final String JSF_PROJECT_NAME = "jbds-7-jsf";

	private static final String SEAM_PROJECT_NAME = "jbds-7-seam";

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
