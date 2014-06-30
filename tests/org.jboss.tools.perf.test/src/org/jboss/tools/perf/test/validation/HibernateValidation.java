package org.jboss.tools.perf.test.validation;

import org.jboss.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestBase;
import org.junit.Test;

@CleanWorkspace
public class HibernateValidation extends PerfTestBase{
    
    public HibernateValidation(){
        projectFolder = "HibernateValidation";
    }
    
    @Test
    public void testHibernate(){
        importProject();
    }


}
