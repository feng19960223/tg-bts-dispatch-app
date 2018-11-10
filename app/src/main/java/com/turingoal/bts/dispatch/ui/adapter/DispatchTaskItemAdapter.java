package com.turingoal.bts.dispatch.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.common.android.biz.domain.DispatchTaskItem;
import com.turingoal.bts.dispatch.R;


/**
 * 任务人员详情adapter
 */
public class DispatchTaskItemAdapter extends BaseQuickAdapter<DispatchTaskItem, BaseViewHolder> {
    public DispatchTaskItemAdapter() {
        super(R.layout.item_dispatch_task_item);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final DispatchTaskItem dispatchTaskItem) {
        holder.setText(R.id.tvGroup, "" + dispatchTaskItem.getWorkNum())
                .setText(R.id.tvName, "" + dispatchTaskItem.getWorkUserRealname());
    }
}
