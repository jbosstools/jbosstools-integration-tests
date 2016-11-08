/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jsf.ui.test.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.jboss.reddeer.core.lookup.WidgetLookup;
import org.jboss.reddeer.core.matcher.WithStyleMatcher;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectFirstPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectSecondPage;
import org.jboss.tools.jsf.reddeer.ui.JSFNewProjectWizard;

public class JSFTestUtils {

    public static void checkProblemsView() {
	// problems view should be empty
	ProblemsView problemsView = new ProblemsView();
	problemsView.open();
	List<Problem> problems = problemsView.getProblems(ProblemType.ERROR);
	assertEquals(0, problems.size());
    }

    public static void checkErrorLog() {
	// error log should be empty
	LogView logView = new LogView();
	logView.open();
	List<LogMessage> errorMessages = logView.getErrorMessages();
	if (errorMessages.size() > 0) {
	    errorMessages = errorMessages.stream()
		    .filter(JSFTestUtils::filterIgnoredLogMessages)
		    .collect(Collectors.toList());
	}
	assertEquals("Error log contains errors: \n" + errorsToString(logView.getErrorMessages()), 0,
		errorMessages.size());
	logView.deleteLog();
    }

    public static void createJSFProject(String projectName, String jsfEnvironment, String template) {
	createJSFProject(projectName, jsfEnvironment, template, false);
    }

    public static void createJSFProject(String projectName, String jsfEnvironment, String template,
	    boolean autodeploy) {
	JSFNewProjectWizard jsfNewProjectWizard = new JSFNewProjectWizard();
	jsfNewProjectWizard.open();

	// first page
	JSFNewProjectFirstPage jsfNewProjectFirstPage = new JSFNewProjectFirstPage();
	jsfNewProjectFirstPage.setProjectName(projectName);
	jsfNewProjectFirstPage.setJSFEnvironment(jsfEnvironment);
	jsfNewProjectFirstPage.setProjectTemplate(template);

	// second page
	jsfNewProjectWizard.next();
	JSFNewProjectSecondPage jsfNewProjectSecondPage = new JSFNewProjectSecondPage();
	// toggle automatic deployment
	getCheckboxes().forEach(checkBox -> checkBox.toggle(autodeploy));

	jsfNewProjectWizard.finish();
    }

    private static String errorsToString(List<LogMessage> errorMessages) {
	StringBuilder sb = new StringBuilder();
	for (LogMessage logMessage : errorMessages) {
	    sb.append(logMessage.toString());
	    sb.append("\n");
	}
	return sb.toString();
    }

    private static List<CheckBox> getCheckboxes() {
	List<Button> activeWidgets = WidgetLookup.getInstance().activeWidgets(new DefaultShell(), Button.class,
		new WithStyleMatcher(SWT.CHECK));
	return activeWidgets.stream().map(MyCheckBox::new).collect(Collectors.toList());
    }

    private static class MyCheckBox extends CheckBox {
	public MyCheckBox(Button b) {
	    this.swtWidget = b;
	}
    }

    private static boolean filterIgnoredLogMessages(LogMessage logMessage){
	if (logMessage.getMessage().startsWith("Unable to retrieve a list of remote deployment scanners")){
	    return false;
	}else if (logMessage.getPlugin().equals("org.eclipse.equinox.p2.publisher.eclipse")){
	    return false;
	}else{
	    return true;
	}
    }
}
