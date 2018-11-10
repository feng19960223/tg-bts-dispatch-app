package com.turingoal.bts.dispatch.ui.fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.turingoal.bts.dispatch.R;
import com.turingoal.bts.dispatch.app.TgAppConfig;
import com.turingoal.common.android.base.TgBaseFragment;
import com.turingoal.common.android.util.doc.TgPdfUtil;

import butterknife.BindView;

/**
 * 帮助Fragment
 */
public class HelpFragment extends TgBaseFragment {
    @BindView(R.id.help_pdfView)
    PDFView pdfView; // pdf组件
    private boolean isLoad = true; // 只加载一次,控制器

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_help;
    }

    @Override
    protected void initialized() {
        initToolbar(R.id.tv_main_title_text,"系统帮助");
    }

    @Override
    public void onHiddenChanged(final boolean hidden) { // PDFView在initialized中显示，是不会成功的
        super.onHiddenChanged(hidden);
        // 不是隐藏状态
        if (!hidden && isLoad) {
            isLoad = false;
            TgPdfUtil.loadPdfFromAsset(pdfView, TgAppConfig.HELP_DOC_FILE_NAME); // 从Asset加载帮助文件pdf
        }
    }
}
