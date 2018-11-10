package com.turingoal.bts.dispatch.ui.adapter;

import android.view.Gravity;

import com.turingoal.bts.dispatch.bean.TgMenuItem;

import java.util.List;

import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.TabView;

/**
 * MainTabAdapter
 */
public class MainTabAdapter implements TabAdapter {
    private List<TgMenuItem> menus;

    public MainTabAdapter(final List<TgMenuItem> menusParm) {
        this.menus = menusParm;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public TabView.TabBadge getBadge(final int position) {
        return null;
    }

    @Override
    public TabView.TabIcon getIcon(final int position) {
        TgMenuItem menu = menus.get(position);
        return new TabView.TabIcon.Builder().setIcon(menu.getSelectIcon(), menu.getNormalIcon())
                .setIconGravity(Gravity.START).setIconMargin(18).setIconSize(24, 24).build();
    }

    @Override
    public TabView.TabTitle getTitle(final int position) {
        TgMenuItem menu = menus.get(position);
        return new TabView.TabTitle.Builder().setContent(menu.getTitle()).setTextColor(0xFF188AE2, 0xFF424242)
                .setTextSize(17).build();
    }

    @Override
    public int getBackground(final int position) {
        return -1;
    }
}
