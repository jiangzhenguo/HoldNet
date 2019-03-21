package com.jhon.code.holdnet.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jhon.code.holdnet.MainActivity;
import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.base.BaseActivity;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.fragment.HttpListFragment;
import com.jhon.code.holdnet.inter.VpnControl;
import com.jhon.code.holdnet.router.HoldNetRouter;
import com.jhon.code.holdnet.viewmodel.ProjectDetaiViewModel;
import com.jhon.code.vpnlibrary.router.VpnRouter;
import com.jhon.code.vpnlibrary.service.HoldNetVpnService;

import java.util.zip.Inflater;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import static com.jhon.code.vpnlibrary.service.HoldNetVpnService.BROADCAST_STOP_VPN;

/**
 * creater : Jhon
 * time : 2019/1/18 0018
 */
@Route(path = HoldNetRouter.ProjectDetialActivity.name)
public class ProjectDetailActivity extends BaseActivity implements VpnControl {

    private static final int VPN_REQUEST_CODE = 0x0F;
    private HttpListFragment mHttpListFragment;

    @Autowired(name = HoldNetRouter.ProjectDetialActivity.project)
    public VpnProject project;

    private ProjectDetaiViewModel mViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_project_detail);
        mViewModel = ViewModelProviders.of(this).get(ProjectDetaiViewModel.class);
        mHttpListFragment = HttpListFragment.getInstance();
        mHttpListFragment.setVpnControl(this);
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.add(R.id.fl_content,mHttpListFragment);
        transaction.commitNowAllowingStateLoss();
        IntentFilter filter = new IntentFilter();
        filter.addAction(HoldNetVpnService.BROADCAST_VPN_STATE);
        registerReceiver(vpnStateReceiver,filter);
        mViewModel.setProject(project);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(vpnStateReceiver);
    }

    @Override
    public int getRootView() {
        return 0;
    }


    @Override
    public void startVpn() {
        Intent vpnIntent = VpnService.prepare(mContext);
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
    public void stopVpn() {
       Intent in = new Intent();
       in.setAction(BROADCAST_STOP_VPN);
       sendBroadcast(in);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
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
                    project.is_run = true;
                    mViewModel.setProject(project);
                }
                else
                {
                    project.is_run = false;
                    mViewModel.setProject(project);
                    stopService(new Intent(mContext, HoldNetVpnService.class));

                }
            }
        }
    };
}
