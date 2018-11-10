package com.turingoal.bts.dispatch.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.common.android.biz.domain.BreakdownRecord;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.common.android.util.lang.TgDateUtil;

/**
 * 故障fragment的adapter
 */
public class BreakdownAdapter extends BaseQuickAdapter<BreakdownRecord, BaseViewHolder> {
    public BreakdownAdapter() {
        super(R.layout.item_breakdown);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BreakdownRecord breakdownRecord) {
        helper.setText(R.id.tv_item_main_num, "" + (mData.indexOf(breakdownRecord) + 1))// 序号
                .setText(R.id.tv_item_main_1, "创建：" + TgDateUtil.date2String(breakdownRecord.getCreateTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_ZH))
                .setText(R.id.tv_item_main_2, "车组：" + breakdownRecord.getTrainSetCodeNum())
                .setText(R.id.tv_item_main_3, "车次：" + breakdownRecord.getTrainFrequency());
        helper.getView(R.id.tv_item_main_onclik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TgSystemHelper.handleIntentWithObj(TgArouterPaths.BREAKDOWN_RECORD, "breakdownRecord", breakdownRecord); // 故障activity
            }
        });
    }
}
