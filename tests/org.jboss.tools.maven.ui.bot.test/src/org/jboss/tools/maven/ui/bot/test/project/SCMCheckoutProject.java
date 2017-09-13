package org.jboss.tools.maven.ui.bot.test.project;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.m2e.scm.wizards.MavenCheckoutWizard;
import org.eclipse.reddeer.eclipse.m2e.scm.wizards.MavenCheckoutLocationPage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.swt.api.Tree;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
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
		MavenCheckoutLocationPage ml = new MavenCheckoutLocationPage(mc);
		assertEquals(1, ml.getAvailableSCMTypes().size());
		ml.setSCMURL("https://github.com/rawagner/eclipse_tutorial");
		assertTrue(ml.isCheckoutAllProjects());
		assertTrue(ml.isCheckoutHeadRevision());
		mc.finish(TimePeriod.getCustom(TimePeriod.LONG.getSeconds() * 2));
		ignoreM2eConnectors();
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
		MavenCheckoutLocationPage ml = new MavenCheckoutLocationPage(mc);
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
		new WaitWhile(new ShellIsAvailable("Import Maven Projects"),TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
		ignoreM2eConnectors();
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		assertTrue(pe.containsProject("eclipsetutorial.core"));
		assertEquals(1,pe.getProjects().size());
	}
	
	private void ignoreM2eConnectors(){
		try{
			new ShellIsAvailable("Discover m2e connectors");
			new PushButton("Cancel").click();
		}catch(CoreLayerException ex){
			//The shell "Discover m2e connectors" is shown only if these connectors are not installed.
		}
	}
}
