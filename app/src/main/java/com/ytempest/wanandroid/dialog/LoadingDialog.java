package com.ytempest.wanandroid.dialog;

import android.content.Context;
import androidx.annotation.NonNull;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.BaseDialog;

/**
 * @author heqidu
 * @since 2020/8/13
 */
@InjectLayout(R.layout.dialog_loading)
public class LoadingDialog extends BaseDialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.common_dialog_transparent);
        setCanceledOnTouchOutside(false);
    }
}
