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
 * This matcher tells if a particular EditPart is a Mapping Class beginning with a given prefix.
 * 
 * @author lfabriko
 * 
 */
public class MappingClassMatcher extends BaseMatcher<EditPart> {

	public static final String MAPPING_CLASS = "<<Mapping Class>>";
	
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
		boolean isMappingClass = false;
		boolean startsWithPrefix = false;
		if (item instanceof GraphicalEditPart) {
			if (item.getClass()
					.toString()
					.equals("class org.teiid.designer.diagram.ui.notation.uml.part.UmlClassifierEditPart")) {
				IFigure figure = ((GraphicalEditPart) item).getFigure();
				
				List<IFigure> children = figure.getChildren();
				for (IFigure figure2 : children){
					if (figure2.getClass().toString().equals("class org.teiid.designer.diagram.ui.notation.uml.figure.UmlClassifierHeader")){
						List<IFigure> children2 = figure2.getChildren();
						for (IFigure figure3 : children2){
							if (figure3 instanceof Label ){
								String text = ((Label) figure3).getText();
								if (text.equals(MAPPING_CLASS)){
									isMappingClass = true;
								}
								if (text.startsWith(this.prefix)){
									startsWithPrefix = true;
									texts.add(text);
								}
								
							} 
						}
					}
				}
			}
			if (isMappingClass && startsWithPrefix){
				return true;
			}
		}
		return false;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is a mapping class");
	}

	@Factory
	public static MappingClassMatcher createMappingClassMatcher() {
		return new MappingClassMatcher();
	}

	public List<String> getTexts() {
		return texts;
	}

}
