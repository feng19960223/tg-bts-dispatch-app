package com.turingoal.bts.dispatch.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgApplication;
import com.turingoal.bts.dispatch.bean.User;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 下拉分组，备选人员aptedr
 */

public class UserExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context mContext; // context
    private Map<String, List<User>> map; // new Adapter时传入一个list，按一个字段，转换成map
    private List<String> keys; // map的key

    public UserExpandableListViewAdapter(final Context context) {
        this.mContext = context;
        List<User> users = TgApplication.getBoxStore().boxFor(User.class).getAll();
        getMapData(users); // 整合
        dataSort(); // 排序
    }

    /**
     * 获得父项的数量
     */
    @Override
    public int getGroupCount() {
        return map == null ? 0 : map.size();
    }

    /**
     * 获得某个父项的子项数目
     */
    @Override
    public int getChildrenCount(final int groupPosition) {
        return map == null ? 0 : map.get(getKey(groupPosition)).size();
    }

    /**
     * 获得某个父项
     */
    @Override
    public List<User> getGroup(final int groupPosition) {
        return map == null ? null : map.get(getKey(groupPosition));
    }

    /**
     * 获得某个父项的某个子项
     */
    @Override
    public User getChild(final int groupPosition, final int childPosition) {
        return map == null ? null : map.get(getKey(groupPosition)).get(childPosition);
    }

    /**
     * 获得某个父项的id
     */
    @Override
    public long getGroupId(final int groupPosition) {
        return groupPosition;
    }

    /**
     * 获得某个父项的某个子项的id
     */
    @Override
    public long getChildId(final int groupPosition, final int childPosition) {
        return childPosition;
    }

    /**
     * 按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * ********** 获得父项显示的view *********
     */
    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_expandable_parent, null);
        }
        convertView.setTag(R.layout.item_expandable_parent, groupPosition);
        convertView.setTag(R.layout.item_expandable_child, -1);
        TextView text = convertView.findViewById(R.id.tv_parent);
        text.setText(getKey(groupPosition));
        return convertView;
    }

    /**
     * ********** 获得子项显示的view *********
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_expandable_child, null);
        }
        convertView.setTag(R.layout.item_expandable_parent, groupPosition);
        convertView.setTag(R.layout.item_expandable_child, childPosition);
        TextView text = convertView.findViewById(R.id.tv_child);
        text.setText(getChild(groupPosition, childPosition).getRealname());
        return convertView;
    }

    /**
     * 子项是否可选中，如果需要设置子项的点击事件，需要返回true
     */
    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {
        return true;
    }

    /**
     * 将List<T>根据一个字段，变为Map<String,List<T>>数据
     * 给map赋值
     * *****String key = user.getWorkGroupName(); // 拆分的根据字段*****
     */
    private void getMapData(final List<User> listData) {
        if (map != null || listData == null || listData.size() < 1) {
            return;
        }
        map = new HashMap<>();
        keys = new ArrayList<>();
        for (User user : listData) {
            String key = user.getWorkGroupName(); // 拆分的根据字段
            if (TextUtils.isEmpty(key)) { // 如果key为空，跳过本次整合数据
                continue;
            }
            List<User> users = new ArrayList<>();
            if (map.containsKey(key)) { // 如果已经有了这个key，得到当前key对应的list，增加一个数据，再设置回去
                users = map.get(key);
            } else {
                keys.add(key);
            }
            users.add(user);
            map.put(key, users);
        }
    }

    /**
     * 得到map的第pos个key
     */
    private String getKey(final int pos) {
        if (keys == null) {
            return "";
        }
        if (keys.size() < pos) {
            return "";
        }
        return keys.get(pos);
    }

    /**
     * 给map按一定顺序排序
     */
    private void dataSort() {
        if (map != null) {
            for (String key : map.keySet()) {
                Collections.sort(map.get(key), new Comparator<User>() { // 排序规则
                    @Override
                    public int compare(final User o1, final User o2) {
                        CollationKey key1 = Collator.getInstance().getCollationKey(Pinyin.toPinyin((o1).getRealname(), "")); // 字符串转拼音，分隔符""
                        CollationKey key2 = Collator.getInstance().getCollationKey(Pinyin.toPinyin((o2).getRealname(), ""));
                        return key1.compareTo(key2);
                    }
                });
            }
        }
    }
}
