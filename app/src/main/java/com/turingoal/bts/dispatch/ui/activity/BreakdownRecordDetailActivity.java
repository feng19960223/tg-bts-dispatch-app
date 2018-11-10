package com.turingoal.bts.dispatch.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bts.common.android.biz.domain.BreakdownRecordItem;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownPattern;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownSystemTypes;
import com.turingoal.bts.common.android.constants.ConstantStatus4Work;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.service.BreakdownRecordItemService;
import com.turingoal.bts.dispatch.ui.adapter.PhotoDetailAdapter;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.lang.TgDateUtil;

import butterknife.BindView;

/**
 * 故障详情
 */
@Route(path = TgArouterPaths.BREAKDOWN_RECORD_ITEM_DETAIL)
public class BreakdownRecordDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvDiscoveryTime)
    TextView tvDiscoveryTime; // 发现时间
    @BindView(R.id.tvTrainSetCodeNum)
    TextView tvTrainSetCodeNum; // 故障车组
    @BindView(R.id.tvSystemType)
    TextView tvSystemType; // 系统分类
    @BindView(R.id.tvDispatchTime)
    TextView tvDispatchTime; // 派工时间
    @BindView(R.id.tvDispatchPlanStartTime)
    TextView tvDispatchPlanStartTime; // 计划开始
    @BindView(R.id.tvHandlingResult)
    TextView tvHandlingResult; // 故障状态
    @BindView(R.id.tvStartTime)
    TextView tvStartTime; // 维修开始
    @BindView(R.id.tvQcStartTime)
    TextView tvQcStartTime; // 质检开始时间
    @BindView(R.id.tvDiscovererRealname)
    TextView tvDiscovererRealname; // 发现人员
    @BindView(R.id.tvCarriage)
    TextView tvCarriage; // 故障车厢
    @BindView(R.id.tvPattern)
    TextView tvPattern; // 故障模式
    @BindView(R.id.tvDispatchRealname)
    TextView tvDispatchRealname; // 派工人员
    @BindView(R.id.tvDispatchPlanFinishTime)
    TextView tvDispatchPlanFinishTime; // 计划结束
    @BindView(R.id.tvHandlerRealname)
    TextView tvHandlerRealname; // 维修人员
    @BindView(R.id.tvFinishTime)
    TextView tvFinishTime; // 维修结束
    @BindView(R.id.tvQcRealname)
    TextView tvQcRealname; // 质检人
    @BindView(R.id.tvQcFinishTime)
    TextView tvQcFinishTime; // 质检结束时间
    @BindView(R.id.tvDiscoveryDesc)
    TextView tvDiscoveryDesc; // 故障描述
    @BindView(R.id.tvHandlingDesc)
    TextView tvHandlingDesc; // 处理描述
    @BindView(R.id.tvQcDesc)
    TextView tvQcDesc; // 质检描述
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @Autowired
    BreakdownRecordItem breakdownRecordItem; // ARouter自动解析参数对象

    @Override
    protected int getLayoutId() {
        return R.layout.activity_breakdown_record_item_detail;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title_text, R.id.iv_black, "故障详情");
        tvDiscoveryTime.setText("发现时间：" + TgDateUtil.date2String(breakdownRecordItem.getDiscoveryTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvTrainSetCodeNum.setText("故障车组：" + breakdownRecordItem.getTrainSetCodeNum());
        tvSystemType.setText("系统分类：" + ConstantMontorBreakdownSystemTypes.getStr(breakdownRecordItem.getSystemType()));
        tvDispatchTime.setText("派工时间：" + TgDateUtil.date2String(breakdownRecordItem.getDispatchTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvDispatchPlanStartTime.setText("计划开始：" + TgDateUtil.date2String(breakdownRecordItem.getDispatchPlanStartTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvStartTime.setText("维修开始：" + TgDateUtil.date2String(breakdownRecordItem.getHandlingStartTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvQcStartTime.setText("质检开始:" + TgDateUtil.date2String(breakdownRecordItem.getQcStartTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvDiscovererRealname.setText("发现人员：" + breakdownRecordItem.getCreateUserRealname());
        tvCarriage.setText("故障车厢：" + breakdownRecordItem.getCarriage());
        tvPattern.setText("故障模式：" + ConstantMontorBreakdownPattern.getStr(breakdownRecordItem.getPattern()));
        tvDispatchRealname.setText("派工人员：" + breakdownRecordItem.getDispatchUserRealname());
        tvDispatchPlanFinishTime.setText("计划结束：" + TgDateUtil.date2String(breakdownRecordItem.getDispatchPlanFinishTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvHandlerRealname.setText("维修人员：" + breakdownRecordItem.getHandlerUserRealname());
        tvFinishTime.setText("维修结束：" + TgDateUtil.date2String(breakdownRecordItem.getHandlingFinishTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvQcRealname.setText("质检人员：" + breakdownRecordItem.getQcUserRealname());
        tvQcFinishTime.setText("质检结束:" + TgDateUtil.date2String(breakdownRecordItem.getQcFinishTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvDiscoveryDesc.setText(breakdownRecordItem.getDescription()); // 故障描述
        tvHandlingDesc.setText(breakdownRecordItem.getHandlingDesc()); // 处理描述
        tvQcDesc.setText(breakdownRecordItem.getQcDesc()); // 质检描述
        tvHandlingResult.setText("维修" + ConstantStatus4Work.getStr(breakdownRecordItem.getHandlingStatus())); // 故障状态 颜色不同
        tvHandlingResult.setTextColor(Color.RED); // 红色
        if (ConstantStatus4Work.FINISHED.equals(breakdownRecordItem.getHandlingStatus())) { // 故障已经处理
            tvHandlingResult.setText("质检" + ConstantStatus4Work.getStr(breakdownRecordItem.getQcStatus()));
            if (ConstantStatus4Work.FINISHED.equals(breakdownRecordItem.getQcStatus())) { // 质检已经处理
                tvHandlingResult.setTextColor(Color.GREEN); // 绿色
            }
        }
        TgApplication.getRetrofit().create(BreakdownRecordItemService.class)
                .get(breakdownRecordItem.getId())
                .enqueue(new TgRetrofitCallback<BreakdownRecordItem>(this, true, true) {
                    @Override
                    public void successHandler(BreakdownRecordItem breakdownRecordItem) {
                        if (breakdownRecordItem.getFilePaths() != null) {
                            if (breakdownRecordItem.getFilePaths().size() > 0) {
                                recyclerView.setLayoutManager(new LinearLayoutManager(BreakdownRecordDetailActivity.this, LinearLayoutManager.HORIZONTAL, false)); // 横滑
                                recyclerView.setAdapter(new PhotoDetailAdapter(breakdownRecordItem.getFilePaths()));
                            } else {
                                recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
