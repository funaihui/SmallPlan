package com.wizardev.smallplanmvp.data;

import java.util.List;

/**
 * anthor : wizardev
 * email : wizarddev@163.com
 * time : 17-11-3
 * desc : 定义获得数据的接口
 * version : 1.0
 */

public interface PlanDataSource {
    void addPlan(String content, String time, int flag);//添加计划到数据库

    void updatePlan(long id, String content, String time, int flag);//更新现有的计划

    void deletePlan(long id);//删除计划

    List<Plan> loadAllPlan();//载入所有的计划

    List<Plan> loadFinishPlan();//载入已完成的计划

    List<Plan> loadUnFinishPlan();//载入未完成的计划
}
