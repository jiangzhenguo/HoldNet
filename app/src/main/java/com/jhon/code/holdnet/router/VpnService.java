package com.jhon.code.holdnet.router;

import android.app.Activity;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jhon.code.holdnet.MainActivity;
import com.jhon.code.holdnet.activity.ProjectDetailActivity;
import com.jhon.code.vpnlibrary.router.VpnRoutService;

/**
 * creater : Jhon
 * time : 2018/12/27 0027
 */
@Route(path = "/vpnservice/vpn1")
public class VpnService implements VpnRoutService {


    @Override
    public void init(Context context) {

    }

    @Override
    public Class getStartActivity() {
        return ProjectDetailActivity.class;
    }
}
