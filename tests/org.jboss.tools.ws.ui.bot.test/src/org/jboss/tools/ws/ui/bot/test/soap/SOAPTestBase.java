package org.jboss.tools.ws.ui.bot.test.soap;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.eclipse.reddeer.eclipse.ui.console.ConsoleView;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.common.reddeer.requirements.JavaFoldingRequirement.JavaFolding;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;

/**
 * Test base for SOAP web service tests
 * 
 * @author Jan Richter
 *
 */
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state = ServerRequirementState.RUNNING, cleanup = false)
@JavaFolding(false)
public abstract class SOAPTestBase {

	@InjectRequirement
    private static ServerRequirement serverReq;
	private String wsProjectName = null;
	private SliderLevel level;
	
	private static final String SOAP_REQUEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\" ?>"
			+ "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\""
			+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
			+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
			+ "<soap:Body>{0}</soap:Body>" + "</soap:Envelope>";
	
	protected static final Logger LOGGER = Logger.getLogger(WSTestBase.class
			.getName());

	protected final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	
	protected SliderLevel getLevel() {
		return level;
	}

	protected void setLevel(SliderLevel level) {
		this.level = level;
	}
	
	@Before
	public void setup() {
		if (getEarProjectName() != null && !ProjectHelper.projectExists(getEarProjectName())) {
			ProjectHelper.createEARProject(getEarProjectName());
			if (!ProjectHelper.projectExists(getWsProjectName())) {
				ProjectHelper.createProjectForEAR(getWsProjectName(),
						getEarProjectName(), "src/jbossws");
			}
		} else if (!ProjectHelper.projectExists(getWsProjectName())) {
			ProjectHelper.createProject(getWsProjectName());
		}
	}
	
	@After
	public void cleanup() {		
		ConsoleView console = new ConsoleView();
		if (!console.isOpen()) {
			console.open();
		}
		console.clearConsole();
	}

	@AfterClass
	public static void deleteAll() {
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		ProjectHelper.deleteAllProjects();
	}

	protected static String getConfiguredRuntimeName() {
		return serverReq.getRuntimeName();
	}

	protected static String getConfiguredServerName() {
		return serverReq.getServerName();
	}

	protected static String getConfiguredServerType() {
		return serverReq.getConfiguration().getFamily().getLabel();
	}

	protected static String getConfiguredServerVersion() {
		return serverReq.getConfiguration().getVersion();
	}

	protected String getWsProjectName() {
		return wsProjectName;
	}

	protected void setWsProjectName(String wsProjectName) {
		this.wsProjectName = wsProjectName;
	}

	protected abstract String getEarProjectName();

	public static String getSoapRequest(String body) {
		return MessageFormat.format(SOAP_REQUEST_TEMPLATE, body);
	}

}
