package com.jhon.code.holdnet.adapter;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.data.Bean.AppBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

/**
 * creater : Jhon
 * time : 2019/1/8 0008
 */
public class AppListAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<AppBean> apps;
    private onAppClickListener onAppClickListener;
    private boolean isSelect;

    public AppListAdapter(Context context){
        this.context = context;
        apps = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<AppBean> apps){
        this.apps.clear();
        this.apps.addAll(apps);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.viewholder_app,parent,false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  AppViewHolder){
            final AppBean info = apps.get(position);
            ((AppViewHolder) holder).mTvName.setText(info.appName);
            ((AppViewHolder) holder).mIvApp.setImageDrawable(info.appIcon);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onAppClickListener != null){
                        onAppClickListener.onAppClick(info);
                    }
                }
            });
            if(isSelect){
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(apps != null) {
            return apps.size();
        } else {
            return 0;
        }
    }

    private class AppViewHolder extends RecyclerView.ViewHolder{
        public TextView mTvName;
        public ImageView mIvApp;
        public AppCompatCheckBox mCbSelect;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_title);
            mIvApp = itemView.findViewById(R.id.iv_icon);
            mCbSelect = itemView.findViewById(R.id.cb_select);
        }
    }

    public void setOnAppClick(onAppClickListener listener){
        this.onAppClickListener = listener;
    }

    public interface onAppClickListener{
       void  onAppClick(AppBean appBean);
    }
}
