package org.jboss.tools.runtime.as.ui.bot.test.detector.cdk2;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.jboss.tools.common.reddeer.utils.FileUtils;
import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;
import org.junit.BeforeClass;

public class DetectCDK2fromGit extends DetectRuntimeTemplate {

	private static final String SERVER_ID = "cdk-git";
	
	private static final String GIT_REPOSITORY_URI = System.getProperty("jbt.cdk_git_uri");
	
	@BeforeClass
	public static void setup(){
		if (GIT_REPOSITORY_URI==null){
			fail("\"jbt.cdk_git_uri\" property was not set.");
		}
		File gitRepositoryDir = new File("target/cdk-git");
		if (gitRepositoryDir.exists()){
			try {
				FileUtils.deleteDirectory(gitRepositoryDir);
			} catch (IOException e) {
				fail("Git repository already exists and its deletion was unsuccessfull");
			}
		}
		gitRepositoryDir.mkdirs();
		try {
			Git.cloneRepository().setURI(GIT_REPOSITORY_URI).setDirectory(gitRepositoryDir).call();
		} catch (GitAPIException e) {
			e.printStackTrace();
			fail("Unable to clone git repository");
		}
	}

	@Override
	protected String getPathID() {
		return SERVER_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime expectedServer = new Runtime();
		expectedServer.setName("cdk-v2");
		expectedServer.setVersion("2.0");
		expectedServer.setType("CDK");
		expectedServer.setLocation(new File("target/cdk-git/cdk-v2").getAbsolutePath());
		return Arrays.asList(expectedServer);
	}

}
