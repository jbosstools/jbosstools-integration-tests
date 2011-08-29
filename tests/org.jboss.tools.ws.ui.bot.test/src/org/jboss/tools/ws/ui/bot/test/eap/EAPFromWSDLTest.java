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

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.WsWizardBase.Slider_Level;
import org.jboss.tools.ws.ui.bot.test.wtp.TopDownWSTest;
import org.jboss.tools.ws.ui.bot.test.wtp.WSTestBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@Require(server=
@Server(type = ServerType.EAP), perspective = "Java EE")
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ EAPCompAllTests.class})
public class EAPFromWSDLTest extends WSTestBase {

    private static final Logger L = Logger.getLogger(EAPFromWSDLTest.class.getName());
    private static boolean servicePassed = false;

    public EAPFromWSDLTest() {
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
        L.info("overridden");
    }

    @AfterClass
    public static void x() {
        L.info("x");
        servers.removeAllProjectsFromServer();
    }

    @Override
    protected String getWsProjectName() {
        return "AreaWSProject";
    }

    @Override
    protected String getEarProjectName() {
        return getWsProjectName() + "EAR";
    }

    protected String getWsClientProjectName() {
        return "AreaWSClientProject";
    }

    protected String getClientEarProjectName() {
        return getWsClientProjectName() + "EAR";
    }

    @Override
    protected String getWsPackage() {
        return "org.jboss.ws";
    }

    @Override
    protected String getWsName() {
        return "AreaService";
    }

    @Override
    protected Slider_Level getLevel() {
        return Slider_Level.DEPLOY;
    }

    @Test
    public void testService() {
        topDownWS(TopDownWSTest.class.getResourceAsStream("/resources/jbossws/AreaService.wsdl"), getWsPackage());

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsProjectName());
        IFile f = project.getFile("src/" + getWsPackage().replace(".", "/") + "/AreaServiceImpl.java");
        String content = readFile(f);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.contains("public class AreaServiceImpl implements AreaService {"));
        Assert.assertTrue(content.contains("public float calculateRectArea(Dimensions parameters)"));
        replaceContent(f, "/resources/jbossws/AreaWS.java.ws");

        f = project.getFile("WebContent/WEB-INF/web.xml");
        content = readFile(f);
        Assert.assertNotNull(content);
        Assert.assertTrue(content.contains("<servlet-class>org.jboss.ws.AreaServiceImpl</servlet-class>"));
        Assert.assertTrue(content.contains("<url-pattern>/AreaService</url-pattern>"));
        runProject(getEarProjectName());
        assertServiceDeployed(getWSDLUrl(), 10000);
        servicePassed = true;
    }

    @Test
    public void testClient() {
        Assert.assertTrue("service must exist", servicePassed);
        createClient(getWSDLUrl(), getWsClientProjectName(), Slider_Level.DEVELOP, "org.jboss.wsclient");
        IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(getWsClientProjectName());
        String pkg = "org/jboss/wsclient";
        String cls = "src/" + pkg + "/AreaService.java";
        Assert.assertTrue(p.getFile(cls).exists());
        cls = "src/" + pkg + "/clientsample/ClientSample.java";
        IFile f = p.getFile(cls);
        Assert.assertTrue(f.exists());
        replaceContent(f, "/resources/jbossws/clientsample.java.ws");
        eclipse.runJavaApplication(getWsClientProjectName(), "org.jboss.wsclient.clientsample.ClientSample", null);
        util.waitForNonIgnoredJobs();
        bot.sleep(15000);
        String output = console.getConsoleText();
        L.info(output);
        Assert.assertTrue(output.contains("Server said: 37.5"));
        Assert.assertTrue(output.contains("Server said: 3512.3699"));
    }

    private void replaceContent(IFile f, String content) {
        try {
            f.delete(true, new NullProgressMonitor());
        } catch (CoreException ce) {
            L.log(Level.WARNING, ce.getMessage(), ce);
        }
        InputStream is = null;
        try {
            is = EAPFromWSDLTest.class.getResourceAsStream(content);
            f.create(is, true, new NullProgressMonitor());
        } catch (CoreException ce) {
            L.log(Level.WARNING, ce.getMessage(), ce);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    //ignore
                }
            }
        }
        try {
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IWorkspaceRoot.DEPTH_INFINITE, new NullProgressMonitor());
        } catch (CoreException e) {
            L.log(Level.WARNING, e.getMessage(), e);
        }
        util.waitForNonIgnoredJobs();
        bot.sleep(1000);
    }
}
