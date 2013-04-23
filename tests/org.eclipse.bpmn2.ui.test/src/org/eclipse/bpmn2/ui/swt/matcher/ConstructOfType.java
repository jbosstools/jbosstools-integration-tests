package org.eclipse.bpmn2.ui.swt.matcher;

import java.util.ArrayList;
import java.util.List;

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
public class ConstructOfType<T extends EditPart> extends BaseMatcher<EditPart> {

	private String type;

	private static List<EObject> duplicateList= new ArrayList<EObject>();
	
	/**
	 * 
	 * @param type
	 */
	public ConstructOfType(String type) {
		this.type = type;
	}

	public boolean matches(Object item) {
		if (item instanceof EditPart) {
			return equalsType((EditPart) item, type);
		}
		return false;
	}

	/**
	 * 
	 */
	public void describeTo(Description description) {
		description.appendText(" construct of type '" + type + "'");
	}

	/**
	 * 
	 * @param editPart
	 * @param type
	 * @return
	 */
	public static boolean equalsType(EditPart editPart, String type) {
		Object model = editPart.getModel();
		if (model instanceof Shape) {
			Shape shape = (Shape) model;
			PictogramLink link = shape.getLink();
			if (link != null) {
				EList<EObject> objectList = link.getBusinessObjects();
				for (EObject eo : objectList) {
					// check the interface name.
					Class<?>[] classArray = eo.getClass().getInterfaces();
					if (classArray.length > 0) {
						for (Class<?> c : classArray) {
							// every element which has it's label below the pictogram will be
							// present two times! One for the object and one for the label. how
							// is this even possible? different graphic representation for each
							// each case but the same object ???
							if (!duplicateList.contains(eo)) {
								if (c.getSimpleName().equals(type)) {
									duplicateList.add(eo);
									return true;
								}
							} else {
								// TBD: log DEBUG or WARNING
							}
						}
					}
				}
			}
		}
		return false;
	}

}
