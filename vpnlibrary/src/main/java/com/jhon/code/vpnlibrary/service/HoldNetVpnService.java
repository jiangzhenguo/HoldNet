package com.jhon.code.vpnlibrary.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;


import com.alibaba.android.arouter.launcher.ARouter;
import com.jhon.code.vpnlibrary.R;
import com.jhon.code.vpnlibrary.TcpProxyServer;
import com.jhon.code.vpnlibrary.http.HttpRequestHeaderParser;
import com.jhon.code.vpnlibrary.network.CommonMethods;
import com.jhon.code.vpnlibrary.network.IPHeader;
import com.jhon.code.vpnlibrary.network.TCPHeader;
import com.jhon.code.vpnlibrary.network.UDPHeader;
import com.jhon.code.vpnlibrary.router.VpnRoutService;
import com.jhon.code.vpnlibrary.router.VpnRouter;
import com.jhon.code.vpnlibrary.session.NatSession;
import com.jhon.code.vpnlibrary.session.NatSessionManager;
import com.jhon.code.vpnlibrary.util.ServerConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * creater : Jhon
 * time : 2018/12/12 0012
 */
public class HoldNetVpnService extends VpnService implements Runnable{
    public static final String VPN_ADDRESS = "10.8.0.2";
    private static final String VPN_ROUTE = "59.110.149.245";
    private static final int VPN_ROUTE_LENGTH = 32;


    public static final String BROADCAST_VPN_STATE = "com.vpn.status";
    public static final String BROADCAST_STOP_VPN = "com.vpn.stop";

    private static int LOCAL_IP;
    private Thread mThread;
    private ParcelFileDescriptor vpnInterface = null;
    private boolean isStop;
    private String TAG = HoldNetVpnService.class.getName();
    private TcpProxyServer mTcpProxy;


    private IPHeader mIPHeader;
    private TCPHeader mTCPHeader;
    private UDPHeader mUDPHeader;
    private byte[] mPacket;
    private VpnRoutService mRouter;
    @Override
    public void onCreate() {
        super.onCreate();
        mRouter = (VpnRoutService)ARouter.getInstance().build(VpnRouter.Vpn1).navigation();
        mPacket = new byte[20000];
        mIPHeader = new IPHeader(mPacket, 0);
        //Offset = ip报文头部长度
        mTCPHeader = new TCPHeader(mPacket, 20);
        mUDPHeader = new UDPHeader(mPacket, 20);
        ServerConfig.instance().setServer(this).setRouter(mRouter);
        registerReceiver(stopReceiver, new IntentFilter(BROADCAST_STOP_VPN));
        try {
            mTcpProxy = new TcpProxyServer(0);
            LOCAL_IP =mTcpProxy.getIp();
        } catch (Exception e){
            Log.d(TAG + "_01",e.toString());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(setupVPN()) {
            sendBroadcast(new Intent(BROADCAST_VPN_STATE).putExtra("running", true));
            mThread = new Thread(this, "vpnthread");
            mThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean setupVPN(){
        if(vpnInterface == null){
            Builder builder = new Builder();
            builder.setMtu(2000);
            builder.addAddress(VPN_ADDRESS,24);
            builder.addRoute(VPN_ROUTE,VPN_ROUTE_LENGTH);
            if(mRouter != null && mRouter.getStartActivity() != null) {
                Intent configure = new Intent(this, mRouter.getStartActivity());
                PendingIntent pi = PendingIntent.getActivity(this, 0, configure, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setConfigureIntent(pi);
                vpnInterface = builder.setSession(getString(R.string.app_name)).establish();
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void run() {
        try {
            mTcpProxy.start();
            while (true){
                if(isStop){
                    break;
                }
                FileOutputStream VpnOutPut = new FileOutputStream(vpnInterface.getFileDescriptor());
                FileInputStream vpnInput = new FileInputStream(vpnInterface.getFileDescriptor());
                int size = 0;
                while (size != -1) {
                    while ((size = vpnInput.read(mPacket)) > 0) {
                        onPacketReceived(mIPHeader, size, VpnOutPut);
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception e){
            Log.d(TAG + "_02",e.toString());
        }



    }

    /**
     * 对接受的包进行处理
     * @param ipHeader
     * @throws Exception
     */
    private void onPacketReceived(IPHeader ipHeader,int size,FileOutputStream mVPNOutputStream){
        try {
            switch (ipHeader.getProtocol()) {
                case IPHeader.TCP:
                    TCPHeader tcpHeader = mTCPHeader;
                    tcpHeader.mOffset = ipHeader.getHeaderLength();
                    if(tcpHeader.getSourcePort() == mTcpProxy.port()){
                        NatSession session = NatSessionManager.getSession(tcpHeader.getDestinationPort());
                        if(session != null){
                            Log.d(TAG,"packet from local");
                            ipHeader.setSourceIP(ipHeader.getDestinationIP());
                            tcpHeader.setSourcePort(session.RemotePort);
                            ipHeader.setDestinationIP(LOCAL_IP);
                            CommonMethods.ComputeTCPChecksum(ipHeader, tcpHeader);
                            mVPNOutputStream.write(ipHeader.mData, ipHeader.mOffset, size);
                        }
                    } else {
                        int portKey = tcpHeader.getSourcePort();
                        NatSession session = NatSessionManager.getSession(portKey);
                        if (session == null || session.RemoteIP != ipHeader.getDestinationIP() || session.RemotePort
                                != tcpHeader.getDestinationPort()) {
                            session = NatSessionManager.createSession(portKey, ipHeader.getDestinationIP(), tcpHeader
                                    .getDestinationPort());
                        }

                        session.LastNanoTime = System.currentTimeMillis();
                        session.PacketSent++; //注意顺序
                        int tcpDataSize = ipHeader.getDataLength() - tcpHeader.getHeaderLength();
                        if (session.PacketSent == 2 && tcpDataSize == 0) {
                            Log.d(TAG,"give up ack");
                            return; //丢弃tcp握手的第二个ACK报文。因为客户端发数据的时候也会带上ACK，这样可以在服务器Accept之前分析出HOST信息。
                        }
                        if(session.BytesSent == 0 && tcpDataSize > 10){
                            int dataOffset = tcpHeader.mOffset + tcpHeader.getHeaderLength();
                            HttpRequestHeaderParser.parseHttpRequestHeader(session, tcpHeader.mData, dataOffset,
                                    tcpDataSize);
                            Log.d("request","method: " + session.Method  + "\n" + "url: " + session.RequestUrl + "\n" + "values: " + session.Values.toString());
                        }
                        ipHeader.setSourceIP(ipHeader.getDestinationIP());
                        ipHeader.setDestinationIP(LOCAL_IP);
                        tcpHeader.setDestinationPort((short)mTcpProxy.port());
                        Log.d(TAG,"firsSend");
                        CommonMethods.ComputeTCPChecksum(ipHeader, tcpHeader);
                        mVPNOutputStream.write(ipHeader.mData, ipHeader.mOffset, size);
                        session.BytesSent += tcpDataSize; //注意顺序
                    }
            }
        } catch (Exception e){
            Log.d(TAG + "_03",e.toString());
        }



    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopVpn();
        ServerConfig.instance().setServer(null).setRouter(null);
        unregisterReceiver(stopReceiver);
    }

    private void stopVpn()
    {

        if(mThread !=null) {
            isStop = true;
        }
        if(vpnInterface !=null) {
            try {
                vpnInterface.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mTcpProxy != null){
            mTcpProxy.stop();
            mTcpProxy = null;
        }
        vpnInterface = null;
        mThread = null;
        sendBroadcast(new Intent(BROADCAST_VPN_STATE).putExtra("running", false));
    }

    private BroadcastReceiver stopReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent == null || intent.getAction() == null)
            {
                return;
            }

            if (BROADCAST_STOP_VPN.equals(intent.getAction()))
            {
                onRevoke();
                stopVpn();

            }
        }
    };
}
