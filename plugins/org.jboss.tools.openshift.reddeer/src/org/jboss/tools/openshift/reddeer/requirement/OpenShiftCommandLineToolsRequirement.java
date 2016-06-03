/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.requirement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.junit.requirement.Requirement;
import org.jboss.tools.openshift.reddeer.exception.OpenShiftToolsException;
import org.jboss.tools.openshift.reddeer.requirement.OpenShiftCommandLineToolsRequirement.OCBinary;
import org.jboss.tools.openshift.reddeer.utils.FileHelper;

/**
 * Requirement to download and extract OpenShift command line tools binary which is necessary 
 * for some functionality of OpenShift tools.
 *  
 * @author mlabuda@redhat.com
 *
 */
public class OpenShiftCommandLineToolsRequirement implements Requirement<OCBinary> {
	
	private static final String SEPARATOR = System.getProperty("file.separator");
	
	public static String PATH_TO_CLIENT;
	
	private Logger logger = new Logger(OpenShiftCommandLineToolsRequirement.class);
	
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface OCBinary {
    }

	@Override
	public boolean canFulfill() {
		return true;
	}

	@Override
	public void fulfill() {
		File ocBinary = new File("binaries" + SEPARATOR + "oc");
		if (!ocBinary.exists()) {
			downloadAndExtractOpenShiftClient();
		} else {
			logger.info("Binary is already downloaded.");
			PATH_TO_CLIENT = ocBinary.getAbsolutePath(); 
		}
	}

	@Override
	public void setDeclaration(OCBinary declaration) {}

	@Override
	public void cleanUp() {}
	
	/**
	 * Gets location of OC binary. Requirement has to be invoked at least once to have 
	 * oc binary available.
	 * 
	 * @return location of oc binary if it is available, null otherwise;
	 */
	public static String getOCLocation() {
		return PATH_TO_CLIENT;
	}
	
	private void downloadAndExtractOpenShiftClient() {
		logger.info("Creating directory binaries");
		FileHelper.createDirectory(new File("binaries"));
		URL downloadLink = null;
		String fileName = null;
		FileOutputStream fileOutputStream = null;
		ReadableByteChannel readableByteChannel = null;
		
		try {
			logger.info("Processing download link.");
			downloadLink = new URL(getDownloadLink());
			fileName = getFileName(downloadLink.getPath());
			
			logger.info("Downloading OpenShift binary.");
			readableByteChannel = Channels.newChannel(downloadLink.openStream());
			fileOutputStream = new FileOutputStream(fileName);
			fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		} catch (MalformedURLException ex) {
			// Never thrown because links are valid
		} catch (IOException ex) {
			throw new OpenShiftToolsException("Cannot download OpenShift binary.\n" + ex.getMessage());
		} finally {
			try {
				readableByteChannel.close();
			} catch (IOException ex) {
				// do nothing
			}
			try {
				fileOutputStream.close();
			} catch (Exception ex) {
				// do nothing
			}
		}

		logger.info("Extracting OpenShift archive.");
		File outputDirectory = new File("binaries");
		if (fileName.endsWith("zip")) {
			FileHelper.unzipFile(new File(fileName), outputDirectory);
		} else if (fileName.endsWith("tar.gz")) {
			FileHelper.extractTarGz(new File(fileName), outputDirectory);
		}
		
		PATH_TO_CLIENT = outputDirectory.getAbsolutePath() + SEPARATOR + "oc";
	}
	
	private String getFileName(String urlPath) {
		String[] pathParts = urlPath.split("/");
		return "binaries" + SEPARATOR + pathParts[pathParts.length - 1];
	}
	
	private String getDownloadLink() {
		if (Platform.getOS().equals(Platform.OS_LINUX)) {
			if (Platform.getOSArch().equals(Platform.ARCH_X86)) {
				return ClientVersion.LINUX_1_2_32.getDownloadLink();
			} else { 
				return ClientVersion.LINUX_1_2_64.getDownloadLink();
			}
		} else {
			return ClientVersion.WINDOWS_1_2_64.getDownloadLink();
		}
	}
	
	public enum ClientVersion {
		LINUX_1_1_32("https://github.com/openshift/origin/releases/download/"
				+ "v1.1/openshift-origin-v1.1-ac7a99a-linux-386.tar.gz"),
		LINUX_1_1_64("https://github.com/openshift/origin/releases/download/"
				+ "v1.1/openshift-origin-v1.1-ac7a99a-linux-amd64.tar.gz"),
		WINDOWS_1_1_64("https://github.com/openshift/origin/releases/download/"
				+ "v1.1/openshift-origin-v1.1-ac7a99a-windows-amd64.zip"),
		
		LINUX_1_2_32("https://github.com/openshift/origin/releases/download/"
				+ "v1.2.0/openshift-origin-client-tools-v1.2.0-2e62fab-linux-32bit.tar.gz"),
		LINUX_1_2_64("https://github.com/openshift/origin/releases/download/"
				+ "v1.2.0/openshift-origin-client-tools-v1.2.0-2e62fab-linux-64bit.tar.gz"),
		WINDOWS_1_2_64("https://github.com/openshift/origin/releases/download/"
				+ "v1.2.0/openshift-origin-client-tools-v1.2.0-2e62fab-windows.zip");
		
		String url;
		
		private ClientVersion(String url) {
			this.url = url;
		}
		
		public String getDownloadLink() {
			return url;
		}		
	}
}
