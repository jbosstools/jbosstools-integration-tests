package org.jboss.tools.perf.test.maven.wildfly;

import org.eclipse.reddeer.requirements.cleanworkspace.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.perf.test.PerfTestMavenBase;
import org.junit.Test;

@CleanWorkspace
public class ImportMavenWildfly extends PerfTestMavenBase{
	
	public ImportMavenWildfly(){
		projectFolder = "wildfly";
	}
	
	@Test
	public void disabledGit(){
		prepareGit(false);
		importProjectWithMaven();
	}
	
	@Test
	public void disabledAutomaticBuild(){
		automaticBuild(false);
		importProjectWithMaven();
	}
	
	@Test
	public void disabledAll(){
		prepareGit(false);
		automaticBuild(false);
		importProjectWithMaven();
	}

}
