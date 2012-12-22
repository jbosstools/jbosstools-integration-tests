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

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.cdi.bot.test.CDIConstants;
import org.jboss.tools.cdi.bot.test.annotations.CDIWizardType;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.Test;

/**
 * Test operates on hyperlinks-openons using CDI support
 * 
 * @author Jaroslav Jankovic
 * 
 */

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

		beansHelper.createEmptyBeansXML(getProjectName());

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
				"/resources/openon/BeanWithDisposerAndProducer.java.cdi");

		editResourceUtil.replaceInEditor("BeanComponent", className);

		bot.sleep(Timing.time2S());

		OpenOnHelper.selectOpenOnOption(bot, className + ".java",
				"disposeMethod", "Open Bound Producer");

		OpenOnHelper.selectOpenOnOption(bot, className + ".java",
				"disposeMethod", "Open Bound Producer");
		assertTrue(bot.activeEditor().toTextEditor().getSelection()
				.equals("produceMethod"));

		OpenOnHelper.selectOpenOnOption(bot, className + ".java",
				"produceMethod", "Open Bound Disposer");
		assertTrue(bot.activeEditor().toTextEditor().getSelection()
				.equals("disposeMethod"));

	}

	@Test
	public void testObserverOpenOn() {
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "EventBean",
				getPackageName(), null, "/resources/openon/EventBean.java.cdi");

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN,
				"ObserverBean", getPackageName(), null,
				"/resources/openon/ObserverBean.java.cdi");

		bot.editorByTitle("EventBean.java").show();
		editResourceUtil.replaceInEditor(" event", " event");

		OpenOnHelper.selectOpenOnOption(bot, "ObserverBean.java",
				"observerMethod", "Open CDI Event");
		assertTrue(bot.activeEditor().toTextEditor().getSelection()
				.equals("event"));
		
		OpenOnHelper.selectOpenOnOption(bot, "EventBean.java",
				"Event<ObserverBean> event", "Open CDI Observer Method");
		assertTrue(bot.activeEditor().toTextEditor().getSelection()
				.equals("observerMethod"));
		
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
				"/resources/openon/InjectedPoints/MyBean2.java.cdi");

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean3",
				getPackageName(), null,
				"/resources/openon/InjectedPoints/MyBean3.java.cdi");

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean4",
				getPackageName(), null,
				"/resources/openon/InjectedPoints/MyBean4.java.cdi");

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MyBean5",
				getPackageName(), null,
				"/resources/openon/InjectedPoints/MyBean5.java.cdi");

		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, "MainBean",
				getPackageName(), null,
				"/resources/openon/InjectedPoints/MainBean.java.cdi");

		util.waitForNonIgnoredJobs();

	}

	private void checkInjectedPoint(String injectedPoint, String option) {
		OpenOnHelper.selectOpenOnOption(bot, "MainBean.java", injectedPoint, option);
		SWTBotEditor editor = bot.activeEditor();
		if (option.equals("Open @Inject Bean")) {
			LOGGER.info("Testing injected point: \"" + injectedPoint
					+ "\" started");
			assertTrue(editor.getTitle().equals("MyBean4.java"));
			assertTrue(editor.toTextEditor().getSelection().equals("MyBean4"));
			LOGGER.info("Testing injected point: \"" + injectedPoint
					+ "\" ended");
		} else {
			SWTBotTable assignBeans = bot.table(0);
			assertTrue(checkAllAssignBeans(injectedPoint, assignBeans));
		}
	}

	private boolean checkAllAssignBeans(String injectedPoint,
			SWTBotTable assignBeans) {
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