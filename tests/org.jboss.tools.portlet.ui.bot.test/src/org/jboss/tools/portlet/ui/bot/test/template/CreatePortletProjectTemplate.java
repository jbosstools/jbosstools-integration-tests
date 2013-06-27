package org.jboss.tools.portlet.ui.bot.test.template;

import static org.hamcrest.core.IsNot.not;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.exist;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.hasFacets;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.isExistingProject;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.DynamicWebProjectDialog;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.DynamicWebProjectWizardPage;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.junit.Test;

/**
 * Template test that creates a new dynamic web project with facets specified by 
 * concrete implementaions. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
@Require(server=@Server(state=ServerState.Present))
public abstract class CreatePortletProjectTemplate extends SWTTaskBasedTestCase {

	protected static final String WEB_INF = "WebContent/WEB-INF/";
	
	protected static final String FACES_CONFIG_XML = WEB_INF + "faces-config.xml";

	protected static final String WEB_XML = WEB_INF + "web.xml";

	protected static final String PORTLET_XML = WEB_INF + "portlet.xml";
	
	protected static final String COMPONENTS_XML = WEB_INF + "components.xml";
	
	protected static final String PAGES_XML = WEB_INF + "pages.xml";
	
	protected static final String JBOSS_WEB_XML = WEB_INF + "jboss-web.xml";

	protected static final String PORTLET_LIBRARIES = "JBoss Portlet Libraries";
	
	protected static final String WEB_APP_LIBRARIES = "Web App Libraries";
	
	public abstract String getProjectName();

	public abstract List<FacetDefinition> getRequiredFacets();

	public abstract void processAdditionalWizardPages(WizardDialog dialog);

	public abstract List<String> getExpectedFiles();
	
	public abstract List<String> getNonExpectedFiles();

	@Test
	public void testcreate(){
		createDynamicWebProject();
		
		doAssertThatInWorkspace(0, isNumberOfErrors());
		doAssertThatInWorkspace(getProjectName(), isExistingProject());
		doAssertThatInWorkspace(getProjectName(), hasFacets(getRequiredFacets()));		
		doAssertThatInWorkspace(getExpectedWorkspaceFiles(), exist());
		if (getNonExpectedFiles().size() > 0){
			doAssertThatInWorkspace(getNonExpectedWorkspaceFiles(), not(exist()));
		}
	}

	protected void createDynamicWebProject() {
		DynamicWebProjectDialog dialog = new DynamicWebProjectDialog();
		dialog.open();
		
		DynamicWebProjectWizardPage page = (DynamicWebProjectWizardPage)dialog.getFirstPage();
		page.setProjectName(getProjectName());
		page.setServerName(SWTTestExt.configuredState.getServer().name);
		page.setWebModuleVersion("2.5");
		page.setFacets(getRequiredFacets());
		
		processAdditionalWizardPages(dialog);
		
		dialog.finish();
		if(new ShellWithTextIsActive("Open Associated Perspective?").test()){
			new PushButton("No").click();
		}
	}

	private List<WorkspaceFile> getExpectedWorkspaceFiles(){
		return wrap(getExpectedFiles());
	}
	
	private List<WorkspaceFile> getNonExpectedWorkspaceFiles(){
		return wrap(getNonExpectedFiles());
	}
	
	private List<WorkspaceFile> wrap(List<String> files){
		List<WorkspaceFile> workspaceFiles = new ArrayList<WorkspaceFile>();

		for (String file : files){
			workspaceFiles.add(new WorkspaceFile(getProjectName(), file));
		}

		return workspaceFiles;
	}
}
