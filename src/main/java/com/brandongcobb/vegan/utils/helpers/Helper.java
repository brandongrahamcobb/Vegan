package com.brandongcobb.vegan.utils.helpers;

import java.util.Properties;

public class Helper {
    private static final Properties defaultProperties = new Properties();
    
    private Helper() {
        setDefaultProperties();
    }
    
    public void setDefaultProperties() {
        Properties props = new Properties();
        propertiesAdd("DB_NAME", "vegan");
        propertiesAdd("H2_USERNAME", "sa");
        propertiesAdd("H2_PASSWORD", "password");
        propertiesAdd("H2_HOST", "localhost");
        propertiesAdd("H2_PORT", "1433");
        
        // PostgreSQL properties
        propertiesAdd("POSTGRES_HOST", "localhost");
        propertiesAdd("POSTGRES_DATABASE", "vegan");
        propertiesAdd("POSTGRES_USER", "vegan");
        propertiesAdd("POSTGRES_PASSWORD", "password");
    }
    
    public String getPasswordName() {
        return (String) getProperty("DB_NAME");
    }
    
    public String getUsername() {
        return (String) getProperty("H2_USERNAME");
    }
    
    public String getPassword() {
        return (String) getProperty("H2_PASSWORD");
    }
    
    public String getHost() {
        return (String) getProperty("H2_HOST");
    }
    
    public int getPort() {
        return Integer.parseInt((String) getProperty("H2_PORT"));
    }
    
    public String getDatabase() {
        return (String) getProperty("DB_NAME");
    }
    
    private void propertiesAdd(String name, String value) {
        defaultProperties.setProperty(name, value);
    }
    
    private Properties getDefaultProperties() {
        return new java.util.Properties();
    }
    
    private boolean isPropertyDefined(String name) {
        return defaultProperties.get(name) != null;
    }
    
    private Object getProperty(String name) {
        return defaultProperties.get(name) == null ? null : getPropertyHelper(name);
    }
    
    private Object getPropertyHelper(String name) {
        return defaultProperties.getProperty(name);
    }

}
