package com.jhon.code.vpnlibrary.tunnel;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * creater : Jhon
 * time : 2018/12/20 0020
 */
public class RawTunnel extends BaseTunnel {

    public RawTunnel(SocketChannel innerChannel, Selector selector) {
        super(innerChannel, selector);
    }

    public RawTunnel(InetSocketAddress serverAddress, Selector selector) throws IOException {
        super(serverAddress, selector);
    }

    @Override
    public void onConnected(ByteBuffer buffer) {
        try {
            this.beginReceived();
            getBrotherTunnel().beginReceived();
        }catch (Exception e){
            Log.d(TAG,e.toString());
        }
    }

    @Override
    public void afterReceived(ByteBuffer buffer) {

    }

    @Override
    public void beforeSend(ByteBuffer buffer) {

    }

    @Override
    public boolean isTunnelEstablished() {
        return true;
    }

    @Override
    public void setIsHttpRequest(boolean isHttpRequest) {

    }
}
