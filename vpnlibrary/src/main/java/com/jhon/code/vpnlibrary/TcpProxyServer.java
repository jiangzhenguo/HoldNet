package com.jhon.code.vpnlibrary;

import android.util.Log;


import com.jhon.code.vpnlibrary.session.NatSession;
import com.jhon.code.vpnlibrary.session.NatSessionManager;
import com.jhon.code.vpnlibrary.tunnel.Tunnel;
import com.jhon.code.vpnlibrary.tunnel.TunnelFactory;
import com.jhon.code.vpnlibrary.util.BitUtils;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * creater : Jhon
 * time : 2018/12/14 0014
 */
public class TcpProxyServer {
    private String TAG = TcpProxyServer.class.getName();

    ServerSocketChannel mServer;
    Selector mSelect;
    InetSocketAddress mAddress;

    public TcpProxyServer(int port) throws Exception{
        mSelect = Selector.open();
        mServer = ServerSocketChannel.open();
        mServer.configureBlocking(false);
        mAddress = new InetSocketAddress(port);
        mServer.socket().bind(mAddress);
        mServer.register(mSelect,SelectionKey.OP_ACCEPT);

    }


    public int port(){
        short port = (short) mServer.socket().getLocalPort();
        return  port;
    }


    public int getIp(){
        return BitUtils.ipStringToInt("10.8.0.2");
    }


    public void start(){
        new Thread(){
            @Override
            public void run() {
                try {
                    while (true) {
                        mSelect.select();
                        Iterator ite = mSelect.selectedKeys().iterator();
                        while (ite.hasNext()){

                            SelectionKey key = (SelectionKey)ite.next();
                            if(key.isValid()) {
                                if (key.isAcceptable()) {
                                    onAccepted();
                                } else if (key.isConnectable()) {
                                    ((Tunnel) key.attachment()).onConnectable();
                                } else if (key.isReadable()) {
                                    ((Tunnel)key.attachment()).onReadable(key);
                                } else if (key.isWritable()){
                                    ((Tunnel)key.attachment()).onWriteable(key);
                                }
                            }
                            ite.remove();
                        }
                    }
                } catch (Exception e){
                    Log.d(TAG,e.toString());
                }
            }
            }. start();
        }



    public void stop(){
        if(mSelect != null){
            try{
                mSelect.close();
                mSelect = null;
            } catch (Exception e){
                Log.d(TAG,e.toString());
            }
        }
        if(mServer != null){
            try {
                mServer.close();
                mServer = null;
            }catch (Exception e){

            }
        }
    }


    void onAccepted(){
         Tunnel localTunnel = null;
         try{
             SocketChannel localChannel = mServer.accept();
             localTunnel = TunnelFactory.wrap(localChannel,mSelect);
             InetSocketAddress destAddress = getDestAddress(localChannel);
             if(destAddress != null){
                 Tunnel remoteTunnel = TunnelFactory.createTunnelByConfig(destAddress,mSelect);
                 remoteTunnel.setIsHttpRequest(localTunnel.getIsHttpRequest());
                 remoteTunnel.setBrotherTunnel(localTunnel);
                 localTunnel.setBrotherTunnel(remoteTunnel);
                 remoteTunnel.connect(destAddress);
             } else {
                 localTunnel.dispose(true);
             }

         }catch (Exception e){
             if (localTunnel != null) {
                 localTunnel.dispose(true);
             }
         }
    }

    /**
     * 得到远程的ip
     * @param localChannel
     * @return
     */
    InetSocketAddress getDestAddress(SocketChannel localChannel){
        short portkey = (short)localChannel.socket().getPort();
        NatSession session = NatSessionManager.getSession(portkey);
        if(session != null){
            return new InetSocketAddress(localChannel.socket().getInetAddress(), session.RemotePort & 0xFFFF);
        } else {
            return null;
        }
    }


}
