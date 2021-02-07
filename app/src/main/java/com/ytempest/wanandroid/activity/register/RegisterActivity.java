package com.ytempest.wanandroid.activity.register;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputFilter;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.tool.util.RegexUtils;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.activity.MvpActivity;
import com.ytempest.wanandroid.http.ErrCode;
import com.ytempest.wanandroid.http.bean.LoginBean;
import com.ytempest.wanandroid.listener.TextWatcherListener;
import com.ytempest.wanandroid.utils.SpaceInputFilter;
import com.ytempest.wanandroid.utils.StatusBarUtil;
import com.ytempest.wanandroid.widget.ModifiableButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author heqidu
 * @since 2020/8/14
 */
@InjectLayout(R.layout.activity_register)
public class RegisterActivity extends MvpActivity<RegisterPresenter> implements IRegisterContract.View {

    @BindView(R.id.ll_register_root)
    ViewGroup mRootView;
    @BindView(R.id.et_register_account)
    EditText mAccountET;
    @BindView(R.id.et_register_pwd)
    EditText mPwdET;
    @BindView(R.id.et_register_pwd_confirm)
    EditText mPwdConfirmET;
    @BindView(R.id.view_register)
    ModifiableButton mRegisterButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,mRootView);
        InputFilter[] filters = new InputFilter[]{new SpaceInputFilter()};
        mAccountET.setFilters(filters);
        mPwdET.setFilters(filters);
        mPwdConfirmET.setFilters(filters);

        TextWatcherListener textWatcher = new TextWatcherListener() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (mAccountET.length() > 0 && mPwdET.length() > 0 && mPwdConfirmET.length() > 0) {
                    mRegisterButton.setNormalStatus();
                } else {
                    mRegisterButton.setDisableStatus();
                }
            }
        };
        mAccountET.addTextChangedListener(textWatcher);
        mPwdET.addTextChangedListener(textWatcher);
        mPwdConfirmET.addTextChangedListener(textWatcher);

        showSoftKeyBoard(mAccountET);
    }

    /**
     * 自动对焦到输入框，并打开软键盘
     */
    private void showSoftKeyBoard(EditText editText) {
        // 延迟200毫秒弹出，以防止自动弹出失效
        editText.postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                editText.requestFocus();
                inputMethodManager.showSoftInput(editText, 0);
            }
        }, 200);
    }

    @OnClick(R.id.iv_register_back)
    void onBackClick() {
        finish();
    }

    @OnClick(R.id.view_register)
    void onRegisterClick() {
        int accountMinLen = getResources().getInteger(R.integer.account_min_len);
        String account = mAccountET.getText().toString().trim();
        if (account.length() < accountMinLen) {
            showToast(getString(R.string.account_len_limit, accountMinLen));
            return;
        }

        if (!RegexUtils.isPhone(account) && !RegexUtils.isEmail(account)) {
            showToast(R.string.account_format_err);
            return;
        }

        int pwdMinLen = getResources().getInteger(R.integer.password_min_len);
        String pwd = mPwdET.getText().toString().trim();
        String confirmPwd = mPwdConfirmET.getText().toString().trim();
        if (pwd.length() < pwdMinLen || confirmPwd.length() < pwdMinLen) {
            showToast(getString(R.string.pwd_len_limit, pwdMinLen));
            return;
        }

        if (!pwd.equals(confirmPwd)) {
            showToast(R.string.pwd_not_match);
            return;
        }

        mPresenter.register(account, pwd, confirmPwd);
    }

    @Override
    public void onRegisterSuccess(LoginBean loginBean) {
        showToast(R.string.register_success);
        finish();
    }

    @Override
    public void onRegisterFail(@ErrCode int code, Throwable throwable) {
        if (code == ErrCode.NET_ERR) {
            showToast(R.string.net_err);

        } else if (code == ErrCode.DATA_ERR) {
            showToast(R.string.account_pwd_err);

        } else if (code == ErrCode.SRC_ERR) {
            showToast(throwable.getMessage());

        } else {
            showToast(R.string.unknown_err);
        }
    }
}
