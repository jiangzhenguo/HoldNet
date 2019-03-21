package com.jhon.code.holdnet.adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhon.code.holdnet.R;
import com.jhon.code.holdnet.unit.DateUtil;
import com.jhon.code.vpnlibrary.http.HttpResponse;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * creater : Jhon
 * time : 2019/1/24 0024
 */
public class HttpListAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<HttpResponse> mLists = new ArrayList<>();


    public HttpListAdapter(Context context){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }


    public void addResponse(HttpResponse response){
        mLists.add(response);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_http_list,parent,false);
        HttpViewHolder viewHolder = new HttpViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HttpViewHolder){
            HttpResponse response = mLists.get(position);
            ((HttpViewHolder) holder).mTvUrl.setText(response.session.uri.getPath());
            ((HttpViewHolder) holder).mTvMethod.setText(response.session.Method);
            ((HttpViewHolder) holder).mTvStatus.setText(String.valueOf(response.getStateCode()));
            ((HttpViewHolder) holder).mTvTime.setText(DateUtil.format(response.session.LastNanoTime));
        }
    }

    @Override
    public int getItemCount() {
        if(mLists != null){
           return mLists.size();
        } else {
           return 0;
        }
    }

    public class HttpViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvUrl;
        private ImageView mIvIcon;
        private TextView mTvMethod;
        private TextView mTvStatus;
        private TextView mTvTime;



        public HttpViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvUrl = itemView.findViewById(R.id.tv_url);
            mIvIcon =  itemView.findViewById(R.id.iv_icon);
            mTvMethod = itemView.findViewById(R.id.tv_method);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            mTvTime = itemView.findViewById(R.id.tv_time);
        }


    }
}
