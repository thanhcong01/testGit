package com.ftu.config;

import com.ftu.core.dbbo.DateTimeRule;
import com.ftu.core.dbbo.LocationRule;
import com.ftu.core.dbbo.ModRule;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

public class Config {

    private HashMap<String, String> sessions = new HashMap();
    private RuleSelector ruleSelector = new RuleSelector();

    public void loadInstance(String cfFile)
            throws IOException, SAXException, Exception {
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.push(this);

            digester.addObjectCreate("ConfigDBC/Session-factory", SessionFactoryElement.class);

            digester.addBeanPropertySetter("ConfigDBC/Session-factory/name", "name");
            digester.addBeanPropertySetter("ConfigDBC/Session-factory/url", "url");
            digester.addSetNext("ConfigDBC/Session-factory", "addSessionFactory");

//            digester.addObjectCreate("ConfigDBC/RuleSellector/Locations/location", LocationRule.class);
//
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Locations/location/name", "name");
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Locations/location/sesion-name", "session_name");
//            digester.addSetNext("ConfigDBC/RuleSellector/Locations/location", "addLocationRule");
//
//            digester.addObjectCreate("ConfigDBC/RuleSellector/Mods/Mod", ModRule.class);
//            digester.addSetProperties("ConfigDBC/RuleSellector/Mods/Mod");
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Mods/Mod/mod-value", "mod_value");
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Mods/Mod/session-name", "session_name");
//            digester.addSetNext("ConfigDBC/RuleSellector/Mods/Mod", "addModRule");
//
//            digester.addObjectCreate("ConfigDBC/RuleSellector/Date-times/Date-time", DateTimeRule.class);
//            digester.addCallMethod("ConfigDBC/RuleSellector/Date-times/Date-time/Start", "setStartDate", 0);
//            digester.addCallMethod("ConfigDBC/RuleSellector/Date-times/Date-time/End", "setEndDate", 0);
//            digester.addBeanPropertySetter("ConfigDBC/RuleSellector/Date-times/Date-time/session-name", "session_name");
//            digester.addSetNext("ConfigDBC/RuleSellector/Date-times/Date-time", "addDateTimeRule");
          
//            URL file = Thread.currentThread().getContextClassLoader().getResource(cfFile);
//            File inputFile = new File(URLDecoder.decode(file.getPath()));
//            if (!inputFile.exists()) {
//            }

            digester.parse(Config.class.getResourceAsStream(cfFile));
        } catch (Exception e) {
            throw e;
        }
    }

    public void addSessionFactory(SessionFactoryElement sessionFactory) {
        this.sessions.put(sessionFactory.getName().toLowerCase(), sessionFactory.getUrl());
    }

    public void addLocationRule(LocationRule loR) {
        this.ruleSelector.addLocationRule(loR);
    }

    public void addModRule(ModRule loR) {
        this.ruleSelector.addModRule(loR);
    }

    public void addDateTimeRule(DateTimeRule loR) {
        this.ruleSelector.addDateTimeRule(loR);
    }

    public HashMap getSessions() {
        return this.sessions;
    }

    public void setSessions(HashMap sessions) {
        this.sessions = sessions;
    }

    public RuleSelector getRuleSelector() {
        return this.ruleSelector;
    }
}