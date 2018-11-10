package com.turingoal.bts.dispatch.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.turingoal.bts.common.android.biz.domain.BreakdownRecord;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.service.BreakdownRecordService;
import com.turingoal.bts.dispatch.ui.adapter.BreakdownAdapter;
import com.turingoal.bts.dispatch.util.TgRetrofitCallback;
import com.turingoal.common.android.base.TgBaseFragment;
import com.turingoal.common.android.util.lang.TgDateUtil;
import com.turingoal.common.android.util.ui.TgDateTimePickUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 故障Fragment
 */
public class BreakdownFragment extends TgBaseFragment {
    @BindView(R.id.tv_main_head_date)
    TextView tvdDate; // 日期
    @BindView(R.id.ll_main_head_whiteornight)
    LinearLayout llMainHead; // 切换按钮，隐藏
    @BindView(R.id.ll_main_head_search)
    LinearLayout lldSearch; // 车组搜索
    @BindView(R.id.et_main_head_search)
    EditText etSearch; // 搜索输入
    @BindView(R.id.rv_main)
    RecyclerView rvMain; // RecyclerView
    private Date currentDate; // 日期
    private BreakdownAdapter mAdapter = new BreakdownAdapter(); // adapter
    private List<BreakdownRecord> breakdownRecordList = null; // 所有数据
    private boolean isSearch = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_breakdown;
    }

    @Override
    protected void initialized() {
        EventBus.getDefault().register(this);
        initToolbar(R.id.tv_main_title_text, "故障单");
        llMainHead.setVisibility(View.GONE);
        lldSearch.setVisibility(View.VISIBLE);
        etSearch.addTextChangedListener(searchWatcher);
        initAdapter();
        getData(Calendar.getInstance().getTime()); // 请求数据
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
     * OnClick
     */
    @OnClick({R.id.iv_main_head_before, R.id.tv_main_head_date, R.id.iv_main_head_next})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.iv_main_head_before: // 前一天
                isSearch = false;
                getData(TgDateUtil.getBeforeDay(currentDate)); // 切换时间重新获取数据
                break;
            case R.id.tv_main_head_date: // 日期
                TgDateTimePickUtil timeSelector = new TgDateTimePickUtil(getActivity(), currentDate, TgDateUtil.DEFAULT_START_DATE, TgDateUtil.DEFAULT_END_DATE, null);
                timeSelector.dateTimePickDialog(new TgDateTimePickUtil.OnSubmitSelectDateListener() {
                    public void onSumbitSelect(final Date date) {
                        if (currentDate.getTime() / 1000 / 60 / 60 / 24 != date.getTime() / 1000 / 60 / 60 / 24) { //用户没有选择当天
                            isSearch = false;
                            getData(date); // 选择日期
                        }
                    }
                });
                break;
            case R.id.iv_main_head_next: // 后一天
                if (!TgDateUtil.isToday(currentDate)) { // 如果现在选择的不是今天
                    isSearch = false;
                    getData(TgDateUtil.getNextDay(currentDate)); // 后一天
                }
                break;
            default:
                break;
        }
    }

    /**
     * event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String message) {
        if ("BreakdownRefresh".equals(message)) { // 数据更新，刷新本页面
            getData(currentDate);
        }
    }

    /**
     * 获取数据，并显示到RecyclerView
     */
    private void getData(final Date date) {
        etSearch.setText(""); // 清空搜索内容
        currentDate = date;
        tvdDate.setText(TgDateUtil.date2String(currentDate, TgDateUtil.FORMAT_YYYY_MM_DD)); // 设置日期
        mAdapter.setNewData(null);
        TgApplication.getRetrofit().create(BreakdownRecordService.class)
                .findByDate(TgDateUtil.date2String(currentDate))
                .enqueue(new TgRetrofitCallback<List<BreakdownRecord>>(getContext(), true, true) {
                    @Override
                    public void successHandler(List<BreakdownRecord> breakdownRecords) {
                        isSearch = true;
                        breakdownRecordList = breakdownRecords;
                        mAdapter.setNewData(breakdownRecords); // 加载数据
                    }
                });
    }

    /**
     * 把包含searchStr的数据查出来
     */
    private List<BreakdownRecord> getDataBySearch(final List<BreakdownRecord> listData, final String searchStr) {
        if (TextUtils.isEmpty(searchStr)) {
            return listData;
        } else {
            List<BreakdownRecord> newData = new ArrayList<>();
            for (BreakdownRecord breakdownRecord : listData) {
                if (breakdownRecord.getTrainSetCodeNum().contains(searchStr)) { // 包含
                    newData.add(breakdownRecord);
                }
            }
            return newData;
        }
    }

    /**
     * 搜索内容监听
     */
    private TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
        }

        @Override
        public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
        }

        @Override
        public void afterTextChanged(final Editable s) {
            if (isSearch) {
                mAdapter.setNewData(getDataBySearch(breakdownRecordList, s.toString().trim()));
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
