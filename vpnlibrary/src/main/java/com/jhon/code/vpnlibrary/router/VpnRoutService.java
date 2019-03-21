package com.jhon.code.vpnlibrary.router;

import android.app.Activity;

import com.alibaba.android.arouter.facade.template.IProvider;
import com.jhon.code.vpnlibrary.http.HttpResponse;

/**
 * creater : Jhon
 * time : 2018/12/27 0027
 *
 */
public interface VpnRoutService extends IProvider {
    Class getStartActivity();

    void observer(HttpResponse response);
}
