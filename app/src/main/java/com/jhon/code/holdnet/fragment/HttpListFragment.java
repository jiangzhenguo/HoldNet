package com.jhon.code.holdnet.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jhon.code.holdnet.MainActivity;
import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.base.BaseFragment;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.viewmodel.ProjectDetaiViewModel;
import com.jhon.code.vpnlibrary.service.HoldNetVpnService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

/**
 * creater : Jhon
 * time : 2019/1/18 0018
 */
public class HttpListFragment extends BaseFragment {


    private Toolbar mToolbar;
    private RecyclerView mRvHttp;

    private VpnProject mProject;
    private FloatingActionButton mFloatButton;
    private ProjectDetaiViewModel mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar =  view.findViewById(R.id.bar_httplist);
        mRvHttp = view.findViewById(R.id.rv_https);
        mToolbar.setTitle(mProject.projectName);
        mToolbar.setTitleTextColor(Color.WHITE);
        mFloatButton = view.findViewById(R.id.fb_status);
        mFloatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        mViewModel = ViewModelProviders.of(getActivity()).get(ProjectDetaiViewModel.class);
        mViewModel.getProject().observe(this, new Observer<VpnProject>() {
            @Override
            public void onChanged(VpnProject project) {
                mProject = project;
            }
        });
    }

    @Override
    public int getContent() {
        return R.layout.fragment_http_list;
    }

    public static HttpListFragment getInstance(){
        return new HttpListFragment();
    }




}
