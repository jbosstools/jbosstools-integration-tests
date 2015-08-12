package org.jboss.tools.batch.ui.bot.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.batch.reddeer.editor.JavaClassEditor;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardDialog;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.After;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CreateBatchArtifactTest extends AbstractBatchTest {

	private static final String PACKAGE = "batch.test";
	
	private static final String CLASS_PREFIX = "My";
	
	@Parameter
	public BatchArtifacts artifact;
	
	@Parameters(name="{0}")
	public static Collection<BatchArtifacts> data() {
		return Arrays.asList(BatchArtifacts.values());
	}
	
	@After
	public void tearDown(){
		new DefaultEditor(CLASS_PREFIX + artifact+ ".java").close();
	}
	
	@Test
	public void test(){
		createArtifact();
		
		assertEditorIsActivated();
		assertNoProblems();
		assertClassName();
		assertAnnotation();
		assertSuperClass();
		assertInterfaces();
	}

	private void createArtifact() {
		NewBatchArtifactWizardDialog dialog = new NewBatchArtifactWizardDialog();
		dialog.open();
		
		NewBatchArtifactWizardPage page = new NewBatchArtifactWizardPage();
		page.setPackage(PACKAGE);
		page.setName(CLASS_PREFIX + artifact);
		page.setArtifact(artifact);
		page.selectImplementInterface();
		dialog.finish();
	}
	
	private void assertEditorIsActivated() {
		assertTrue(getEditor().isActive());
	}
	
	private void assertClassName() {
		assertThat(getEditor().getClassName(), is(CLASS_PREFIX + artifact));
	}
	
	private void assertAnnotation() {
		List<String> annotations = getEditor().getClassAnnotations();
		
		assertThat(annotations.size(), is(1));
		assertThat(annotations, hasItem("Named"));		
	}
	
	private void assertSuperClass() {
		assertNull(getEditor().getSuperClass());
	}
	
	private void assertInterfaces() {
		assertThat(getEditor().getInterfaces().size(), is(1));
	}
	
	private JavaClassEditor getEditor(){
		return new JavaClassEditor(CLASS_PREFIX + artifact + ".java");
	}
}
