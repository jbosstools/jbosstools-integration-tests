package org.jboss.tools.bpmn2.itests.swt.matcher;

import java.util.regex.Pattern;

import org.eclipse.gef.EditPart;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ConstructWithId<T extends EditPart> extends ConstructAttributeMatchingRegex<EditPart> {

	/**
	 * 
	 * @param label
	 */
	public ConstructWithId(String id) {
		super("id", Pattern.compile(id));
	}

}
