/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.batch.reddeer.editor.design;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.core.matcher.WithTooltipTextMatcher;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.keyboard.KeyboardFactory;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;

/**
 * Represents the table in batch job.xml editor and its control buttons.
 * @author lvalach
 *
 */
public class BatchEditorTable {

	public static final String BUTTON_ADD = "Add.*";
	public static final String BUTTON_DELETE = "Delete";
	public static final String BUTTON_MOVE_UP = "Move Up";
	public static final String BUTTON_MOVE_DOWN = "Move Down";

	private final String sectionName;
	private final DefaultSection section;
	private final DefaultTable defaultTable;
	private final int index;

	/**
	 * Represents table located in the specific section with the specified order.
	 *
	 * @param section section title
	 * @param index the index of the table in the given section
	 */
	public BatchEditorTable(String section, int index) {
		super();
		this.sectionName = section;
		this.index = index;
		this.section = new DefaultSection(this.sectionName);
		this.defaultTable = new DefaultTable(this.index);
	}
	
	/**
	 * Represents table located in the specific section.
	 *
	 * @param section section title
	 */
	public BatchEditorTable(String section) {
		this(section, 0);
	}

	/**
	 * Add new item to the table. Number of values cannot exceed number of columns.
	 * @param cellsValues the text to insert
	 */
	public void addItem(final String... cellsValues) {		
		this.click(BUTTON_ADD);
		KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		
		for (int column = 0; column < cellsValues.length; column++) {
			int row = defaultTable.rowCount() - 1;
			
			defaultTable.getItem(row).doubleClick(column);
			DefaultText dt = new DefaultText(defaultTable);
			dt.setText(cellsValues[column]);
			
			KeyboardFactory.getKeyboard().invokeKeyCombination(SWT.CR);
		}

		
	}

	/**
	 * Delete the item with specific text. Searches only in first column.
	 * @param itemText the text in the first column of wanted item 
	 */
	public void deleteItem(String itemText) {
		this.selectItem(itemText);
		this.click(BUTTON_DELETE);
	}

	/**
	 * Move up the item with specific text. Searches only in first column.
	 * @param itemText the text in the first column of wanted item 
	 */
	public void moveUpItem(String itemText) {
		this.selectItem(itemText);
		this.click(BUTTON_MOVE_UP);
	}

	/**
	 * Move down the item with specific text. Searches only in first column.
	 * @param itemText the text in the first column of wanted item 
	 */
	public void moveDownItem(String itemText) {
		this.selectItem(itemText);
		this.click(BUTTON_MOVE_DOWN);
	}
	
	/**
	 * Select item with specified text in the first column.
	 * @param itemText the text in the first column of wanted item 
	 */
	private void selectItem(final String itemText) {
		defaultTable.getItem(itemText).select();
	}
	
	
	/**
	 * Click the tool item with specified text.
	 * @param toolItemLabel the text of tool item
	 */
	private void click(String toolItemLabel) {
		DefaultToolItem toolitem = new DefaultToolItem(section, index, new WithTooltipTextMatcher(new RegexMatcher(toolItemLabel)));
		toolitem.click();
	}
}
