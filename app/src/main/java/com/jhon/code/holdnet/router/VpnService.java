package com.jhon.code.holdnet.router;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.textfield.TextInputLayout;
import com.jhon.code.holdnet.MainActivity;
import com.jhon.code.holdnet.activity.ProjectDetailActivity;
import com.jhon.code.holdnet.unit.LiveDataBus;
import com.jhon.code.holdnet.unit.LiveDateBusKey;
import com.jhon.code.vpnlibrary.http.HttpResponse;
import com.jhon.code.vpnlibrary.router.VpnRoutService;

import androidx.lifecycle.LiveData;

/**
 * creater : Jhon
 * time : 2018/12/27 0027
 */
@Route(path = "/vpnservice/vpn1")
public class VpnService implements VpnRoutService {

    private final String TAG = VpnService.class.getName();

    @Override
    public void init(Context context) {

    }

    @Override
    public void observer(HttpResponse response) {
        if(response != null){
            LiveDataBus.get().getChannel(LiveDateBusKey.KEY_HTTP_RESPONSE,HttpResponse.class).postValue(response);
        }
    }

    @Override
    public Class getStartActivity() {
        return ProjectDetailActivity.class;
    }
}
