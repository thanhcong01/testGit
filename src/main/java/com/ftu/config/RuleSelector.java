package com.ftu.config;

import com.ftu.core.dbbo.DateTimeRule;
import com.ftu.core.dbbo.LocationRule;
import com.ftu.core.dbbo.ModRule;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

public class RuleSelector {

    private HashMap locations = new HashMap();

    private HashMap mods = new HashMap();

    private HashMap times = new HashMap();

    public void addLocationRule(LocationRule location) {
        this.locations.put(location.getName().toLowerCase(), location);
    }

    public void addModRule(ModRule modRule) {
        this.mods.put(Integer.valueOf(modRule.getModValue()), modRule);
    }

    public void addDateTimeRule(DateTimeRule dateTimeRule) {
        this.times.put(dateTimeRule.getSessionName(), dateTimeRule);
    }

    public HashMap getLocations() {
        return this.locations;
    }

    public void setLocations(HashMap locations) {
        this.locations = locations;
    }

    public HashMap getMods() {
        return this.mods;
    }

    public void setMods(HashMap mods) {
        this.mods = mods;
    }

    public HashMap getTimes() {
        return this.times;
    }

    public void setTimes(HashMap times) {
        this.times = times;
    }

    public void checkRule() {
    }

    public String getValueLocation(String key) {
        LocationRule tmp = (LocationRule) this.locations.get(key.toLowerCase());
        return tmp.getSessionName();
    }

    public String getValueMod(String key) {
        ModRule tmp = (ModRule) this.mods.get(Integer.valueOf(Integer.parseInt(key)));
        return tmp.getSessionName();
    }

    public String getValueDateTimeRule(Date d) {
        Collection cols = this.times.values();
        Iterator iter = cols.iterator();
        while (iter.hasNext()) {
            DateTimeRule dateTimeRule = (DateTimeRule) iter.next();
            if ((d.after(dateTimeRule.getStartDate())) && (d.before(dateTimeRule.getEndDate()))) {
                return dateTimeRule.getSessionName();
            }
        }
        return "";
    }
}
