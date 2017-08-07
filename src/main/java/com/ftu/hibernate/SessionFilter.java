// Decompiled by DJ v3.11.11.95 Copyright 2009 Atanas Neshkov  Date: 8/15/2012 3:00:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VsaFilter.java
package com.ftu.hibernate;

import com.ftu.admin.consumer.AuthenticationConsumer;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.login.ValidateUser;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.ResourceBundleUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.*;

public class SessionFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {
    }
    private final String timeoutPage = "/login.xhtml";

    private final String loginPage = "/login.xhtml";
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        boolean errorWhenExcuteAction = false;
        try {
            HttpServletRequest req = null;
            HttpServletResponse res = null;
            if (request instanceof HttpServletRequest) {
                req = (HttpServletRequest) request;
            }

            if (response instanceof HttpServletResponse) {
                res = (HttpServletResponse) response;
            }

            String url = ResourceBundleUtil.getString("url_path_nologin");
            String[] arrUrl;
            String regex = ",";
            arrUrl = url.split(regex);
//            if (!(req.getRequestURL().toString().endsWith(req.getContextPath() + "/")
//                    || req.getRequestURL().toString().endsWith("/login.xhtml")
//                    || req.getRequestURL().toString().endsWith("/login.jsf"))) {
//                HttpSession session = req.getSession();
//                SessionData sd = (SessionData) session.getAttribute("username");
//                if (sd != null && sd.getIdentityEntity().getStaffId() != null ) {
//                    StaffDAO stDAO = new StaffDAO();
//                    Staff staff = stDAO.findByStaffId(sd.getIdentityEntity().getStaffId());
//                    boolean dkReject  = false;
//                    Date dateCurrent = DateTimeUtils.convertDateTimeToDate(new Date());
//                    if(staff!=null && InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(staff.getStaffStatus())){
//                        dkReject = true;
//                    }else if(staff != null && staff.getEndDate()!= null
//                            && dateCurrent.compareTo(staff.getEndDate()) > 0){
//                        dkReject = true;
//                    } else if(staff != null && staff.getStartDate()!= null
//                            && dateCurrent.compareTo(staff.getStartDate()) < 0){
//                        dkReject = true;
//                    } else if(staff != null){
//                        ShopDAO sDAO = new ShopDAO();
//                        Shop s = (sDAO.findById(staff.getShopId()));
//                        if(s!=null && InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(s.getShopStatus())){
//                            dkReject = true;
//                        }
//                    }
//
//                    if ( dkReject && isAJAXRequest(req)) {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"")
//                                .append(req.getContextPath() + timeoutPage)
//                                .append("\"></redirect></partial-response>");
//                        res.setHeader("Cache-Control", "no-cache");
//                        res.setCharacterEncoding("UTF-8");
//                        res.setContentType("text/xml");
//                        PrintWriter pw = response.getWriter();
//                        pw.println(sb.toString());
//                        pw.flush();
//                        pw.close();
//                        session.setAttribute("username",null);
//                        return;
//                    }else if(dkReject) {
//                        res.sendRedirect(req.getContextPath() + "/login.jsf");
//                        session.setAttribute("username",null);
//                        return;
//                    }
//                }
//            }
            if (req != null && !"POST".equals(req.getMethod())) {
                //Nghiepnc begin
                boolean k = false;
                if (req.getRequestURL().toString().endsWith(req.getContextPath() + "/")
                        || req.getRequestURL().toString().endsWith("/login.xhtml")
                        || req.getRequestURL().toString().endsWith("/login.jsf")
                        || req.getRequestURL().toString().contains("/javax.faces.resource/")) {
                    if(req.getRequestURL().toString().endsWith(req.getContextPath() + "/")
                            || req.getRequestURL().toString().endsWith("/login.xhtml")
                            || req.getRequestURL().toString().endsWith("/login.jsf")){
                        HttpSession session = req.getSession();
                        SessionData sd=(SessionData)session.getAttribute("username");
                        if(sd != null && sd.getTransEntity().getChanglePass()){
                            res.sendRedirect(req.getContextPath() + "/login.xhtml");
                        }else if ( sd != null) {
                            res.sendRedirect(req.getContextPath() + "/index.xhtml");
                        }else {
                            chain.doFilter(request, response);
                        }
                    }else {
                        chain.doFilter(request, response);
                    }
                    k = true;
                } else {
                    for (int i = 0; i < arrUrl.length; i++) {
                        if (!arrUrl[i].isEmpty() && !arrUrl[i].trim().isEmpty() && req.getRequestURL().indexOf(req.getContextPath() + arrUrl[i].trim()) >= 0) {
                            chain.doFilter(request, response);
                            k = true;
                            break;
                        } 
                    }
                }
                if (!k) {
                    HttpSession session = req.getSession();

                        SessionData sd=(SessionData)session.getAttribute("username");
                    if ( sd == null) {
                        session.setAttribute("urlBlock", req.getRequestURL());
                        res.sendRedirect(req.getContextPath() + "/login.jsf");
                    } else
                        if (ValidateUser.checkPermission(sd.getSitemaps(),req.getRequestURL().toString())) {
                        chain.doFilter(request, response);
                    } else {
                        res.sendRedirect(req.getContextPath() + "/block-access.jsf");
                    }
                }
            } else {
                if (!(req.getRequestURL().toString().endsWith(req.getContextPath() + "/")
                        || req.getRequestURL().toString().endsWith("/login.xhtml")
                        || req.getRequestURL().toString().endsWith("/login.jsf"))) {
                    HttpSession session = req.getSession();
                    SessionData sd=(SessionData)session.getAttribute("username");
                    if ( sd == null) {
                        if (isAJAXRequest(req)) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"")
                                    .append(req.getContextPath() + timeoutPage)
                                    .append("\"></redirect></partial-response>");
                            res.setHeader("Cache-Control", "no-cache");
                            res.setCharacterEncoding("UTF-8");
                            res.setContentType("text/xml");
                            PrintWriter pw = response.getWriter();
                            pw.println(sb.toString());
                            pw.flush();
                            pw.close();
                            return;
                        }
//                        res.sendRedirect(req.getContextPath() + "/login.jsf");
                    }else {
                        chain.doFilter(request, response);
                    }
                }else {
                    chain.doFilter(request, response);
                }
            }
        } catch (IOException | ServletException en) {
            errorWhenExcuteAction = true;
        } catch (Exception ex) {
            errorWhenExcuteAction = true;
        } finally {
            if (!errorWhenExcuteAction) {
                try {
                    com.ftu.hibernate.HibernateUtil.commitCurrentSessions();
                } catch (Exception ex) {
                    Logger.getLogger(SessionFilter.class.getName()).log(
                            Level.SEVERE, null, ex);
                } finally {
                    try {
                        com.ftu.hibernate.HibernateUtil.closeCurrentSessions();
                    } catch (Exception ex) {
                        Logger.getLogger(SessionFilter.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
    private boolean isAJAXRequest(HttpServletRequest request) {
        boolean check = false;
        String facesRequest = request.getHeader("Faces-Request");
        if (facesRequest != null && facesRequest.equals("partial/ajax")) {
            check = true;
        }
        return check;
    }
    @Override
    public void destroy() {
    }
}
