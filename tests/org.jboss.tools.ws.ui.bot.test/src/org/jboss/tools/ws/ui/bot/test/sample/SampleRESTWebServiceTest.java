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
package org.jboss.tools.ws.ui.bot.test.sample;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ws.ui.bot.test.uiutils.actions.TreeItemAction;
import org.jboss.tools.ws.ui.bot.test.uiutils.wizards.SampleWSWizard.Type;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class SampleRESTWebServiceTest extends SampleWSBase {

	private final String REST_WS_NODE = "JAX-RS REST Web Services";
	private final String REST_SUPPORT = "Add JAX-RS 1.1 support...";
	private final String CONFIGURE_CONTEXT = "Configure";
	
    @Override
    protected String getWsProjectName() {
        return "SampleRESTWS";
    }
    
    @Test
    public void testSampleRestWS() {
    	if ("JBOSS_AS".equals(configuredState.getServer().type)) {
            fail("This test requires RESTEasy jars in the server");
        }
        IFile dd = getDD(getWsProjectName());
        if (!dd.exists()) {
            createDD(getWsProjectName());
        }
        assertTrue(dd.exists());
        createSampleRESTWS(getWsProjectName(), "RESTSample", "rest.sample", "Sample", "RESTApp");        
        checkRESTService(getWsProjectName(), "RESTSample", "rest.sample", "Sample", "Hello World!", "RESTApp");
    }  
    @Ignore //not implemented yet
    @Test
    public void testSimpleRestWS() {
    	
    } 

    private int numberOfService(ArrayList<String> services, String serviceType) {
    	int count = 0;
    	for (String service: services) {
    		if (service.contains(serviceType)) {
    			count++;
    		}
    	}
    	return count;
    }              
   
    private void checkRESTService(String project, String svcName, String svcPkg, String svcClass, String msgContent, String appCls) {
        checkService(Type.REST, project, svcName, svcPkg, svcClass, msgContent, appCls);        
        checkRestSupport(project,svcName);        
    }

    private void createSampleRESTWS(String project, String name, String pkg, String cls, String appCls) {
    	SWTBotEditor ed = createSampleService(Type.REST, project, name, pkg, cls, appCls);      	
    	resourceHelper.copyResourceToClass(ed, SampleRESTWebServiceTest.class.
    			getResourceAsStream("/resources/jbossws/Rest.java.ws"),false);    	
    }
 
    private void checkRestSupport(String project, String servName) {
    	addRestSupport(project);
    	testRestSupport(project, servName);
    }
    
    private void  addRestSupport(String project) {    	
    	SWTBotTree tree = projectExplorer.bot().tree();   
    	SWTBotTreeItem ti = tree.expandNode(project);  
        new TreeItemAction(ti, CONFIGURE_CONTEXT, REST_SUPPORT).run();
    	bot.sleep(Timing.time500MS());
    	util.waitForNonIgnoredJobs();
    	try {
    		ti.getNode(REST_WS_NODE);
    	}catch (WidgetNotFoundException exc) {
    		fail("REST support was not configured properly");
    	}    	
    }
    
    private void testRestSupport(String project, String servName) {
    	SWTBotTree tree = projectExplorer.bot().tree();   
    	SWTBotTreeItem ti = tree.expandNode(project, REST_WS_NODE);
    	ArrayList<String> nodes = (ArrayList<String>)ti.getNodes();
    	
    	
    	assertTrue("Should be 2 GET services instead of " + 
    				numberOfService(nodes,"GET"), numberOfService(nodes,"GET") == 2);    	
    	assertTrue("Should be 1 DELETE service instead of " + 
				numberOfService(nodes,"DELETE"), numberOfService(nodes,"DELETE") == 1);
    	assertTrue("Should be 1 POST service instead of " + 
				numberOfService(nodes,"POST"), numberOfService(nodes,"POST") == 1);
    	assertTrue("Should be 1 PUT service instead of " + 
				numberOfService(nodes,"PUT"), numberOfService(nodes,"PUT") == 1);
    	
    	
    	assertTrue("Node's form should be {GET /RESTSample} instead of {" + 
				nodes.get(0) + "}",nodes.get(0).equals("GET /" + servName));
    	assertTrue("Node's form should be {DELETE /RESTSample/DeleteMethod} instead of {" + 
				nodes.get(1) + "}",nodes.get(1).equals("DELETE /" + servName + "/DeleteMethod"));
    	assertTrue("Node's form should be {POST /RESTSample/PostMethod} instead of {" + 
				nodes.get(2) + "}",nodes.get(2).equals("POST /" + servName + "/PostMethod"));
    	assertTrue("Node's form should be {PUT /RESTSample/PutMethod} instead of {" + 
				nodes.get(3) + "}",nodes.get(3).equals("PUT /" + servName + "/PutMethod"));
    	assertTrue("Node's form should be {GET /RESTSample/{name}} instead of {" + 
				nodes.get(4) + "}",nodes.get(4).equals("GET /" + servName + "/{name}"));
    	
    }

}
