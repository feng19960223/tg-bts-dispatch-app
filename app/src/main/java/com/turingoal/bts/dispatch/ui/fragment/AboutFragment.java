package com.turingoal.bts.dispatch.ui.fragment;

import android.widget.TextView;

import com.turingoal.bts.dispatch.BuildConfig;
import com.turingoal.bts.dispatch.R;
import com.turingoal.common.android.base.TgBaseFragment;
import com.turingoal.common.android.util.system.TgSystemUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于Fragment
 */
public class AboutFragment extends TgBaseFragment {
    @BindView(R.id.about_tv_version)
    TextView tvVersion;  // 版本信息

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_main_title_text, "关于系统");
        tvVersion.setText("V " + BuildConfig.VERSION_NAME); // 版本
    }

    /**
     * onClick
     */
    @OnClick(R.id.about_tv_web)
    public void goWeb() {
        TgSystemUtil.openWebSite(getString(R.string.contract_website)); // 打开网址
    }
}
