package com.turingoal.bts.dispatch.ui.activity.common;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgFragmentFactory;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.bts.dispatch.bean.BtsRole;
import com.turingoal.bts.dispatch.bean.TgMenuItem;
import com.turingoal.bts.dispatch.manager.DbInitManager;
import com.turingoal.bts.dispatch.ui.adapter.MainTabAdapter;
import com.turingoal.common.android.app.TgArouterCommonPaths;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.ui.TgDialogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.verticaltablayout.VerticalTabLayout;

/**
 * 主页
 */
@Route(path = TgArouterCommonPaths.MAIN_INDEX)
public class MainActivity extends TgBaseActivity {
    @BindView(R.id.tablayout)
    VerticalTabLayout tablayout; // tab 布局

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialized() {
        init();
        // 更新数据库
        DbInitManager.initData();
    }

    /**
     * 初始化
     */
    private void init() {
        List<Fragment> fragments = new ArrayList<>(); // fragments
        if (TgSystemHelper.checkPermission(BtsRole.SCHEDULING_ORDER)) { // 是否有查看总单的权限
            fragments.add(TgFragmentFactory.createFragment(TgFragmentFactory.FRAGMENT_SCHEDULING_ORDER));  // 调度任务总单
        }
        if (TgSystemHelper.checkPermission(BtsRole.SCHEDULING_TASK)) { // 是否有查看任务的权限
            fragments.add(TgFragmentFactory.createFragment(TgFragmentFactory.FRAGMENT_SCHEDULING_TASK));  // 调度任务
        }
        if (TgSystemHelper.checkPermission(BtsRole.BREAKDOWN)) { // 是否有查看故障的权限
            fragments.add(TgFragmentFactory.createFragment(TgFragmentFactory.FRAGMENT_BREAKDOWN));  // 故障
        }
        fragments.add(TgFragmentFactory.createFragment(TgFragmentFactory.FRAGMENT_ABOUT)); // 关于
        fragments.add(TgFragmentFactory.createFragment(TgFragmentFactory.FRAGMENT_HELP)); // 帮助
        List<TgMenuItem> menus = new ArrayList<>(); // 菜单
        if (TgSystemHelper.checkPermission(BtsRole.SCHEDULING_ORDER)) { // 是否有查看总单的权限
            menus.add(new TgMenuItem("调度", R.mipmap.app_ic_home_dispatch_checked, R.mipmap.app_ic_home_dispatch_normal));  // 调度任务总单
        }
        if (TgSystemHelper.checkPermission(BtsRole.SCHEDULING_TASK)) { // 是否有查看任务的权限
            menus.add(new TgMenuItem("任务", R.mipmap.app_ic_home_task_checked, R.mipmap.app_ic_home_task_normal));  // 调度任务
        }
        if (TgSystemHelper.checkPermission(BtsRole.BREAKDOWN)) { // 是否有查看故障的权限
            menus.add(new TgMenuItem("故障", R.mipmap.app_ic_home_breakdown_checked, R.mipmap.app_ic_home_breakdown_normal));  // 故障
        }
        menus.add(new TgMenuItem("关于", R.mipmap.app_ic_home_about_checked, R.mipmap.app_ic_home_about_normal));  // 关于
        menus.add(new TgMenuItem("帮助", R.mipmap.app_ic_home_help_checked, R.mipmap.app_ic_home_help_normal));  // 帮助
        tablayout.setTabAdapter(new MainTabAdapter(menus));
        tablayout.setupWithFragment(getSupportFragmentManager(), R.id.fragment_container, fragments);
        tablayout.setTabSelected(0); // 默认选中第几个，从0开始
    }

    /**
     * OnClick
     */
    @OnClick({R.id.iv_off, R.id.tv_off})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_off:
            case R.id.tv_off:
                TgDialogUtil.showDialog(this, "确定要退出当前用户吗?", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        TgFragmentFactory.mFragments.clear(); //Fragment缓存
                        TgSystemHelper.logout(MainActivity.this); // 注销退出系统
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 点击返回按钮
     */
    @Override
    public void onBackPressed() {
        TgSystemHelper.dbClickExit(); // 再按一次退出系统
    }
}
