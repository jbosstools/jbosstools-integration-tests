package org.jboss.tools.bpmn2.itests.swt.matcher;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
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
		if (model instanceof PictogramElement) {
			PictogramElement pe = (PictogramElement) model;
			PictogramLink link = pe.getLink();
			if (link != null) {
				EList<EObject> objectList = link.getBusinessObjects();
				for (EObject eo : objectList) {
					/*
					 * Make sure we do not include pictogram labels. E.g. gateway has it's name under the pictogram
					 * and this is a separate object which does not allow actions to be taken on it.
					 * 
					 * A label object is represented by the GFMultilineText and must be the only child! Other objects
					 * may have a label too but these are complex objects not just simple labels.
					 */
					if (matches(eo, pattern) && !matches(editPart, "org.eclipse.graphiti.ui.internal.figures.GFMultilineText")) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param editPart
	 * @param className
	 * @return
	 */
	public boolean matches(EditPart editPart, String className) {
			IFigure figure = ((GraphicalEditPart) editPart).getFigure();
			
			@SuppressWarnings("unchecked")
			List<Object> children = figure.getChildren();
			
			return (children.size() == 1 && children.get(0).getClass().getName().equals(className));
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
