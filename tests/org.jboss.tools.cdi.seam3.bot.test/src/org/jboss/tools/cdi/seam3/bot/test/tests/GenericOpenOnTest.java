/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.seam3.bot.test.tests;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.cdi.bot.test.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.jboss.tools.ui.bot.ext.condition.ActiveShellContainsWidget;
import org.jboss.tools.ui.bot.ext.helper.OpenOnHelper;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test operates on generic points openOn in Seam3 using CDI tools
 * 
 * @author jjankovi
 * 
 */

public class GenericOpenOnTest extends Seam3TestBase {

	private static final String GENERIC_POINT_1 = "MyExtendedConfiguration ";
	private static final String GENERIC_POINT_2 = "MyConfigurationProducer.getOneConfig()";
	private static final String GENERIC_POINT_3 = "MyConfigurationProducer.getSecondConfig()";

	private static String projectName = "generic";

	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1);
	}

	/**
	 * https://issues.jboss.org/browse/JBIDE-8692
	 */
	@Test
	public void testGenericOpenOn() {

		packageExplorer.openFile(projectName, CDIConstants.SRC, "cdi.seam",
				"MyBeanInjections.java");

		checkFirstOpenOnAndGeneric();
		checkSecondOpenOnAndGeneric();
		checkThirdOpenOnAndGeneric();

		String parameter = "MyConfiguration config";
		String classTitle = "MyGenericBean.java";
		checkAllGenericPointsForAtribute(parameter, classTitle);

		classTitle = "MyGenericBean2.java";
		String[] atributes = { "MyConfiguration config", "MyBean c",
				"MyBean2 c2", "MyBean3 c3", "MyBean parameter1" };
		for (String atribute : atributes) {
			checkAllGenericPointsForAtribute(atribute, classTitle);
		}

	}

	private void checkFirstOpenOnAndGeneric() {
		checkOpenOnAndGeneric("first1", "MyBeanInjections.java",
				"Generic Configuration Point", "MyConfigurationProducer.java",
				"getOneConfig");
		checkOpenOnAndGeneric("first1", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean.java", "createMyFirstBean");

		checkOpenOnAndGeneric("first2", "MyBeanInjections.java",
				"Generic Configuration Point", "MyConfigurationProducer.java",
				"getSecondConfig");
		checkOpenOnAndGeneric("first2", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean.java", "createMyFirstBean");

		checkOpenOnAndGeneric("first3", "MyBeanInjections.java",
				"Generic Configuration Point", "MyExtendedConfiguration.java",
				"MyExtendedConfiguration");
		checkOpenOnAndGeneric("first3", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean.java", "createMyFirstBean");
	}

	private void checkSecondOpenOnAndGeneric() {
		checkOpenOnAndGeneric("second1", "MyBeanInjections.java",
				"Generic Configuration Point", "MyConfigurationProducer.java",
				"getOneConfig");
		checkOpenOnAndGeneric("second1", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean2.java", "createMySecondBean");

		checkOpenOnAndGeneric("second2", "MyBeanInjections.java",
				"Generic Configuration Point", "MyConfigurationProducer.java",
				"getSecondConfig");
		checkOpenOnAndGeneric("second2", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean2.java", "createMySecondBean");

		checkOpenOnAndGeneric("second3", "MyBeanInjections.java",
				"Generic Configuration Point", "MyExtendedConfiguration.java",
				"MyExtendedConfiguration");
		checkOpenOnAndGeneric("second3", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean2.java", "createMySecondBean");
	}

	private void checkThirdOpenOnAndGeneric() {
		checkOpenOnAndGeneric("third1", "MyBeanInjections.java",
				"Generic Configuration Point", "MyConfigurationProducer.java",
				"getOneConfig");
		checkOpenOnAndGeneric("third1", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean.java", "myThirdBean");

		checkOpenOnAndGeneric("third2", "MyBeanInjections.java",
				"Generic Configuration Point", "MyConfigurationProducer.java",
				"getSecondConfig");
		checkOpenOnAndGeneric("third2", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean.java", "myThirdBean");

		checkOpenOnAndGeneric("third3", "MyBeanInjections.java",
				"Generic Configuration Point", "MyExtendedConfiguration.java",
				"MyExtendedConfiguration");
		checkOpenOnAndGeneric("third3", "MyBeanInjections.java",
				"@Inject Bean", "MyGenericBean.java", "myThirdBean");
	}

	private void checkOpenOnAndGeneric(String openOnString, String titleName,
			String chosenOption, String afterOpenOnTitleName,
			String injectSelectionAtribute) {
		OpenOnHelper.checkOpenOnFileIsOpened(bot, titleName, openOnString,
				chosenOption, afterOpenOnTitleName);
		
		String selectedString = bot.activeEditor().toTextEditor()
				.getSelection();
		
		assertTrue(injectSelectionAtribute + " should be selected. " +
				"Actual selected text: " + selectedString,selectedString.
				equals(injectSelectionAtribute));
	}

	private void checkAllGenericPointsForAtribute(String atribute,
			String classTitle) {
		OpenOnHelper.selectOpenOnOption(bot, classTitle, atribute, 
				"Show All Generic Configuration Points...");
		bot.waitUntil(new ActiveShellContainsWidget(bot, Table.class));
		SWTBotTable genericPointTable = bot.table(0);
		assertTrue(checkAllGenericConfPoints(genericPointTable));
	}

	private boolean checkAllGenericConfPoints(SWTBotTable genericPointTable) {
		boolean isGenericPoint1Present = false;
		boolean isGenericPoint2Present = false;
		boolean isGenericPoint3Present = false;
		for (int rowIterator = 0; rowIterator < genericPointTable.rowCount(); rowIterator++) {
			String itemInTable = genericPointTable.getTableItem(rowIterator)
					.getText();
			if (itemInTable.contains(GENERIC_POINT_1)) {
				isGenericPoint1Present = true;
				continue;
			}
			if (itemInTable.contains(GENERIC_POINT_2)) {
				isGenericPoint2Present = true;
				continue;
			}
			if (itemInTable.contains(GENERIC_POINT_3)) {
				isGenericPoint3Present = true;
				continue;
			}
		}
		return isGenericPoint1Present && isGenericPoint2Present
				&& isGenericPoint3Present;
	}

}
