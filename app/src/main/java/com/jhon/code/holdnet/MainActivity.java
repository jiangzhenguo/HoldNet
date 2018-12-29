package com.jhon.code.holdnet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jhon.code.vpnlibrary.service.HoldNetVpnService;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final int VPN_REQUEST_CODE = 0x0F;
    Button mBtnVpn;
    private  boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnVpn = findViewById(R.id.btn_vpn);
        registerReceiver(vpnStateReceiver, new IntentFilter(HoldNetVpnService.BROADCAST_VPN_STATE));
        mBtnVpn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStart) {
                    startVPN();
                }else{
                    sendBroadcast(new Intent(HoldNetVpnService.BROADCAST_STOP_VPN));
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(vpnStateReceiver);
    }

    private void startVPN()
    {
        Intent vpnIntent = VpnService.prepare(this);
        if (vpnIntent != null)
        {
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        }
        else
        {
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK)
        {
            startService(new Intent(this, HoldNetVpnService.class));
        }
    }


    private BroadcastReceiver vpnStateReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (HoldNetVpnService.BROADCAST_VPN_STATE.equals(intent.getAction()))
            {
                if (intent.getBooleanExtra("running", false))
                {
                    isStart = true;
                    mBtnVpn.setText("stop");
                }
                else
                {
                    isStart =false;
                    mBtnVpn.setText("start");
                    stopService(new Intent(MainActivity.this, HoldNetVpnService.class));
                }
            }
        }
    };

}
