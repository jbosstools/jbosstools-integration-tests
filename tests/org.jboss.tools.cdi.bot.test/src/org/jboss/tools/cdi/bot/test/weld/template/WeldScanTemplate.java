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
package org.jboss.tools.cdi.bot.test.weld.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hamcrest.core.Is;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfClassAvailableDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIfSystemPropertyDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.AddIncludeExcludeDialog;
import org.jboss.tools.cdi.reddeer.common.model.ui.editor.EditorPartWrapper;
import org.jboss.tools.common.reddeer.label.IDELabel;
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
public abstract class WeldScanTemplate extends CDITestBase {

	private EditorPartWrapper beansEditor;
	
	@Before
	public void openBeansXml() {
		beansEditor = beansXMLHelper.openBeansXml(PROJECT_NAME);
	}

	@Test
	public void testBeansXmlWeldExtension() {
		beansEditor.activateTreePage();
		checkWeldScanCreation();
		checkIncludeExcludeProperty();
		checkClassAvailable();
		checkSystemProperty();
	}

	private void checkWeldScanCreation() {
		beansEditor.newWeldScan();
		beansEditor.save();
		
		String errorMessage = "Scan item was not found in Beans editor: ";
		try {
			new DefaultTreeItem("beans.xml", "Weld Scan");
		} catch (CoreLayerException exc) {
			fail(errorMessage + exc.getMessage());
		}
		
		/** try to create new weld scan again - should be not possible **/
		try {
			beansEditor.newWeldScan();
			fail("There should be not option to create Scan object model again");
		} catch (CoreLayerException exc) {
			// do nothing here - exception is expected behaviour
		}
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
		
		Table includeExcludeTable = beansEditor.getWeldIncludeExcludeTable();
		
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
		
		beansEditor.removeWeldIncludeExclude("org.test.remove1");
		beansEditor.save();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		assertTableRowsCount(2);
	}
	
	private void checkEditIncludeExclude() {
		beansEditor.editWeldIncludeExclude("org.test.add1", "org.test.edit1", false);
		beansEditor.save();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		Table includeExcludeTable = beansEditor.getWeldIncludeExcludeTable();
		assertEquals(includeExcludeTable.getItem(0).getText(0), "include");
		assertEquals(includeExcludeTable.getItem(0).getText(1), "org.test.edit1");
		assertEquals(includeExcludeTable.getItem(0).getText(2), "false");
	}
	
	private void checkClassAvailable() {
		AddIfClassAvailableDialog dialog =  beansEditor.
				invokeWeldAddClassAvailableDialog("org.test.edit1");
		dialog.setName("class1");
		dialog.finish();
		
		beansEditor.save();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		assertTrue("'class1' is not in beans editor", beansEditor.isObjectInEditor(
				IDELabel.WebProjectsTree.BEANS_XML, 
				"Weld Scan", 
				"org.test.edit1",
				"class1"));
	}

	private void checkSystemProperty() {
		AddIfSystemPropertyDialog dialog = beansEditor.
				invokeWeldAddIfSystemPropertyDialog("org.test.add2");
		dialog.setName("property name");
		dialog.setValue("property value");
		dialog.finish();
		
		beansEditor.save();
		new DefaultEditor(IDELabel.WebProjectsTree.BEANS_XML);
		
		assertTrue("'property name' is not in beans editor", beansEditor.isObjectInEditor(
				IDELabel.WebProjectsTree.BEANS_XML, 
				"Weld Scan", 
				"org.test.add2",
				"property name"));
	}

	private void createIncludeExclude(String name, boolean include, 
			boolean isRegularExpression) {
		AddIncludeExcludeDialog dialog = beansEditor.invokeWeldAddIncludeExcludeDialog();
		dialog.setName(name);
		if (include) {
			dialog.include();
		} else {
			dialog.exclude();
		}
		dialog.setRegularExpressionState(isRegularExpression);
		dialog.finish();
		
		beansEditor.save();
	}
	
	private void assertTableRowsCount(int expectedRowCount) {
		assertThat("Expected number of include exclude properties is: " + 
				expectedRowCount + ", found is: " + 
				beansEditor.getWeldIncludeExcludeTable().rowCount(), 
				beansEditor.getWeldIncludeExcludeTable().rowCount(), 
				Is.is(expectedRowCount));
	}
	
}
