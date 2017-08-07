package com.ftu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;

public class EncryptedResourceBundleUtil {

    private static String defaultResource = "encryptedProperties";
    private static Logger log = Logger.getLogger(EncryptedResourceBundleUtil.class);
    private static HashMap<String, ResourceBundle> resourceBundles = new HashMap();

    public static ResourceBundle getBundle(String resource) {
        if (!resourceBundles.containsKey(resource)) {
            ResourceBundle rb = new EncryptedResourceBundle(resource);
            resourceBundles.put(resource, rb);
        }
        return (ResourceBundle) resourceBundles.get(resource);
    }

    public static String getString(String resource, String key) {
        return getBundle(resource).getString(key);
    }

    public static String getString(String key) {
        return getBundle(defaultResource).getString(key);
    }

    /**
     * @deprecated
     */
    public static String getResource(String resource, String key) {
        return getBundle(resource).getString(key);
    }

    private static class EncryptedResourceBundle extends ListResourceBundle {

        private String resourceName = "";
        private String[][] contents = {new String[0]};

        public EncryptedResourceBundle(String resourceName) {
            this.resourceName = resourceName;
        }

        public Object[][] getContents() {
            String filePath = this.resourceName;
            InputStream stream = null;
            try {
                ArrayList temps = new ArrayList();

                boolean hasPros = false;
                while (!hasPros) {
                    String s = filePath;
                    String stripped = s.startsWith("/") ? s.substring(1) : s;
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if (classLoader != null) {
                        stream = classLoader.getResourceAsStream(stripped);
                        hasPros = stream != null;
                    }
                    if (stream == null) {
                        stream = ResourceBundleUtil.class.getResourceAsStream(s);
                        hasPros = stream != null;
                    }
                    if (stream == null) {
                        stream = ResourceBundleUtil.class.getClassLoader().getResourceAsStream(stripped);
                        hasPros = stream != null;
                    }
                    if (stream == null) {
                        File inputFile = new File(s);
                        if (inputFile.exists()) {
                            stream = new FileInputStream(inputFile);
                            hasPros = stream != null;
                        }
                    }
                }
                if (stream == null) {
                    throw new FileNotFoundException(String.format("File %s not found", new Object[]{this.resourceName}));
                }

                String decryptString = EncryptDecryptUtils.decryptFile(stream);
                String[] properties = decryptString.split("\r\n");
                EncryptedResourceBundleUtil.log.info("Number of encrypted properties: " + properties.length);
                for (String property : properties) {
                    String[] temp = property.split("=", 2);
                    if (temp.length == 2) {
                        temps.add(temp);
                    }
                }
                this.contents = new String[temps.size()][2];
                for (int i = 0; i < temps.size(); i++) {
                    this.contents[i][0] = ((String[]) temps.get(i))[0];
                    this.contents[i][1] = ((String[]) temps.get(i))[1];
                }
                EncryptedResourceBundleUtil.log.info("Done reading encrypted file :" + filePath);

                return this.contents;
            } catch (Exception ex) {
                EncryptedResourceBundleUtil.log.error("Error while reading encrypted file :" + filePath);
                EncryptedResourceBundleUtil.log.error(ex);
            }
            return this.contents;
        }
    }
}