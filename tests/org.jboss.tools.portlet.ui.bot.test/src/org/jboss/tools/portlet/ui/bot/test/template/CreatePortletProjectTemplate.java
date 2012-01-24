package org.jboss.tools.portlet.ui.bot.test.template;

import static org.hamcrest.Matchers.not;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.DefaultMatchersFactory.isNumberOfErrors;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.exist;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.hasFacets;
import static org.jboss.tools.portlet.ui.bot.matcher.factory.WorkspaceMatchersFactory.isExistingProject;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.facet.FacetsSelectionTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardOpeningAndFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.DynamicWebProjectCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.DynamicWebProjectWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Test;

/**
 * Template test that creates a new dynamic web project with facets specified by 
 * concrete implementaions. 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(state=ServerState.Present, type=ServerType.EPP))
public abstract class CreatePortletProjectTemplate extends SWTTaskBasedTestCase {

	protected static final FacetDefinition JAVA_FACET = new FacetDefinition("Java", null, "1.6");
	
	protected static final FacetDefinition JSF_FACET = new FacetDefinition("JavaServer Faces");
	
	protected static final String JBOSS_FACET_CATEGORY = "JBoss Portlets";

	protected static final FacetDefinition CORE_PORTLET_FACET = new FacetDefinition("JBoss Core Portlet", JBOSS_FACET_CATEGORY);

	protected static final FacetDefinition JSF_PORTLET_FACET = new FacetDefinition("JBoss JSF Portlet", JBOSS_FACET_CATEGORY);
	
	public static final FacetDefinition SEAM_PORTLET_FACET = new FacetDefinition("JBoss Seam Portlet", JBOSS_FACET_CATEGORY);
	
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

	public abstract List<WizardPageFillingTask> getAdditionalWizardPages();

	public abstract List<String> getExpectedFiles();
	
	public abstract List<String> getNonExpectedFiles();

	@Test
	public void testcreate(){
		doPerform(getCreateDynamicWebProjectTask());
		
		doAssertThatInWorkspace(0, isNumberOfErrors());
		doAssertThatInWorkspace(getProjectName(), isExistingProject());
		doAssertThatInWorkspace(getProjectName(), hasFacets(getRequiredFacets()));		
		doAssertThatInWorkspace(getExpectedWorkspaceFiles(), exist());
		if (getNonExpectedFiles().size() > 0){
			doAssertThatInWorkspace(getNonExpectedWorkspaceFiles(), not(exist()));
		}
	}

	protected WizardOpeningAndFillingTask getCreateDynamicWebProjectTask() {
		DynamicWebProjectWizardPageFillingTask task = new DynamicWebProjectWizardPageFillingTask();
		task.setProjectName(getProjectName());
		task.setWebModuleVersion("2.5");
		task.setServerName(SWTTestExt.configuredState.getServer().name);
		task.setSelectFacetsTask(getSelectFacetsTask());
		
		DynamicWebProjectCreationTask wizardTask = new DynamicWebProjectCreationTask();
		wizardTask.addWizardPage(task);
		wizardTask.addAllWizardPages(getAdditionalWizardPages());
		return wizardTask;
	}

	protected FacetsSelectionTask getSelectFacetsTask() {
		FacetsSelectionTask task = new FacetsSelectionTask();
		task.addAllFacets(getRequiredFacets());
		return task;
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
