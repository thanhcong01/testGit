/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.login;

import com.ftu.admin.consumer.entity.PrivilegeEntity;
import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.utils.ResourceBundleUtil;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author E5420
 */
public class ValidateUser {

    public static boolean checkPermission(List<SiteMapEntity> ls, String url) {
        boolean m = checkNologin(url);
        if (m) {
            return m;
        }
        for (SiteMapEntity d : ls) {
            if (d.getStrUrl() != null && url.toString().contains(d.getStrUrl().substring(0, (d.getStrUrl() + ".").indexOf(".")))) {
                return true;
            }
            boolean k = checkSub(d, url);
            if (k) {
                return k;
            }
        }
        return false;
       // return true;

    }

    private static boolean checkNologin(String url) {
        String url1 = ResourceBundleUtil.getString("url_path_nologin");
        String[] arrUrl;
        String regex = ",";
        arrUrl = url1.split(regex);
        boolean k = false;
        if (url.endsWith("/") || url.contains("/javax.faces.resource/")) {
            k = true;
        } else {
            for (int i = 0; i < arrUrl.length; i++) {
                if (!arrUrl[i].isEmpty() && !arrUrl[i].trim().isEmpty() && url.indexOf(arrUrl[i].trim()) >= 0) {
                    k = true;
                    break;
                }
            }
        }
        return k;
    }

    private static boolean checkSub(SiteMapEntity ls, String url) {
        for (SiteMapEntity d : ls.getChildrent()) {
            if (d.getStrUrl() != null && url.toString().contains(d.getStrUrl().substring(0, (d.getStrUrl() + ".").indexOf(".")))) {
                return true;
            } else {
                if (d.getChildrent() != null) {
                    for (SiteMapEntity sb : d.getChildrent()) {
                        if (sb.getStrUrl() != null && url.toString().contains(sb.getStrUrl().substring(0, (sb.getStrUrl() + ".").indexOf(".")))) {
                            return true;
                        } else {
                            boolean k = checkSub(sb, url);
                            if (k) {
                                return k;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean checkRole(List<SiteMapEntity> listAction,String rqUrl, String roleKey) {
        boolean rs = false;
        SiteMapEntity se = null;

            String[] s = rqUrl.split("/");
            if (s.length > 0) {
                String url = s[s.length - 1].replace(".jsf", "").replace(".xhtml", "");

                for (SiteMapEntity d : listAction) {
                    se = getSiteMapEntity(d, url);
                    if (se != null) {
                        break;
                    }
                }
            }

        if (se != null) {
            for (PrivilegeEntity en : se.getPrivileges()) {
                if (en.getCode().equals(roleKey)) {
                    return true;
                }
            }
        }
        return rs;
    }

    public static SiteMapEntity getSiteMapEntity(SiteMapEntity d, String url) {
        SiteMapEntity rs = null;
        String url2 = "";
        if (d.getStrUrl() != null) {
            String s[] = d.getStrUrl().split("/");
            url2 = s[s.length - 1].replace(".jsf", "").replace(".xhtml", "");
        }
        if (url2.equals(url) && d.getStrUrl() != null) {
            return d;
        } else {
            for (SiteMapEntity itm : d.getChildrent()) {
                rs = getSiteMapEntity(itm, url);
                if (rs != null) {
                    return rs;
                }
            }
        }
        return rs;
    }
}
