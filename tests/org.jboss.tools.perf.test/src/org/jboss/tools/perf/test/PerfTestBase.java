package org.jboss.tools.perf.test;

import java.io.File;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.maven.reddeer.preferences.MavenUserPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizard;
import org.jboss.tools.maven.reddeer.wizards.MavenImportWizardFirstPage;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class PerfTestBase {
    
    protected String projectFolder;
    public static final String USER_SETTINGS = "resources/usersettings/settings.xml"; 
    
    @BeforeClass
    public static void prepareEclipse(){
       mavenOffline();
       setMavenSettings();
       AbstractWait.sleep(TimePeriod.getCustom(120));
    }
    
    @Before
    public void enableAll(){
    	prepareGit(true);
    	automaticBuild(true);
    }
    
    private static void setMavenSettings() {
    	WorkbenchPreferenceDialog wd = new WorkbenchPreferenceDialog();
    	wd.open();
		MavenUserPreferencePage up = new MavenUserPreferencePage();
		wd.select(up);
		up.setUserSettings(new File(USER_SETTINGS).getAbsolutePath());
		wd.ok();
		
	}

	public void prepareGit(boolean enable){
    	 new ShellMenu("Window","Preferences").select();
         new DefaultShell("Preferences");
         new DefaultTreeItem("Team","Git","Projects").select();
         new CheckBox(0).toggle(enable);
         new CheckBox(1).toggle(enable);
         new CheckBox(2).toggle(enable);
         new OkButton().click();
         new WaitWhile(new ShellWithTextIsActive("Preferences"));
         new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
    }
    
    private static void mavenOffline(){
    	new ShellMenu("Window","Preferences").select();
    	new DefaultShell("Preferences");
        new DefaultTreeItem("Maven").select();
        new CheckBox("Offline").toggle(true);
        new OkButton().click();
        new WaitWhile(new ShellWithTextIsActive("Preferences"));
        new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
    }
    
    public void automaticBuild(boolean enable){
    	ShellMenu m = new ShellMenu("Project","Build Automatically");
    	if(enable){
    		if(!m.isSelected()){
    			m.select();
    		}
    	} else {
    		if(m.isSelected()){
    			m.select();
    		}
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
    
    public void importProjectWithMaven(){
    	MavenImportWizard mw = new MavenImportWizard();
    	mw.open();
    	MavenImportWizardFirstPage mp = (MavenImportWizardFirstPage)mw.getWizardPage(0);
    	mp.setRootDirectory("/home/jbossqa/validation/maven/"+projectFolder);
    	new FinishButton().click();
        new WaitUntil(new JobIsRunning(), TimePeriod.ETERNAL);
        new WaitWhile(new JobIsRunning(), TimePeriod.ETERNAL);
    }

}
