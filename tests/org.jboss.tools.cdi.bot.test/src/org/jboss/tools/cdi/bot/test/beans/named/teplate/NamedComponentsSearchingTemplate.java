/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.beans.named.teplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.menu.ShellMenu;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.CDIConstants;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.OpenCDINamedBeanDialog;
import org.jboss.tools.cdi.reddeer.condition.NamedDialogHasMatchingItems;
import org.jboss.tools.cdi.reddeer.uiutils.EditorResourceHelper;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.Before;
import org.junit.Test;


//TODO
////http://stackoverflow.com/questions/19414709/cant-find-named-cdi-bean-with-default-name-in-el-facelet
public abstract class NamedComponentsSearchingTemplate extends CDITestBase{
	
	private static final String beanName = "Bean1";
	private static final String stereotypeName = "Stereotype1";
	private OpenCDINamedBeanDialog namedDialog = null;
	private static final String BEAN_STEREOTYPE_PATH = "resources/named/BeanWithStereotype.java.cdi";
	private static final String BEAN_STEREOTYPE_NAMED_PATH = "resources/named/BeanWithStereotypeAndNamed.java.cdi";
	
	@Before
	public void waitForJobs() {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).refresh();
		if(pe.getProject(getProjectName()).
				containsResource(CDIConstants.JAVA_RESOURCES,CDIConstants.SRC,getPackageName())){
			new EditorResourceHelper().deletePackage(getProjectName(), getPackageName());
		}
	}
	
	@Test
	public void testSearchDefaultNamedBean() {
		beansHelper.createBean(beanName, getPackageName(), false, false, false, 
				false, false, false,true,null, null);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
	}
	
	@Test
	public void testSearchBeanWithoutNamed() {
		beansHelper.createBean(beanName, getPackageName(), false, false, false, 
				false, false, false,false,null, null);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 0));
		assertTrue(namedDialog.matchingItems().size() == 0);
		namedDialog.cancel();
	}
	
	@Test
	public void testSearchNamedParameterBean() {
		
		String namedParam = "someBean";
		
		beansHelper.createBean(beanName, getPackageName()
				, false, false, false, false, false, false,true,namedParam, null);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertEquals(1,namedDialog.matchingItems().size());
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
	
	}
	
	@Test
	public void testSearchNamedParameterChangeBean() {
				
		String namedParam = "someBean";
		String changedNamedParam = "someOtherBean";
		
		beansHelper.createBean(beanName, getPackageName(), false, false, false,
				false, false, false, true, namedParam, null);
				
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertEquals(1, namedDialog.matchingItems().size());
		namedDialog.ok();
		assertEquals(beanName + ".java", new DefaultEditor().getTitle());
		assertEquals(beanName, new TextEditor().getSelectedText());
		
		new EditorResourceHelper().replaceInEditor(beanName + ".java", namedParam, changedNamedParam);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 0));
		assertEquals(0, namedDialog.matchingItems().size());
		namedDialog.setNamedPrefix(changedNamedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertEquals(1, namedDialog.matchingItems().size());
		namedDialog.ok();
		assertEquals(beanName + ".java", new DefaultEditor().getTitle());
		assertEquals(beanName, new TextEditor().getSelectedText());
		
	}
	
	@Test
	public void testSearchTwoSameNamedBean() {
				
		String beanName2 = "Bean2";
		String namedParam = "someBean";
		
		beansHelper.createBean(beanName, getPackageName(), false, false, false, false,
				false, false, true, namedParam, null);
		
		beansHelper.createBean(beanName2, getPackageName(), false, false, false, false, false,
				false, true, namedParam, null);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		List<String> matchingItems = namedDialog.matchingItems();
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 2));
		assertTrue(matchingItems.size() == 2);
		for (String matchingItem : matchingItems) {
			if (matchingItem.contains(beanName)) {
				namedDialog.setMatchingItems(matchingItem);
				break;
			}
		}		
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
	}
	
	@Test
	public void testSearchBeansWithSamePrefixNamedParam() {
	
		String[] beansNames = {"SomeBean", "SomeBean1", "SomeBean2", "SomeBean22", "SomeOtherBean"};
		
		Map<String, Integer> prefixesWithCount = new LinkedHashMap<String, Integer>();
		prefixesWithCount.put(beansNames[0], 4);
		prefixesWithCount.put(beansNames[1], 1);
		prefixesWithCount.put(beansNames[2], 2);
		prefixesWithCount.put(beansNames[3], 1);
		prefixesWithCount.put("Some", 5);
		
		for (String beanName : beansNames) {
			beansHelper.createBean(beanName, getPackageName(), false, false, false, false,
					false, false, true, null, null);
		}
		
		for (String prefix : prefixesWithCount.keySet()) {
			namedDialog = openSearchNamedDialog();		
			namedDialog.setNamedPrefix(prefix);
			new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, prefixesWithCount.get(prefix)));
			assertTrue("Prefix "+prefix+" has " +namedDialog.matchingItems().size()+" matching items but expected is "+prefixesWithCount.get(prefix),
					namedDialog.matchingItems().size() == prefixesWithCount.get(prefix));
			namedDialog.cancel();			
		}
		
	}
	
	@Test
	public void testSearchBeanWithStereotype() {
		
		beansHelper.createStereotype(stereotypeName, getPackageName(), false, true,
				false, false, false);
		
		beansHelper.createBean(beanName, getPackageName(),false,false, false,false,false,false,false,null,null);
		
		editResourceUtil.replaceClassContentByResource(beanName+".java", readFile(BEAN_STEREOTYPE_PATH), false);
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
	}
	
	@Test
	public void testSearchBeanWithStereotypeAndNamedParam() {
		
		String namedParam = "someBean";
		
		beansHelper.createStereotype(stereotypeName, getPackageName(), false, true,
				false, false, false);
		
		beansHelper.createBean(beanName, getPackageName(),false,false, false,false,false,false,false,null,null);
		
		editResourceUtil.replaceClassContentByResource(beanName+".java", readFile(BEAN_STEREOTYPE_NAMED_PATH), false);
		
		namedDialog = openSearchNamedDialog();		
		namedDialog.setNamedPrefix(beanName);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 0));
		assertTrue(namedDialog.matchingItems().size() == 0);
		namedDialog.setNamedPrefix(namedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
	}
	
	@Test
	public void testSearchBeanWithStereotypeWithNamedParamChange() {
		
		String namedParam = "someBean";
		String changedNamedParam = "someOtherBean";
		
		beansHelper.createStereotype(stereotypeName, getPackageName(), false,
				true, false, false, false);
		
		beansHelper.createBean(beanName, getPackageName(),false,false, false,false,false,false,false,null,null);
		
		editResourceUtil.replaceClassContentByResource(beanName+".java", readFile(BEAN_STEREOTYPE_NAMED_PATH), false);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 0));
		assertEquals(0,namedDialog.matchingItems().size());
		namedDialog.setNamedPrefix(namedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertEquals(1,namedDialog.matchingItems().size());
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
		new EditorResourceHelper().replaceInEditor(beanName + ".java",namedParam, changedNamedParam);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 0));
		assertEquals(0, namedDialog.matchingItems().size());
		namedDialog.setNamedPrefix(changedNamedParam);
		new WaitUntil(new NamedDialogHasMatchingItems(namedDialog, 1));
		assertEquals(1, namedDialog.matchingItems().size());
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
	}
	
	
	private OpenCDINamedBeanDialog openSearchNamedDialog() {
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu().getItem(IDELabel.Menu.NAVIGATE, IDELabel.Menu.OPEN_CDI_NAMED_BEAN).select();
		new DefaultShell("Open CDI Named Bean");
		AbstractWait.sleep(TimePeriod.SHORT);
		return new OpenCDINamedBeanDialog();
	}

}
