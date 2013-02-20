package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating WSDL connection profile
 * 
 * @author apodhrad
 *
 */
public class WsdlProfileWizard extends TeiidProfileWizard {

	public static final String WSDL_PATH = "Connection URL or File Path";
	public static final String END_POINT = "End Point";

	private String wsdl;
	private String endPoint;

	public WsdlProfileWizard() {
		super("Web Services Data Source (SOAP)");
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public void execute() {
		open();
		new LabeledText(WSDL_PATH).setText(wsdl);
		next();
		new DefaultCombo(END_POINT).setSelection(endPoint);
		finish();
	}

}
