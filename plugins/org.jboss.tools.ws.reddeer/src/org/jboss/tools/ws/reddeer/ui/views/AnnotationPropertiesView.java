/*******************************************************************************
 * Copyright (c) 2010-2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.reddeer.ui.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.ws.reddeer.helper.RedDeerHelper;

/**
 * Web Services > Annotation Properties View
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 **/
public class AnnotationPropertiesView extends WorkbenchView {

	/**
	 * Image of checked checkbox ( = checkbox is on)
	 * 
	 * Based on org.eclipse.jst.ws.jaxws.ui since JBDS 7.1.0.Alpha1
	 */
	private Image checkboxOnImage;
	
	/**
	 * Image of unchecked checkbox ( = checkbox is off)
	 * 
	 * Based on org.eclipse.jst.ws.jaxws.ui since JBDS 7.1.0.Alpha1
	 */
	private Image checkboxOffImage;
	
	public AnnotationPropertiesView() {
		super("Web Services", "Annotation Properties");
		
		loadCheckboxImages();
	}

	/**
	 * Load images representing checkboxes's states.
	 */
	private void loadCheckboxImages() {
		final String pluginId = "org.eclipse.jst.ws.jaxws.ui";

		final String checkboxOnIconPath = "icons/obj16/true.gif";
		final String checkboxOffIconPath = "icons/obj16/false.gif";

		ImageDescriptor onDescriptor = org.eclipse.ui.plugin.AbstractUIPlugin
				.imageDescriptorFromPlugin(pluginId, checkboxOnIconPath);
		ImageDescriptor offDescriptor = org.eclipse.ui.plugin.AbstractUIPlugin
				.imageDescriptorFromPlugin(pluginId, checkboxOffIconPath);

		if (onDescriptor == null)
			throw new NullPointerException("Problem with loading image ("
					+ checkboxOnIconPath + ") from plugin '" + pluginId + "'.");
		if (offDescriptor == null)
			throw new NullPointerException("Problem with loading image ("
					+ checkboxOffIconPath + ") from plugin '" + pluginId + "'.");

		checkboxOnImage = onDescriptor.createImage();
		checkboxOffImage = offDescriptor.createImage();

		if (checkboxOnImage == null)
			throw new NullPointerException("\"Checkbox ON\" image used in Annotation Properties View wasn't loaded.");
		if (checkboxOffImage == null)
			throw new NullPointerException("\"Checkbox OFF\" image used in Annotation Properties View wasn't loaded.");
	}
	
	public List<TreeItem> getAllAnnotations() {
		List<TreeItem> annotations = new ArrayList<TreeItem>();
		
		List<TreeItem> items = new DefaultTree().getItems();
		for (int i=0; i < items.size(); i++) {
			annotations.add(items.get(i));
		}

		return annotations;
	}
	
	/**
	 * Returns {@link TreeItem} representing annotation with the specified name
	 */
	public TreeItem getAnnotation(String annotationName) {
		for (TreeItem ti : getAllAnnotations()) {
			if (ti.getText().equals(annotationName)) {
				return ti;
			}
		}
		throw new IllegalArgumentException("Annotation with name '" + annotationName + "' is not present");
	}

	/**
	 * Values = children
	 */
	public List<TreeItem> getAnnotationValues(TreeItem annotation) {
		List<TreeItem> annotations = new ArrayList<TreeItem>();
		
		List<TreeItem> items = annotation.getItems();
		for (int i=0; i < items.size(); i++) {
			annotations.add(items.get(i));
		}
		
		return annotations;

	}
	
	/**
	 * Identify the state of checkbox in the specified {@link TreeItem}
	 * 
	 * @param item
	 * @return true if checkbox in specified row is checked (checkbox is on),
	 * 				false if the checkbox is unchecked (checkbox is off)
	 */
	public boolean isAnnotationActive(final TreeItem item) {
		if(item == null)
			throw new NullPointerException("item");
		return Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				// get the image from the first column
				final Image image = item.getSWTWidget().getImage(1);
				if(image == null)
					throw new IllegalArgumentException("TreeItem \"" + item + "\" is without checkbox image.");
				// compare with on and off
				if(image.equals(checkboxOnImage) || compare(image, checkboxOnImage)) {
					return true;
				} else if(image.getImageData().equals(checkboxOffImage) || compare(image, checkboxOffImage)) {
					return false;
				}
				throw new IllegalArgumentException("TreeItem \"" + item + "\" has unrecognized image "
						+ "representation of checkbox.");
			}
		});
	}	
	
	public List<TreeItem> getAllActiveAnnotation() {
		return getAllAnnotation(true);
	}
	
	public List<TreeItem> getAllDeactiveAnnotation() {
		return getAllAnnotation(false);
	}
	
	private List<TreeItem> getAllAnnotation(boolean active) {
		List<TreeItem> selectedAnnotations = new ArrayList<TreeItem>();
		List<TreeItem> allAnnotations = getAllAnnotations();
		for (TreeItem annotation : allAnnotations) {
			if (isAnnotationActive(annotation) == active) {
				selectedAnnotations.add(annotation);
			}
		}
		return selectedAnnotations;
	}
	
	public void activateAnnotation(TreeItem annotation) {
		if(isAnnotationActive(annotation) == false)
			changeStateOfAnnotation(annotation);
	}

	public void deactivateAnnotation(TreeItem annotation) {
		if(isAnnotationActive(annotation))
			changeStateOfAnnotation(annotation);
	}

	public void changeAnnotationParamValue(TreeItem annotation, String param, String newValue) {
		for (TreeItem parameter : getAnnotationValues(annotation)) {
			if (parameter.getText().equals(param)) {
				parameter.select();
				RedDeerHelper.click(parameter, 1);
				new DefaultText().setText(newValue);
				RedDeerHelper.click(parameter, 0);
				return;
			}
		}
		throw new IllegalArgumentException("There is not parameter of \"" + annotation.getText() + "\""
				+ " with value \"" + param + "\"");
	}
	
	private void changeStateOfAnnotation(TreeItem annotation) {
		annotation.select();
		RedDeerHelper.click(annotation, 1);
	}
	
	/**
	 * Takes two Images and compare it by pixels.
	 * 
	 * This method was needed because method equals of class Image and ImageData didn't worked.
	 * 
	 * @param image1
	 * @param image2
	 * @return true if the images has the same pixel representation
	 */
	private boolean compare(Image image1, Image image2) {
		if(image1 == null)
			throw new NullPointerException("image1");
		if(image2 == null)
			throw new NullPointerException("image2");
		if(! image1.getBounds().equals(image2.getBounds())){
			return false;
		}
		ImageData image1Data = image1.getImageData();
		ImageData image2Data = image2.getImageData();
		for(int x=0;x<image1Data.width;x++) {
			for(int y=0;y<image1Data.height;y++) {
				if(image1Data.getPixel(x, y) != image2Data.getPixel(x, y)) {
					return false;
				}
			}
		}
		return true;
	}

}
