package org.jboss.tools.perf.test.validation;

import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

@CleanWorkspace
public class NoValidationTest extends PerfTestBase{
    
    public NoValidationTest(){
        projectFolder = "NoValidation";
    }
    
    @Test
    public void testNoValidation(){
        importProject();
    }

}
