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
package org.jboss.tools.ws.ui.bot.test.eap;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.NewFileWizardAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.Wizard;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.jboss.tools.ws.ui.bot.test.wtp.WSTestBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Require(server =
@Server(type = ServerType.EAP), perspective = "Java EE")
public class EAPFromJavaTest extends WSTestBase {

    private static final Logger L = Logger.getLogger(EAPFromJavaTest.class.getName());
    private static boolean servicePassed = false;

    public EAPFromJavaTest() {
    }

    @Before
    @Override
    public void setup() {
        if (!projectExists(getWsProjectName())) {
            createProject(getWsProjectName());
        }
        if (!projectExists(getWsClientProjectName())) {
            createProject(getWsClientProjectName());
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

    protected String getClientEarProjectName() {
        return getWsClientProjectName() + "EAR";
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
    public void testService() {
        //create a class representing some complex type
        SWTBotEclipseEditor st = createClass("test", "Person").toTextEditor();
        st.selectRange(0, 0, st.getText().length());
        st.setText(readStream(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/Person.java.ws")));
        st.saveAndClose();
        //refresh workspace - workaround??? for JBIDE-6731
        try {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException e) {
            L.log(Level.WARNING, e.getMessage(), e);
        }
        bot.sleep(500);
        bottomUpJbossWebService(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/Echo.java.ws"));
        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsProjectName());
        IFile f = project.getFile("WebContent/WEB-INF/web.xml");
        String content = readFile(f);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.contains("<servlet-class>test.ws.Echo</servlet-class>"));
        Assert.assertTrue(content.contains("<url-pattern>/Echo</url-pattern>"));
        runProject(getEarProjectName());
        assertServiceDeployed(getWSDLUrl(), 10000);
        servicePassed = true;
    }

    @Test
    public void testClient() {
        Assert.assertTrue("service must exist", servicePassed);
        createClient(getWSDLUrl(), getWsClientProjectName(), getLevel(), "");
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
        bot.sleep(5000);
        bot.activeShell().bot().button("Skip").click();
        bot.sleep(5000);
        SWTBotEclipseEditor st = bot.editorByTitle("index.jsp").toTextEditor();
        st.selectRange(0, 0, st.getText().length());
        st.setText(readStream(EAPFromJavaTest.class.getResourceAsStream("/resources/jbossws/index.jsp.ws")));
        st.saveAndClose();
        runProject(getWsClientProjectName());
        String pageContent = getPage("http://localhost:8080/" + getWsClientProjectName() + "/index.jsp", 15000);
        L.info(pageContent);
        Assert.assertTrue(pageContent.contains("BartSimpson(age: 12)"));
        Assert.assertTrue(pageContent.contains("Homer(age: 44)"));
    }
}
