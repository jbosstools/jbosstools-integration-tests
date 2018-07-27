/*******************************************************************************
 * Copyright (c) 2016-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.ui.bot.test.wizard;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.reddeer.junit.internal.runner.ParameterizedRequirementsRunnerFactory;
import org.jboss.tools.batch.reddeer.wizard.BatchArtifacts;
import org.jboss.tools.batch.reddeer.wizard.NewBatchArtifactWizardPage;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.UseParametersRunnerFactory;

@UseParametersRunnerFactory(ParameterizedRequirementsRunnerFactory.class)
public class CreateAllArtifactsFromAbstractClassTest extends AbstractCreateArtifactTest {

	@Parameter
	public BatchArtifacts artifact;
	
	@Parameters(name="{0}")
	public static Collection<BatchArtifacts> data() {
		List<BatchArtifacts> artifacts = new ArrayList<>();
		for (BatchArtifacts artifact : BatchArtifacts.values()){
			if (artifact.hasAbstractClass()){
				artifacts.add(artifact);
			}
		}
		
		return artifacts;
	}

	@Override
	protected String getPackage() {
		return super.getPackage() + ".classes";
	}
	
	@Override
	protected String getClassName() {
		return artifact + "fromAbstractClass";
	}
	
	@Override
	protected BatchArtifacts getArtifact() {
		return artifact;
	}
	
	@Override
	protected void createArtifactHook(NewBatchArtifactWizardPage page) {
		page.selectExtendAbstractClass();
	}
	
	@Test
	public void test(){
		createArtifact();
		
		assertEditorIsActivated();
		assertNoProblems();
		assertClassName();
		assertSuperClass();
		assertInterfaces();
	}
	
	private void assertSuperClass() {
		assertNotNull(getEditor().getSuperClass());
	}
	
	private void assertInterfaces() {
		assertThat(getEditor().getInterfaces().size(), is(0));
	}
}
