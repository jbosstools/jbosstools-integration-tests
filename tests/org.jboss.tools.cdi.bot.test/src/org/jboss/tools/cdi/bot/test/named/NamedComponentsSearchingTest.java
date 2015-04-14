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

package org.jboss.tools.cdi.bot.test.named;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.wizard.OpenCDINamedBeanDialog;
import org.jboss.tools.common.reddeer.label.IDELabel;
import org.junit.After;
import org.junit.Test;

/**
 * Test operates on @Named searching  
 * 
 * @author Jaroslav Jankovic
 * 
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class NamedComponentsSearchingTest extends CDITestBase {
	
	private static final String beanName = "Bean1";
	private static final String stereotypeName = "Stereotype1";
	private OpenCDINamedBeanDialog namedDialog = null;
	private static final String BEAN_STEREOTYPE_PATH = "/resources/named/BeanWithStereotype.java.cdi";
	private static final String BEAN_STEREOTYPE_NAMED_PATH = "/resources/named/BeanWithStereotypeAndNamed.java.cdi";

	@After
	public void waitForJobs() {
		editResourceUtil.deletePackage(getProjectName(), getPackageName());
	}
	
	@Test
	public void testSearchDefaultNamedBean() {
		NewBeanCreationWizard nb = new NewBeanCreationWizard();
		nb.open();
		nb.setName(beanName);
		nb.setPackage(getPackageName());
		nb.setPublic(true);
		nb.setAbstract(false);
		nb.setFinal(false);
		nb.setGenerateComments(false);
		nb.setAlternative(false);
		nb.setRegisterInBeans(false);
		nb.setNamed(true);
		nb.finish();
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
	}
	
	@Test
	public void testSearchNamedParameterBean() {
		
		String namedParam = "someBean";
		
		NewBeanCreationWizard nb = new NewBeanCreationWizard();
		nb.open();
		nb.setName(beanName);
		nb.setPackage(getPackageName());
		nb.setPublic(true);
		nb.setAbstract(false);
		nb.setFinal(false);
		nb.setGenerateComments(false);
		nb.setAlternative(false);
		nb.setRegisterInBeans(false);
		nb.setNamed(true);
		nb.setBeanName(namedParam);
		nb.finish();
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		assertEquals(1,namedDialog.matchingItems().size());
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
	
	}
	
	@Test
	public void testSearchNamedParameterChangeBean() {
				
		String namedParam = "someBean";
		String changedNamedParam = "someOtherBean";
		
		NewBeanCreationWizard nb = new NewBeanCreationWizard();
		nb.open();
		nb.setName(beanName);
		nb.setPackage(getPackageName());
		nb.setPublic(true);
		nb.setAbstract(false);
		nb.setFinal(false);
		nb.setGenerateComments(false);
		nb.setAlternative(false);
		nb.setRegisterInBeans(false);
		nb.setNamed(true);
		nb.setBeanName(namedParam);
		nb.finish();
				
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		assertEquals(1, namedDialog.matchingItems().size());
		namedDialog.ok();
		assertEquals(beanName + ".java", new DefaultEditor().getTitle());
		assertEquals(beanName, new TextEditor().getSelectedText());
		
		editResourceUtil.replaceInEditor(beanName + ".java", namedParam, changedNamedParam);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		assertEquals(0, namedDialog.matchingItems().size());
		namedDialog.setNamedPrefix(changedNamedParam);
		assertEquals(1, namedDialog.matchingItems().size());
		namedDialog.ok();
		assertEquals(beanName + ".java", new DefaultEditor().getTitle());
		assertEquals(beanName, new TextEditor().getSelectedText());
		
	}
	
	@Test
	public void testSearchTwoSameNamedBean() {
				
		String beanName2 = "Bean2";
		String namedParam = "someBean";
		
		NewBeanCreationWizard nb = new NewBeanCreationWizard();
		nb.open();
		nb.setName(beanName);
		nb.setPackage(getPackageName());
		nb.setPublic(true);
		nb.setAbstract(false);
		nb.setFinal(false);
		nb.setGenerateComments(false);
		nb.setAlternative(false);
		nb.setRegisterInBeans(false);
		nb.setNamed(true);
		nb.setBeanName(namedParam);
		nb.finish();
		
		nb.open();
		nb.setName(beanName2);
		nb.setPackage(getPackageName());
		nb.setPublic(true);
		nb.setAbstract(false);
		nb.setFinal(false);
		nb.setGenerateComments(false);
		nb.setAlternative(false);
		nb.setRegisterInBeans(false);
		nb.setNamed(true);
		nb.setBeanName(namedParam);
		nb.finish();
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		List<String> matchingItems = namedDialog.matchingItems();
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
			NewBeanCreationWizard nb = new NewBeanCreationWizard();
			nb.open();
			nb.setName(beanName);
			nb.setPackage(getPackageName());
			nb.setPublic(true);
			nb.setAbstract(false);
			nb.setFinal(false);
			nb.setGenerateComments(false);
			nb.setAlternative(false);
			nb.setRegisterInBeans(false);
			nb.setNamed(true);
			nb.finish();
		}
		
		for (String prefix : prefixesWithCount.keySet()) {
			namedDialog = openSearchNamedDialog();		
			namedDialog.setNamedPrefix(prefix);
			assertTrue("Prefix "+prefix+" has " +namedDialog.matchingItems().size()+" matching items but expected is "+prefixesWithCount.get(prefix),
					namedDialog.matchingItems().size() == prefixesWithCount.get(prefix));
			namedDialog.cancel();			
		}
		
	}
	
	@Test
	public void testSearchBeanWithStereotype() {
		
		NewStereotypeCreationWizard nb = new NewStereotypeCreationWizard();
		nb.open();
		nb.setName(stereotypeName);
		nb.setPackage(getPackageName());
		nb.setInherited(false);
		nb.setNamed(true);
		nb.setAlternative(false);
		nb.setGenerateComments(false);
		nb.setRegisterInBeans(false);
		nb.finish();
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, beanName, getPackageName(), 
				null, NamedComponentsSearchingTest.class.getResourceAsStream(BEAN_STEREOTYPE_PATH));
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
	}
	
	@Test
	public void testSearchBeanWithStereotypeAndNamedParam() {
		
		String namedParam = "someBean";
		
		NewStereotypeCreationWizard nb = new NewStereotypeCreationWizard();
		nb.open();
		nb.setName(stereotypeName);
		nb.setPackage(getPackageName());
		nb.setInherited(false);
		nb.setNamed(true);
		nb.setAlternative(false);
		nb.setGenerateComments(false);
		nb.setRegisterInBeans(false);
		nb.finish();
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, beanName, getPackageName(), 
				null, NamedComponentsSearchingTest.class.getResourceAsStream(BEAN_STEREOTYPE_NAMED_PATH));
		
		namedDialog = openSearchNamedDialog();		
		namedDialog.setNamedPrefix(beanName);
		assertTrue(namedDialog.matchingItems().size() == 0);
		namedDialog.setNamedPrefix(namedParam);
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
	}
	
	@Test
	public void testSearchBeanWithStereotypeWithNamedParamChange() {
		
		String namedParam = "someBean";
		String changedNamedParam = "someOtherBean";
		
		NewStereotypeCreationWizard nb = new NewStereotypeCreationWizard();
		nb.open();
		nb.setName(stereotypeName);
		nb.setPackage(getPackageName());
		nb.setInherited(false);
		nb.setNamed(true);
		nb.setAlternative(false);
		nb.setGenerateComments(false);
		nb.setRegisterInBeans(false);
		nb.finish();
		
		wizard.createCDIComponentWithContent(CDIWizardType.BEAN, beanName, getPackageName(), 
				null, NamedComponentsSearchingTest.class.getResourceAsStream(BEAN_STEREOTYPE_NAMED_PATH));
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(beanName);
		assertTrue(namedDialog.matchingItems().size() == 0);
		namedDialog.setNamedPrefix(namedParam);
		assertTrue(namedDialog.matchingItems().size() == 1);
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
		
		editResourceUtil.replaceInEditor(beanName + ".java",namedParam, changedNamedParam);
		
		namedDialog = openSearchNamedDialog();
		namedDialog.setNamedPrefix(namedParam);
		assertEquals(0, namedDialog.matchingItems().size());
		namedDialog.setNamedPrefix(changedNamedParam);
		assertEquals(1, namedDialog.matchingItems().size());
		namedDialog.ok();
		assertTrue(new DefaultEditor().getTitle().equals(beanName + ".java"));
		assertTrue(new TextEditor().getSelectedText().equals(beanName));
	}
	
	
	private OpenCDINamedBeanDialog openSearchNamedDialog() {
		AbstractWait.sleep(TimePeriod.SHORT);
		new ShellMenu(IDELabel.Menu.NAVIGATE, IDELabel.Menu.OPEN_CDI_NAMED_BEAN).select();
		new DefaultShell("Open CDI Named Bean");
		return new OpenCDINamedBeanDialog();
	}

}
