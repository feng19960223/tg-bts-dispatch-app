package com.turingoal.bts.dispatch.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.common.android.biz.domain.SchedulingTask;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.lang.TgStringUtil;

/**
 * 任务总单adapter
 */
public class SchedulingOrderAdapter extends BaseQuickAdapter<SchedulingTask, BaseViewHolder> {
    public SchedulingOrderAdapter() {
        super(R.layout.item_scheduling_order);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final SchedulingTask schedulingTask) {
        String arriveTime = TgDateUtil.date2String(schedulingTask.getArriveTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_ZH);
        helper.setText(R.id.tv_item_main_num, "" + (mData.indexOf(schedulingTask) + 1)) // 序号
                .setText(R.id.tv_item_main_1, "入库：" + (TgStringUtil.isBlank(arriveTime) ? "---" : arriveTime))
                .setText(R.id.tv_item_main_2, "车号：" + schedulingTask.getTrainSetCodeNum())
                .setText(R.id.tv_item_main_3, "股道：" + schedulingTask.getTrackCodeNum() + "-" + schedulingTask.getTrackRowNum())
                .setText(R.id.tv_item_main_4, "任务：" + schedulingTask.getMaintenanceTask())
                .setText(R.id.tv_item_main_5, "项目：" + schedulingTask.getMaintenanceTaskItem())
                .setText(R.id.tv_item_main_6, "班组：" + schedulingTask.getWorkGroupName());
        helper.getView(R.id.tv_item_main_onclik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // 跳转到调度任务详情页面
                TgSystemHelper.handleIntentWithObj(TgArouterPaths.SCHEDULING_ORDER_DETAIL, "schedulingTask", schedulingTask);
            }
        });
    }

}
