package org.jboss.tools.cdi.reddeer.uiutils;

import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardPage;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewAnnotationLiteralCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorBindingCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewQualifierCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewScopeCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;

public class BeansHelper {
	
	public void createClass(String name, String pckg){
		NewJavaClassWizardDialog cd = new NewJavaClassWizardDialog();
		cd.open();
		NewJavaClassWizardPage cp = new NewJavaClassWizardPage();
		cp.setName(name);
		cp.setPackage(pckg);
		cd.finish();
	}
	
	public void createBean(String name, String pckg, boolean isAbstract, boolean alternative,
			boolean isDefault, boolean isFinal, boolean generate,boolean register, boolean isNamed,
			String namedParam, String scope){
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		bw.open();
		bw.setName(name);
		bw.setPackage(pckg);
		bw.setAbstract(isAbstract);
		bw.setAlternative(alternative);
		bw.setFinal(isFinal);
		bw.setGenerateComments(generate);
		bw.setNamed(isNamed);
		if(isNamed == true && namedParam != null){ 
			bw.setBeanName(namedParam);
		}
		if(scope != null){
			bw.setScope(scope);
		}
		bw.finish();
	}
	
	public void createStereotype(String stereotypeName, String pckg, boolean inherited,
			boolean named, boolean alternative, boolean registerInBeans, boolean comments){
		NewStereotypeCreationWizard sw = new NewStereotypeCreationWizard();
		sw.open();
		sw.setName(stereotypeName);
		sw.setPackage(pckg);
		sw.setInherited(inherited);
		sw.setNamed(named);
		sw.setAlternative(alternative);
		sw.setRegisterInBeans(registerInBeans);
		sw.setGenerateComments(comments);
		sw.finish();
	}
	public void createIBinding(String name, String pckg, String target, boolean inherited, boolean comments){
		NewInterceptorBindingCreationWizard bw = new NewInterceptorBindingCreationWizard();
		bw.open();
		bw.setName(name);
		bw.setPackage(pckg);
		if(target != null){
			bw.setTarget(target);
		}
		bw.setInherited(inherited);
		bw.setGenerateComments(comments);
		bw.finish();
	}
	
	public void createInterceptor(String name, String pckg, String binding, boolean comments, 
			boolean registerInBeans){
		NewInterceptorCreationWizard iw = new NewInterceptorCreationWizard();
		iw.open();
		iw.setName(name);
		iw.setPackage(pckg);
		if(binding != null){
			iw.addInterceptorBindings(binding);
		}
		iw.setGenerateComments(comments);
		iw.setRegisterInBeans(registerInBeans);
		iw.finish();
	}
	
	public void createDecorator(String name, String pckg, String decoratedInterface, String fieldName,
			boolean isPublic, boolean isAbstract, boolean isFinal, boolean comments, boolean registerInBeans){
		NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
		dw.open();
		dw.setName(name);
		dw.setPackage(pckg);
		dw.addDecoratedTypeInterfaces(decoratedInterface);
		if(fieldName != null){
			dw.setDelegateFieldName("field");
		}
		dw.setPublic(isPublic);
		dw.setAbstract(isAbstract);
		dw.setFinal(isFinal);
		dw.setGenerateComments(comments);
		dw.setRegisterInBeans(registerInBeans);
		dw.finish();
	}
	
	public void createAnnotationLiteral(String name, String pckg, boolean isAbstract,
			boolean isFinal, boolean generate, String qualifier){
		NewAnnotationLiteralCreationWizard alw = new NewAnnotationLiteralCreationWizard();
		alw.open();
		alw.setName(name);
		alw.setPackage(pckg);
		alw.setAbstract(isAbstract);
		alw.setFinal(isFinal);
		alw.setGenerateComments(generate);
		if(qualifier == null){
			alw.addQualifier(alw.getQualifiers().get(0));
		} else {
			alw.setQualifier(qualifier);
		}
		alw.finish();
	}
	
	public void createQualifier(String name, String packageName, boolean inherited, boolean generate){
		NewQualifierCreationWizard qw = new NewQualifierCreationWizard();
		qw.open();
		qw.setName(name);
		qw.setPackage(packageName);
		qw.setInherited(inherited);
		qw.setGenerateComments(generate);
		qw.finish();
	}
	
	public void createScope(String name, String packageName, boolean passivating,
			boolean scope, boolean inherited,boolean generate){
		NewScopeCreationWizard sw = new NewScopeCreationWizard();
		sw.open();
		sw.setName(name);
		sw.setPackage(packageName);
		sw.setPassivating(passivating);
		sw.setNormalScope(scope);
		sw.setInherited(inherited);
		sw.setGenerateComments(generate);
		sw.finish();
	}

}
