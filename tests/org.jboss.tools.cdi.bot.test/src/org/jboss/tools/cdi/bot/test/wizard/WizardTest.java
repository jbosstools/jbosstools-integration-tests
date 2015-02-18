/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.bot.test.wizard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerReqType;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaEEPerspective;
import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewAnnotationLiteralCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeansXMLCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorBindingCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewQualifierCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewScopeCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;
import org.junit.Test;

/**
 * Test checks all CDI components wizardExts
 * 
 * @author Lukas Jungmann
 * @author jjankovi
 */
@JBossServer(state=ServerReqState.PRESENT, type=ServerReqType.AS7_1)
@OpenPerspective(JavaEEPerspective.class)
@CleanWorkspace
public class WizardTest extends CDITestBase {

	@Override
	public void waitForJobs() {
		
		new WaitWhile(new JobIsRunning());
		/**
		 * needed for creating non-dependant components
		 */
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(getProjectName()).select();
	}
		
	@Test
	public void testComponentsWizards() {
		testQualifier();
		testScope();
		testIBinding();
		testStereotype();
		testDecorator();
		testInterceptor();
		testBeansXml();
		testBean();
		testAnnLiteral();
	}
	
	private void testQualifier() {
		NewQualifierCreationWizard qw = new NewQualifierCreationWizard();
		qw.open();
		qw.setName("Q1");
		qw.setPackage(getPackageName());
		qw.setInherited(false);
		qw.setGenerateComments(false);
		qw.finish();
		
		TextEditor e = new TextEditor();
		assertTrue(("Q1.java").equals(e.getTitle()));
		String code = e.getText();
		assertTrue(code.contains("@Qualifier"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, PARAMETER, FIELD })"));
		assertFalse(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));
		
		qw.open();
		qw.setName("Q2");
		qw.setPackage(getPackageName());
		qw.setInherited(true);
		qw.setGenerateComments(true);
		qw.finish();
		
		e = new TextEditor();
		assertTrue(("Q2.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@Qualifier"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, PARAMETER, FIELD })"));
		assertTrue(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));
	}
	
	private void testScope() {
		NewScopeCreationWizard sw = new NewScopeCreationWizard();
		sw.open();
		sw.setName("Scope1");
		sw.setPackage(getPackageName());
		sw.setInherited(true);
		sw.setGenerateComments(false);
		sw.setNormalScope(true);
		sw.setPassivating(false);
		sw.finish();
		
		
		
		TextEditor e = new TextEditor();
		assertTrue(("Scope1.java").equals(e.getTitle()));
		String code = e.getText();
		assertTrue(code.contains("@NormalScope"));
		assertFalse(code.contains("@Scope"));
		assertFalse(code.contains("passivating"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertTrue(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		sw.open();
		sw.setName("Scope2");
		sw.setPackage(getPackageName());
		sw.setInherited(false);
		sw.setGenerateComments(true);
		sw.setNormalScope(true);
		sw.setPassivating(true);
		sw.finish();
		
		e = new TextEditor();
		assertTrue(("Scope2.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@NormalScope(passivating = true)"));
		assertFalse(code.contains("@Scope"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));

		
		sw.open();
		sw.setName("Scope3");
		sw.setPackage(getPackageName());
		sw.setInherited(false);
		sw.setGenerateComments(true);
		sw.setNormalScope(false);
		sw.setPassivating(false);
		sw.finish();
		
		e = new TextEditor();
		assertTrue(("Scope3.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@Scope"));
		assertFalse(code.contains("@NormalScope"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));
	}
	
	private void testIBinding() {
		NewInterceptorBindingCreationWizard bw = new NewInterceptorBindingCreationWizard();
		bw.open();
		bw.setName("B1");
		bw.setPackage(getPackageName());
		bw.setInherited(true);
		bw.setGenerateComments(false);
		//assertEquals(2, bw.getTargets().size());
		bw.finish();
		
		TextEditor e = new TextEditor();
		assertTrue(("B1.java").equals(e.getTitle()));
		String code = e.getText();
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD })"));
		assertTrue(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		
		bw.open();
		bw.setName("B2");
		bw.setPackage(getPackageName());
		bw.setTarget("TYPE");
		bw.setInherited(false);
		bw.setGenerateComments(true);
		bw.finish();
		
		e = new TextEditor();
		assertTrue(("B2.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));

		
		bw.open();
		bw.setName("B3");
		bw.setPackage(getPackageName());
		bw.setTarget("TYPE");
		bw.setInherited(false);
		bw.setGenerateComments(true);
		bw.finish();
		
		e = new TextEditor();
		assertTrue(("B3.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertFalse(code.contains("@Inherited"));
		assertTrue(code.startsWith("/**"));

		
		bw.open();
		bw.setName("B4");
		bw.setPackage(getPackageName());
		bw.setTarget("TYPE");
		bw.setInherited(true);
		bw.setGenerateComments(false);
		bw.addInterceptorBindings(getPackageName()+".B2");
		bw.finish();
		
		e = new TextEditor();
		assertTrue(("B4.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@InterceptorBinding"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertTrue(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));
		assertTrue(code.contains("@B2"));
	}
	
	private void testStereotype() {
		NewStereotypeCreationWizard sw = new NewStereotypeCreationWizard();
		sw.open();
		sw.setName("S1");
		sw.setPackage(getPackageName());
		sw.setInherited(false);
		sw.setNamed(false);
		sw.setAlternative(false);
		sw.setRegisterInBeans(false);
		sw.setGenerateComments(false);
		//assertEquals(9, sw.getScopes().size());
		//assertEquals(5, sw.getTargets().size());
		sw.finish();
		
		TextEditor e = new TextEditor();
		assertTrue(("S1.java").equals(e.getTitle()));
		String code = e.getText();
		assertTrue(code.contains("@Stereotype"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE, METHOD, FIELD })"));
		assertFalse(code.contains("@Named"));
		assertFalse(code.contains("@Alternative"));
		assertFalse(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));

		
		sw.open();
		sw.setName("S2");
		sw.setPackage(getPackageName());
		sw.setScope("@Scope3");
		sw.setTarget("FIELD");
		sw.setInherited(true);
		sw.setNamed(true);
		sw.setAlternative(true);
		sw.setRegisterInBeans(false);
		sw.setGenerateComments(true);
		sw.finish();
		
		e = new TextEditor();
		assertTrue(("S2.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@Stereotype"));
		assertTrue(code.contains("@Scope3"));
		assertTrue(code.contains("@Named"));
		assertTrue(code.contains("@Alternative"));
		assertTrue(code.contains("@Inherited"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ FIELD })"));
		assertTrue(code.startsWith("/**"));

		sw.open();
		sw.setName("S3");
		sw.setPackage(getPackageName());
		sw.setInherited(false);
		sw.setNamed(false);
		sw.setAlternative(true);
		sw.setRegisterInBeans(false);
		sw.setGenerateComments(false);
		sw.addInterceptorBindings(getPackageName() + ".B1");
		sw.addStereoptypes(getPackageName() + ".S1");
		sw.finish();

		e = new TextEditor();
		assertTrue(("S3.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("@Stereotype"));
		assertFalse(code.contains("@Scope3"));
		assertFalse(code.contains("@Named"));
		assertTrue(code.contains("@Alternative"));
		assertTrue(code.contains("@B1"));
		assertTrue(code.contains("@S1"));
		assertTrue(code.contains("@Retention(RUNTIME)"));
		assertTrue(code.contains("@Target({ TYPE })"));
		assertFalse(code.contains("@Inherited"));
		assertFalse(code.startsWith("/**"));
	}
	
	private void testDecorator() {
		NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
		dw.open();
		dw.setName("");
		dw.setPackage(getPackageName());
		dw.addDecoratedTypeInterfaces("java.lang.Comparable");
		dw.setPublic(true);
		dw.setAbstract(true);
		dw.setFinal(false);
		dw.setGenerateComments(false);
		dw.finish();
		
		String code = new TextEditor("ComparableDecorator.java").getText();
		assertTrue(code.contains("@Decorator"));
		assertTrue(code.contains("abstract class"));
		assertTrue(code.contains("@Delegate"));
		assertTrue(code.contains("@Inject"));
		assertTrue(code.contains("@Any"));
		assertTrue(code.contains("private Comparable<T> comparable;"));
		assertFalse(code.contains("final"));
		assertFalse(code.startsWith("/**"));
		
		
		dw.open();
		dw.setName("");
		dw.setPackage(getPackageName());
		dw.addDecoratedTypeInterfaces("java.util.Map");
		dw.setDelegateFieldName("field");
		dw.setPublic(false);
		dw.setAbstract(false);
		dw.setFinal(true);
		dw.setGenerateComments(true);
		dw.finish();
		
		code = new TextEditor("MapDecorator.java").getText();
		assertTrue(code.contains("@Decorator"));
		assertFalse(code.contains("abstract"));
		assertTrue(code.contains("@Delegate"));
		assertTrue(code.contains("@Inject"));
		assertTrue(code.contains("@Any"));
		assertTrue(code.contains("private Map<K, V> field;"));
		assertTrue(code.contains("final class"));
		assertTrue(code.startsWith("/**"));
	}
	
	private void testInterceptor() {
		NewInterceptorCreationWizard iw = new NewInterceptorCreationWizard();
		iw.open();
		iw.setName("I1");
		iw.setPackage(getPackageName());
		iw.addInterceptorBindings("B2");
		iw.setGenerateComments(false);
		iw.finish();

		String code = new TextEditor("I1.java").getText();
		assertTrue(code.contains("@B2"));
		assertTrue(code.contains("@Interceptor"));
		assertTrue(code.contains("@AroundInvoke"));
		assertTrue(code.contains("public Object aroundInvoke(InvocationContext ic) throws Exception {"));
		assertFalse(code.contains("final"));
		assertFalse(code.startsWith("/**"));
		
		
		iw.open();
		iw.setName("I2");
		iw.setPackage(getPackageName());
		iw.addInterceptorBindings("B4");
		iw.setGenerateComments(true);
		iw.setSuperclass("java.util.Date");
		iw.setAroundInvokeMethodName("sample");
		iw.finish();
		
		
		code = new TextEditor("I2.java").getText();
		assertTrue(code.contains("@B4"));
		assertTrue(code.contains("@Interceptor"));
		assertTrue(code.contains("@AroundInvoke"));
		assertTrue(code.contains("public Object sample(InvocationContext ic) throws Exception {"));
		assertFalse(code.contains("final"));
		assertTrue(code.startsWith("/**"));
		assertTrue(code.contains("extends Date"));
	}
	
	private void testBeansXml() {
		NewBeansXMLCreationWizard xmlw = new NewBeansXMLCreationWizard();
		xmlw.open();
		xmlw.setSourceFolder(getProjectName(),"WebContent","WEB-INF");
		assertFalse(new PushButton("Finish").isEnabled());

		List<String> folder = new ArrayList<String>();
		folder.add(getProjectName());
		folder.add("src");
		folder.addAll(Arrays.asList(getPackageName().split(".")));
		
		xmlw.setSourceFolder(folder.toArray(new String[folder.size()]));
		assertTrue(new PushButton("Finish").isEnabled());
		new CancelButton().click();
		
		xmlw.open();
		assertFalse(new PushButton("Finish").isEnabled());
		new CancelButton().click();
	}
	
	private void testBean() {
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		bw.open();
		bw.setName("Bean1");
		bw.setPackage(getPackageName());
		bw.setPublic(true);
		bw.setAbstract(true);
		bw.setFinal(false);
		bw.setGenerateComments(false);
		bw.setAlternative(false);
		bw.setRegisterInBeans(false);
		bw.finish();

		TextEditor e = new TextEditor();
		assertTrue(("Bean1.java").equals(e.getTitle()));
		String code = e.getText();
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("public abstract class Bean1 {"));
		assertFalse(code.contains("@Named"));
		assertFalse(code.contains("final"));
		assertFalse(code.startsWith("/**"));
		
		bw.open();
		bw.setName("Bean2");
		bw.setPackage(getPackageName());
		bw.setPublic(false);
		bw.setAbstract(false);
		bw.setFinal(true);
		bw.setGenerateComments(true);
		bw.setAlternative(false);
		bw.setRegisterInBeans(false);
		bw.setNamed(true);
		bw.setBeanName("");
		bw.setScope("@Dependent");
		bw.finish();
		
		
		e = new TextEditor();
		assertTrue(("Bean2.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("@Named"));
		assertFalse(code.contains("@Named("));
		assertTrue(code.contains("@Dependent"));
		assertTrue(code.contains("final class Bean2 {"));
		assertTrue(code.startsWith("/**"));
		
		bw.open();
		bw.setName("Bean3");
		bw.setPackage(getPackageName());
		bw.setPublic(true);
		bw.setAbstract(false);
		bw.setFinal(false);
		bw.setGenerateComments(true);
		bw.setAlternative(false);
		bw.setRegisterInBeans(false);
		bw.setNamed(true);
		bw.setBeanName("TestedBean");
		bw.setScope("@Scope2");
		bw.addQualifier("Q1");
		bw.finish();

		e = new TextEditor();
		assertTrue(("Bean3.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("@Named(\"TestedBean\")"));
		assertTrue(code.contains("@Scope2"));
		assertTrue(code.contains("@Q1"));
		assertTrue(code.contains("public class Bean3 {"));
		assertFalse(code.contains("final"));
		assertTrue(code.startsWith("/**"));
	}
	
	private void testAnnLiteral() {
		NewAnnotationLiteralCreationWizard lw = new NewAnnotationLiteralCreationWizard();
		lw.open();
		lw.setName("AnnL1");
		lw.setPackage(getPackageName());
		lw.setPublic(true);
		lw.setAbstract(false);
		lw.setFinal(true);
		lw.setGenerateComments(false);
		lw.addQualifier(getPackageName() + ".Q1 - "+getProjectName());
		lw.finish();
		
		
		TextEditor e = new TextEditor();
		assertTrue(("AnnL1.java").equals(e.getTitle()));
		String code = e.getText();
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("public final class AnnL1 extends AnnotationLiteral<Q1> implements Q1"));
		assertTrue(code.contains("public static final Q1 INSTANCE = new AnnL1();"));
		assertFalse(code.contains("abstract"));
		assertFalse(code.startsWith("/**"));
		
		lw.open();
		lw.setName("AnnL2");
		lw.setPackage(getPackageName());
		lw.setPublic(false);
		lw.setAbstract(true);
		lw.setFinal(false);
		lw.setGenerateComments(true);
		lw.addQualifier(getPackageName() + ".Q2 - "+getProjectName());
		lw.finish();
		
		e = new TextEditor();
		assertTrue(("AnnL2.java").equals(e.getTitle()));
		code = e.getText();
		assertTrue(code.contains("package cdi;"));
		assertTrue(code.contains("abstract class AnnL2 extends AnnotationLiteral<Q2> implements Q2 {"));
		assertTrue(code.contains("public static final Q2 INSTANCE = new AnnL2();"));
		assertFalse(code.substring(code.indexOf("final") + 5).contains("final"));
		assertTrue(code.contains("abstract"));
		assertTrue(code.startsWith("/**"));
	}
}
