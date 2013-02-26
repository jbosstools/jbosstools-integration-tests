/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.webservice.eap;

import java.util.logging.Level;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ws.ui.bot.test.WSAllBotTests;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.jboss.tools.ws.ui.bot.test.webservice.WebServiceTestBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test operates on creating non-trivial EAP project from Java class
 * @author jlukas
 *
 */
@SuiteClasses({ WSAllBotTests.class, EAPCompAllTests.class })
@Require(perspective="Java EE", 
		server=@Server)
//		(type=ServerType.EAP, 
//		version = "5.1", operator = ">="))
public class EAPFromJavaTest extends WebServiceTestBase {

    private static boolean servicePassed = false;

    @Before
    @Override
    public void setup() {
        if (!projectExists(getWsProjectName())) {
            projectHelper.createProject(getWsProjectName());
        }
        if (!projectExists(getWsClientProjectName())) {
        	projectHelper.createProject(getWsClientProjectName());
        }
    }

    @After
    @Override
    public void cleanup() {
        //do nothing here
        //we don't want to undeploy our app yet
    }

    @AfterClass
    public static void removeAllProjects() {
        servers.removeAllProjectsFromServer();
    }

    @Override
    protected String getWsProjectName() {
        return "TestWSProject";
    }

    @Override
    protected String getEarProjectName() {
        return getWsProjectName() + "EAR";
    }

    protected String getWsClientProjectName() {
        return "TestWSClientProject";
    }

    @Override
    protected String getWsPackage() {
        return "test.ws";
    }

    @Override
    protected String getWsName() {
        return "Echo";
    }

    @Override
    protected Slider_Level getLevel() {
        return Slider_Level.DEPLOY;
    }
    
    @Test
    public void testEAPFromJava() {
    	testService();
    	testClient();
    }

    private void testService() {
        //create a class representing some complex type
        SWTBotEclipseEditor st = projectHelper.createClass(getWsProjectName(), "test", "Person").toTextEditor();
        st.selectRange(0, 0, st.getText().length());
        st.setText(resourceHelper.readStream(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/Person.java.ws")));
        st.saveAndClose();
        //refresh workspace - workaround??? for JBIDE-6731
        try {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException e) {
            LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        bottomUpJbossWebService(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/Echo.java.ws"));
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsProjectName());
        IFile f = project.getFile("WebContent/WEB-INF/web.xml");
        String content = resourceHelper.readFile(f);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.contains("<servlet-class>test.ws.Echo</servlet-class>"));
        Assert.assertTrue(content.contains("<url-pattern>/Echo</url-pattern>"));
        deploymentHelper.runProject(getEarProjectName());
        deploymentHelper.assertServiceDeployed(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 10000);
        servicePassed = true;
    }

    private void testClient() {
        Assert.assertTrue("service must exist", servicePassed);
        clientHelper.createClient(deploymentHelper.getWSDLUrl(getWsProjectName(), getWsName()), 
        			getWsClientProjectName(), getLevel(), "");
        IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsClientProjectName());
        String pkg = "test/ws";
        String cls = "src/" + pkg + "/EchoService.java";
        Assert.assertTrue(p.getFile(cls).exists());
        cls = "src/" + pkg + "/clientsample/ClientSample.java";
        Assert.assertTrue(p.getFile(cls).exists());
        //create JSP
        new NewFileWizardAction().run().selectTemplate("Web", "JSP File").next();
        Wizard w = new Wizard();
        w.bot().textWithLabel("File name:").setText("index");
        w.bot().textWithLabel("Enter or select the parent folder:").setText(getWsClientProjectName() + "/WebContent");
        w.finish();
        util.waitForNonIgnoredJobs();
        /**
         *  Workaround for 4.x branch
         * 
         *	bot.activeShell().bot().button("Skip").click();
         *	bot.sleep(TIME_5S);
         */
        SWTBotEclipseEditor st = bot.editorByTitle("index.jsp").toTextEditor();
        st.selectRange(0, 0, st.getText().length());
        st.setText(resourceHelper.readStream(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/index.jsp.ws")));
        st.saveAndClose();
        bot.sleep(TIME_1S*2);
        deploymentHelper.runProject(getWsClientProjectName());
        servers.cleanServer(configuredState.getServer().name);
        String pageContent = deploymentHelper.getPage("http://localhost:8080/" + getWsClientProjectName() + "/index.jsp", 15000);
        LOGGER.info(pageContent);
        Assert.assertTrue(pageContent.contains("BartSimpson(age: 12)"));
        Assert.assertTrue(pageContent.contains("Homer(age: 44)"));
    }
}
