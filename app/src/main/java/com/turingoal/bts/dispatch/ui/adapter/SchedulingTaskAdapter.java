package com.turingoal.bts.dispatch.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.common.android.biz.domain.SchedulingTask;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.common.android.constants.TgConstantYesNo;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.lang.TgStringUtil;

/**
 * 任务adapter
 */
public class SchedulingTaskAdapter extends BaseQuickAdapter<SchedulingTask, BaseViewHolder> {
    public SchedulingTaskAdapter() {
        super(R.layout.item_scheduling_task);
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
                .setBackgroundRes(R.id.tv_item_main_onclik, schedulingTask.getDispatchStatus() == TgConstantYesNo.NO ? R.drawable.bg_main_head_switch_yellow : R.drawable.bg_main_head_switch_bule)
                .setText(R.id.tv_item_main_onclik, schedulingTask.getDispatchStatus() == TgConstantYesNo.NO ? "派工" : "查看");
        helper.getView(R.id.tv_item_main_onclik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (schedulingTask.getDispatchStatus() == TgConstantYesNo.NO) { // 未派工
                    TgSystemHelper.handleIntentWithObj(TgArouterPaths.DISPATCH_DECLARE, "schedulingTask", schedulingTask);
                } else {
                    TgSystemHelper.handleIntentWithObj(TgArouterPaths.DISPATCH_DETAIL, "schedulingTask", schedulingTask);
                }
            }
        });
    }
}
