/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.openon;

import static org.junit.Assert.*;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.api.Editor;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.beansxml.BeansXMLValidationTest;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.junit.Test;

/**
 * Test operates on hyperlinks-openons using CDI support
 * 
 * @author Jaroslav Jankovic
 * 
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class OpenOnTest extends OpenOnBase {

	@Test
	public void testBeanInjectOpenOn() {

		prepareInjectedPointsComponents();

		String injectOption = null;
		for (int i = 1; i < 12; i++) {
			String injectPoint = "myBean" + i;
			injectOption = CDIConstants.SHOW_ALL_ASSIGNABLE;
			if (i > 8)
				injectOption = "Open @Inject Bean";
			checkInjectedPoint(injectPoint, injectOption);
		}

	}

	// https://issues.jboss.org/browse/JBIDE-7025
	@Test
	public void testBeansXMLClassesOpenOn() {

		beansHelper.createBeansXMLWithContent(getProjectName(), 
				this.getClass().getResourceAsStream(BeansXMLValidationTest.CLEAR_BEANS_XML));

		checkBeanXMLDecoratorOpenOn(getPackageName(), "D1");

		checkBeanXMLInterceptorOpenOn(getPackageName(), "I1");

		checkBeanXMLAlternativeOpenOn(getPackageName(), "A1");

	}

	// https://issues.jboss.org/browse/JBIDE-6251
	@Test
	public void testDisposerProducerOpenOn() {

		String className = "Bean1";

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, className,
				getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/BeanWithDisposerAndProducer.java.cdi"));

		editResourceUtil.replaceInEditor(className+".java","BeanComponent", className);
		
		Editor e = new DefaultEditor(className + ".java");
		new DefaultStyledText().selectText("disposeMethod");
		AbstractWait.sleep(TimePeriod.SHORT);
		ContentAssistant ca = e.openOpenOnAssistant();
		ca.chooseProposal("Open Bound Producer Method produceMethod");
		
		assertEquals("produceMethod", new DefaultStyledText().getSelectionText());
		
		e = new DefaultEditor(className + ".java");
		new DefaultStyledText().selectText("produceMethod");
		AbstractWait.sleep(TimePeriod.SHORT);
		ca = e.openOpenOnAssistant();
		ca.chooseProposal("Open Bound Disposer Method disposeMethod");

		assertEquals("disposeMethod", new DefaultStyledText().getSelectionText());

	}

	@Test
	public void testObserverOpenOn() {
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "EventBean",
				getPackageName(), null, OpenOnTest.class.getResourceAsStream("/resources/openon/EventBean.java.cdi"));

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN,
				"ObserverBean", getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/ObserverBean.java.cdi"));

		new TextEditor("EventBean.java");
		editResourceUtil.replaceInEditor("EventBean.java"," event", " event");

		Editor e = new DefaultEditor("ObserverBean.java");
		new DefaultStyledText().selectText("observerMethod");
		AbstractWait.sleep(TimePeriod.SHORT);
		ContentAssistant ca = e.openOpenOnAssistant();
		ca.chooseProposal("Open CDI Event EventBean.event");
		
		assertEquals("event", new DefaultStyledText().getSelectionText());
		
		
		e = new DefaultEditor("EventBean.java");
		new DefaultStyledText().selectText("Event<ObserverBean> event");
		AbstractWait.sleep(TimePeriod.SHORT);
		ca = e.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.startsWith("Open CDI Observer")){
				ca.chooseProposal(p);
				break;
			}
		}
		
		assertEquals("observerMethod", new DefaultStyledText().getSelectionText());
		
	}

	private void prepareInjectedPointsComponents() {
		wizard.createCDIComponent(CDIWizardType.QUALIFIER, "Q1",
				getPackageName(), null);

		wizard.createCDIComponent(CDIWizardType.QUALIFIER, "Q2",
				getPackageName(), null);

		wizard.createCDIComponent(CDIWizardType.BEAN, "MyBean1",
				getPackageName(), null);

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean2",
				getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/InjectedPoints/MyBean2.java.cdi"));

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean3",
				getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/InjectedPoints/MyBean3.java.cdi"));

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean4",
				getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/InjectedPoints/MyBean4.java.cdi"));

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean5",
				getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/InjectedPoints/MyBean5.java.cdi"));

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MainBean",
				getPackageName(), null,
				OpenOnTest.class.getResourceAsStream("/resources/openon/InjectedPoints/MainBean.java.cdi"));

	}

	private void checkInjectedPoint(String injectedPoint, String option) {
		Editor e = new DefaultEditor("MainBean.java");
		new DefaultStyledText().selectText(injectedPoint);
		AbstractWait.sleep(TimePeriod.SHORT);
		ContentAssistant ca = e.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.startsWith(option)){
				ca.chooseProposal(p);
				break;
			}
		}
		
		//OpenOnHelper.selectOpenOnOption(bot, "MainBean.java", injectedPoint, option);
		//SWTBotEditor editor = bot.activeEditor();
		if (option.equals("Open @Inject Bean")) {
			LOGGER.info("Testing injected point: \"" + injectedPoint
					+ "\" started");
			new WaitUntil(new EditorWithTitleIsActive("MyBean4.java"));
			assertEquals("MyBean4", new DefaultStyledText().getSelectionText());
			LOGGER.info("Testing injected point: \"" + injectedPoint
					+ "\" ended");
		} else {
			Shell s = new DefaultShell("Assignable Beans");
			Table assignBeans = new DefaultTable();
			boolean check = checkAllAssignBeans(injectedPoint, assignBeans);
			s.close();
			assertTrue(check);
		}
	}
	
	private boolean checkAllAssignBeans(String injectedPoint,
			Table assignBeans) {
		String packageProjectPath = getPackageName() + " - /"
				+ getProjectName() + "/src";
		String paramAssignBean = "XXX - " + packageProjectPath;
		String prodInjPoint = "@Produces MyBean3.getMyBeanXXX()";
		boolean allassignBeans = false;
		String indexOfInjPoint = injectedPoint.split("myBean")[1];
		int intIndexOfInjPoint = Integer.parseInt(indexOfInjPoint);
		LOGGER.info("Testing injected point: \"" + injectedPoint + "\" started");
		switch (intIndexOfInjPoint) {
		case 1:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean1"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean2"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithIMB2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2")))) {
				allassignBeans = true;
			}
			break;
		case 2:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean2"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2")))) {
				allassignBeans = true;
			}
			break;
		case 3:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean4"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithQ1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithIMB2Q1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ1")))) {
				allassignBeans = true;
			}
			break;
		case 4:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean4"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ1")))) {
				allassignBeans = true;
			}
			break;
		case 5:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean4"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean5"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithQ2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithIMB2Q2")))) {
				allassignBeans = true;
			}
			break;
		case 6:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean4"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean5"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ2")))) {
				allassignBeans = true;
			}
			break;
		case 7:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean1"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean2"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean4"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean5"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithIMB2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithIMB2Q1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithIMB2Q2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithQ1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "1WithQ2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ2")))) {
				allassignBeans = true;
			}
			break;
		case 8:
			if (assignBeans.containsItem(paramAssignBean.replace("XXX",
					"MyBean2"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean4"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							"MyBean5"))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ1")))
					&& assignBeans.containsItem(paramAssignBean.replace("XXX",
							prodInjPoint.replace("XXX", "2WithQ2")))) {
				allassignBeans = true;
			}
			break;
		case 9:
		case 10:
		case 11:
			throw new IllegalStateException("Injection Point \""
					+ injectedPoint + "\" should "
					+ "have been tested earlier!!");
		}
		LOGGER.info("Testing injected point: \"" + injectedPoint + "\" ended");
		return allassignBeans;
	}
}