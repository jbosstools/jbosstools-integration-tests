package org.jboss.tools.perf.test.validation;

import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

@CleanWorkspace
public class WSTValidation extends PerfTestBase{
    
    public WSTValidation(){
        projectFolder = "WSTValidation";
    }
    
    @Test
    public void testWST(){
        importProject();
    }

}
