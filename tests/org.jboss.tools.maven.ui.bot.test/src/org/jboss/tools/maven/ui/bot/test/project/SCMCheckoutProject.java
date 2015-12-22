package org.jboss.tools.maven.ui.bot.test.project;

import static org.junit.Assert.*;


import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.m2e.core.ui.wizard.MavenCheckoutWizard;
import org.jboss.reddeer.eclipse.m2e.scm.wizard.MavenCheckoutLocationPage;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Before;
import org.junit.Test;

@OpenPerspective(JavaEEPerspective.class)
public class SCMCheckoutProject extends AbstractMavenSWTBotTest {
	
	@Before
	public void deleteProjects(){
		deleteProjects(true);
	}

	@Test
	public void checkoutAllProjectsFromSCM() {
		MavenCheckoutWizard mc = new MavenCheckoutWizard();
		mc.open();
		MavenCheckoutLocationPage ml = new MavenCheckoutLocationPage();
		assertEquals(1, ml.getAvailableSCMTypes().size());
		ml.setSCMURL("https://github.com/rawagner/eclipse_tutorial");
		assertTrue(ml.isCheckoutAllProjects());
		assertTrue(ml.isCheckoutHeadRevision());
		mc.finish(TimePeriod.getCustom(TimePeriod.LONG.getSeconds() * 2));
		new WaitUntil(new ShellWithTextIsAvailable("Import Maven Projects"),TimePeriod.LONG);
		new CancelButton().click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject("eclipsetutorial"));
		assertTrue(pe.containsProject("eclipsetutorial.core"));
		assertTrue(pe.containsProject("eclipsetutorial.feature"));
		assertTrue(pe.containsProject("eclipsetutorial.site"));
		assertTrue(pe.containsProject("eclipsetutorial.test"));
		assertEquals(5,pe.getProjects().size());
	}

	
	@Test
	public void checkoutProjectFromSCM() {
		MavenCheckoutWizard mc = new MavenCheckoutWizard();
		mc.open();
		MavenCheckoutLocationPage ml = new MavenCheckoutLocationPage();
		assertEquals(1, ml.getAvailableSCMTypes().size());
		ml.setSCMURL("https://github.com/rawagner/eclipse_tutorial");
		assertTrue(ml.isCheckoutAllProjects());
		ml.toggleCheckoutAllProjects(false);
		assertFalse(ml.isCheckoutAllProjects());
		assertTrue(ml.isCheckoutHeadRevision());
		mc.finish(TimePeriod.getCustom(TimePeriod.LONG.getSeconds() * 2));
		new DefaultShell("Import Maven Projects");
		new PushButton("Deselect All").click();
		Tree t = new DefaultTree();
		for(TreeItem ti: t.getAllItems()){
			if(ti.getText().contains("eclipsetutorial.core")){
				ti.setChecked(true);
				break;
			}
		}
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsAvailable("Import Maven Projects"),TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		new WaitUntil(new ShellWithTextIsAvailable("Import Maven Projects"),TimePeriod.LONG); //different shell, same name
		new CancelButton().click();
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject("eclipsetutorial.core"));
		assertEquals(1,pe.getProjects().size());
	}
}
