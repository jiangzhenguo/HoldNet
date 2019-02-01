package com.jhon.code.holdnet.adapter;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.VpnApplication;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.data.dao.SettingDao;
import com.jhon.code.holdnet.router.HoldNetRouter;
import com.jhon.code.holdnet.unit.SettingUnits;
import com.jhon.code.holdnet.viewmodel.ProjectViewModel;
import com.jhon.code.vpnlibrary.router.VpnRouter;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

/**
 * creater : Jhon
 * time : 2019/1/7 0007
 */
public class VpnProjectAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private List<VpnProject> mData;
    private ProjectViewModel mViewModel;
    private onTypeChanageListener mListener;
    private int type = 1;

    public VpnProjectAdapter(Context context){
        mInflater =LayoutInflater.from(context);
        mData = new ArrayList<>();
        mViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance((Application)context.getApplicationContext()).create(ProjectViewModel.class);
        mViewModel.getAdapterStatus().observe((LifecycleOwner)context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                type = integer;
                notifyDataSetChanged();
                if(mListener != null){
                    mListener.onTypeChange(type);
                }
            }
        });
    }


    public void setProject(List<VpnProject> data){
        mData.clear();
        mData.addAll(data);
    }

    public List<VpnProject> getProject(){
        return mData;
    }


    public int getType(){
        return type;
    }

    public void setType(int type){
        mViewModel.getAdapterStatus().setValue(type);
    }

    public void setOnTypeChangeListener(onTypeChanageListener listener){
        this.mListener = listener;
    }

    public ArrayList<VpnProject> getSelectProject(){
        ArrayList<VpnProject> projects = new ArrayList<>();
        for(VpnProject project:mData){
            if(project.is_select){
                projects.add(project);
            }
        }
        return projects;
    }

    public void removeSelectProject(){

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewholder_project,parent,false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ProjectViewHolder){
            final VpnProject project = mData.get(position);
            ((ProjectViewHolder) holder).mTvName.setText(project.projectName);
            ((ProjectViewHolder) holder).mCheckBox.setOnCheckedChangeListener(null);
            ((ProjectViewHolder) holder).mCheckBox.setChecked(project.is_select);
            ((ProjectViewHolder) holder).mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    project.is_select = b;
                    notifyDataSetChanged();
                }
            });
            if(type == 1){
                ((ProjectViewHolder) holder).mCheckBox.setVisibility(View.GONE);
            } else {
                ((ProjectViewHolder) holder).mCheckBox.setVisibility(View.VISIBLE);
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    type = 2;
                    mViewModel.getAdapterStatus().postValue(type);
                    return false;
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ARouter.getInstance().build(HoldNetRouter.ProjectDetialActivity.name).withSerializable(HoldNetRouter.ProjectDetialActivity.project,project).navigation();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mData != null){
            return mData.size();
        }
        return 0;
    }


    public class ProjectViewHolder extends RecyclerView.ViewHolder{
        public TextView mTvName;
        public ImageView mIvStatus;
        public AppCompatCheckBox mCheckBox;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_project_title);
            mCheckBox = itemView.findViewById(R.id.cb_select);
        }
    }

    public interface onTypeChanageListener{
        void onTypeChange(int type);
    }



}
