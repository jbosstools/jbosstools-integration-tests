package org.jboss.tools.portlet.ui.bot.test.compatibility;

import org.jboss.tools.portlet.ui.bot.test.template.JBDSCompatibilityGateinTemplate;

/**
 * Tests that project from JBDS 6 can work in JBDS 7/8 without any problems.
 * 
 * @author Petr Suchy
 *
 */
public class JBDS6CompatibilityGatein extends JBDSCompatibilityGateinTemplate {

	private static final String ZIP_FILE = "resources/compatibility/jbds6-compatibility.zip";
	
	private static final String CORE_PROJECT_NAME = "jbds-6-core";

	private static final String JSF_PROJECT_NAME = "jbds-6-jsf";

	private static final String SEAM_PROJECT_NAME = "jbds-6-seam";

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
