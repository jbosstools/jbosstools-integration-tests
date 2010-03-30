/**
* Generated Mon Mar 22 17:33:35 CET 2010 JBDS 3.0.0-#50-GA
*/
package org.jboss.tools.ui.bot.ext.gen;

import java.util.List;

public interface IActionItem {

	/**
	* gets label name
	*/
	public String getName();
	/**
	* gets path (in tree,list) to reach leaf returned by 'getName()'
	*/
	public List<String> getGroupPath();

}

