/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.deltaspike.ui.bot.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.m2e.core.ui.wizard.MavenImportWizard;
import org.eclipse.reddeer.eclipse.ui.dialogs.PropertyDialog;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.wst.common.project.facet.ui.RuntimesPropertyPage;
import org.eclipse.reddeer.junit.annotation.RequirementRestriction;
import org.eclipse.reddeer.junit.requirement.matcher.RequirementMatcher;
import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.eclipse.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenu;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.handler.EditorHandler;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.ide.eclipse.as.reddeer.server.family.ServerMatcher;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.tools.cdi.reddeer.cdi.ui.UpdateMavenProjectDialog;
import org.jboss.tools.cdi.reddeer.cdi.ui.preferences.CDISettingsPreferencePage;
import org.jboss.tools.cdi.reddeer.condition.SpecificProblemExists;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage;
import org.jboss.tools.common.reddeer.preferences.SourceLookupPreferencePage.SourceAttachmentEnum;
import org.junit.BeforeClass;

@CleanWorkspace
@OpenPerspective(JavaEEPerspective.class)
@JBossServer(state = ServerRequirementState.PRESENT, cleanup = false)
public class DeltaspikeTestBase {

	private static final Logger log = Logger.getLogger(DeltaspikeTestBase.class);

	protected static final ProblemsView problemsView = new ProblemsView();

	@RequirementRestriction
	public static RequirementMatcher getRestrictionMatcher() {
	  return new RequirementMatcher(JBossServer.class, "family", ServerMatcher.WildFly());
	}
	
	@BeforeClass
	public static void disableSourceLookup() {

		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		SourceLookupPreferencePage sp = new SourceLookupPreferencePage(preferenceDialog);
		preferenceDialog.select(sp);
		sp.setSourceAttachment(SourceAttachmentEnum.NEVER);;
		preferenceDialog.ok();
	}

	/**
	 * Import project, configure project to use given target platform and check
	 * if CDI support is enabled. If isn't, then enable CDI support and
	 * revalidate project.
	 * 
	 * @param projectName
	 *            Project to import
	 * @param sr
	 *            The target platform which should be used
	 */
	protected static void importDeltaspikeProject(String projectName, ServerRequirement sr) {

		try {
			importMavenProject(projectName);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unable to import " + projectName);
		}

		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		Project project = pe.getProject(projectName);

		PropertyDialog dialog = project.openProperties();
		RuntimesPropertyPage runtimePage = new RuntimesPropertyPage(dialog);
		CDISettingsPreferencePage cdiPage = new CDISettingsPreferencePage(dialog);
		String targetRuntime = sr.getRuntimeName();

		dialog.open();
		dialog.select(runtimePage);
		runtimePage.selectRuntime(targetRuntime);

		dialog.select(cdiPage);

		if (!cdiPage.isCDISupport()) {
			log.warn("CDI support is disabled! CDI should be enabled automatically after project import.");
			log.warn("Enabling CDI support for '%s' project.", projectName);
			cdiPage.toggleCDISupport(true);
		}

		dialog.ok();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		pe.getProject(projectName).select();
		new ContextMenu().getItem("Maven", "Update Project...").select();
		UpdateMavenProjectDialog updateProject = new UpdateMavenProjectDialog();
		updateProject.clean(true);
		updateProject.ok();
	}

	public static void importMavenProject(String projectName) throws IOException {

		try {
			final Path sourceFolder = new File("target/classes/prj/" + projectName).toPath();
			File dir = new File("target/copies/" + projectName);
			if (dir.exists()) {
				deleteDir(dir);
			}
			final Path destFolder = dir.toPath();
			Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
						throws IOException {
					Files.createDirectories(destFolder.resolve(sourceFolder.relativize(dir)));
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
					Files.copy(file, destFolder.resolve(sourceFolder.relativize(file)));
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			fail("Unable to find pom " + projectName);
		}

		String pomPath = "target/copies/" + projectName;
		MavenImportWizard.importProject((new File(pomPath)).getCanonicalPath());
	}

	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					fail("Unable to delete " + dir);
				}
			}
		}

		return dir.delete();
	}

	protected void insertIntoFile(String projectName, String packageName, String bean, int line, int column,
			String insertedText) {
		openClass(projectName, packageName, bean);
		TextEditor e = new TextEditor(bean);
		e.insertText(line, column, insertedText);
		// SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		// editor.insertText(line, column, insertedText);
		e.save();
	}

	protected void replaceInEditor(String projectName, String packageName, String bean, String target,
			String replacement, boolean save) {
		openClass(projectName, packageName, bean);
		replaceInEditor(target, replacement, save);
	}

	protected void replaceInEditor(String target, String replacement, boolean save) {
		TextEditor e = new TextEditor();
		// eclipseEditor.selectRange(0, 0, eclipseEditor.getText().length());
		e.setText(e.getText().replace(target + (replacement.equals("") ? System.getProperty("line.separator") : ""),
				replacement));
		if (save)
			e.save();
	}

	protected void annotateBean(String projectName, String packageName, String bean, int line, int column,
			String annotation) {

		insertIntoFile(projectName, packageName, bean, line, column, annotation);
		new WaitUntil(new SpecificProblemExists(new RegexMatcher(".*cannot be resolved.*")), TimePeriod.DEFAULT);

		TextEditor e = new TextEditor();
		new ShellMenuItem(new WithTextMatcher("Source"), new RegexMatcher("Organize Imports.*")).select();
		
		e.save();
	}

	protected void openClass(String projectName, String packageName, String classFullName) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(projectName).getProjectItem("Java Resources", "src", packageName, classFullName).open();
	}

	protected static void cleanProjects() {
		new ShellMenuItem(new WithTextMatcher("Project"), new RegexMatcher("Clean.*")).select();
		new WaitUntil(new ShellIsAvailable("Clean"));
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Clean"));
		new WaitWhile(new JobIsRunning());
	}

	protected void deleteAllProjects() {
		EditorHandler.getInstance().closeAll(false);
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		for (Project p : pe.getProjects()) {
			try {
				org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
			} catch (Exception ex) {
				AbstractWait.sleep(TimePeriod.DEFAULT);
				if (!p.getTreeItem().isDisposed()) {
					org.eclipse.reddeer.direct.project.Project.delete(p.getName(), true, true);
				}
			}
		}
	}
}
