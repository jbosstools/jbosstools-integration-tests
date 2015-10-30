package org.jboss.tools.batch.reddeer.editor.jobxml;

import org.eclipse.ui.texteditor.ITextEditor;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;

/**
 * Represents the source tab of job xml editor. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JobXMLEditorSourcePage extends XMLSourcePage {

	public static final String JOB = ":job";
	
	public static final String DECISION = ":decision";
	
	public static final String FLOW = ":flow";
	
	public static final String SPLIT = ":split";
	
	public static final String STEP = ":step";
	
	public static final String BATCHLET = ":batchlet";
	
	public static final String CHUNK = ":chunk";
	
	public static final String READER = ":reader";
	
	public static final String WRITER = ":writer";
	
	public static final String CHECKPOINT = ":checkpoint-algorithm";
	
	public static final String PROCESSOR = ":processor";
	
	public static final String ID = "@id";
	
	public static final String REF = "@ref";
	
	private static final Logger log = Logger.getLogger(JobXMLEditorSourcePage.class);
	
	public JobXMLEditorSourcePage(ITextEditor editor) {
		super(editor);
	}
	
	public String evaluateXPath(String... xPathElements) {
		String xPathExpression = createExpression(xPathElements);
		log.debug("Evaluating xPathExpression: " + xPathExpression);
		log.trace("Editor text: " + getText());
		return this.evaluateXPath(xPathExpression);
	}

	@Override
	public String evaluateXPath(String xPathExpression) {
		// TODO Fix the hard-coded wait
		AbstractWait.sleep(TimePeriod.SHORT);
		return super.evaluateXPath(xPathExpression);
	}
	
	private String createExpression(String[] xPathElements) {
		StringBuilder builder = new StringBuilder("/");
		
		for (int i = 0; i < xPathElements.length; i++){
			builder.append(xPathElements[i]);
			if (i != xPathElements.length - 1){
				builder.append("/");
			}
		}
		return builder.toString();
	}
}
