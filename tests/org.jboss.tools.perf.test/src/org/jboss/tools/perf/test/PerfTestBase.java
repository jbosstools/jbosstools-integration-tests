package org.jboss.tools.perf.test;

import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.BeforeClass;

public abstract class PerfTestBase {
    
    protected String projectFolder;
    
    @BeforeClass
    public static void prepareEclipse(){
       disableGit();
       turnOffAutomaticBuild();
       AbstractWait.sleep(TimePeriod.getCustom(120));
    }
    
    private static void disableGit(){
    	 new ShellMenu("Window","Preferences").select();
         new DefaultShell("Preferences");
         new DefaultTreeItem("Team","Git","Projects").select();
         new CheckBox(0).toggle(false);
         new CheckBox(1).toggle(false);
         new CheckBox(2).toggle(false);
         new OkButton().click();
         new WaitWhile(new ShellWithTextIsActive("Preferences"));
         new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
    }
    
    private static void turnOffAutomaticBuild(){
    	ShellMenu m = new ShellMenu("Project","Build Automatically");
    	if(m.isSelected()){
    		m.select();
    	}
    }
    
    public void importProject(){
        ExternalProjectImportWizardDialog ew = new ExternalProjectImportWizardDialog();
        ew.open();
        WizardProjectsImportPage ip = ew.getFirstPage();
        ip.setRootDirectory("/home/jbossqa/validation/jee/"+projectFolder);
        ip.copyProjectsIntoWorkspace(true);
        new CheckBox(new DefaultGroup("Options"),"Search for nested projects").toggle(true);
        new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.LONG);
        new FinishButton().click();
        new WaitUntil(new JobIsRunning(), TimePeriod.ETERNAL);
        new WaitWhile(new JobIsRunning(), TimePeriod.ETERNAL);
    }

}
