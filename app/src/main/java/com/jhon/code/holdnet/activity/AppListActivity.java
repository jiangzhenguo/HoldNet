package com.jhon.code.holdnet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.adapter.AppListAdapter;
import com.jhon.code.holdnet.base.BaseActivity;
import com.jhon.code.holdnet.data.Bean.AppBean;
import com.jhon.code.holdnet.router.HoldNetRouter;
import com.jhon.code.holdnet.unit.LiveDataBus;
import com.jhon.code.holdnet.viewmodel.AppListViewModel;
import com.jhon.code.vpnlibrary.router.VpnRouter;

import java.util.List;

@Route(path = HoldNetRouter.AppListActivity.name)
public class AppListActivity extends BaseActivity {

    private RecyclerView mRvAppList;
    private AppListAdapter mAdapter;
    private AppListViewModel mViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRvAppList = findViewById(R.id.rv_apps);
        mRvAppList.setLayoutManager(new GridLayoutManager(mContext,4));
        mAdapter = new AppListAdapter(mContext);
        mRvAppList.setAdapter(mAdapter);
        showLoadingDialog();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(AppListViewModel.class);
        mViewModel.getAppList().observeForever(new Observer<List<AppBean>>() {
            @Override
            public void onChanged(List<AppBean> appBeans) {
                hideDialog();
                mAdapter.setData(appBeans);
            }
        });
        mAdapter.setOnAppClick(new AppListAdapter.onAppClickListener() {
            @Override
            public void onAppClick(AppBean appBean) {
                LiveDataBus.get().getChannel("app").setValue(appBean);
                onBackPressed();
            }
        });

    }

    @Override
    public int getRootView() {
        return R.layout.app_list_activity;
    }
}
