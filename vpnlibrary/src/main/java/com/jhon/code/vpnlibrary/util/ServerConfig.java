package com.jhon.code.vpnlibrary.util;

import android.net.VpnService;

import java.net.Socket;

/**
 * creater : Jhon
 * time : 2018/12/14 0014
 */
public class ServerConfig {

    VpnService mServer;

    private static  ServerConfig min;

    public static ServerConfig instance(){
        if(min == null){
            min = new ServerConfig();
        }
        return min;
    }

    private ServerConfig(){

    }


    public void setServer(VpnService server){
        mServer = server;
    }


    public boolean protect(Socket socket){
        if(mServer != null) {
            mServer.protect(socket);
            return true;
        } else {
            return false;
        }
    }

}
