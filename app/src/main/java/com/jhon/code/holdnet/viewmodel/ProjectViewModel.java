package com.jhon.code.holdnet.viewmodel;

import com.jhon.code.holdnet.VpnApplication;
import com.jhon.code.holdnet.data.Bean.VpnProject;
import com.jhon.code.holdnet.data.repository.VpnRepository;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * creater : Jhon
 * time : 2018/12/29 0029
 */
public class ProjectViewModel extends ViewModel {

     private MutableLiveData<List<VpnProject>> projects;
     private VpnRepository mVpnRepository;

     public LiveData<List<VpnProject>> getProject(){
         if(projects == null){
             projects = new MutableLiveData<>();
             loadProject();
         }
         return projects;
     }

     private void loadProject(){
         mVpnRepository = VpnRepository.instance(VpnApplication.getContext());
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
