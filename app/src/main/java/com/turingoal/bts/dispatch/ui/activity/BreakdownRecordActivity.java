package com.turingoal.bts.dispatch.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bts.common.android.biz.domain.BreakdownRecord;
import com.turingoal.bts.common.android.biz.domain.BreakdownRecordItem;
import com.turingoal.bts.common.android.constants.ConstantMontorBreakdownSource;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.service.BreakdownRecordItemService;
import com.turingoal.bts.dispatch.ui.adapter.BreakdownItemAdapter;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.util.lang.TgDateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * 故障详情
 */
@Route(path = TgArouterPaths.BREAKDOWN_RECORD)
public class BreakdownRecordActivity extends TgBaseActivity {
    @BindView(R.id.tvCreateTime)
    TextView tvCreateTime; // 创建时间
    @BindView(R.id.tvTrainSetCodeNum)
    TextView tvTrainSetCodeNum; // 车组
    @BindView(R.id.tvSource)
    TextView tvSource; // 来源
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.rvLegacyBreakdown)
    RecyclerView rvLegacyBreakdown; // 遗留故障
    @BindView(R.id.llLegacy)
    LinearLayout llLegacy; // 遗留故障视图控制器
    private BreakdownItemAdapter adapter = new BreakdownItemAdapter();
    private BreakdownItemAdapter legacyAdapter = new BreakdownItemAdapter(); // 遗留
    @Autowired
    BreakdownRecord breakdownRecord; // ARouter自动解析参数对象

    @Override
    protected int getLayoutId() {
        return R.layout.activity_breakdown_record;
    }

    @Override
    protected void initialized() {
        EventBus.getDefault().register(this);
        initToolbar(R.id.tv_title_text, R.id.iv_black, "故障详情");
        initAdapter();
        tvCreateTime.setText("创建时间：" + TgDateUtil.date2String(breakdownRecord.getCreateTime(), TgDateUtil.FORMAT_YYYY_MM_DD_HH_MM_ZH));
        tvTrainSetCodeNum.setText("故障车组:" + breakdownRecord.getTrainSetCodeNum());
        tvSource.setText("故障来源:" + ConstantMontorBreakdownSource.getStr(breakdownRecord.getSource()));
        getData();
        getLegacyData();
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvLegacyBreakdown.setLayoutManager(new LinearLayoutManager(this));
        adapter.openLoadAnimation(); // 动画
        legacyAdapter.openLoadAnimation(); // 动画
        adapter.setEmptyView(getNotDataView((ViewGroup) recyclerView.getParent())); // 设置空白view
        recyclerView.setAdapter(adapter);
        rvLegacyBreakdown.setAdapter(legacyAdapter);
    }

    /**
     * 得到分故障
     */
    private void getData() {
        TgApplication.getRetrofit().create(BreakdownRecordItemService.class)
                .getList(breakdownRecord.getId())
                .enqueue(new TgRetrofitCallback<List<BreakdownRecordItem>>(this, true, true) {
                    @Override
                    public void successHandler(List<BreakdownRecordItem> breakdownRecordItems) {
                        adapter.setNewData(breakdownRecordItems); // 加载数据
                    }
                });
    }

    /**
     * 得到遗留故障
     */
    private void getLegacyData() {
        TgApplication.getRetrofit().create(BreakdownRecordItemService.class)
                .getLegacyList(breakdownRecord.getId(), breakdownRecord.getTrainSetCodeNum())
                .enqueue(new TgRetrofitCallback<List<BreakdownRecordItem>>(this, true, true) {
                    @Override
                    public void successHandler(List<BreakdownRecordItem> breakdownRecordItems) {
                        legacyAdapter.setNewData(breakdownRecordItems); // 加载数据
                        if (legacyAdapter.getData().size() > 0) { // 如果没有遗留数据，隐藏视图
                            llLegacy.setVisibility(View.VISIBLE);
                        } else {
                            llLegacy.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message) {
        if ("BreakdownRefresh".equals(message)) { // 数据更新，刷新本页面
            getData();
            getLegacyData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
