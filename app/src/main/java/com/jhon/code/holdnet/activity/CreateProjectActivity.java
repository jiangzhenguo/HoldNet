package com.jhon.code.holdnet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.base.BaseActivity;
import com.jhon.code.holdnet.data.Bean.AppBean;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.router.HoldNetRouter;
import com.jhon.code.holdnet.unit.LiveDataBus;
import com.jhon.code.holdnet.unit.ToastUtil;
import com.jhon.code.holdnet.viewmodel.ProjectViewModel;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * creater : Jhon
 * time : 2019/1/2 0002
 */
@Route(path = HoldNetRouter.CreateProjectActivity.name)
public class CreateProjectActivity extends BaseActivity {

    VpnProject project = new VpnProject();
    private int requestCode = 1010;
    private ProjectViewModel mModel;
    private EditText mEditText;
    private ImageView mIvApp;
    private TextView mTvTitle;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()).create(ProjectViewModel.class);
        mEditText = findViewById(R.id.edit_name);
        mIvApp = findViewById(R.id.add_app);
        mTvTitle = findViewById(R.id.tv_app_name);
        mIvApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(HoldNetRouter.AppListActivity.name).navigation(mContext);
            }
        });
        LiveDataBus.get().getChannel("app",AppBean.class).observe(this, new Observer<AppBean>() {
            @Override
            public void onChanged(AppBean appBean) {
                mIvApp.setImageDrawable(appBean.appIcon);
                mTvTitle.setText(appBean.appName);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_project_menu,menu);
        return true;
    }

    @Override
    public int getRootView() {
        return R.layout.activity_create_project;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                createProject();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1010:
                    if(data != null) {
                        AppBean appBean = (AppBean)data.getSerializableExtra("app");
                        mIvApp.setImageDrawable(appBean.appIcon);
                        project.packageName = appBean.appPackageName;
                    }
                    break;
            }
        }
    }

    private void createProject(){
        if(!TextUtils.isEmpty(mEditText.getText().toString())) {
            project.projectName = mEditText.getText().toString();
            mModel.inseart(project);
            onBackPressed();
        } else {
            ToastUtil.show(mContext,R.string.project_name_toast);
        }
    }
}
