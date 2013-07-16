package org.jboss.tools.teiid.reddeer.matcher;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;

/**
 * This matcher tells if a particular EditPart is an attribute with label starting with 
 * given prefix.
 * 
 * @author lfabriko
 * 
 */
public class AttributeMatcher extends BaseMatcher<EditPart> {

	/*public static final int WIDTH = 40;
	public static final int HEIGHT = 60;*/
	public String prefix = "";
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<String> texts = new ArrayList<String>();

	
	@Override
	public boolean matches(Object item) {
		if (item instanceof GraphicalEditPart) {
			if (item.getClass()
					.toString()
					.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlAttributeEditPart")) {
				IFigure figure = ((GraphicalEditPart) item).getFigure();
				for (Object o : figure.getChildren()) {
					if ((o instanceof Label)
							&& (((Label) o).getText().startsWith(this.prefix))) {
						texts.add(((Label) o).getText());
						return true;
					} 
				}
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is an attribute starting with given prefix");
	}

	@Factory
	public static AttributeMatcher createAttributeMatcher() {
		return new AttributeMatcher();
	}

	public List<String> getTexts() {
		return texts;
	}

}
