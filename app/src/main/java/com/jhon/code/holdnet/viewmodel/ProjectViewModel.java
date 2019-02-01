package com.jhon.code.holdnet.viewmodel;

import com.jhon.code.holdnet.VpnApplication;
import com.jhon.code.holdnet.data.Bean.AppBean;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.data.repository.VpnRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * creater : Jhon
 * time : 2018/12/29 0029
 */
public class ProjectViewModel extends ViewModel {

     private LiveData<List<VpnProject>> projects;
     private VpnRepository mVpnRepository;
     private LiveData<Integer> mListType;

     public LiveData<List<VpnProject>> getProject(){
         if(projects == null){
             projects = new MutableLiveData<>();
             loadProject();
         }
         return projects;
     }

     private void loadProject(){
         mVpnRepository = VpnRepository.instance(VpnApplication.getContext());
         projects = mVpnRepository.getProject();
     }

     public MutableLiveData<Integer> getAdapterStatus(){
         if(mListType == null){
             mListType = new MutableLiveData<>();
         }
         return (MutableLiveData<Integer>)mListType;
     }


     public void inseart(VpnProject project){
         if(mVpnRepository == null){
             loadProject();
         }
         mVpnRepository.insertProject(project);
     }

     public void deleteProject(VpnProject project){
         if(mVpnRepository == null){
             loadProject();
         }
         mVpnRepository.deleteProject(project);
     }


}
