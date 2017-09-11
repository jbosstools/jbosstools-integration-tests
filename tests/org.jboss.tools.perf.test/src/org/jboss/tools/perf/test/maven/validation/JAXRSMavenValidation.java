package org.jboss.tools.perf.test.maven.validation;

import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestMavenBase;
import org.junit.Test;

@CleanWorkspace
public class JAXRSMavenValidation extends PerfTestMavenBase{
    
    public JAXRSMavenValidation(){
        projectFolder = "jaxrs";
    }
    
    @Test
    public void testJAXRS(){
    	importProjectWithMaven();
    }


}
