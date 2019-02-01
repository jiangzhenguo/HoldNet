package com.jhon.code.holdnet;

import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.jhon.code.holdnet.adapter.VpnProjectAdapter;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.diff.ProjectCallback;
import com.jhon.code.holdnet.router.HoldNetRouter;
import com.jhon.code.holdnet.viewmodel.ProjectViewModel;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ProjectViewModel mViewModel;
    private RecyclerView mRv;
    private VpnProjectAdapter mAdapter;
    private MenuItem delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
    }


    private void initView(){
        final Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(HoldNetRouter.CreateProjectActivity.name).navigation();
            }
        });
        mRv = findViewById(R.id.rv_project);
        mRv.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new VpnProjectAdapter(HomeActivity.this);
        mRv.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        mRv.setAdapter(mAdapter);
        mAdapter.setOnTypeChangeListener(new VpnProjectAdapter.onTypeChanageListener() {
            @Override
            public void onTypeChange(int type) {
                if(type == 2){
                    delete.setVisible(true);
                } else {
                    delete.setVisible(false);
                }
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    private void initData(){
        mViewModel = ViewModelProviders.of(this).get(ProjectViewModel.class);
        mViewModel.getProject().observeForever(new Observer<List<VpnProject>>() {
            @Override
            public void onChanged(List<VpnProject> vpnProjects) {
                    List<VpnProject> old_list = mAdapter.getProject();
                    DiffUtil.DiffResult result = DiffUtil.calculateDiff(new ProjectCallback(old_list,vpnProjects),true);
                    mAdapter.setProject(vpnProjects);
                    result.dispatchUpdatesTo(mAdapter);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mAdapter.getType() == 2){
           mAdapter.setType(1);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        delete = menu.findItem(R.id.action_delete);
        delete.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            for(VpnProject project:mAdapter.getSelectProject()){
                mViewModel.deleteProject(project);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
