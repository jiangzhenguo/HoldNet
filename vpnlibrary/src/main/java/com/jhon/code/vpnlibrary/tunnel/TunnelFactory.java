package com.jhon.code.vpnlibrary.tunnel;



import com.jhon.code.vpnlibrary.session.NatSession;
import com.jhon.code.vpnlibrary.session.NatSessionManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * creater : Jhon
 * time : 2018/12/20 0020
 */
public class TunnelFactory {


    public static Tunnel wrap(SocketChannel channel, Selector selector) {
        Tunnel tunnel = new RawTunnel(channel, selector);
        NatSession session = NatSessionManager.getSession((short) channel.socket().getPort());
        if (session != null) {
            tunnel.setIsHttpRequest(session.IsHttpsSession);
        }
        return tunnel;
    }

    public static Tunnel createTunnelByConfig(InetSocketAddress destAddress, Selector selector) throws IOException {
        //TODO 这里只是简单创建一个RawTunnel，日后可以根据代理类型创建不同的Tunnel
        return new RemoteTunnel(destAddress, selector);
    }

}
