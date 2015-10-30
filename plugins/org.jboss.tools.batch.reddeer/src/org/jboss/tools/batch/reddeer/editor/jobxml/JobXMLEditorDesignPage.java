package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 * Represents the design tab of job xml editor. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditorDesignPage {

	public void selectJob(){
		new DefaultTreeItem("Job").select();
	}
	
	public String getJobID(){
		return new DefaultText(new DefaultSection("Job"), 0).getText();
	}
}
