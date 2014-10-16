/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hamcrest.core.Is;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.exception.WorkbenchPartNotFound;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfClassAvailableDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfSystemPropertyDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIncludeExcludeDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests weld extension to beans.xml. It consists of creating of Scan object, 
 * managment of include/exclude object models and system property/class if available property
 * on specific include/exclude property
 * 
 * @author jjankovi
 *
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class WeldExtensionTest extends CDITestBase {

	private EditorPartWrapper beansEditor;
	
	@InjectRequirement
	protected static ServerRequirement sr;
	
	@Override
	protected String getProjectName() {
		return "weldExtension";
	}
	
	@Before
	public void prepareWorkspace() {
		if (!projectHelper.projectExists(getProjectName())) {
			projectHelper.createCDIProjectWithCDIWizard(getProjectName(),sr.getRuntimeNameLabelText(sr.getConfig()));
		}
		if (!beansXmlNotOpened()) {
			openBeansXml();
		}
	}
	
	@AfterClass
	public static void cleanUp() {
		saveBeansXmlEditor();
	}

	@Test
	public void testBeansXmlWeldExtension() {
		checkWeldScanCreation();
		checkIncludeExcludeProperty();
		checkClassAvailable();
		checkSystemProperty();
	}

	private void checkWeldScanCreation() {
		beansEditor.newWeldScan();
		saveBeansXmlEditor();
		
		assertScanCreated();
		attemptToCreateNewWeldScan();
	}

	private void checkIncludeExcludeProperty() {
		checkAddIncludeExclude();
		checkRemoveIncludeExclude();
		checkEditIncludeExclude();
	}

	private void checkAddIncludeExclude() {
		assertTableRowsCount(0);
		
		createIncludeExclude("org.test.add1", true, true);
		createIncludeExclude("org.test.add2", true, false);
		
		assertTableRowsCount(2);
		
		Table includeExcludeTable = beansEditor.getIncludeExcludeTable();
		
		assertEquals(includeExcludeTable.getItem(0).getText(0), "include");
		assertEquals(includeExcludeTable.getItem(0).getText(1), "org.test.add1");
		assertEquals(includeExcludeTable.getItem(0).getText(2), "true");
		assertEquals(includeExcludeTable.getItem(1).getText(0), "include");
		assertEquals(includeExcludeTable.getItem(1).getText(1), "org.test.add2");
		assertEquals(includeExcludeTable.getItem(1).getText(2), "false");
	}
	
	private void checkRemoveIncludeExclude() {
		createIncludeExclude("org.test.remove1", true, false);
		
		assertTableRowsCount(3);
		
		beansEditor.removeIncludeExclude("org.test.remove1");
		saveBeansXmlEditor();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		assertTableRowsCount(2);
	}
	
	private void checkEditIncludeExclude() {
		beansEditor.editIncludeExclude("org.test.add1", "org.test.edit1", false);
		saveBeansXmlEditor();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		Table includeExcludeTable = beansEditor.getIncludeExcludeTable();
		assertEquals(includeExcludeTable.getItem(0).getText(0), "include");
		assertEquals(includeExcludeTable.getItem(0).getText(1), "org.test.edit1");
		assertEquals(includeExcludeTable.getItem(0).getText(2), "false");
	}
	
	private void checkClassAvailable() {
		AddIfClassAvailableDialog dialog =  beansEditor.
				invokeAddClassAvailableDialog("org.test.edit1");
		dialog.setName("class1");
		dialog.finish();
		
		saveBeansXmlEditor();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		assertTrue("'class1' is not in beans editor", beansEditor.isObjectInEditor(
				IDELabel.WebProjectsTree.BEANS_XML, 
				"Scan", 
				"org.test.edit1",
				"class1"));
	}

	private void checkSystemProperty() {
		AddIfSystemPropertyDialog dialog = beansEditor.
				invokeAddIfSystemPropertyDialog("org.test.add2");
		dialog.setName("property name");
		dialog.setValue("property value");
		dialog.finish();
		
		saveBeansXmlEditor();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		assertTrue("'property name' is not in beans editor", beansEditor.isObjectInEditor(
				IDELabel.WebProjectsTree.BEANS_XML, 
				"Scan", 
				"org.test.add2",
				"property name"));
	}

	private void createIncludeExclude(String name, boolean include, 
			boolean isRegularExpression) {
		AddIncludeExcludeDialog dialog = beansEditor.invokeAddIncludeExcludeDialog();
		dialog.setName(name);
		if (include) {
			dialog.include();
		} else {
			dialog.exclude();
		}
		dialog.setRegularExpressionState(isRegularExpression);
		dialog.finish();
		
		saveBeansXmlEditor();
	}
	
	private void attemptToCreateNewWeldScan() {
		/** try to create new weld scan again - should be not possible **/
		try {
			beansEditor.newWeldScan();
			fail("There should be not option to create Scan object model again");
		} catch (SWTLayerException exc) {
			// do nothing here - exception is expected behaviour
		}
	}

	private void assertScanCreated() {
		String errorMessage = "Scan item was not found in Beans editor: ";
		try {
			new DefaultTreeItem("beans.xml", "Scan");
		} catch (SWTLayerException exc) {
			fail(errorMessage + exc.getMessage());
		}
	}

	private boolean beansXmlNotOpened() {
		try{
			beansEditor = new EditorPartWrapper();
		} catch (WorkbenchPartNotFound ex){
			return false;
		}
		return true;
	}
	
	private static void saveBeansXmlEditor() {
		new EditorPartWrapper().save();
	}

	private void openBeansXml() {
		PackageExplorer packageExplorer = new PackageExplorer();
		packageExplorer.open();
		
		Project project = packageExplorer.getProject(getProjectName());
		project.select();
		project.getProjectItem(IDELabel.WebProjectsTree.WEB_CONTENT,
				IDELabel.WebProjectsTree.WEB_INF,
				IDELabel.WebProjectsTree.BEANS_XML)
			.open();
		beansEditor = new EditorPartWrapper();
	}
	
	private void assertTableRowsCount(int expectedRowCount) {
		assertThat("Expected number of include exclude properties is: " + 
				expectedRowCount + ", found is: " + 
				beansEditor.getIncludeExcludeTable().rowCount(), 
				beansEditor.getIncludeExcludeTable().rowCount(), 
				Is.is(expectedRowCount));
	}
	
}
