package org.jboss.tools.perf.test.validation;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

@CleanWorkspace
public class CDIValidation extends PerfTestBase{
    
   public CDIValidation(){
       projectFolder = "CDIValidation";
   }
    
    @Test
    public void testCDI(){
        importProject();
    }

}
