package com.turingoal.bts.dispatch.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgArouterPaths;
import com.turingoal.bts.dispatch.app.TgSystemHelper;
import com.turingoal.bts.dispatch.ui.activity.common.TgVideoPlayerActivity;
import com.turingoal.common.android.bean.TgPhotoGroupBean;
import com.turingoal.common.android.util.io.TgJsonUtil;
import com.turingoal.common.android.util.media.TgGlideUtil;
import com.turingoal.common.android.util.media.TgMediaUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 故障查看照片adapter
 */

public class PhotoDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PhotoDetailAdapter(List<String> data) {
        super(R.layout.item_photo_detail, data);
    }

    class NetWorkAsyncTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public NetWorkAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap netVideoBitmap = TgMediaUtil.getNetVideoBitmap(strings[0]); // 耗时操作
            return netVideoBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (mContext instanceof Activity) {
                if (!((Activity) mContext).isFinishing()) { // mContext存活
                    TgGlideUtil.load(mContext, bitmap, imageView);
                }
            }
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String dataUrl) {
        if (dataUrl.endsWith(".mp4")) {
            new NetWorkAsyncTask((ImageView) helper.getView(R.id.ivPhoto)).execute(dataUrl);
            helper.setGone(R.id.ivVideo, true); // 显示播放按钮
        } else {
            TgGlideUtil.load(mContext, dataUrl, (ImageView) helper.getView(R.id.ivPhoto)); // 设置图片
            if (dataUrl.startsWith("content")) {
                helper.setGone(R.id.ivVideo, true); // 显示播放按钮
            } else {
                helper.setGone(R.id.ivVideo, false);
            }
        }
        // 点击事件
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (dataUrl.startsWith("content") || dataUrl.endsWith(".mp4")) { // 查看视频
                    TgVideoPlayerActivity.actionStart(mContext, dataUrl);
                } else { // 查看图片
                    List<String> photos = new ArrayList<>(mData);
                    Iterator<String> it = photos.iterator();
                    while (it.hasNext()) { // 删除mp4结尾的数据
                        String x = it.next();
                        if (x.startsWith("content") || x.endsWith(".mp4")) {
                            it.remove();
                        }
                    }
                    TgPhotoGroupBean photoBean = new TgPhotoGroupBean();
                    photoBean.setIndexNum(photos.indexOf(dataUrl));
                    photoBean.setPhotos(photos);
                    TgSystemHelper.handleIntentWithStr(TgArouterPaths.PHOTO_SHOW, "photoBeanStr", TgJsonUtil.object2Json(photoBean)); // 参考图片
                }
            }
        });
    }
}
