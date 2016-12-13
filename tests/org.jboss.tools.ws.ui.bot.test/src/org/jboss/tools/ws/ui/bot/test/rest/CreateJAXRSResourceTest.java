package org.jboss.tools.ws.ui.bot.test.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulWebService;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSApplicationWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSResourceCreateApplicationWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSResourceCreateResourceWizardPage;
import org.jboss.tools.ws.reddeer.ui.wizards.jaxrs.JAXRSResourceWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests operates on JAX-RS Resource wizard
 * 
 * @author Radoslav Rabara
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerReqState.PRESENT)
public class CreateJAXRSResourceTest extends RESTfulTestBase {

	private final String PACKAGE_NAME = "org.rest.test";
	private final String FILE_NAME = "RestService";
	private final String RESOURCE_PATH = "/customer";
	private final String TARGET_ENTITY = PACKAGE_NAME + ".Customer";
	private final String APPLICATION_FILE_NAME = "App";
	private final String APPLICATION_PATH = "/rest";

	private final JAXRSResourceWizard wizard = new JAXRSResourceWizard();

	@Before
	public void setup() {
		super.setup();
		wizard.open();
	}

	@After
	public void cleanup() {
		//close the wizard if it's present
		try {
			wizard.cancel();
		} catch(CoreLayerException e) {
		}

		super.cleanup();
	}

	@Override
    protected String getWsProjectName() {
        return "resource";
    }

	@Test
	public void createResourceWithEntityAndMethodsAndCommentsTest() {
		/* set wizard to create JAX-RS resource with entity and all methods */
		JAXRSResourceCreateResourceWizardPage firstPage = new JAXRSResourceCreateResourceWizardPage();
		firstPage.setPackage(PACKAGE_NAME);
		firstPage.setTargetEntity(TARGET_ENTITY);
		firstPage.setName(FILE_NAME);
		firstPage.setResourcePath(RESOURCE_PATH);
		firstPage.setCreate(true);
		firstPage.setUpdate(true);
		firstPage.setDeleteById(true);
		firstPage.setFindById(true);
		firstPage.setListAll(true);
		firstPage.setGenerateComments(true);
		wizard.next();

		/* set wizard to create also JAX-RS Application */
		JAXRSResourceCreateApplicationWizardPage secondPage = new JAXRSResourceCreateApplicationWizardPage();
		JAXRSResourceCreateApplicationWizardPage.SubclassOfApplicationWizardPart wp = secondPage.useSubclassOfApplication();
		wp.setPackage(PACKAGE_NAME);
		wp.setName(APPLICATION_FILE_NAME);
		wp.setApplicationPath(APPLICATION_PATH);
		wizard.finish();

		/* there should be no error */
		assertCountOfProblemsExists(ProblemType.ERROR, 0);

		/* JAX-RS Resource was created */
		assertTrue("JAX-RS Application was not created", new ProjectExplorer().getProject(getWsProjectName())
				.containsItem("Java Resources", "src", PACKAGE_NAME, FILE_NAME + ".java") == true);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(getWsProjectName());

		/* created methods should be displayed in JAX-RS Explorer */
		assertCountOfRESTServices(restServices, 5);
		assertThatAllRestServicesArePresent(restServices);

		/* JAX-RS Application class was also created*/
		assertTrue("JAX-RS Application was not created", new ProjectExplorer().getProject(getWsProjectName())
				.containsItem("Java Resources", "src", PACKAGE_NAME, APPLICATION_FILE_NAME + ".java"));
	}

	/**
	 * Resolved - JBIDE-17457
	 * (JAX-RS Resource Wizard has inconsistent validation of target entity)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17457
	 */
	@Test
	public void testSetTargetEntity() {
		final String NON_EXISTING_TARGET_ENTITY = "NON_EXISTING_TARGET_ENTITY";
		final String ERROR_TARGET_CLASS_NOT_EXISTS = " Target Class does not exist in project's classpath";
		JAXRSResourceCreateResourceWizardPage firstPage = new JAXRSResourceCreateResourceWizardPage();

		/* set name to dismiss "type name is empty" error */
		firstPage.setName(FILE_NAME);

		firstPage.setTargetEntity(NON_EXISTING_TARGET_ENTITY);
		assertThat(firstPage.getWizardPageInfoText(), Is.is(ERROR_TARGET_CLASS_NOT_EXISTS));

		firstPage.setTargetEntity("");
		assertThat("JBIDE-17457", firstPage.getWizardPageInfoText(), IsNot.not(Is.is(ERROR_TARGET_CLASS_NOT_EXISTS)));

		firstPage.setTargetEntity(TARGET_ENTITY);
		assertThat(firstPage.getWizardPageInfoText(), IsNot.not(Is.is(ERROR_TARGET_CLASS_NOT_EXISTS)));

		assertThat(new PushButton("Next >").isEnabled(), Is.is(true));
	}

	/**
	 * Resolved - JBIDE-17457
	 * (JAX-RS Resource Wizard has inconsistent validation of target entity)
	 * 
	 * @see https://issues.jboss.org/browse/JBIDE-17457
	 */
	@Test
	public void testWithoutTargetEntityCantUseMethods() {
		JAXRSResourceCreateResourceWizardPage firstPage = new JAXRSResourceCreateResourceWizardPage();

		assertTrue("Dialog shouldn't allow to create methods when the target entity is not set",
				!firstPage.areAvailableJAXRSResourceMethodEnabled());

		firstPage.setTargetEntity("");
		assertTrue("Dialog shouldn't allow to create methods when the target entity is not set",
				!firstPage.areAvailableJAXRSResourceMethodEnabled());

		firstPage.setTargetEntity(TARGET_ENTITY);
		assertTrue("Dialog have to allow to create methods when the target entity is set",
				firstPage.areAvailableJAXRSResourceMethodEnabled());

		firstPage.setName(FILE_NAME);

		assertThat(new PushButton("Next >").isEnabled(), Is.is(true));
	}

	@Test
	public void createResourceWithoutTargetEntity() {
		/* set wizard to create JAX-RS resource with entity and all methods */
		JAXRSResourceCreateResourceWizardPage firstPage = new JAXRSResourceCreateResourceWizardPage();
		firstPage.setPackage(PACKAGE_NAME);
		firstPage.setName(FILE_NAME);
		firstPage.setResourcePath(RESOURCE_PATH);
		wizard.next();

		/* set wizard to create also JAX-RS Application */
		JAXRSResourceCreateApplicationWizardPage secondPage = new JAXRSResourceCreateApplicationWizardPage();
		JAXRSApplicationWizardPage.SubclassOfApplicationWizardPart wp = secondPage.useSubclassOfApplication();
		wp.setPackage(PACKAGE_NAME);
		wp.setName(APPLICATION_FILE_NAME);
		wp.setApplicationPath(APPLICATION_PATH);
		wizard.finish();

		/* there should be no error */
		assertCountOfProblemsExists(ProblemType.ERROR, 0);

		/* JAX-RS Resource was created */
		assertTrue("JAX-RS Application was not created", new ProjectExplorer().getProject(getWsProjectName())
				.containsItem("Java Resources", "src", PACKAGE_NAME, FILE_NAME + ".java") == true);

		/* get RESTful services from JAX-RS REST explorer for the project */
		List<RESTfulWebService> restServices = restfulServicesForProject(getWsProjectName());

		/* created methods should be displayed in JAX-RS Explorer */
		assertCountOfRESTServices(restServices, 0);

		/* JAX-RS Application class was also created*/
		assertTrue("JAX-RS Application was not created", new ProjectExplorer().getProject(getWsProjectName())
				.containsItem("Java Resources", "src", PACKAGE_NAME, APPLICATION_FILE_NAME + ".java") == true);
	}

	private void assertThatAllRestServicesArePresent(List<RESTfulWebService> restServices) {
		final String idPathURITemplate = "/{id:[0-9][0-9]*}";
		final String mediaTypes = "application/xml,application/json";
		final String PATH_PREFIX = APPLICATION_PATH + RESOURCE_PATH;
		for (RESTfulWebService restService : restServices) {
			final String serviceName = restService.getMethod();
			if(serviceName.equals("POST")) {
				assertEquals("Path of POST operation ", restService.getPath(), PATH_PREFIX);
				assertEquals("Consumes info of POST operation ", restService.getConsumingContentType(), mediaTypes);
				assertEquals("Produces info of POST operation ", restService.getProducingContentType(),  mediaTypes);
				continue;
			}
			if(serviceName.equals("GET")) {
				if(restService.getPath().equals(PATH_PREFIX + idPathURITemplate)) {
					assertEquals("Consumes info of GET operation ", restService.getConsumingContentType(), mediaTypes);
					assertEquals("Produces info of GET operation ", restService.getProducingContentType(),  mediaTypes);
					continue;
				}
				assertEquals("Path of GET operation ", restService.getPath(), PATH_PREFIX + "?start={Integer}&max={Integer}");
				assertEquals("Consumes info of GET operation ", restService.getConsumingContentType(), mediaTypes);
				assertEquals("Produces info of GET operation ", restService.getProducingContentType(),  mediaTypes);
				continue;
			}
			if(serviceName.equals("PUT")) {
				assertEquals("Path of PUT operation ", restService.getPath(), PATH_PREFIX + idPathURITemplate);
				assertEquals("Consumes info of PUT operation ", restService.getConsumingContentType(), mediaTypes);
				assertEquals("Produces info of PUT operation ", restService.getProducingContentType(),  mediaTypes);
				continue;
			}
			if(serviceName.equals("DELETE")) {
				assertEquals("Path of DELETE operation ", restService.getPath(), PATH_PREFIX + idPathURITemplate);
				assertEquals("Consumes info of DELETE operation ", restService.getConsumingContentType(), mediaTypes);
				assertEquals("Produces info of DELETE operation ", restService.getProducingContentType(),  mediaTypes);
				continue;
			}
			fail("Not expected rest service: " + serviceName);
		}
	}
}
