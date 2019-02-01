package com.jhon.code.holdnet.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.jhon.code.holdnet.router.HoldNetRouter;
import com.jhon.code.vpnlibrary.router.VpnRouter;
import com.jhon.code.vpnlibrary.service.HoldNetVpnService;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

/**
 * creater : Jhon
 * time : 2019/1/18 0018
 */
@Route(path = HoldNetRouter.ProjectDetialActivity.name)
public class ProjectDetailActivity extends BaseActivity {

    private static final int VPN_REQUEST_CODE = 0x0F;
    private HttpListFragment mHttpListFragment;

    @Autowired(name = HoldNetRouter.ProjectDetialActivity.project)
    public VpnProject project;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_project_detail);
        mHttpListFragment = HttpListFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putSerializable("project",project);
        mHttpListFragment.setArguments(bundle);
        FragmentTransaction transaction = mManager.beginTransaction();
        transaction.add(R.id.fl_content,mHttpListFragment);
        transaction.commitNowAllowingStateLoss();

    }


    @Override
    public int getRootView() {
        return 0;
    }


    private void startVPN()
    {
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
                    project.is_run = false;

                }
                else
                {
                    project.is_run = true;
                    stopService(new Intent(mContext, HoldNetVpnService.class));

                }
            }
        }
    };
}
