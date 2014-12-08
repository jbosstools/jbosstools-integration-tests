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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.seam3.bot.test.base.Seam3TestBase;
import org.jboss.tools.cdi.seam3.bot.test.util.SeamLibrary;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test operates on generic points openOn in Seam3 using CDI tools
 * 
 * @author jjankovi
 * 
 */
@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
public class GenericOpenOnTest extends Seam3TestBase {

	private static final String GENERIC_POINT_1 = "MyExtendedConfiguration ";
	private static final String GENERIC_POINT_2 = "MyConfigurationProducer.getOneConfig()";
	private static final String GENERIC_POINT_3 = "MyConfigurationProducer.getSecondConfig()";

	private static String projectName = "generic";
	
	@InjectRequirement
    private static ServerRequirement sr;

	@BeforeClass
	public static void setup() {
		importSeam3ProjectWithLibrary(projectName, SeamLibrary.SOLDER_3_1, sr.getRuntimeNameLabelText(sr.getConfig()));
	}

	/**
	 * https://issues.jboss.org/browse/JBIDE-8692
	 */
	@Test
	public void testGenericOpenOn() {
		
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem(CDIConstants.SRC, "cdi.seam","MyBeanInjections.java").open();

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
		
		TextEditor te = new TextEditor(titleName);
		te.selectText(openOnString);
		ContentAssistant ca = te.openOpenOnAssistant();
		for(String p: ca.getProposals()){
			if(p.contains(chosenOption)){
				ca.chooseProposal(p);
				break;
			}
		}
		TextEditor t = new TextEditor(afterOpenOnTitleName);
		
		
		//OpenOnHelper.checkOpenOnFileIsOpened(bot, titleName, openOnString,
		//		chosenOption, afterOpenOnTitleName);
		
		String selectedString = t.getSelectedText();
		
		assertTrue(injectSelectionAtribute + " should be selected. " +
				"Actual selected text: " + selectedString,selectedString.
				equals(injectSelectionAtribute));
	}

	private void checkAllGenericPointsForAtribute(String atribute,
			String classTitle) {
		TextEditor te = new TextEditor(classTitle);
		te.selectText(atribute);
		ContentAssistant ca = te.openOpenOnAssistant();
		ca.chooseProposal("Show All Generic Configuration Points...");
		AbstractWait.sleep(TimePeriod.SHORT);
		Shell s = new DefaultShell();
		List<String> proposals = new ArrayList<String>();
		for(TableItem i: new DefaultTable().getItems()){
			proposals.add(i.getText());
		}
		s.close();
		
		
	//	OpenOnHelper.selectOpenOnOption(bot, classTitle, atribute, 
		//		"Show All Generic Configuration Points...");
		//bot.waitUntil(new ActiveShellContainsWidget(bot, Table.class));
		//SWTBotTable genericPointTable = bot.table(0);
		assertTrue(checkAllGenericConfPoints(proposals));
	}

	private boolean checkAllGenericConfPoints(List<String> proposals) {
		boolean isGenericPoint1Present = false;
		boolean isGenericPoint2Present = false;
		boolean isGenericPoint3Present = false;
		for (String item: proposals) {
			if (item.contains(GENERIC_POINT_1)) {
				isGenericPoint1Present = true;
				continue;
			}
			if (item.contains(GENERIC_POINT_2)) {
				isGenericPoint2Present = true;
				continue;
			}
			if (item.contains(GENERIC_POINT_3)) {
				isGenericPoint3Present = true;
				continue;
			}
		}
		return isGenericPoint1Present && isGenericPoint2Present
				&& isGenericPoint3Present;
	}

}
