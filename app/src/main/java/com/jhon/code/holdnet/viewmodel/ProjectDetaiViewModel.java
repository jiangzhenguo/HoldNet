package com.jhon.code.holdnet.viewmodel;

import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.unit.LiveDataBus;
import com.jhon.code.holdnet.unit.LiveDateBusKey;
import com.jhon.code.vpnlibrary.http.HttpResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * creater : Jhon
 * time : 2019/1/30 0030
 */
public class ProjectDetaiViewModel extends ViewModel {

    private MutableLiveData<VpnProject> mProject;

    public ProjectDetaiViewModel(){
        mProject = new MutableLiveData<>();
    }

    public LiveData<VpnProject> getProject(){
        return mProject;
    }

    public void setProject(VpnProject project){
        mProject.setValue(project);
    }


    public LiveData<HttpResponse> getResponseLists(){
        return LiveDataBus.get().getChannel(LiveDateBusKey.KEY_HTTP_RESPONSE,HttpResponse.class);
    }




}
