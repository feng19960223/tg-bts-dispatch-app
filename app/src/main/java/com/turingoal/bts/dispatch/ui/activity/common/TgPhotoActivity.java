package com.turingoal.bts.dispatch.ui.activity.common;

import android.support.v4.view.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.common.android.base.TgBaseActivity;
import com.turingoal.common.android.bean.TgPhotoGroupBean;
import com.turingoal.common.android.ui.adapter.TgPhotoViewPagerAdapter;
import com.turingoal.common.android.ui.fragment.TgPhotoFragment;
import com.turingoal.common.android.util.io.TgJsonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * 通用单独图片查看界面
 */
@Route(path = TgArouterPaths.PHOTO_SHOW)
public class TgPhotoActivity extends TgBaseActivity {
    @BindView(R.id.vpPhoto)
    ViewPager vpPhoto; // 图片
    @Autowired
    String photoBeanStr; // 照片数据
    private List<TgPhotoFragment> fragments; // 图片fragment

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_photo;
    }

    @Override
    protected void initialized() {
        TgPhotoGroupBean photoBean = TgJsonUtil.json2Object(photoBeanStr, TgPhotoGroupBean.class);
        if (photoBean != null) {
            fragments = TgPhotoFragment.createPhotoFragments(photoBean);
        }
        vpPhoto.setAdapter(new TgPhotoViewPagerAdapter(getSupportFragmentManager(), fragments));
        vpPhoto.setCurrentItem(photoBean.getIndexNum());
    }
}