package org.jboss.tools.examples.ui.bot.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.reddeer.junit.logging.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class is responsible for verification of correctness of the XML file,
 * accessibility of examples source (URLs), downloading archives and extracting them.
 * 
 * @author mlabuda@redhat.com
 * @version 0.9
 */
public class InitializeExamplesImport {

	private Logger logger = new Logger(InitializeExamplesImport.class);
	
	private Document document;
	
	private List<String> malformedURLs = new ArrayList<String>();
	private List<String> nonExistingURLs = new ArrayList<String>();
	private List<String> inputOutputErrors = new ArrayList<String>();
	
	private List<String> notProcessedExamples = new ArrayList<String>();
	
	@Before
	public void loadXMLfile() throws ParserConfigurationException, SAXException, IOException {
		String path = System.getProperty("importTestDefinition");
		Assert.assertFalse("XML definition file has not been set", path == null || path.equals(""));
				
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		try {
			document = builder.parse(new File(path));
		} catch (FileNotFoundException ex) {
			logger.error("Definition file " + path + " does not exist");
			throw ex;
		}
	}

	@Test
	public void checkURLandDownloadArchives() throws IOException{
		NodeList archiveList = document.getElementsByTagName("archive");
		for (int i=0; i<archiveList.getLength(); i++) {		
			Element archive = (Element) archiveList.item(i);
			Attr archiveURL = (Attr) archive.getAttributes().getNamedItem("url");
			try {
				// Possible URL malformation or unaccessibility
				URL url = new URL(archiveURL.getValue());
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("HEAD");
				if (!(connection.getResponseCode() == HttpURLConnection.HTTP_OK)) {
					nonExistingURLs.add(archiveURL.getValue());
					notProcessedExamples.addAll(getProjectsPaths(archive));
					continue;
				}
				connection.disconnect();
				
				File directory = new File("archives");
				if (!directory.exists() && directory.mkdirs() == false) {
					// This have to fail, bcs. it is not possible to store examples without directory
					// But it should not happen. Only in unpredictable external impacts
					fail("Directory for archives storage has not been created.");
				}
				
				storeArchive(url, "archives" + File.separator + archiveURL.getValue().substring(archiveURL.getValue().lastIndexOf("/") + 1));
				
				// Name element of archive
				String name = archive.getElementsByTagName("name").item(0).getTextContent();
				String zipName = archiveURL.getValue().substring(archiveURL.getValue().lastIndexOf("/") + 1);
				unzipArchive("archives", zipName, name);
				
				Examples.addArchive(name, getProjectsPaths(archive));
			} catch (MalformedURLException ex) {
				malformedURLs.add(archiveURL.getValue());
				notProcessedExamples.addAll(getProjectsPaths(archive));
			} catch (IOException ex) {
				inputOutputErrors.add(archiveURL.getValue());
				notProcessedExamples.addAll(getProjectsPaths(archive));
			}
		}
	}
	
	/**
	 * Get path to the project examples in archive with the given node. 
	 * This method is useful in case of adding paths to examples which will be processed or in case of reporting not processed examples 
	 * @param archiveNode node of archive with the examples
	 * @return list of paths to the examples in the given archive node
	 */
	private List<String> getProjectsPaths(Element archiveNode) {
		List<String> paths = new ArrayList<String>();
		Element archiveElement = (Element) archiveNode;
		NodeList projects = archiveElement.getElementsByTagName("example");
		
		for (int k=0; k < projects.getLength(); k++) {
			Attr importExample = (Attr) projects.item(k).getAttributes().getNamedItem("import");
			if (!(importExample !=null && importExample.getValue().equals("false"))) {
				Attr path = (Attr) projects.item(k).getAttributes().getNamedItem("path");
				paths.add(path.getValue());
			}
		}
		return paths;
	}

	/**
	 * Store archive file from the given url to the archive location on local storage
	 * @param url source where is the archive located
	 * @param archiveLocation where to store archive on local storage
	 * @throws IOException can be thrown in some unpredictable IO errors during processing the archive
	 */
	private void storeArchive(URL url, String archiveLocation) throws IOException {
		ReadableByteChannel byteChannel = Channels.newChannel(url.openStream());
		FileOutputStream fileOutputStream = new FileOutputStream(archiveLocation);
		fileOutputStream.getChannel().transferFrom(byteChannel, 0, Long.MAX_VALUE);
		byteChannel.close();
		fileOutputStream.close();
	}
	
	/**
	 * Unzip the archive with the given name to the directory and subdirectory
	 * @param dir parent directory where to unzip archive. This directory has to exist. 
	 * @param fileZipName name of archive to extract
	 * @param archiveName subdirectory where will be archive extracted
	 * @throws IOException can be caused in some unpredictable IO situations
	 */
	private void unzipArchive(String dir, String fileZipName, String archiveName) throws IOException {
		byte[] buffer = new byte[1024];
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(dir + File.separator + fileZipName));
		ZipEntry zipEntry;
	
		while ((zipEntry = zipInputStream.getNextEntry()) != null) {
			String archiveFolder = dir + File.separator + archiveName;
			new File(archiveFolder).mkdir();
			String name = zipEntry.getName();
			
			if (zipEntry.isDirectory()) {
				new File(archiveFolder + File.separator + zipEntry.getName()).mkdir();
			} else {
				FileOutputStream fileOutputStream = new FileOutputStream(archiveFolder + File.separator + name);
				int length;
				while ((length = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, length);
				}
				fileOutputStream.close();
			}
		}
		
		zipInputStream.closeEntry();
		zipInputStream.close();
	}
	
	@After
	public void report() {
		if (!nonExistingURLs.isEmpty()) {
			logger.error("There are some non-existing URLs");
			for (String url: nonExistingURLs) {
				logger.error("Non-existing URL \"" + url + "\". Could not download examples from this source.");
			}
		}
		
		if (!malformedURLs.isEmpty()) {
			logger.error("Malformed URLs occured in initialization of examples.");
			for (String url: malformedURLs) {
				logger.error("Malformed URL \"" + url + "\". Could not download examples from this source.");
			}
		}
		
		if (!inputOutputErrors.isEmpty()) {
			logger.error("IOException(s) occured while processing archives.");
			for (String ioError: inputOutputErrors) {
				logger.error("IOException has occured during processing archives from URL " + ioError);
			}
		}
		
		if (!notProcessedExamples.isEmpty()) {
			logger.error("These examples will not be processed further bcs. of errors occured while processing their source / paths");
			for (String example: notProcessedExamples) {
				logger.error("Example name: " + example);
			}
			
			fail("Initialization failed, because some examples will not be tested. "
					+ "Examples testing will continue without those examples");
		}
	}
}