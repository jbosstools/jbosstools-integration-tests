/*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.editor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Data Representation for Menu Entity
 * @author vlado pakan
 *
 */
public class MenuItemEntity {
  private String label = null;
  private List<MenuItemEntity> children = null;
  
  public MenuItemEntity(String label) {
    super();
    this.label = label;
    this.children = null;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((children == null) ? 0 : children.hashCode());
    result = prime * result + ((label == null) ? 0 : label.hashCode());
    return result;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    MenuItemEntity other = (MenuItemEntity) obj;
    if (children == null) {
      if (other.children != null)
        return false;
    } else if(other.children == null){
      return false;
    }
    else {
      if (children.size() == other.children.size()){
        Iterator<MenuItemEntity> it = children.iterator();
        Iterator<MenuItemEntity> itOther = other.children.iterator();
        while (it.hasNext()){
          if (!it.next().equals(itOther.next())){
            return false;
          }
        }
      }
      else {
        return false;
      }
    }
    if (label == null) {
      if (other.label != null)
        return false;
    } else if (!label.equals(other.label))
      return false;
    return true;
  }
  
  public String getLabel() {
    return label;
  }
  
  public void setLabel(String label) {
    this.label = label;
  }
  
  public List<MenuItemEntity> getChildren() {
    return children;
  }
  
  public void setChildren(List<MenuItemEntity> children) {
    this.children = children;
  }

  @Override
  public String toString() {
    return "MenuItemEntity [label=" + label + "]\nChildren:" + childrenToString(0);
  }
  
  public String childrenToString (int level){
    char[] fill = new char[level];
    Arrays.fill(fill, ' ');
    StringBuffer sb = new StringBuffer(new String(fill));
    sb.append(getLabel());
    sb.append("\n");
    if (children != null){
      level +=2;
      for (MenuItemEntity child : children){
        sb.append(child.childrenToString(level));
      }
    }
    
    return sb.toString();
    
  }
  
}
