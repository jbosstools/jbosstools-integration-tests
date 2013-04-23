package org.eclipse.bpmn2.ui.swt.matcher;

import java.util.regex.Pattern;

import org.eclipse.gef.EditPart;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ConstructWithName<T extends EditPart> extends ConstructAttributeMatchingRegex<EditPart> {

	/**
	 * 
	 * @param label
	 */
	public ConstructWithName(String name) {
		super("name", Pattern.compile(name));
	}

}
