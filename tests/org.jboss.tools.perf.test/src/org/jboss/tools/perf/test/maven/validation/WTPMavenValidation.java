package org.jboss.tools.perf.test.maven.validation;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestMavenBase;
import org.junit.Test;

@CleanWorkspace
public class WTPMavenValidation extends PerfTestMavenBase{
    
    public WTPMavenValidation(){
        projectFolder = "wtp";
    }
    
    @Test
    public void testWTP(){
    	importProjectWithMaven();
    }

}
