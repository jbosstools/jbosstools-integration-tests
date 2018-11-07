/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.cdi.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.matcher.RegexMatcher;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.matcher.WithTextMatcher;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.condition.TableContainsItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;
import org.eclipse.reddeer.swt.impl.table.DefaultTableItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.cdi.reddeer.CDIConstants;

public class NewAnnotationLiteralCreationWizard extends NewMenuWizard{
	
	public static final String NAME="Annotation Literal";
	public static final String SHELL_TEXT = "New Annotation Literal";
	
	public NewAnnotationLiteralCreationWizard(){
		super(SHELL_TEXT, CDIConstants.CDI_GROUP,NAME);
	}
	
	public void setPackage(String packageName){
		new LabeledText("Package:").setText(packageName);
	}
	
	public void setName(String name){
		new LabeledText("Name:").setText(name);
	}
	
	public void setPublic(boolean isPublic){
		new RadioButton("public").toggle(isPublic);
	}

	public void setPrivate(boolean isPrivate){
		new RadioButton("private").toggle(isPrivate);
	}

	public void setProtected(boolean isProtected){
		new RadioButton("protected").toggle(isProtected);
	}
	
	public void setFinal(boolean isFinal){
		new CheckBox("final").toggle(isFinal);
	}
	
	public void setAbstract(boolean isAbstract){
		new CheckBox("abstract").toggle(isAbstract);
	}
	
	public void setStatic(boolean isStatic){
		new CheckBox("static").toggle(isStatic);
	}
	
	public void setGenerateComments(boolean generate){
		new CheckBox("Generate comments").toggle(generate);
	}
	
	public void setQualifier(String qualifier){
		new LabeledText("Qualifier:").setText(qualifier);
	}
	
	public void addQualifier(String qualifier){
		new PushButton("Browse").click();
		new DefaultShell("Select Qualifier Annotation Type");
		new WaitUntil(new TableContainsItem(new DefaultTable(), qualifier, 0));
		new DefaultTableItem(new WithTextMatcher(new RegexMatcher(".*"+qualifier+".*"))).select();
		new PushButton("OK").click();
		new DefaultShell("New Annotation Literal");
	}
	
	public List<String> getQualifiers(){
		new PushButton("Browse").click();
		new DefaultShell("Select Qualifier Annotation Type");
		List<String> qual = new ArrayList<String>();
		for(TableItem i: new DefaultTable().getItems()){
			qual.add(i.getText());
		}
		new PushButton("OK").click();
		new DefaultShell("New Annotation Literal");
		return qual;
	}

}
