package com.jhon.code.holdnet.diff;

import com.jhon.code.holdnet.data.Bean.VpnProject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

/**
 * creater : Jhon
 * time : 2019/1/17 0017
 */
public class ProjectCallback extends DiffUtil.Callback {

    private List<VpnProject> old_project,new_project;

    public ProjectCallback(List<VpnProject> old_project,List<VpnProject> new_project){
        this.old_project = old_project;
        this.new_project = new_project;
    }


    @Override
    public int getOldListSize() {
        return old_project.size();
    }

    @Override
    public int getNewListSize() {
        return new_project.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return old_project.get(oldItemPosition)._id == new_project.get(newItemPosition)._id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return old_project.get(oldItemPosition)._id == new_project.get(newItemPosition)._id;
    }
}
