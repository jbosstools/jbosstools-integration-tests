package org.jboss.tools.batch.ui.bot.test.wizard;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jboss.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertNull;

@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CreateAllArtifactsFromInterfaceTest extends AbstractCreateArtifactTest {

	private static final String PACKAGE = "batch.test.wizard.artifact.interfaces";
	
	private static final String CLASS_PREFIX = "My";
	
	@Parameter
	public BatchArtifacts artifact;
	
	@Parameters(name="{0}")
	public static Collection<BatchArtifacts> data() {
		return Arrays.asList(BatchArtifacts.values());
	}

	@Override
	protected String getPackage() {
		return PACKAGE;
	}
	
	@Override
	protected String getClassName() {
		return CLASS_PREFIX + artifact + "FromInterface";
	}
	
	@Override
	protected BatchArtifacts getArtifact() {
		return artifact;
	}
	
	@Override
	protected void createArtifactHook(NewBatchArtifactWizardPage page) {
		page.selectImplementInterface();
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
}
