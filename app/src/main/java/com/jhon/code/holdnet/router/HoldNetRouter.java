package com.jhon.code.holdnet.router;

import com.jhon.code.holdnet.activity.CreateProjectActivity;

/**
 * creater : Jhon
 * time : 2019/1/4 0004
 */
public interface HoldNetRouter {

    interface CreateProjectActivity{
        String name = "/vpn/createproject";
    }

    interface AppListActivity{
        String name = "/vpn/applist";
    }


    interface ProjectDetialActivity{
        String name = "/vpn/projectdetail";
        String project = "project";
    }

}
