package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.jboss.reddeer.swt.api.Group;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class ExampleRequirement {
	
	private String type;
	private String name;
	private boolean met;
	private Table table;
	private int requirementIndex;
	
	private static final long IS_MET=274;
	private static final long NOT_MET=148;
	
	public ExampleRequirement(Table table, int requirementIndex){
		this.table=table;
		this.requirementIndex = requirementIndex;
		this.type = table.getItem(requirementIndex).getText();
		this.name = table.getItem(requirementIndex).getText(1);
		this.met = isRequirementMet(table.getItem(requirementIndex).getImage(2));
	}
	
	public String getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isMet(){
		return met;
	}
	
	public DownloadRuntimeDialog downloadAndInstall(){
		table.select(requirementIndex);
		Group reqGroup = new DefaultGroup("Requirements");
		new PushButton(reqGroup, "Download and Install...").click();
		new DefaultShell("Download Runtimes");
		return new DownloadRuntimeDialog();
	}
	
	public JBossRuntimeDetectionPreferencePage install(){
		table.select(requirementIndex);
		Group reqGroup =  new DefaultGroup("Requirements");
		new PushButton(reqGroup, "Install...").click();
		new DefaultShell("Preferences");
		return new JBossRuntimeDetectionPreferencePage();
	}
	
	private boolean isRequirementMet(Image i){
		ImageLoader saver = new ImageLoader();
		saver.data = new ImageData[] { i.getImageData() };
		saver.save("output.png", SWT.IMAGE_PNG);
		if(new File("output.png").length() == IS_MET){
			return true;
		} else if(new File("output.png").length() == NOT_MET){
			return false;
		}
		fail("unable to match picture in requirements. Unable to decide if requirement is met nor not");
		return false;
	}

}
