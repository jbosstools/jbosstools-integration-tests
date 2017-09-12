/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.reddeer.editor.jobxml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.eclipse.wst.xml.ui.tabletree.XMLSourcePage;
import org.eclipse.ui.texteditor.ITextEditor;

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

	public String evaluateXPath(String xPathExpression) {
		// TODO: Remove hard-coded wait
		//AbstractWait.sleep(TimePeriod.getCustom(2));
		return getAssociatedFile().xpath(xPathExpression);
	}
	
	// convert InputStream to String
	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

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
