/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.cdi.reddeer.uiutils;

import java.io.InputStream;
import java.util.List;

import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.reddeer.annotation.CDIWizardType;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewAnnotationLiteralCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeanCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewBeansXMLCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewDecoratorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorBindingCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewInterceptorCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewQualifierCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewScopeCreationWizard;
import org.jboss.tools.cdi.reddeer.cdi.ui.NewStereotypeCreationWizard;

public class CDIWizardHelper {
	private EditorResourceHelper editResourceUtil = new EditorResourceHelper();
	
	/**
	 * Method creates Java Annotation with selected name and package	 
	 * @param name
	 * @param packageName
	 */
	/*
	public void createAnnotation(String name, String packageName) {
		wizardExt.annotation(open, util, packageName, name);
		new WaitUntil(new OpenedEditorHasTitleCondition(name + ".java"));
		bot.editorByTitle(name + ".java").show();
	}
	*/
	/**
	 * Method creates CDI component 
	 * 	 
	 * @param component
	 * @param name
	 * @param packageName
	 * @param necessaryParam
	 */
	
	public void createCDIComponent(CDIWizardType component, String name,
			String packageName, String necessaryParam) {			
		createComponent(component, name, packageName, necessaryParam);
		String editorTitle = name + ".java";
		if (name.contains(".xml")) {
			editorTitle = name;
		}
		new DefaultEditor(editorTitle);
	}
	
	/**
	 * Method creates CDI component with content of resource
	 * @param component
	 * @param name
	 * @param packageName
	 * @param necessaryParam
	 * @param resource
	 */
	
	public void createCDIComponentWithContent(CDIWizardType component, String name,
			String packageName, String necessaryParam, InputStream resource) {			
		createCDIComponent(component, name, packageName, necessaryParam);
		new DefaultEditor(name + ".java");
		editResourceUtil.replaceClassContentByResource(name+".java", resource, false);
	}
	
	/**
	 * Method creates CDI component with content of resource
	 * @param component
	 * @param name
	 * @param packageName
	 * @param necessaryParam
	 * @param resource
	 */
	public void createCDIBeanComponentWithContent(String name,
			String packageName, String necessaryParam, InputStream resource) {			
		NewBeanCreationWizard bw = new NewBeanCreationWizard();
		bw.open();
		bw.setName(name);
		bw.setPackage(packageName);
		bw.finish();

		new TextEditor(name+".java");

		editResourceUtil.replaceClassContentByResource(name+".java", resource, false);
	}
	
	
	/**
	 * Method creates larger number("amount") of the same component. 	 
	 * @param component
	 * @param amount
	 * @param baseName
	 * @param packageBaseName
	 * @param necessaryParam
	 * @param differentPackages
	 */
	
	public void createCDIComponents(CDIWizardType component, int amount, String baseName, 
			String packageBaseName, String necessaryParam, boolean differentPackages) {
		
		for (int i = 1; i <= amount; i++) {
			String packageName = (differentPackages) ? packageBaseName + i : packageBaseName;
			createCDIComponent(component, baseName + i, packageName, necessaryParam);
		}
		
	}
	
	/**
	 * Method creates larger number("amount") of the same component with using 
	 * List of class names
	 * @param component
	 * @param packageBaseName
	 * @param classNames
	 * @param necessaryParam
	 * @param differentPackages
	 */
	
	public void createCDIComponents(CDIWizardType component, String packageBaseName, 
			List<String> classNames, String necessaryParam, boolean differentPackages) {
		if (classNames == null) {
			throw new IllegalArgumentException("List with class names should not be " +
					"null");
		}
		if (classNames.size() == 0) {
			throw new IllegalArgumentException("List with class names should not be " +
					"empty");
		}
		for (int i = 0; i < classNames.size(); i++) {
			String packageName = (differentPackages) ? packageBaseName + i : packageBaseName;
			createCDIComponent(component, classNames.get(i), packageName, necessaryParam);
		}
		
	}
	
	/**
	 * Method that actually creates CDI component according to parameter
	 * @param component
	 * @param name
	 * @param packageName
	 * @param necessaryParam
	 */
	
	private void createComponent(CDIWizardType component, String name,
			String packageName, String necessaryParam) {
		switch (component) {
		case STEREOTYPE:
			boolean alternative = false;
			boolean regInBeansXml = false;
			if (necessaryParam != null) {
				if (necessaryParam.equals("alternative+beansxml")) {
					alternative = true;
					regInBeansXml = true;
				} else if (necessaryParam.equals("alternative")) {
					alternative = true;
				}
			}
			
			NewStereotypeCreationWizard nb = new NewStereotypeCreationWizard();
			nb.open();
			nb.setName(name);
			nb.setPackage(packageName);
			nb.setInherited(false);
			nb.setNamed(true);
			nb.setAlternative(alternative);
			nb.setGenerateComments(false);
			nb.setRegisterInBeans(regInBeansXml);
			nb.finish();
			
			break;
		case QUALIFIER:
			
			NewQualifierCreationWizard qb = new NewQualifierCreationWizard();
			qb.open();
			qb.setName(name);
			qb.setPackage(packageName);
			qb.setInherited(false);
			qb.setGenerateComments(false);
			qb.finish();
			break;
		case SCOPE:
			NewScopeCreationWizard sw = new NewScopeCreationWizard();
			sw.open();
			sw.setName(name);
			sw.setPackage(packageName);
			sw.setInherited(false);
			sw.setGenerateComments(false);
			sw.setNormalScope(true);
			sw.setPassivating(false);
			sw.finish();
			break;
		case BEAN:
			alternative = false;
			regInBeansXml = false;
			String scope = null;
			if (necessaryParam != null) {
				if (necessaryParam.equals("alternative+beansxml")) {
					alternative = true;
					regInBeansXml = true;
				} else if (necessaryParam.equals("alternative")) {
					alternative = true;
				} else if(necessaryParam.equals("@ApplicationScoped")){
					scope = necessaryParam;
				}
			}
			NewBeanCreationWizard nb1 = new NewBeanCreationWizard();
			nb1.open();
			nb1.setName(name);
			nb1.setPackage(packageName);
			nb1.setPublic(true);
			nb1.setAbstract(false);
			nb1.setFinal(false);
			nb1.setGenerateComments(false);
			nb1.setAlternative(alternative);
			nb1.setRegisterInBeans(regInBeansXml);
			if(scope != null){
				nb1.setScope(scope);
			}
			nb1.finish();
			break;
		case INTERCEPTOR:
			NewInterceptorCreationWizard iw = new NewInterceptorCreationWizard();
			iw.open();
			iw.setName(name);
			iw.setPackage(packageName);
			iw.setGenerateComments(false);
			iw.finish();
			break;
		case DECORATOR:
			NewDecoratorCreationWizard dw = new NewDecoratorCreationWizard();
			dw.open();
			dw.setName(name);
			dw.setPackage(packageName);
			dw.addDecoratedTypeInterfaces(necessaryParam);
			dw.setPublic(true);
			dw.setAbstract(false);
			dw.setFinal(false);
			dw.setGenerateComments(false);
			dw.finish();
			break;
		case ANNOTATION_LITERAL:
			NewAnnotationLiteralCreationWizard aw = new NewAnnotationLiteralCreationWizard();
			aw.open();
			aw.setName(name);
			aw.setPackage(packageName);
			aw.setPublic(true);
			aw.setAbstract(false);
			aw.setFinal(false);
			aw.setGenerateComments(false);
			aw.addQualifier(aw.getQualifiers().get(0));
			aw.finish();
			break;
		case INTERCEPTOR_BINDING:
			NewInterceptorBindingCreationWizard bw = new NewInterceptorBindingCreationWizard();
			bw.open();
			bw.setName(name);
			bw.setPackage(packageName);
			bw.setInherited(true);
			bw.setGenerateComments(false);
			bw.finish();
			break;
		case BEANS_XML:
			NewBeansXMLCreationWizard xw = new NewBeansXMLCreationWizard();
			xw.open();
			xw.setSourceFolder(packageName.split("."));
			xw.finish();		
			break;
		}					
	}
	
}
