package com.turingoal.bts.dispatch.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.turingoal.bts.common.android.biz.domain.SchedulingOrder;
import com.turingoal.bts.common.android.constants.ConstantWorkShiftTypes;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.bts.dispatch.service.SchedulingOrderService;
import com.turingoal.bts.dispatch.ui.adapter.SchedulingTaskAdapter;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseFragment;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.lang.TgStringUtil;
import com.turingoal.common.android.util.ui.TgDateTimePickUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.turingoal.bts.dispatch.app.TgApplication.getTgUserPreferences;

/**
 * 任务fragment
 */
public class ScheduingTaskFragment extends TgBaseFragment {
    @BindView(R.id.tvWorkGroupName)
    TextView tvWorkGroupName; // 检修班组
    @BindView(R.id.tvOrderNum)
    TextView tvOrderNum; // 单号
    @BindView(R.id.tvSchedulingUserRealname)
    TextView tvSchedulingUserRealname; // 当前调度
    @BindView(R.id.ivAdd)
    ImageView ivAdd; // 新建任务
    @BindView(R.id.tv_main_head_date)
    TextView tvMainHeadDate; // 日期
    @BindView(R.id.tv_main_head_day)
    TextView tvMainHeadWhite; // 白班
    @BindView(R.id.tv_main_head_night)
    TextView tvMainHeadNight; // 夜班
    @BindView(R.id.rv_main)
    RecyclerView rvMain; // RecyclerView
    @BindView(R.id.tvKeyBreakdownDesc)
    TextView tvKeyBreakdownDesc; // 重点故障
    @BindView(R.id.tvKeyTasksDesc)
    TextView tvKeyTasksDesc; // 重点任务
    @BindView(R.id.tvKeyAttentionDesc)
    TextView tvKeyAttentionDesc; // 作业注意事项
    @BindView(R.id.sv_main)
    ScrollView svMain; // 视图控制器
    private Date currentDate; // 日期
    private boolean dayOrNight = true; // 白班true 夜班 false
    private SchedulingTaskAdapter mAdapter = new SchedulingTaskAdapter(); // adapter

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scheduing_task;
    }

    @Override
    protected void initialized() {
        EventBus.getDefault().register(this);
        initToolbar(R.id.tv_main_title_text, "任务单");
        ivAdd.setVisibility(View.VISIBLE);
        tvWorkGroupName.setText("检修班组：" + getTgUserPreferences().getWorkGroupName());
        initAdapter();
        getData(Calendar.getInstance().getTime());
    }

    /**
     * OnClick
     */
    @OnClick({R.id.ivAdd, R.id.iv_main_head_before, R.id.tv_main_head_date, R.id.iv_main_head_next, R.id.tv_main_head_day, R.id.tv_main_head_night})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.ivAdd:
                TgSystemHelper.handleIntent(TgArouterPaths.TASK_ADD);
                break;
            case R.id.iv_main_head_before: // 前一天
                getData(TgDateUtil.getBeforeDay(currentDate)); // 切换时间重新获取数据
                break;
            case R.id.tv_main_head_date: // 日期
                TgDateTimePickUtil timeSelector = new TgDateTimePickUtil(getActivity(), currentDate, TgDateUtil.DEFAULT_START_DATE, TgDateUtil.DEFAULT_END_DATE, null);
                timeSelector.dateTimePickDialog(new TgDateTimePickUtil.OnSubmitSelectDateListener() {
                    public void onSumbitSelect(final Date date) {
                        if (currentDate.getTime() / 1000 / 60 / 60 / 24 != date.getTime() / 1000 / 60 / 60 / 24) { //用户没有选择当天
                            getData(date); // 选择日期
                        }
                    }
                });
                break;
            case R.id.iv_main_head_next: //后一天
                if (!TgDateUtil.isToday(currentDate)) { // 如果现在选择的不是今天
                    getData(TgDateUtil.getNextDay(currentDate)); // 后一天
                }
                break;
            case R.id.tv_main_head_day:  // 白班
                if (dayOrNight) {
                    return;
                }
                dayOrNight = true;
                getData(currentDate);
                break;
            case R.id.tv_main_head_night: // 夜班
                if (!dayOrNight) {
                    return;
                }
                dayOrNight = false;
                getData(currentDate);
                break;
            default:
                break;
        }
    }

    /**
     * 获取适配器
     */
    private void initAdapter() {
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.openLoadAnimation(); // 动画
        mAdapter.setEmptyView(getNotDataView((ViewGroup) rvMain.getParent())); // 设置空白view
        rvMain.setAdapter(mAdapter);
    }

    /**
     * 获取数据，并显示到RecyclerView
     */
    private void getData(final Date date) {
        currentDate = date;
        tvMainHeadDate.setText(TgDateUtil.date2String(currentDate, TgDateUtil.FORMAT_YYYY_MM_DD)); // 设置日期
        if (dayOrNight) {
            tvMainHeadWhite.setBackgroundResource(R.drawable.bg_main_head_switch_bule);
            tvMainHeadNight.setBackgroundResource(R.drawable.bg_main_head_switch_transparent);
        } else {
            tvMainHeadWhite.setBackgroundResource(R.drawable.bg_main_head_switch_transparent);
            tvMainHeadNight.setBackgroundResource(R.drawable.bg_main_head_switch_bule);
        }
        setTitle("任务单");
        tvOrderNum.setText("");
        tvSchedulingUserRealname.setText("");
        tvKeyBreakdownDesc.setText("");
        tvKeyTasksDesc.setText("");
        tvKeyAttentionDesc.setText("");
        tvKeyBreakdownDesc.setVisibility(View.VISIBLE);
        tvKeyTasksDesc.setVisibility(View.VISIBLE);
        tvKeyAttentionDesc.setVisibility(View.VISIBLE);
        svMain.setVisibility(View.GONE);
        mAdapter.setNewData(null);
        TgApplication.getRetrofit().create(SchedulingOrderService.class)
                .getByDateAndWorkShiftTypeAndWorkGroup(TgDateUtil.date2String(currentDate),
                        dayOrNight ? ConstantWorkShiftTypes.DAY_SHIFT : ConstantWorkShiftTypes.NIGHY_SHIFT,
                        TgApplication.getTgUserPreferences().getWorkGroupName())
                .enqueue(new TgRetrofitCallback<SchedulingOrder>(getContext(), true, true) {
                    @Override
                    public void successHandler(SchedulingOrder schedulingOrder) {
                        if (schedulingOrder == null) {
                            return;
                        }
                        setTitle("");
                        if (!TextUtils.isEmpty(schedulingOrder.getCodeNum())) {
                            tvOrderNum.setText("单号：" + schedulingOrder.getCodeNum());
                        } else {
                            setTitle("任务单");
                        }
                        if (!TgStringUtil.isEmpty(schedulingOrder.getSchedulingUserRealname())) {
                            tvSchedulingUserRealname.setText("当班调度：" + schedulingOrder.getSchedulingUserRealname());
                        }
                        svMain.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(schedulingOrder.getKeyBreakdownDesc())) { // 重点故障
                            tvKeyBreakdownDesc.setText("重点故障：---");
                        } else {
                            tvKeyBreakdownDesc.setText("" + schedulingOrder.getKeyBreakdownDesc());
                        }
                        if (TextUtils.isEmpty(schedulingOrder.getKeyTasksDesc())) { // 重点任务
                            tvKeyTasksDesc.setText("重点任务：---");
                        } else {
                            tvKeyTasksDesc.setText("" + schedulingOrder.getKeyTasksDesc());
                        }
                        if (TextUtils.isEmpty(schedulingOrder.getKeyAttentionDesc())) { // 作业注意事项
                            tvKeyAttentionDesc.setText("作业注意事项：---");
                        } else {
                            tvKeyAttentionDesc.setText("" + schedulingOrder.getKeyAttentionDesc());
                        }
                        if (schedulingOrder.getTasksList() != null && schedulingOrder.getTasksList().size() > 0) { // 没有数据
                            mAdapter.setNewData(schedulingOrder.getTasksList()); // 加载数据
                        }
                    }
                });
    }

    /**
     * event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message) {
        if ("SchedulingTaskRefresh".equals(message)) { // 派工成功刷新本页面
            getData(currentDate);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
