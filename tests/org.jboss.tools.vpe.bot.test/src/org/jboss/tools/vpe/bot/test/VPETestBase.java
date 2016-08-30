/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.bot.test;

import static org.junit.Assert.*;

import java.util.List;

import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectFirstPage;
import org.jboss.reddeer.eclipse.jst.servlet.ui.WebProjectWizard;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.wst.html.ui.wizard.NewHTMLFileWizardPage;
import org.jboss.reddeer.eclipse.wst.html.ui.wizard.NewHTMLTemplatesWizardPage;
import org.jboss.reddeer.eclipse.wst.html.ui.wizard.NewHTMLWizard;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.AfterClass;
import org.junit.BeforeClass;

@CleanWorkspace
public class VPETestBase {
	
	protected static String PROJECT_NAME = "WebProject";
	
	private static String noLogEntry = "No log entry found within maximum log size .*";
	
	private static List<ErrorInLog> errors;
	
	@BeforeClass
	public static void prepareWorkspaceBase(){
		errors = null;
		openErrorLog().deleteLog();
	}
	
	@AfterClass
	public static void checkErrorLog(){
		List<LogMessage> msgs = openErrorLog().getErrorMessages();
		
		if(errors != null){
			assertEquals(errors.size(),  msgs.size());
			for(LogMessage lm: msgs){
				for(ErrorInLog er: errors){
					if(lm.getMessage().equals(er.getMessage()) && lm.getPlugin().equals(er.getPlugin())){
						break;
					}
				}
				fail("Unexpected error "+lm);
			}
		} else {
			if(msgs.size() == 1){
				if(!msgs.get(0).getMessage().matches(noLogEntry)){
					fail("There's error in error log "+msgs.get(0));
				}
			} else {
				assertEquals(0, openErrorLog().getErrorMessages().size());
			}
		}
	}
	
	protected static void expectedErrors(List<ErrorInLog> errors){
		VPETestBase.errors = errors;
	}
	
	private static LogView openErrorLog(){
		LogView lw = new LogView();
		lw.open();
		return lw;
	}
	
	public static void createWebProject() {
		WebProjectWizard ww = new WebProjectWizard();
		ww.open();
		WebProjectFirstPage fp  = new WebProjectFirstPage();
		fp.setProjectName(PROJECT_NAME);
		//fp.setTargetRuntime(sr.getRuntimeNameLabelText(sr.getConfig()));
		ww.finish();
		new WaitUntil(new JobIsRunning(),TimePeriod.NORMAL, false);
		new WaitWhile(new JobIsRunning(),TimePeriod.LONG);
	}
	
	public void checkNoErrors(){
		LogView lw = new LogView();
		lw.open();
		List<LogMessage> lms = lw.getErrorMessages();
		if(lms.size() == 1){
			assertTrue(lms.get(0).getMessage().contains("No log entry found within maximum log size")); //this is ok
		} else {
			assertTrue(lw.getErrorMessages().isEmpty());
		}
	}
	
	public static String createHTMLPageWithJS(){
		String pageName = createHTMLPage(null);
		addJQueryToPage(pageName);
		return pageName;
	}
	
	public static String createHTMLPage(String template){
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		NewHTMLWizard nw = new NewHTMLWizard();
		nw.open();
		NewHTMLFileWizardPage fp = new NewHTMLFileWizardPage();
		String pageName = fp.getFileName();
		nw.next();
		if(template != null){
			NewHTMLTemplatesWizardPage tp = new NewHTMLTemplatesWizardPage();
			tp.setTemplate(template);
		}
		nw.finish();
		return pageName;
	}
	
	private static void addJQueryToPage(String pageName){
		TextEditor te = new TextEditor(pageName);
		
		te.insertText(3, 0, "<script src=\"http://code.jquery.com/jquery-1.11.2.min.js\"></script>");
		
		te.save();
	}
	
	public static boolean isGTK2(){
		String isGTK3 = System.getProperty("SWT_GTK3");
		Boolean isLinux = RunningPlatform.isLinux();
		
		return isLinux && "0".equals(isGTK3);
	}

}
