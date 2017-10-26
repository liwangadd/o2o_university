package com.yijianzhai.jue.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;;

public class ConnectionConfigReader {
    public static final String DEFAULT_REMOTE_HOST = "http://115.28.145.218:8080/jueserver/";
    private static final String REMOTE_HOST_KEY = "remoteHost";
    
    private Properties connectionConfig = null;
    private String remoteServer = null;

    public String getServer(){
        if(remoteServer == null){
            getProperties();
        }

        if(connectionConfig != null){
            remoteServer = connectionConfig.getProperty(REMOTE_HOST_KEY);
        }
        
        return remoteServer;
    }

    public Properties getProperties(){
           if(connectionConfig != null){
               return connectionConfig;
           }

           InputStream in = null;
           try {
               in = ConnectionConfigReader.class.getResourceAsStream("/assets/connectionconfig.properties");
               connectionConfig = new Properties();
               connectionConfig.load(in);
           } catch (Exception e) {
               e.printStackTrace();    
               connectionConfig = null;
           } finally {
               try {
                   in.close();
               } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
               } 
           }

           return connectionConfig;
           
        }
    }
