package org.jboss.tools.ws.ui.bot.test.soap;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.ws.reddeer.ui.wizards.wst.WebServiceWizardPageBase.SliderLevel;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.EclipseCDIHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Test base for SOAP web service tests
 * 
 * @author Jan Richter
 *
 */
@OpenPerspective(JavaEEPerspective.class)
@JBossServer()
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
	
	@BeforeClass
	public static void initialize() {
		EclipseCDIHelper.disableFolding();
	}
	
	@Before
	public void setup() {
		if (getEarProjectName() != null && !ProjectHelper.projectExists(getEarProjectName())) {
			ProjectHelper.createEARProject(getEarProjectName());
			if (!ProjectHelper.projectExists(getWsProjectName())) {
				ProjectHelper.createProjectForEAR(getWsProjectName(),
						getEarProjectName());
			}
		}
		if (!ProjectHelper.projectExists(getWsProjectName())) {
			ProjectHelper.createProject(getWsProjectName());
		}
	}
	
	@After
	public void cleanup() {
		ServersViewHelper.removeAllProjectsFromServer(getConfiguredServerName());
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		ServersViewHelper.serverClean(getConfiguredServerName());
		
		ConsoleView console = new ConsoleView();
		if (!console.isOpened()) {
			console.open();
		}
		console.clearConsole();
	}

	@AfterClass
	public static void deleteAll() {
		ProjectHelper.deleteAllProjects();
	}

	protected static String getConfiguredRuntimeName() {
		return serverReq.getRuntimeNameLabelText(serverReq.getConfig());
	}

	protected static String getConfiguredServerName() {
		return serverReq.getServerNameLabelText(serverReq.getConfig());
	}

	protected static String getConfiguredServerType() {
		return serverReq.getConfig().getServerFamily().getLabel();
	}

	protected static String getConfiguredServerVersion() {
		return serverReq.getConfig().getServerFamily().getVersion();
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
