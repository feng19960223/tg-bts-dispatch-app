package com.turingoal.bts.dispatch.ui.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.common.android.biz.domain.BreakdownRecordItem;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownSystemTypes;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.common.android.constants.TgConstantYesNo;
import com.turingoal.common.android.util.lang.TgDateUtil;

/**
 * 故障activity的adapter
 */
public class BreakdownItemAdapter extends BaseQuickAdapter<BreakdownRecordItem, BaseViewHolder> {
    public BreakdownItemAdapter() {
        super(R.layout.item_breakdown_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final BreakdownRecordItem breakdownRecordItem) {
        helper.setText(R.id.tv_item_main_num, "" + (mData.indexOf(breakdownRecordItem) + 1))// 序号
                .setText(R.id.tv_item_main_1, "发现：" + TgDateUtil.date2String(breakdownRecordItem.getDiscoveryTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_ZH))
                .setText(R.id.tv_item_main_2, "车组：" + breakdownRecordItem.getTrainSetCodeNum())
                .setText(R.id.tv_item_main_3, "车厢：" + breakdownRecordItem.getCarriage())
                .setText(R.id.tv_item_main_4, "分类：" + ConstantMontorBreakdownSystemTypes.getStr(breakdownRecordItem.getSystemType()))
                .setText(R.id.tv_item_main_5, "发现人：" + breakdownRecordItem.getCreateUserUsername())
                .setBackgroundRes(R.id.tv_item_main_onclik, breakdownRecordItem.getDispatchStatus() == TgConstantYesNo.NO ? R.drawable.bg_main_head_switch_yellow : R.drawable.bg_main_head_switch_bule)
                .setText(R.id.tv_item_main_onclik, breakdownRecordItem.getDispatchStatus() == TgConstantYesNo.NO ? "派工" : "详情");
        helper.getView(R.id.tv_item_main_onclik).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (breakdownRecordItem.getDispatchStatus() == TgConstantYesNo.NO) {
                    TgSystemHelper.handleIntentWithObj(TgArouterPaths.BREAKDOWN_RECORD_ITEM_DISPATCH, "breakdownRecordItem", breakdownRecordItem); // 故障派工
                } else {
                    TgSystemHelper.handleIntentWithObj(TgArouterPaths.BREAKDOWN_RECORD_ITEM_DETAIL, "breakdownRecordItem", breakdownRecordItem); // 查看详情
                }
            }
        });
    }
}
