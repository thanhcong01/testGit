/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.exportpdf;

import java.util.List;

/**
 *
 * @author Vu Manh Ha
 */
public class GroupModel {
    String groupName;
    List lstItems;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List getLstItems() {
        return lstItems;
    }

    public void setLstItems(List lstItems) {
        this.lstItems = lstItems;
    }

}
