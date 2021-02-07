package com.ytempest.wanandroid.listener;

import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

/**
 * @author heqidu
 * @since 2020/8/12
 */
public class PasswordStatusChangeListener implements View.OnClickListener {

    private final EditText mPwdEditText;

    public PasswordStatusChangeListener(EditText pwdEditText) {
        this.mPwdEditText = pwdEditText;
    }

    @Override
    public void onClick(View view) {
        view.setSelected(!view.isSelected());
        if (view.isSelected()) {
            // 显示密码
            mPwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            // 隐藏密码
            mPwdEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // 最后把光标移动到末尾
        Editable editable = mPwdEditText.getText();
        Selection.setSelection(editable, editable.length());
    }
}
