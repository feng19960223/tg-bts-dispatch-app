package com.turingoal.bts.dispatch.manager;

import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.bean.MaintenanceTaskDb;
import com.turingoal.bts.dispatch.bean.MaintenanceWorkItemDb;
import com.turingoal.bts.dispatch.bean.TrackDb;
import com.turingoal.bts.dispatch.bean.TrainSetDb;
import com.turingoal.bts.dispatch.bean.User;
import com.turingoal.bts.dispatch.service.MaintenanceTaskService;
import com.turingoal.bts.dispatch.service.MaintenanceWorkItemService;
import com.turingoal.bts.dispatch.service.TrackService;
import com.turingoal.bts.dispatch.service.TrainSetService;
import com.turingoal.bts.dispatch.service.UserService;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;

import java.util.List;

/**
 * 数据库初始化
 */
public class DbInitManager {
    private DbInitManager() {
        throw new Error("工具类不能实例化！");
    }

    public static void initData() {
        initUserData(); // 初始化[人员]数据
        initTrackDatas(); //  初始化[股道]数据
        initTrainSetDatas(); // 初始化[车号]数据
        initMaintenanceTaskDatas(); // 初始化[检修任务]数据
        initMaintenanceWorkItemDatas(); // 初始化[作业项目]数据
    }

    private static void initUserData() {
        TgApplication.getRetrofit().create(UserService.class)
                .list()
                .enqueue(new TgRetrofitCallback<List<User>>(null, false, true) {
                    @Override
                    public void successHandler(List<User> users) {
                        if (users != null && users.size() > 0) {
                            TgApplication.getBoxStore().boxFor(User.class).removeAll(); // 清空数据库
                            for (User user : users) {
                                TgApplication.getBoxStore().boxFor(User.class).put(user);
                            }
                        }
                    }
                });
    }

    /**
     * 初始化[股道]数据
     */
    private static void initTrackDatas() {
        TgApplication.getRetrofit().create(TrackService.class)
                .list()
                .enqueue(new TgRetrofitCallback<List<TrackDb>>(null, false, false) {
                    @Override
                    public void successHandler(List<TrackDb> list) {
                        TgApplication.getBoxStore().boxFor(TrackDb.class).removeAll();
                        TgApplication.getBoxStore().boxFor(TrackDb.class).put(list);
                    }
                });
    }

    /**
     * 初始化[车号]数据
     */
    private static void initTrainSetDatas() {
        TgApplication.getRetrofit().create(TrainSetService.class)
                .list()
                .enqueue(new TgRetrofitCallback<List<TrainSetDb>>(null, false, false) {
                    @Override
                    public void successHandler(List<TrainSetDb> list) {
                        TgApplication.getBoxStore().boxFor(TrainSetDb.class).removeAll();
                        TgApplication.getBoxStore().boxFor(TrainSetDb.class).put(list);
                    }
                });
    }

    /**
     * 初始化[检修任务]数据
     */
    private static void initMaintenanceTaskDatas() {
        TgApplication.getRetrofit().create(MaintenanceTaskService.class)
                .list()
                .enqueue(new TgRetrofitCallback<List<MaintenanceTaskDb>>(null, false, false) {
                    @Override
                    public void successHandler(List<MaintenanceTaskDb> list) {
                        TgApplication.getBoxStore().boxFor(MaintenanceTaskDb.class).removeAll();
                        TgApplication.getBoxStore().boxFor(MaintenanceTaskDb.class).put(list);
                    }
                });
    }

    /**
     * 初始化[作业项目]数据
     */
    private static void initMaintenanceWorkItemDatas() {
        TgApplication.getRetrofit().create(MaintenanceWorkItemService.class)
                .list()
                .enqueue(new TgRetrofitCallback<List<MaintenanceWorkItemDb>>(null, false, false) {
                    @Override
                    public void successHandler(List<MaintenanceWorkItemDb> list) {
                        TgApplication.getBoxStore().boxFor(MaintenanceWorkItemDb.class).removeAll();
                        TgApplication.getBoxStore().boxFor(MaintenanceWorkItemDb.class).put(list);
                    }
                });
    }
}
