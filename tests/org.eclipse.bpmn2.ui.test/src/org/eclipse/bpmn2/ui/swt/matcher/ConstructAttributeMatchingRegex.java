package org.eclipse.bpmn2.ui.swt.matcher;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.Shape;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ConstructAttributeMatchingRegex<T extends EditPart> extends BaseMatcher<EditPart> {

	private String methodName;
	
	private Pattern pattern;

	/**
	 * 
	 * @param attributeName
	 * @param pattern
	 */
	public ConstructAttributeMatchingRegex(String attributeName, Pattern pattern) {
		this.methodName = "get" + attributeName.toUpperCase().charAt(0) + attributeName.toLowerCase().substring(1);
		this.pattern = pattern;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			return matches((EditPart) item, pattern);
		}
		return false;
	}

	/**
	 * 
	 * @param description
	 */
	public void describeTo(Description description) {
		description.appendText(" construct label matches '" + pattern + "'");
	}

	/**
	 * 
	 * @param editPart
	 * @param name
	 * @return
	 */
	public boolean matches(EditPart editPart, Pattern pattern) {
		Object model = editPart.getModel();
		if (model instanceof Shape) {
			Shape shape = (Shape) model;
			PictogramLink link = shape.getLink();
			if (link != null) {
				EList<EObject> objectList = link.getBusinessObjects();
				for (EObject eo : objectList) {
					try {
						// Just one of the business objects should have a name!
						Method method = eo.getClass().getMethod(methodName);
						String name = method.invoke(eo).toString();
						if(pattern.matcher(name).matches()) {
							return true;
						}
					} catch (Exception e) {
						// Ignore
					}
				}
			}
		}
		return false;
	}
}
