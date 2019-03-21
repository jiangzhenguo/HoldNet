package com.jhon.code.holdnet.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.adapter.HttpListAdapter;
import com.jhon.code.holdnet.base.BaseFragment;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.inter.VpnControl;
import com.jhon.code.holdnet.viewmodel.ProjectDetaiViewModel;
import com.jhon.code.vpnlibrary.http.HttpResponse;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
    private VpnControl mControl;
    private HttpListAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setVpnControl(VpnControl mControl){
        this.mControl = mControl;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar =  view.findViewById(R.id.bar_httplist);
        mRvHttp = view.findViewById(R.id.rv_https);
        mRvHttp.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new HttpListAdapter(getActivity());
        mRvHttp.setAdapter(mAdapter);
        mToolbar.setTitleTextColor(Color.WHITE);
        mFloatButton = view.findViewById(R.id.fb_status);
        mFloatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mControl != null){
                    if(mProject.is_run) {
                        mControl.stopVpn();
                    } else {
                        mControl.startVpn();
                    }
                }
            }
        });
        mViewModel = ViewModelProviders.of(getActivity()).get(ProjectDetaiViewModel.class);
        mViewModel.getProject().observe(this, new Observer<VpnProject>() {
            @Override
            public void onChanged(VpnProject project) {
                mProject = project;
                if(mProject != null) {
                    mToolbar.setTitle(mProject.projectName);
                    if (mProject.is_run) {
                        mFloatButton.setImageResource(R.drawable.ic_pause);
                    } else {
                        mFloatButton.setImageResource(R.drawable.ic_play_arrow);
                    }
                }
            }
        });
        mViewModel.getResponseLists().observe(this, new Observer<HttpResponse>() {
            @Override
            public void onChanged(HttpResponse response) {
                mAdapter.addResponse(response);
            }
        });
    }

    @Override
    public int getContent() {
        return R.layout.fragment_http_list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mControl = null;
    }

    public static HttpListFragment getInstance(){
        return new HttpListFragment();
    }




}
