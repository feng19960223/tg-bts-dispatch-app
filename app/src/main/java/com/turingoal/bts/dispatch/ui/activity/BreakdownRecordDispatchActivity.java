package com.turingoal.bts.dispatch.ui.activity;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.turingoal.bts.common.android.biz.domain.BreakdownRecordItem;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownPattern;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownSource;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownSystemTypes;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.bean.User;
import com.turingoal.bts.dispatch.service.BreakdownRecordItemService;
import com.turingoal.bts.dispatch.ui.adapter.PhotoDetailAdapter;
import com.turingoal.bts.dispatch.ui.adapter.UserExpandableListViewAdapter;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.ui.TgBarUtil;
import com.turingoal.common.android.util.ui.TgDialogUtil;
import com.turingoal.common.android.util.ui.TgToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 故障派工
 */
@Route(path = TgArouterPaths.BREAKDOWN_RECORD_ITEM_DISPATCH)
public class BreakdownRecordDispatchActivity extends TgBaseActivity {
    @BindView(R.id.iv_title_delete)
    ImageView ivDelete;
    @BindView(R.id.tvDiscoveryTime)
    TextView tvDiscoveryTime; // 发现时间
    @BindView(R.id.tvSource)
    TextView tvSource; // 故障来源
    @BindView(R.id.tvTrainSetCodeNum)
    TextView tvTrainSetCodeNum; // 故障车组
    @BindView(R.id.tvhandlerRealname)
    TextView tvhandlerRealname; // 维修人员
    @BindView(R.id.tvCarriage)
    TextView tvCarriage; // 故障车厢
    @BindView(R.id.tvDiscoveryDesc)
    TextView tvDiscoveryDesc; // 问题描述
    @BindView(R.id.tvSystemType)
    TextView tvSystemType; // 系统分类
    @BindView(R.id.tvPattern)
    TextView tvPattern; // 故障模式
    @BindView(R.id.tvDispatchPlanStartTime)
    TextView tvDispatchPlanStartTime; // 计划开始时间
    @BindView(R.id.tvDispatchPlanFinishTime)
    TextView tvDispatchPlanFinishTime; // 计划结束时间
    @BindView(R.id.tvQcRealname)
    TextView tvQcRealname; // 质检员
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private User handlerUser = new User(); // 维修人员被选中的user
    private User qcUser = new User(); // 质检人员被选中的user
    private boolean isClick = false; // 用户修改过默认的数据
    private Calendar dispatchPlanStartTime = Calendar.getInstance(); // 计划开始时间
    private Calendar dispatchPlanFinishTime = Calendar.getInstance(); // 计划结束时间
    @Autowired
    BreakdownRecordItem breakdownRecordItem; // ARouter自动解析参数对象

    @Override
    protected int getLayoutId() {
        return R.layout.activity_breakdown_record_item_dispatch;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_title_text, R.id.iv_black, "故障派工", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isEditDialog();
            }
        });
        ivDelete.setVisibility(View.VISIBLE);
        tvDiscoveryTime.setText("发现时间：" + TgDateUtil.date2String(breakdownRecordItem.getDiscoveryTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvSource.setText("故障来源：" + ConstantMontorBreakdownSource.getStr(breakdownRecordItem.getSource()));
        tvTrainSetCodeNum.setText("故障车组：" + breakdownRecordItem.getTrainSetCodeNum());
        tvCarriage.setText("故障车厢：" + breakdownRecordItem.getCarriage());
        tvSystemType.setText("系统分类：" + ConstantMontorBreakdownSystemTypes.getStr(breakdownRecordItem.getSystemType()));
        tvPattern.setText("故障模式：" + ConstantMontorBreakdownPattern.getStr(breakdownRecordItem.getPattern()));
        dispatchPlanFinishTime.add(Calendar.DAY_OF_MONTH, 1); // 计划结束时间，当前时间加一天
        tvDispatchPlanStartTime.setText(TgDateUtil.date2String(dispatchPlanStartTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvDispatchPlanFinishTime.setText(TgDateUtil.date2String(dispatchPlanFinishTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
        tvDiscoveryDesc.setText(breakdownRecordItem.getDescription()); // 问题描述
        if (breakdownRecordItem.getFilePaths() != null) {
            if (breakdownRecordItem.getFilePaths().size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 横滑
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(new PhotoDetailAdapter(breakdownRecordItem.getFilePaths()));
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        isEditDialog();
    }

    /**
     * 如果用户编辑过数据，但是没有保存，提示是否没有保存就退出
     */
    private void isEditDialog() {
        if (isClick) { // 修改过数据
            TgDialogUtil.showDialog(this, "您修改了数据,但没有确定派工,是否要返回?", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                    defaultFinish();
                }
            });
        } else {
            defaultFinish();
        }
    }


    /**
     * OnClick
     */
    @OnClick({R.id.tv_fault_report_yes, R.id.tvQcRealname, R.id.tvhandlerRealname, R.id.tvDispatchPlanStartTime, R.id.tvDispatchPlanFinishTime, R.id.iv_title_delete})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.tvQcRealname:
                showDialogList(false);
                break;
            case R.id.tvhandlerRealname:
                showDialogList(true);  // 弹出选择人员对话框
                break;
            case R.id.tv_fault_report_yes:
                submitRequest(); // 提交请求
                break;
            case R.id.tvDispatchPlanStartTime:
                TimePickerView pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        isClick = true;
                        dispatchPlanStartTime.setTime(date);
                        tvDispatchPlanStartTime.setText(TgDateUtil.date2String(dispatchPlanStartTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).isDialog(true).build();
                pvTime1.setDate(dispatchPlanStartTime);
                pvTime1.show();
                break;
            case R.id.tvDispatchPlanFinishTime:
                TimePickerView pvTime2 = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        if (dispatchPlanStartTime.getTime().getTime() > date.getTime()) {
                            TgToastUtil.showLong("结束时间早于开始时间!");
                            return;
                        }
                        isClick = true;
                        dispatchPlanFinishTime.setTime(date);
                        tvDispatchPlanFinishTime.setText(TgDateUtil.date2String(dispatchPlanFinishTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS_ZH));
                    }
                }).setType(new boolean[]{true, true, true, true, true, true}).isDialog(true).build();
                pvTime2.setDate(dispatchPlanFinishTime);
                pvTime2.show();
                break;
            case R.id.iv_title_delete:
                TgDialogUtil.showDialog(this, "确定要删除本条故障信息吗?", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                        deleteRequest();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 删除一条故障记录
     */
    private void deleteRequest() {
        TgApplication.getRetrofit().create(BreakdownRecordItemService.class)
                .delete(breakdownRecordItem.getId())
                .enqueue(new TgRetrofitCallback<Object>(this, true, true) {
                    @Override
                    public void successHandler(Object o) {
                        TgToastUtil.showLong("删除成功");
                        EventBus.getDefault().post("BreakdownRefresh");
                        defaultFinish();
                    }
                });
    }

    /**
     * 故障派工
     */
    private void submitRequest() {
        if (TextUtils.isEmpty(handlerUser.getRealname())) {
            TgToastUtil.showLong("请选择维修人员!");
            return;
        }
        if (TextUtils.isEmpty(qcUser.getRealname())) {
            TgToastUtil.showLong("请选择质检人员!");
            return;
        }
        if (dispatchPlanStartTime.getTime().getTime() >= dispatchPlanFinishTime.getTime().getTime()) {
            TgToastUtil.showLong("请选择正确的开始或结束时间!");
            return;
        }
        TgDialogUtil.showDialog(this, "确定要派工吗?", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull final MaterialDialog dialog, @NonNull final DialogAction which) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", "" + breakdownRecordItem.getId());
                map.put("handlerUserId", "" + handlerUser.getId());
                map.put("handlerUserRealname", "" + handlerUser.getRealname());
                map.put("handlerUserUsername", "" + handlerUser.getUsername());
                map.put("qcUserId", "" + qcUser.getId());
                map.put("qcUserRealname", "" + qcUser.getRealname());
                map.put("qcUserUsername", "" + qcUser.getUsername());
                map.put("dispatchPlanStartTime", TgDateUtil.date2String(dispatchPlanStartTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
                map.put("dispatchPlanFinishTime", TgDateUtil.date2String(dispatchPlanFinishTime.getTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_SS));
                map.put("dispatchUserId", TgApplication.getTgUserPreferences().getUserId());
                map.put("dispatchUserUsername", TgApplication.getTgUserPreferences().getUsername());
                map.put("dispatchUserRealname", TgApplication.getTgUserPreferences().getRealname());
                TgApplication.getRetrofit().create(BreakdownRecordItemService.class)
                        .dispatch(map)
                        .enqueue(new TgRetrofitCallback<Object>(BreakdownRecordDispatchActivity.this, true, true) {
                            @Override
                            public void successHandler(Object o) {
                                TgToastUtil.showLong("派工成功!"); // 派工成功
                                EventBus.getDefault().post("BreakdownRefresh");
                                defaultFinish();
                            }
                        });
            }
        });
    }

    /**
     * 侧滑,isHandlerOrQc是处理人员还是质检人员，true处理，false质检
     */
    private void showDialogList(final boolean isHandlerOrQc) {
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
        if (isHandlerOrQc) {
            textView.setText("当前选择人员：" + handlerUser.getRealname());
        } else {
            textView.setText("当前选择人员：" + qcUser.getRealname());
        }
        ExpandableListView expandableListView = view.findViewById(R.id.expandable);
        expandableListView.setGroupIndicator(getDrawable(R.drawable.group_icon_selector));
        expandableListView.setAdapter(new UserExpandableListViewAdapter(this));
        // 单击事件 返回 false表示不触发单击事件
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView parent, final View v, final int groupPosition, final long id) {
                return false;
            }
        });
        // Child 单击事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View view, final int groupPosition, final int childPosition, final long id)
                    throws RuntimeException {
                // 得到被点击的选项
                if (isHandlerOrQc) {
                    handlerUser = (User) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    tvhandlerRealname.setText(handlerUser.getRealname());
                } else {
                    qcUser = (User) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    tvQcRealname.setText(qcUser.getRealname());
                }
                isClick = true;
                dialog.dismiss();
                return true;
            }
        });
    }
}
