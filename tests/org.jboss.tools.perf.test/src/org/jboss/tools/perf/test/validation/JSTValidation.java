package org.jboss.tools.perf.test.validation;

import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

@CleanWorkspace
public class JSTValidation extends PerfTestBase{
    
    public JSTValidation(){
        projectFolder = "JSTValidation";
    }
    
    @Test
    public void testJST(){
        importProject();
    }


}
