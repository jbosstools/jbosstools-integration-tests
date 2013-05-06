package org.jboss.tools.teiid.reddeer.matcher;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.ViewForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 * @author apodhrad
 * 
 */
public class IsTransformationEditor extends BaseMatcher<StyledText> {

	Logger log = Logger.getLogger(this.getClass());

	@Override
	public boolean matches(Object obj) {
		if (obj instanceof StyledText) {
			return isInTransformationView((StyledText) obj);
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("Transformation Editor");
	}

	private boolean isInTransformationView(Composite composite) {
		if (composite == null) {
			return false;
		}
		if (composite instanceof ViewForm) {
			ViewForm viewForm = (ViewForm) composite;
			Control control = viewForm.getTopLeft();
			if (control instanceof CLabel) {
				CLabel cLabel = (CLabel) control;
				return cLabel.getText().contains("Transformation Editor");
			}
		}
		return isInTransformationView(composite.getParent());
	}
}
