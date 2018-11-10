package com.turingoal.bts.dispatch.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bts.common.android.biz.domain.DispatchTaskItem;
import com.turingoal.bts.common.android.biz.domain.SchedulingTask;
import com.turingoal.bts.common.android.constants.ConstantTrainSetLengthTypes;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.service.TaskService;
import com.turingoal.bts.dispatch.ui.adapter.DispatchTaskItemAdapter;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.constants.TgConstantYesNo;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.lang.TgStringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 调度任务详情页面
 */
@Route(path = TgArouterPaths.SCHEDULING_ORDER_DETAIL)
public class SchedulingOrderDetailActivity extends TgBaseActivity {
    @BindView(R.id.tvTrainSetModelAndTrainSetLengthType)
    TextView tvTrainSetModelAndTrainSetLengthType; // 检修车组
    @BindView(R.id.tvArriveTime)
    TextView tvArriveTime; // 入库时间
    @BindView(R.id.tvTrackCodeNumAndTrackRowNum)
    TextView tvTrackCodeNumAndTrackRowNum; // 检修股道
    @BindView(R.id.tvWorkGroupName)
    TextView tvWorkGroupName; // 检修班组
    @BindView(R.id.tvMaintenanceTask)
    TextView tvMaintenanceTask; // 检修任务
    @BindView(R.id.tvMaintenanceTaskItem)
    TextView tvMaintenanceTaskItem; // 检修项目
    @BindView(R.id.tvPlanStartTime)
    TextView tvPlanStartTime; // 计划开始时间
    @BindView(R.id.tvPlanFinishTime)
    TextView tvPlanFinishTime; // 计划结束时间
    @BindView(R.id.tvDispatchStatus)
    TextView tvDispatchStatus; // 派工状态
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView; // RecyclerView
    private DispatchTaskItemAdapter mAdapter = new DispatchTaskItemAdapter(); // adapter
    @Autowired
    SchedulingTask schedulingTask; // ARouter自动解析参数对象

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scheduling_order_detail;
    }

    @Override
    protected void initialized() {
        String arriveTime = TgDateUtil.date2String(schedulingTask.getArriveTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM);
        tvArriveTime.setText("入库时间：" + (TgStringUtil.isBlank(arriveTime) ? "---" : arriveTime));
        tvTrainSetModelAndTrainSetLengthType.setText("检修车型：" + schedulingTask.getTrainSetModel() + "（" + ConstantTrainSetLengthTypes.getShortStr(schedulingTask.getTrainSetLengthType()) + "）");
        tvTrackCodeNumAndTrackRowNum.setText("检修股道：" + schedulingTask.getTrackCodeNum() + "-" + schedulingTask.getTrackRowNum());
        tvWorkGroupName.setText("检修班组：" + schedulingTask.getWorkGroupName());
        tvMaintenanceTask.setText("检修任务：" + schedulingTask.getMaintenanceTask());
        tvMaintenanceTaskItem.setText("检修项目：" + schedulingTask.getMaintenanceTaskItem());
        tvPlanStartTime.setText("开始时间：" + TgDateUtil.date2String(schedulingTask.getStartTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvPlanFinishTime.setText("结束时间：" + TgDateUtil.date2String(schedulingTask.getFinishTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        initAdapter();
        getData();
    }

    /**
     * OnClick
     */
    @OnClick(R.id.iv_black)
    public void onViewClicked() {
        defaultFinish();
    }

    /**
     * adapter
     */
    private void initAdapter() {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mAdapter.openLoadAnimation(); // 动画
        mAdapter.setEmptyView(getNotDataView((ViewGroup) recyclerView.getParent())); // 设置空白view
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * 通过数据
     */
    private void getData() {
        if (schedulingTask.getDispatchStatus() == TgConstantYesNo.NO) {
            tvDispatchStatus.setText("派工详情：未派工");
            return;
        } else if (schedulingTask.getDispatchStatus() == TgConstantYesNo.YES) {
            TgApplication.getRetrofit().create(TaskService.class)
                    .findDispatchTaskItemsByTask(schedulingTask.getCodeNum())
                    .enqueue(new TgRetrofitCallback<List<DispatchTaskItem>>(this, true, true) {
                        @Override
                        public void successHandler(List<DispatchTaskItem> list) {
                            mAdapter.setNewData(list);
                        }
                    });
        }
    }
}
