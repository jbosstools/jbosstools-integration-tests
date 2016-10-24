/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.widgets;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

public class WidgetHandler {
	
	public static void notify(final int eventType, final Event createEvent,
			final Widget widget) {
		createEvent.type = eventType;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if ((widget == null) || widget.isDisposed()) {
					return;
				}

				widget.notifyListeners(eventType, createEvent);
			}
		});
		// Wait for synchronization
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				// do nothing here
			}
		});
	}
	
	public static void notify(final int eventType, final Widget widget){
		notify(eventType, createEvent(widget), widget);
	}
	
	private static Event createEvent(Widget widget) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = RDDisplay.getDisplay();
		return event;
	}
	
	public static <T extends Widget> void setFocus(final T w) {

			RDDisplay.syncExec(new Runnable() {
				@Override
				public void run() {
					if (w instanceof Control) {
						((Control) w).setFocus();
					} else {
						throw new IllegalArgumentException("Unsupported type");
					}
				}
			});
		}
	
	public static void sendHide(final Menu menu, final boolean recur) {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {

				if (menu != null) {
					menu.notifyListeners(SWT.Hide, new Event());
					if (recur) {
						if (menu.getParentMenu() != null) {
							sendHide(menu.getParentMenu(), recur);
						} else {
							menu.setVisible(false);
						}
					}
				}
			}

		});

	}
	
	public static Menu getMenu(final Control control){
		return RDDisplay.syncExec(new ResultRunnable<Menu>() {

			@Override
			public Menu run() {
				return control.getMenu();
			}
		});
	}
	
	public static MenuItem[] getMenuItems(final Menu menu){
		return RDDisplay.syncExec(new ResultRunnable<MenuItem[]>() {

			@Override
			public MenuItem[] run() {
				sendHide(menu, true);
				menu.notifyListeners(SWT.Show, new Event());
				return menu.getItems();
			}
		});
	}
	
	public static boolean isMenuSelected(final MenuItem[] menuItems, final String item){
		return RDDisplay.syncExec(new ResultRunnable<Boolean>() {

			@Override
			public Boolean run() {
				for(MenuItem i: menuItems){
					String normalized = i.getText().replace("&", "");
					if(normalized.equals(item)){
						return i.getSelection();
					}
				}
				throw new IllegalArgumentException("Unable to find menu item "+item);
				
			}
		});
	}
	
	public static MenuItem[] getMenuItemsFromMenuItem(final String menuItem, final MenuItem[] items){
		return RDDisplay.syncExec(new ResultRunnable<MenuItem[]>() {

			@Override
			public MenuItem[] run() {
				for(MenuItem i: items){
					String normalized = i.getText().replace("&", "");
					if(normalized.equals(menuItem)){
						Menu menu = i.getMenu();
						sendHide(menu, true);
						menu.notifyListeners(SWT.Show, new Event());
						return menu.getItems();
					}
				}
				return null;
			}
		});
	}
	
	public static List<String> getMenuItemsText(final MenuItem[] items){
		return RDDisplay.syncExec(new ResultRunnable<List<String>>() {

			@Override
			public List<String> run() {
				List<String> itemsText = new ArrayList<>();
				for(MenuItem i: items){
					itemsText.add(i.getText());
				}
				return itemsText;
			}
		});
	}
	
	public static void menuItemClick(final MenuItem[] menuItems, final String itemText){
		final MenuItem i = RDDisplay.syncExec(new ResultRunnable<MenuItem>() {

			@Override
			public MenuItem run() {
				for(MenuItem i: menuItems){
					if(i.getText().contains(itemText)){
						return i;
					}
				}
				return null;
			}
		});
		
		RDDisplay.asyncExec(new Runnable() {

			@Override
			public void run() {
				final Event event = new Event();
				event.time = (int) System.currentTimeMillis();
				event.widget = i;
				event.item = i;
				event.display = i.getDisplay();
				event.type = SWT.Selection;
				i.notifyListeners(SWT.Selection, event);
			}
		});
		RDDisplay.syncExec(new Runnable() {
			@Override
			public void run() {

			}
		});
	}
	
	public static <T extends Widget> String getText(final T widget) {
		Object o = ObjectUtil.invokeMethod(widget, "getText");

		if (o == null){
			return null;
		}

		if (o instanceof String) {
			return (String) o;
		}

		throw new IllegalArgumentException(
				"Return value of method getText() on class " + o.getClass()
						+ " should be String, but was " + o.getClass());
	}

}
