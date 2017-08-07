/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.EquipmentsProfile;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author E5420
 */
@ManagedBean
@ViewScoped
public class CommonBean implements Serializable{

    private String goodsCode;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    public String getGoodsCode() {
        return goodsCode;
    }

    /**
     * @param goodsCode the goodsCode to set
     */
    public void changeGoods(Long gid) {
        EquipmentsProfile g = sessionBean.getGoods(gid.toString());
        if (g != null) {
            goodsCode = g.getProfileCode();
        }
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * @param sessionBean the sessionBean to set
     */
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }
}
