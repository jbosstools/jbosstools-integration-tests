package org.jboss.tools.bpmn2.itests.swt.matcher;

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
		/*
		 * Check weather the process edit part attribute matches the pattern.
		 * 
		 * See org.eclipse.graphiti.mm.pictograms.impl.DiagramImpl;
		 */
		if (matches(model, pattern)) {
			return true;
		}
		/*
		 * Check weather a business object attribute matches the patterns.
		 * 
		 * See org.eclipse.graphiti.mm.pictograms.impl.ContainerShapeImpl.getLink();
		 */
		if (model instanceof Shape) {
			Shape shape = (Shape) model;
			PictogramLink link = shape.getLink();
			if (link != null) {
				EList<EObject> objectList = link.getBusinessObjects();
				for (EObject eo : objectList) {
					if (matches(eo, pattern)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param instance
	 * @param pattern
	 * @return
	 */
	public boolean matches(Object instance, Pattern pattern) {
		try {
			Method method = instance.getClass().getMethod(methodName);
			String name = method.invoke(instance).toString();
			if(pattern.matcher(name).matches()) {
				return true;
			}
		} catch (Exception e) {
			// ignore
		}
		return false;
	}
}
