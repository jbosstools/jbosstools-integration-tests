package org.jboss.tools.bpmn2.itests.validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.api.io.Resource;

/**
 * 
 * @author mbaluch
 */
public class JBPM6Validator {
	
	Results results;
	
	/**
	 * 
	 */
	public JBPM6Validator() {
	}
	
	/**
	 * 
	 * @param file
	 * @return
	 */
	public boolean validate(File file) {
		Resource resource = KieServices.Factory.get().getResources().newFileSystemResource(file);
		resource.setTargetPath("/" + file.getName());
		
		return validate(resource);
	}
	
	/**
	 * 
	 * @param xml
	 * @return
	 */
	public boolean validate(String xml) {
		Resource resource = KieServices.Factory.get().getResources().newByteArrayResource(xml.getBytes());
		resource.setTargetPath("/ValidatedProcess");
		
		return validate(resource);
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	protected boolean validate(Resource process) {
		KieServices ks = KieServices.Factory.get();
		KieFileSystem kfs = ks.newKieFileSystem();
		kfs.write(process);

		KieBuilder kb = ks.newKieBuilder(kfs);
		kb.buildAll();
		
		results = kb.getResults();

		return !results.hasMessages(Level.ERROR);
    }
	
	/**
	 * 
	 * @return
	 */
	public List<String> getErrorList() {
		List<String> ret = new ArrayList<String>();
		if (results != null) {
			for (Message m : results.getMessages(Level.ERROR)) {
				ret.add(m.toString());
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getWarningList() {
		List<String> ret = new ArrayList<String>();
		if (results != null) {
			for (Message m : results.getMessages(Level.WARNING)) {
				ret.add(m.toString());
			}
		}
		return ret;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getResultMessage() {
		String resultMessage = "";
		if (results != null) {
			resultMessage = results.toString();
		}
		return resultMessage;
	}
	
}