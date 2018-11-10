package com.turingoal.bts.dispatch.ui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.bean.MaintenanceWorkPositionBean;


/**
 * 任务管理详情adapter
 */
public class MaintenanceWorkPositionAdapter extends BaseQuickAdapter<MaintenanceWorkPositionBean, BaseViewHolder> {
    public MaintenanceWorkPositionAdapter() {
        super(R.layout.item_mintenance_work_position);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final MaintenanceWorkPositionBean maintenanceWorkPositionBean) {
        holder.setText(R.id.tvWorkNum, "" + maintenanceWorkPositionBean.getWorkNum())
                .setText(R.id.tvName, TextUtils.isEmpty(maintenanceWorkPositionBean.getName()) ? "点击选择人员" : maintenanceWorkPositionBean.getName());
        holder.addOnClickListener(R.id.llPosition);
    }
}
