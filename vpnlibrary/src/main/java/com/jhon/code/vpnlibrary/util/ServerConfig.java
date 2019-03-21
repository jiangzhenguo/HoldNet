package com.jhon.code.vpnlibrary.util;

import android.net.VpnService;

import com.jhon.code.vpnlibrary.router.VpnRoutService;

import java.net.Socket;

/**
 * creater : Jhon
 * time : 2018/12/14 0014
 */
public class ServerConfig {

    VpnService mServer;

    private static ServerConfig min;

    private static VpnRoutService  mRouter;

    public static ServerConfig instance(){
        if(min == null){
            min = new ServerConfig();
        }
        return min;
    }

    private ServerConfig(){

    }


    public ServerConfig setServer(VpnService server){
        mServer = server;
        return this;
    }

    public ServerConfig setRouter(VpnRoutService mRouter){
        this.mRouter = mRouter;
        return this;
    }

    public VpnRoutService getRouter(){
        return mRouter;
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
