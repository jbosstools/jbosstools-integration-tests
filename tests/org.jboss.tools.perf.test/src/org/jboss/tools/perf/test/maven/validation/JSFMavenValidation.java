package org.jboss.tools.perf.test.maven.validation;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestMavenBase;
import org.junit.Test;

@CleanWorkspace
public class JSFMavenValidation extends PerfTestMavenBase{
    
    public JSFMavenValidation(){
        projectFolder = "jsf";
    }
    
    @Test
    public void testJSF(){
    	importProjectWithMaven();
    }

}
