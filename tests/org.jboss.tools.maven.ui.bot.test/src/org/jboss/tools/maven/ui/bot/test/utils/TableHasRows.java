package org.jboss.tools.maven.ui.bot.test.utils;

import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class TableHasRows implements WaitCondition  {
	  private final DefaultTable table;

	  public TableHasRows(DefaultTable table) {
	    this.table = table;
	  }


	  public boolean test() {
		return  table.rowCount() > 1;
	  }

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}
	}