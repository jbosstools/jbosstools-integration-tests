package org.jboss.tools.common.reddeer.preferences;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * Reddeer model of JBoss Tools > Source Lookup preference page
 * 
 * @author jniederm
 */
public class SourceLookupPreferencePage extends PreferencePage  {
	
	protected final static Logger log = Logger.getLogger(SourceLookupPreferencePage.class);
	
	public SourceLookupPreferencePage() {
		super("JBoss Tools", "Source Lookup");
	}

	@Override
	public void open() {
		super.open();
	}
	
	public void setSourceAttachment(SourceLookupPreferencePage.SourceAttachmentEnum option) {
		new RadioButton(option.getText()).click();
		log.info("Attach sources '" + option.getText() + "' selected.");
	}
	
	public enum SourceAttachmentEnum {
		ALWAYS("Always"),
		NEVER("Never"),
		PROMPT("Prompt");
		
		private String text;
		
		SourceAttachmentEnum(String text) {
			this.text = text;
		}
		
		public String getText() {
			return this.text;
		}
	}
}
