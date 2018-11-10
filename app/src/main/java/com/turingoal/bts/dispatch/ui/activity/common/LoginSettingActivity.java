package com.turingoal.bts.dispatch.ui.activity.common;

import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.common.android.app.TgArouterCommonPaths;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.bean.TgValidateResultBean;
import com.turingoal.common.android.util.ui.TgToastUtil;
import com.turingoal.common.android.validatror.TgNetValidator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服务器设置
 */
@Route(path = TgArouterCommonPaths.COMMON_AMIN_CONFIG)
public class LoginSettingActivity extends TgBaseActivity {
    @BindView(R.id.et_ip)
    EditText etIp; // 输入框 ip
    @BindView(R.id.et_port)
    EditText etPort;  // 输入框 端口

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_login_setting;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title_text, R.id.iv_black, "服务器配置");
        String ip = TgApplication.getTgUserPreferences().getServerIp();
        if (!TextUtils.isEmpty(ip)) {
            etIp.setText(ip); // 如果IP已设置显示进输入框
        }
        Integer port = TgApplication.getTgUserPreferences().getServerPort();
        if (port > 0) {
            etPort.setText(String.valueOf(port)); // 如果端口已设置显示进输入框
        }
    }

    /**
     * 提交
     */
    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        final String ip = etIp.getText().toString().trim(); // ip
        final String port = etPort.getText().toString().trim(); // 端口
        TgValidateResultBean validateResult = TgNetValidator.validateIpAndPort(ip, port);  // 校验IP地址和端口
        if (!validateResult.isSuccess()) {
            TgToastUtil.showShort(validateResult.getMsg());
        } else {
            TgApplication.getTgUserPreferences().setServerIp(ip); // 保存服务器ip
            TgApplication.getTgUserPreferences().setServerPort(Integer.parseInt(port)); // 保存服务器端口
            TgApplication.initRetrofit(); // 重新初始化retrofit
            TgToastUtil.showLong("服务器配置修改成功！");
            defaultFinish(); //返回
        }
    }
}