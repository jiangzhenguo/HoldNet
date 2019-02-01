package com.jhon.code.holdnet.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jhon.code.holdnet.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

/**
 * creater : Jhon
 * time : 2018/12/28 0028
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected View mRootLayout;
    protected CoordinatorLayout mLayout;
    protected Context mContext;
    protected Toolbar mBar;
    protected FrameLayout mContentLayout;
    protected ProgressBar mProgress;
    protected FragmentManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if(getRootView() != 0) {
            setContentView(null);
            setSupportActionBar(mBar);
            mRootLayout = findViewById(R.id.layout_root);
            setContentView(getRootView());
        }
        mManager = getSupportFragmentManager();
    }

    public abstract int getRootView();

    @Override
    public void setContentView(int layoutResID) {
        if(getRootView() != 0) {
            mContentLayout.removeAllViews();
            mContentLayout.addView(View.inflate(this, layoutResID, null));
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public void setContentView(View view) {
        if (null == mRootLayout) {
            mRootLayout = initBaseLayout();
        }
        super.setContentView(mRootLayout, new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private View initBaseLayout(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_base,null);
        mLayout = view.findViewById(R.id.layout_root);
        mBar = view.findViewById(R.id.toolbar);
        mContentLayout = view.findViewById(R.id.fl_content);
        mProgress = view.findViewById(R.id.bar_progress);
        return view;
    }

    protected void showLoadingDialog(){
        mProgress.setVisibility(View.VISIBLE);
    }

    protected void hideDialog(){
        mProgress.setVisibility(View.GONE);
    }

    protected int getFragmentId(){
        return R.id.fl_content;
    }
}
