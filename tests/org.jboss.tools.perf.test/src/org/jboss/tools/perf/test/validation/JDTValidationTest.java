package org.jboss.tools.perf.test.validation;

import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

@CleanWorkspace
public class JDTValidationTest extends PerfTestBase{
    
    public JDTValidationTest() {
        projectFolder = "JDTValidation";
    }
    
    @Test
    public void testJDT(){
        importProject();
    }

}
