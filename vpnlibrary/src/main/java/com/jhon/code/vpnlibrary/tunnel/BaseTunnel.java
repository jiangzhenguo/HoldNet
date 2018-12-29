package com.jhon.code.vpnlibrary.tunnel;

import android.util.Log;


import com.jhon.code.vpnlibrary.http.HttpResponse;
import com.jhon.code.vpnlibrary.util.ServerConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * creater : Jhon
 * time : 2018/12/19 0019
 */
public abstract class BaseTunnel implements Tunnel{

    protected static String TAG = BaseTunnel.class.getName();
    final static ByteBuffer GL_BUFFER = ByteBuffer.allocate(20000);
    private Tunnel mBrotherTunnel;
    private boolean isHttpsRequest = false;
    protected  boolean isRemoteTunnel;
    private InetSocketAddress mServerAddress;
    private InetSocketAddress mDestAdress;
    private ByteBuffer mSendRemainBuffer; //发送数据缓存
    private Selector mSelector;
    private boolean mDisposed;
    private HttpResponse mHttpResponse;
    private SocketChannel mInnerChannel; //自己的Channel

    public BaseTunnel(InetSocketAddress address,Selector selector) throws IOException {
        this.mServerAddress = address;
        this.mSelector = selector;
        this.mInnerChannel = SocketChannel.open();
        this.mInnerChannel.configureBlocking(false);
    }


    public BaseTunnel(SocketChannel innerChannel, Selector selector) {
        mInnerChannel = innerChannel;
        mSelector = selector;
    }

    @Override
    public void connect(InetSocketAddress destAddress){
        try {
            if(ServerConfig.instance().protect(mInnerChannel.socket())){
                Log.d(TAG,"connect:" + destAddress.getHostName());
                mDestAdress = destAddress;
                mInnerChannel.register(mSelector,SelectionKey.OP_CONNECT,this);
                mInnerChannel.connect(mServerAddress);
            }
        } catch ( Exception e){
            Log.d(TAG,"connect:" + e.toString());
            dispose(true);
        }

    }

    @Override
    public void onConnectable() {
        try{
            if(mServerAddress != null)
                Log.d(TAG, "onConnectable : " + mServerAddress.getPort());
            if(mInnerChannel.finishConnect()){
                onConnected(GL_BUFFER);
            } else {
                if(mServerAddress != null)
                Log.d(TAG,"close :" + mServerAddress.getHostName());
                dispose(true);
            }

        } catch (Exception e){
            Log.d(TAG,"onConnectable:" + e.toString());
            dispose(true);
        }

    }


    @Override
    public void onReadable(SelectionKey key) {
       try {
           ByteBuffer buffer = GL_BUFFER;
           buffer.clear();
           int byteRead = mInnerChannel.read(buffer);
           if(byteRead > 0){
               buffer.flip();
               afterReceived(buffer);
               if(isRemoteTunnel && !isHttpsRequest){
                   if(mHttpResponse == null) {
                       if (buffer.limit() - buffer.position() > 5) {
                           ByteBuffer httpBuffer = ByteBuffer.wrap(buffer.array(), buffer.position(), buffer.limit() - buffer.position());
                           int oldPosition = httpBuffer.position();
                           byte[] firstFiveBytes = new byte[5];
                           httpBuffer.get(firstFiveBytes);
                           httpBuffer.position(oldPosition);
                           if ("HTTP/".equals(new String(firstFiveBytes))) {//HTTP报文回复
                               mHttpResponse = new HttpResponse(isHttpsRequest);
                               mHttpResponse.write(httpBuffer);
                           }
                       }
                   }else {
                       mHttpResponse.write(buffer);
                   }
                   if(mHttpResponse != null){
                       ByteBuffer httpBuffer = null;
                       httpBuffer = mHttpResponse.getBuffer();
                       if (httpBuffer != null) {
                           Log.d(TAG,"header: " + mHttpResponse.getHeaderString() + " \n" + "Body: " + mHttpResponse.getBody());
                           sendToBrother(key, httpBuffer);
                           mHttpResponse = null; //节约内存
                       }
                   } else {
                       sendToBrother(key, buffer);
                   }
               } else {
                   sendToBrother(key, buffer);
               }
           }
       } catch (Exception e){
           Log.d(TAG,"onReadable:" + e.toString());
       }
    }

    @Override
    public void setIsHttpRequest(boolean isHttpRequest) {
        this.isHttpsRequest = isHttpRequest;
    }

    @Override
    public boolean getIsHttpRequest() {
        return isHttpsRequest;
    }

    @Override
    public void beginReceived() throws Exception{
        if(mInnerChannel.isBlocking()){
            mInnerChannel.configureBlocking(false);
        }
        mInnerChannel.register(mSelector,SelectionKey.OP_READ,this);
    }

    @Override
    public boolean write(ByteBuffer buffer, boolean copyRemainData) throws Exception {
        int byteSend;
        while (buffer.hasRemaining()){
            byteSend = mInnerChannel.write(buffer);
            if(byteSend == 0){
                break;
            }
        }
        if(buffer.hasRemaining()){
             if(copyRemainData){
                 if(mSendRemainBuffer == null){
                     mSendRemainBuffer = ByteBuffer.allocate(buffer.capacity());
                 }
                 mSendRemainBuffer.clear();
                 mSendRemainBuffer.put(buffer);
                 mSendRemainBuffer.flip();
                 mInnerChannel.register(mSelector,SelectionKey.OP_WRITE,this);
             }
             return false;
        } else {
            return true;
        }
    }

    protected void sendToBrother(SelectionKey key, ByteBuffer buffer) throws Exception {
        if (isTunnelEstablished() &&  buffer.hasRemaining()) { //将读到的数据，转发给兄弟
            mBrotherTunnel.beforeSend(buffer); //发送之前，先让子类处理，例如做加密等。
            if (!mBrotherTunnel.write(buffer, true)) {
                key.cancel(); //兄弟吃不消，就取消读取事件
            }
        }
    }


    @Override
    public void onWriteable(SelectionKey key) {
        try {
            if(mDestAdress != null)
            Log.d(TAG,"onWriteable " + mDestAdress.getPort());
            this.beforeSend(mSendRemainBuffer);//发送之前，先让子类处理，例如做加密等
            if(this.write(mSendRemainBuffer,false)){//如果剩余数据已经发送完毕
                key.channel();
                if(isTunnelEstablished()){
                    mBrotherTunnel.beginReceived();
                } else {
                    this.beginReceived();
                }
            }

        }catch (Exception e){
            Log.d(TAG,"onWriteable" + e.toString());
        }
    }

    public void dispose(boolean disposeBrother) {

         disposeInternal(disposeBrother);
    }

     void disposeInternal(boolean disposeBrother){
        if(!mDisposed){
            try {
                mInnerChannel.close();
            } catch (Exception e){
                Log.d(TAG,e.toString());
            }

            if(mBrotherTunnel != null && disposeBrother){
                mBrotherTunnel.dispose(false);
            }

            mInnerChannel = null;
            mSendRemainBuffer = null;
            mSelector = null;
            mBrotherTunnel = null;
            mDisposed = true;
            mHttpResponse = null;
        }
    }

    @Override
    public void setBrotherTunnel(Tunnel brotherTunnel) {
        this.mBrotherTunnel = brotherTunnel;
    }

    @Override
    public Tunnel getBrotherTunnel() {
        return mBrotherTunnel;
    }
}
