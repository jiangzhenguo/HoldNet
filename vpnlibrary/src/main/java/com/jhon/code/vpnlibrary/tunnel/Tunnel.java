package com.jhon.code.vpnlibrary.tunnel;

import com.jhon.code.vpnlibrary.session.NatSession;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

/**
 * creater : Jhon
 * time : 2018/12/19 0019
 * 用来传递数据的通道
 */
public interface Tunnel {

    void connect(InetSocketAddress destAddress);

    void onConnected(ByteBuffer buffer);

    void onConnectable();

    void onReadable(SelectionKey key);

    void beginReceived() throws Exception;//接受数据前

    void afterReceived(ByteBuffer buffer);//接受数据后

    void onWriteable(SelectionKey key);

    void beforeSend(ByteBuffer buffer);

    /**
     * 能进行通讯
     * @return
     */
    boolean isTunnelEstablished();

    /**
     * 写入数据
     * @param buffer
     * @param copy
     * @return
     */
    boolean write(ByteBuffer buffer, boolean copy) throws Exception;

    void dispose(boolean isBrother);

    void setBrotherTunnel(Tunnel brotherTunnel);

    void setRequest(NatSession request);

    Tunnel getBrotherTunnel();

    void setIsHttpRequest(boolean isHttpRequest);

    boolean getIsHttpRequest();
}
