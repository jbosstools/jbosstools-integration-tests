/*******************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.uiutils.views;

import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swtbot.swt.finder.results.BoolResult;
import org.eclipse.swtbot.swt.finder.results.IntResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.view.ViewBase;
import org.jboss.tools.ws.ui.bot.test.annotation.TreeAnnotationItem;

/**
 * 
 * @author jjankovi, rrabara
 * @version 20130920
 */
public class AnnotationPropertiesView extends ViewBase {

	// in tree there are images which represents states of checkbox (checked/unchecked)
	/**
	 * Image of checked checkbox
	 * 
	 * Based on org.eclipse.jst.ws.jaxws.ui in JBDS 7.1.0.Alpha1
	 */
	private Image trueImage;
	
	/**
	 * Image of unchecked checkbox
	 * Based on org.eclipse.jst.ws.jaxws.ui in JBDS 7.1.0.Alpha1
	 */
	private Image falseImage;
	
	public AnnotationPropertiesView() {
		viewObject = ActionItem.View.JAXWSAnnotationProperties.LABEL;
		
		// load images used in tree as representation of checkboxes's states (checked = trueImage, unchecked = falseImage)
		final String pluginId = "org.eclipse.jst.ws.jaxws.ui";
		
		final String trueImagePath = "icons/obj16/true.gif";
		final String falseImagePath = "icons/obj16/false.gif";
		
		ImageDescriptor trueImageDescriptor = org.eclipse.ui.plugin.AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, trueImagePath);
		ImageDescriptor falseImageDescriptor = org.eclipse.ui.plugin.AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, falseImagePath);
		
		if(trueImageDescriptor == null)
			throw new NullPointerException("Problem with loading image ("+trueImagePath+") from plugin '"+pluginId+"'!");
		if(falseImageDescriptor == null)
			throw new NullPointerException("Problem with loading image ("+falseImagePath+") from plugin '"+pluginId+"'!");
		
		trueImage = trueImageDescriptor.createImage();		
		falseImage = falseImageDescriptor.createImage();
		
		if(trueImage == null)
			throw new NullPointerException("(true) image of checkbox used in annotation view wasn't loaded!");		
		if(falseImage == null)
			throw new NullPointerException("(false) image of checkbox used in annotation view wasn't loaded!");
	}

	public List<TreeAnnotationItem> getAllAnnotations() {

		List<TreeAnnotationItem> annotations = new ArrayList<TreeAnnotationItem>();
		SWTBotTree annotationViewTree = null;

		annotationViewTree = show().bot().tree();
		SWTBotTreeItem[] items = annotationViewTree.getAllItems();
		for (int i=0;i<items.length;i++) {
			annotations.add(new TreeAnnotationItem(items[i], i));
		}

		return annotations;
	}
	
	public TreeAnnotationItem getAnnotation(String annotationName) {
		
		for (TreeAnnotationItem annotation : getAllAnnotations()) {
			if (annotation.getText().equals(annotationName)) {
				return annotation;
			}
		}
		return null;
	}

	public List<SWTBotTreeItem> getAnnotationValues(TreeAnnotationItem annotation) {

		List<SWTBotTreeItem> annotationValues = new ArrayList<SWTBotTreeItem>();
		
		SWTBotTreeItem parent = annotation.getTreeItem();
		parent.expand();
		
		for (SWTBotTreeItem ti : parent.getItems()) {
			annotationValues.add(ti);
		}
		return annotationValues;

	}

	/**
	 * Identify the state of checkbox in specified TreeAnnotationItem.
	 * 
	 * @param row
	 * @return true if checkbox in specified row is checked, false otherwise
	 */
	public boolean isAnnotationActive(final TreeAnnotationItem item) {
		return isAnnotationActive(item.getRow());
	}
	
	/**
	 * Identify the state of checkbox in specified row.
	 * 
	 * @param row
	 * @return true if checkbox in specified row is checked, false otherwise
	 */
	private boolean isAnnotationActive(final int row) {
		return syncExec(new BoolResult() {
			@Override
			public Boolean run() {
				// get the image from the first column
				final Image image = show().bot().tree().widget.getItem(row).getImage(1);								
				if(image.equals(trueImage) || compare(image, trueImage))
					return true;
				else if(image.getImageData().equals(falseImage) || compare(image, falseImage))
					return false;
				throw new IllegalStateException("Unrecognized image representation of checkbox!");
			}
		});
	}	
	
	public List<TreeAnnotationItem> getAllActiveAnnotation() {
		return getAllAnnotation(true);
	}
	
	public List<TreeAnnotationItem> getAllDeactiveAnnotation() {		
		return getAllAnnotation(false);
	}
	
	private List<TreeAnnotationItem> getAllAnnotation(boolean active) {
		List<TreeAnnotationItem> selectedAnnotations = new ArrayList<TreeAnnotationItem>();
		List<TreeAnnotationItem> allAnnotations = getAllAnnotations();
		for (int row=0;row<allAnnotations.size();row++) {
			if (isAnnotationActive(row) == active) {
				selectedAnnotations.add(allAnnotations.get(row));
			}
		}
		return selectedAnnotations;
	}
	
	public void activateAnnotation(TreeAnnotationItem annotation) {
		if(isAnnotationActive(annotation) == false)
			changeStateOfAnnotation(annotation);
	}

	public void deactivateAnnotation(TreeAnnotationItem annotation) {
		if(isAnnotationActive(annotation))
			changeStateOfAnnotation(annotation);
	}

	public TreeAnnotationItem changeAnnotationParamValue(TreeAnnotationItem annotation, String param, String newValue) {
		for (SWTBotTreeItem parameter : getAnnotationValues(annotation)) {
			if (parameter.getText().equals(param)) {
				annotation.getTreeItem().getNode(param).select();
				parameter.click(1);
				show().bot().text().setText(newValue);
				parameter.click(0);
				break;
			}
		}
		return annotation;
	}
	
	private void changeStateOfAnnotation(TreeAnnotationItem annotation) {
		SWTBotTreeItem treeItem = annotation.getTreeItem();
		show().bot().tree().select(treeItem.getText());		
		annotation.setTreeItem(treeItem.click(1));
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
