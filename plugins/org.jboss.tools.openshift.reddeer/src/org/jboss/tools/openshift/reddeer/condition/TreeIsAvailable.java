package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

public class TreeIsAvailable extends AbstractWaitCondition {

	public TreeIsAvailable() { }

	@Override
	public boolean test() {
		try {
			new DefaultTree();
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}

	@Override
	public String description() {
		return "Tree with index 0 is available";
	}
}
