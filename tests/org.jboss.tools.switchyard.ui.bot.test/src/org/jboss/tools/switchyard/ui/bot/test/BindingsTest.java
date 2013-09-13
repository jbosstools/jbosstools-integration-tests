package org.jboss.tools.switchyard.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.CamelBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FTPSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.FileBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JMSBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.JPABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.MailBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyTCPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.NettyUDPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.RESTBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SCABindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SFTPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SQLBindingPage;
import org.jboss.tools.switchyard.reddeer.binding.SchedulingBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.preference.PropertiesPreferencePage;
import org.jboss.tools.switchyard.reddeer.wizard.DefaultServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for creating various bindings
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@RunWith(SwitchyardSuite.class)
public class BindingsTest {

	public static final String CONTEXT_PATH = "Context Path";

	public static final String PROJECT = "binding_project";
	public static final String SERVICE = "HelloService";
	public static final String OPERATION = "sayHello";
	public static final String PACKAGE = "com.example.switchyard." + PROJECT;
	public static final String[] GATEWAY_BINDINGS = new String[] { "Camel Core (SEDA/Timer/URI)",
			"File", "File Transfer (FTP/FTPS/SFTP)", "HTTP", "JCA", "JMS", "JPA", "Mail",
			"Network (TCP/UDP)", "REST", "SCA", "Scheduling", "SOAP", "SQL" };
	public static final String[] BINDINGS = new String[] { "Camel", "FTP", "FTPS", "File", "HTTP",
			"JCA", "JMS", "JPA", "Mail", "Netty TCP", "Netty UDP", "REST", "SCA", "SFTP", "SOAP",
			"SQL", "Scheduling" };

	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

	@BeforeClass
	public static void createProject() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		new SwitchYardProjectWizard(PROJECT).binding(GATEWAY_BINDINGS).create();

		// Sometimes the editor is not displayed properly, this happens only
		// when the project is created by bot
		new SwitchYardEditor().close();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("SwitchYard").open();
		new ProjectExplorer().getProject(PROJECT).select();

		// Create new interface
		NewJavaClassWizardDialog wizard = new NewJavaClassWizardDialog();
		wizard.open();
		wizard.getFirstPage().setName("Hello");
		wizard.finish();
		new TextEditor("Hello.java").deleteLineWith("Hello").type("public interface Hello {")
				.typeBefore("Hello", "import javax.ws.rs.Produces;").newLine()
				.type("import javax.ws.rs.GET;").newLine().type("import javax.ws.rs.Path;")
				.newLine().type("import javax.ws.rs.PathParam;").newLine()
				.typeAfter("Hello", "@GET()").newLine().type("@Path(\"/{name}\")").newLine()
				.type("@Produces(\"text/plain\")").newLine()
				.type("String sayHello(@PathParam(\"name\") String name);").saveAndClose();
	}

	@AfterClass
	public static void deleteProject() {
		new SwitchYardEditor().saveAndClose();
		new ProjectExplorer().getProject(PROJECT).delete(true);
	}

	@Before
	public void addService() {
		new SwitchYardEditor().addComponent("Service");
		new DefaultShell("New Service");
		new SWTWorkbenchBot().shell("New Service").activate();
		new DefaultServiceWizard().selectJavaInterface("Hello").setServiceName("HelloService")
				.finish();
		new SwitchYardEditor().save();
	}

	@After
	public void deleteService() {
		bot.closeAllShells();
		new Service("HelloService").delete();
		new SwitchYardEditor().save();
	}

	@Test
	public void camelBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Camel");
		BindingWizard<CamelBindingPage> wizard = BindingWizard.createCamelBindingWizard();
		wizard.getBindingPage().setConfigURI("camel-uri");
		wizard.getBindingPage().setOperation(OPERATION);
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("camel-uri", properties.getCamelBindingPage().getConfigURI());
		properties.ok();
	}

	@Test
	public void ftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("FTP");
		BindingWizard<FTPBindingPage> wizard = BindingWizard.createFTPBindingWizard();
		wizard.getBindingPage().setDirectory("ftp-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("ftp-directory", properties.getFTPBindingPage().getDirectory());
		properties.ok();
	}

	@Test
	public void ftpsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SFTP");
		BindingWizard<FTPSBindingPage> wizard = BindingWizard.createFTPSBindingWizard();
		wizard.getBindingPage().setDirectory("ftps-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("ftps-directory", properties.getFTPSBindingPage().getDirectory());
		properties.ok();
	}

	@Test
	public void fileBindingTest() throws Exception {
		new Service(SERVICE).addBinding("File");
		BindingWizard<FileBindingPage> wizard = BindingWizard.createFileBindingWizard();
		wizard.getBindingPage().setDirectory("file-directory");
		wizard.getBindingPage().setDirAutoCreation(true);
		wizard.getBindingPage().setMoveDirectory("processed");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("file-directory", properties.getFileBindingPage().getDirectory());
		assertTrue(properties.getFileBindingPage().isDirAutoCreation());
		properties.ok();
	}

	@Test
	public void httpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("HTTP");
		BindingWizard<HTTPBindingPage> wizard = BindingWizard.createHTTPBindingWizard();
		wizard.getBindingPage().setContextPath("http-context");
		wizard.getBindingPage().setOperation("sayHello");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("http-context", properties.getHTTPBindingPage().getContextPath());
		assertEquals("sayHello", properties.getHTTPBindingPage().getOperation());
		new PushButton("OK").click();
	}

	@Test
	public void jcaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JCA");
	}

	@Test
	public void jmsBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JMS");
		BindingWizard<JMSBindingPage> wizard = BindingWizard.createJMSBindingWizard();
		wizard.getBindingPage().setQueueTopicName("queue-name");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("queue-name", properties.getJMSBindingPage().getQueueTopicName());
		properties.ok();
	}

	@Test
	public void jpaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("JPA");
		BindingWizard<JPABindingPage> wizard = BindingWizard.createJPABindingWizard();
		wizard.getBindingPage().setEntityClassName("entity-class");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("entity-class", properties.getJPABindingPage().getEntityClassName());
		properties.ok();
	}

	@Test
	public void mailBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Mail");
		BindingWizard<MailBindingPage> wizard = BindingWizard.createMailBindingWizard();
		wizard.getBindingPage().setHost("mail-host");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("mail-host", properties.getMailBindingPage().getHost());
		properties.ok();
	}

	@Test
	public void nettyTcpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty TCP");
		BindingWizard<NettyTCPBindingPage> wizard = BindingWizard.createNettyTCPBindingWizard();
		wizard.getBindingPage().setHost("tcp-host");
		wizard.getBindingPage().setPort("tcp-port");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("tcp-host", properties.getNettyTCPBindingPage().getHost());
		assertEquals("tcp-port", properties.getNettyTCPBindingPage().getPort());
		properties.ok();
	}

	@Test
	public void nettyUdpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Netty UDP");
		BindingWizard<NettyUDPBindingPage> wizard = BindingWizard.createNettyUDPBindingWizard();
		wizard.getBindingPage().setHost("udp-host");
		wizard.getBindingPage().setPort("udp-port");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("udp-host", properties.getNettyUDPBindingPage().getHost());
		assertEquals("udp-port", properties.getNettyUDPBindingPage().getPort());
		properties.ok();
	}

	@Test
	public void restBindingTest() throws Exception {
		new Service(SERVICE).addBinding("REST");
		BindingWizard<RESTBindingPage> wizard = BindingWizard.createRESTBindingWizard();
		wizard.getBindingPage().setContextPath("rest-binding");
		wizard.getBindingPage().addInterface("Hello");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("rest-binding", properties.getRESTBindingPage().getContextPath());
		assertTrue(properties.getRESTBindingPage().getInterfaces().contains(PACKAGE + ".Hello"));
		properties.ok();
	}

	@Test
	public void scaBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SCA");
		BindingWizard<SCABindingPage> wizard = BindingWizard.createSCABindingWizard();
		wizard.getBindingPage().setClustered(true);
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertTrue(properties.getSCABindingPage().isClustered());
		properties.ok();
	}

	@Test
	public void sftpBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SFTP");
		BindingWizard<SFTPBindingPage> wizard = BindingWizard.createSFTPBindingWizard();
		wizard.getBindingPage().setDirectory("sftp-directory");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("sftp-directory", properties.getSFTPBindingPage().getDirectory());
		properties.ok();
	}

	@Test
	public void soapBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SOAP");
		BindingWizard<SOAPBindingPage> wizard = BindingWizard.createSOAPBindingWizard();
		wizard.getBindingPage().setContextPath("soap-binding");
		wizard.getBindingPage().setWsdlURI("soap-wsdl");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("soap-binding", properties.getSOAPBindingPage().getContextPath());
		properties.ok();
	}

	@Test
	public void sqlBindingTest() throws Exception {
		new Service(SERVICE).addBinding("SQL");
		BindingWizard<SQLBindingPage> wizard = BindingWizard.createSQLBindingWizard();
		wizard.getBindingPage().setQuery("sql-query");
		wizard.getBindingPage().setDataSource("data-source");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("sql-query", properties.getSQLBindingPage().getQuery());
		assertEquals("data-source", properties.getSQLBindingPage().getDataSource());
		properties.ok();
	}

	@Test
	public void schedulingBindingTest() throws Exception {
		new Service(SERVICE).addBinding("Scheduling");
		BindingWizard<SchedulingBindingPage> wizard = BindingWizard.createSchedulingBindingWizard();
		wizard.getBindingPage().setCron("scheduling-cron");
		wizard.finish();

		new SwitchYardEditor().save();

		PropertiesPreferencePage properties = new Service(SERVICE).showProperties();
		properties.selectBindingsTab();
		assertEquals("scheduling-cron", properties.getSchedulingBindingPage().getCron());
		properties.ok();
	}
}
