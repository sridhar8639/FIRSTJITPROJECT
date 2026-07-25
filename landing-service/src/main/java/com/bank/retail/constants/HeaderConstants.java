package com.bank.retail.constants;

public final class HeaderConstants {
    
    // Private constructor to prevent instantiation
    private HeaderConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    // Header constants
    public static final String SERVICE_ID_HEADER = "serviceId";
    public static final String CHANNEL_HEADER = "channel";
    public static final String UNIT_HEADER = "unit";
    public static final String IP_ADDRESS_HEADER = "ipAddress";
    public static final String DEVICE_ID_HEADER = "deviceId";
    public static final String ACCEPT_LANGUAGE_HEADER = "accept-language";
    public static final String MODULE_ID = "moduleId";

    public static final String PARTNER_ID_HEADER = "partnerId";
    
    
    public static final String SUB_MODULE_ID   = "subModuleId";     
    public static final String SCREEN_ID       = "screenId";        
}
