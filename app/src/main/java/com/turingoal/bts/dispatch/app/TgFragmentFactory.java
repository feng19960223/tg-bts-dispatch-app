package com.turingoal.bts.dispatch.app;

import com.turingoal.bts.dispatch.ui.fragment.AboutFragment;
import com.turingoal.bts.dispatch.ui.fragment.BreakdownFragment;
import com.turingoal.bts.dispatch.ui.fragment.HelpFragment;
import com.turingoal.bts.dispatch.ui.fragment.ScheduingTaskFragment;
import com.turingoal.bts.dispatch.ui.fragment.SchedulingOrderFragment;
import com.turingoal.common.android.base.TgBaseFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment工厂
 */
public class TgFragmentFactory {
    public static final int FRAGMENT_SCHEDULING_ORDER = 0; // 调度总单
    public static final int FRAGMENT_SCHEDULING_TASK = 1; // 调度任务
    public static final int FRAGMENT_BREAKDOWN = 2; // 故障
    public static final int FRAGMENT_ABOUT = 10; // 关于
    public static final int FRAGMENT_HELP = 11; // 帮助
    private static TgBaseFragment fragment; // fragment
    public static Map<Integer, TgBaseFragment> mFragments = new HashMap<>(); //Fragment缓存,退出的时候要清理重新加载，不要设置为private

    private TgFragmentFactory() {
        throw new Error("Fragment工厂类不能实例化！");
    }

    /**
     * 根据类型创建Fragment
     */
    public static TgBaseFragment createFragment(final int type) {
        fragment = mFragments.get(type);
        if (fragment == null) {
            switch (type) {
                case FRAGMENT_SCHEDULING_ORDER:
                    fragment = new SchedulingOrderFragment(); // 调度总单
                    break;
                case FRAGMENT_SCHEDULING_TASK:
                    fragment = new ScheduingTaskFragment();  // 调度任务
                    break;
                case FRAGMENT_BREAKDOWN:
                    fragment = new BreakdownFragment();  // 故障
                    break;
                case FRAGMENT_ABOUT:
                    fragment = new AboutFragment(); // 关于
                    break;
                case FRAGMENT_HELP:
                    fragment = new HelpFragment(); // 帮助
                    break;
                default:
                    break;
            }
            if (fragment != null) {
                mFragments.put(type, fragment); // 如果新new了Fragment，加到缓存中
            }
        }
        return fragment;
    }
}
