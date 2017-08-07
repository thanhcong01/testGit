package com.ftu.core.dbbo;

import com.ftu.utils.DateTimeUtils;
import java.util.Date;

public class DateTimeRule {

    private Date startDate;
    private Date endDate;
    private String sessionName;

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndDate(String endDate)
            throws Exception {
        this.endDate = DateTimeUtils.convertStringToDate(endDate);
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(String startDate)
            throws Exception {
        this.startDate = DateTimeUtils.convertStringToDate(startDate);
    }
}
