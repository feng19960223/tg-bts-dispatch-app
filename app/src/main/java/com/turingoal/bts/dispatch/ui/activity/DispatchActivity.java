package com.turingoal.bts.dispatch.ui.activity;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.turingoal.bts.common.android.biz.domain.DispatchTaskItem;
import com.turingoal.bts.common.android.biz.domain.SchedulingTask;
import com.turingoal.bts.common.android.constants.ConstantTrainSetLengthTypes;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.bean.MaintenanceWorkPositionBean;
import com.turingoal.bts.dispatch.bean.User;
import com.turingoal.bts.dispatch.service.TaskService;
import com.turingoal.bts.dispatch.ui.adapter.MaintenanceWorkPositionAdapter;
import com.turingoal.bts.dispatch.ui.adapter.UserExpandableListViewAdapter;
import com.turingoal.bts.dispatch.util.RetrofitUtils;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.io.TgJsonUtil;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.lang.TgStringUtil;
import com.turingoal.common.android.util.ui.TgBarUtil;
import com.turingoal.common.android.util.ui.TgDialogUtil;
import com.turingoal.common.android.util.ui.TgToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 派工页面,确认派工
 */
@Route(path = TgArouterPaths.DISPATCH_DECLARE)
public class DispatchActivity extends TgBaseActivity {
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
    @BindView(R.id.tvSchedulingPlanStartTime)
    TextView tvSchedulingPlanStartTime; // 调度计划任务开始时间
    @BindView(R.id.tvSchedulingPlanFinishTime)
    TextView tvSchedulingPlanFinishTime; // 调度计划任务结束时间
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView; // recyclerView
    @BindView(R.id.tvDispatch)
    TextView tvDispatch; // 确定派工
    private MaintenanceWorkPositionAdapter mAdapter = new MaintenanceWorkPositionAdapter();
    private boolean isClick; // 用户修改过默认的数据
    private Calendar schedulingPlanStartTime = Calendar.getInstance(); // 调度计划任务开始时间
    private Calendar schedulingPlanFinishTime = Calendar.getInstance(); // 调度计划任务结束时间
    private Map<Integer, User> userMap = new HashMap<>();
    @Autowired
    SchedulingTask schedulingTask; // ARouter自动解析参数对象

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dispatch_declare;
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
        if (schedulingTask.getSchedulingPlanStartTime() != null) {
            schedulingPlanStartTime.setTime(schedulingTask.getSchedulingPlanStartTime());
        }
        if (schedulingTask.getSchedulingPlanFinishTime() != null) {
            schedulingPlanFinishTime.setTime(schedulingTask.getSchedulingPlanFinishTime());
        }
        tvSchedulingPlanStartTime.setText(TgDateUtil.date2String(schedulingPlanStartTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvSchedulingPlanFinishTime.setText(TgDateUtil.date2String(schedulingPlanFinishTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        initAdapter();
        getData();
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
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showDialogList(position);
            }
        });
    }

    /**
     * 得到名单选择框
     */
    private void getData() {
        TgApplication.getRetrofit().create(TaskService.class)
                .findByMaintenanceWorkItem(schedulingTask.getMaintenanceTaskItem())
                .enqueue(new TgRetrofitCallback<List<MaintenanceWorkPositionBean>>(this, true, true) {
                    @Override
                    public void successHandler(List<MaintenanceWorkPositionBean> list) {
                        mAdapter.setNewData(list);
                        if (!mAdapter.getData().isEmpty()) { // 如果派工人员数据，显示派工按钮
                            tvDispatch.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * OnClick
     */
    @OnClick({R.id.iv_black, R.id.tvDispatch, R.id.tvSchedulingPlanStartTime, R.id.tvSchedulingPlanFinishTime})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.iv_black:
                isEditDialog();
                break;
            case R.id.tvSchedulingPlanStartTime:
                TimePickerView pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        schedulingPlanStartTime.setTime(date);
                        isClick = true;
                        tvSchedulingPlanStartTime.setText(TgDateUtil.date2String(schedulingPlanStartTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).isDialog(true).build();
                pvTime1.setDate(schedulingPlanStartTime);
                pvTime1.show();
                break;
            case R.id.tvSchedulingPlanFinishTime:
                TimePickerView pvTime2 = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (schedulingPlanStartTime.getTime().getTime() > date.getTime()) {
                            TgToastUtil.showLong("结束时间早于开始时间!");
                            return;
                        }
                        schedulingPlanFinishTime.setTime(date);
                        isClick = true;
                        tvSchedulingPlanFinishTime.setText(TgDateUtil.date2String(schedulingPlanFinishTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).isDialog(true).build();
                pvTime2.setDate(schedulingPlanFinishTime);
                pvTime2.show();
                break;
            case R.id.tvDispatch:
                String content = "确定要派工吗？";
                if (userMap.size() != mAdapter.getData().size()) {
                    content = "还有编组没有选择派工人员，确定要派工吗？";
                }
                //post派工,提交修改后的人员名单
                TgDialogUtil.showDialog(this, content, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                        postDispatchDeclareYes();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 如果用户编辑过数据，但是没有保存，提示是否没有保存就退出
     */
    private void isEditDialog() {
        if (isClick) { // 修改过数据
            TgDialogUtil.showDialog(this, "您修改了数据,但没有确定,是否要返回?", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                    defaultFinish();
                }
            });
        } else {
            defaultFinish();
        }
    }

    @Override
    public void onBackPressed() {
        isEditDialog();
    }

    /**
     * 发送网络post,上传派工人员
     */
    private void postDispatchDeclareYes() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", schedulingTask.getId());
        map.put("dispatchPlanStartTime", TgDateUtil.date2String(schedulingPlanStartTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
        map.put("dispatchPlanFinishTime", TgDateUtil.date2String(schedulingPlanFinishTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
        List<DispatchTaskItem> list = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            DispatchTaskItem dispatchTaskItem = new DispatchTaskItem();
            MaintenanceWorkPositionBean maintenanceWorkPositionBean = mAdapter.getData().get(i);
            User user = userMap.get(i);
            dispatchTaskItem.setWorkNum(maintenanceWorkPositionBean.getWorkNum());
            dispatchTaskItem.setWorkUserId(user.getId());
            dispatchTaskItem.setWorkUserUsername(user.getUsername());
            dispatchTaskItem.setWorkUserRealname(user.getRealname());
            list.add(dispatchTaskItem);
        }
        map.put("dispatchTaskItemsStr", TgJsonUtil.object2Json(list));
        TgApplication.getRetrofit().create(TaskService.class)
                .dispatch(RetrofitUtils.mapToRequestBodyMap(map))
                .enqueue(new TgRetrofitCallback<Object>(this, true, true) {
                    @Override
                    public void successHandler(Object o) {
                        TgToastUtil.showLong("派工成功!"); // 派工成功
                        EventBus.getDefault().post("SchedulingTaskRefresh");
                        defaultFinish();
                    }
                });
    }

    /**
     * 侧滑
     */
    private void showDialogList(final int pos) {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        View view = getLayoutInflater().inflate(R.layout.dialog_expandable_user, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.END | Gravity.TOP);
        window.setWindowAnimations(R.style.dialogWindowAnim); // 进入，退出动画
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (getResources().getDisplayMetrics().widthPixels * 25) / 100; // 宽度
        lp.height = getResources().getDisplayMetrics().heightPixels - TgBarUtil.getStatusBarHeight(); // 高度
        window.setAttributes(lp);
        dialog.show();
        TextView textView = view.findViewById(R.id.tv_expandable_content);
        if (userMap.get(pos) != null) {
            textView.setText("当前选择人员：" + userMap.get(pos).getRealname());
        }
        ExpandableListView expandableListView = view.findViewById(R.id.expandable);
        expandableListView.setGroupIndicator(getDrawable(R.drawable.group_icon_selector));
        expandableListView.setAdapter(new UserExpandableListViewAdapter(this));
        /**
         * 1 单击事件 返回 false表示不触发单击事件
         */
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView parent, final View v, final int groupPosition, final long id) {
                return false;
            }
        });
        /**
         * Child 单击事件
         */
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View view, final int groupPosition, final int childPosition, final long id)
                    throws RuntimeException {
                // 得到被点击的选项
                User user = (User) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                userMap.put(pos, user);
                mAdapter.getData().get(pos).setName(user.getRealname());
                mAdapter.notifyItemChanged(pos);
                isClick = true;
                dialog.dismiss();
                return true;
            }
        });
    }
}
