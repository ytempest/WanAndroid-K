package com.ytempest.wanandroid.activity.login

import android.os.Bundle
import android.text.InputFilter
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.activity.register.RegisterActivity
import com.ytempest.wanandroid.base.activity.MvpActivity
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.tool.util.RegexUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.listener.PasswordStatusChangeListener
import com.ytempest.wanandroid.listener.TextWatcherListener
import com.ytempest.wanandroid.utils.SpaceInputFilter
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author heqidu
 * @since 21-2-22
 */
@InjectLayout(R.layout.activity_login)
class LoginActivity : MvpActivity<LoginPresenter>(), ILoginView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view_login_pwd_status.setOnClickListener(PasswordStatusChangeListener(et_login_password))

        // EditText空格过滤器
        val filters = arrayOf<InputFilter>(SpaceInputFilter())
        et_login_account.filters = filters
        et_login_password.filters = filters

        // 输入监听
        val textWatcher = object : TextWatcherListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (et_login_account.length() > 0 && et_login_password.length() > 0) {
                    bt_login_confirm.setNormalStatus()
                } else {
                    bt_login_confirm.setDisableStatus()
                }
            }
        }
        et_login_account.addTextChangedListener(textWatcher)
        et_login_password.addTextChangedListener(textWatcher)

        tv_login_register.setOnClickListener { ActivityLauncher.startActivity(this, RegisterActivity::class.java) }
        bt_login_confirm.setOnClickListener {
            val account = et_login_account.text.toString()
            if (!RegexUtils.isPhone(account) && !RegexUtils.isEmail(account)) {
                showToast(R.string.account_format_err)
                return@setOnClickListener
            }

            val pwd = et_login_password.text.toString()
            mPresenter.login(account, pwd)
        }
    }

    override fun onLoginSuccess(loginBean: LoginBean) {
        finish()
    }

    override fun onLoginFail(code: Int, throwable: Throwable) = when (code) {
        ErrCode.NET_ERR -> showToast(R.string.net_err)
        ErrCode.DATA_ERR -> showToast(R.string.account_pwd_err)
        ErrCode.SRC_ERR -> showToast(throwable.message)
        else -> showToast(R.string.unknown_err)
    }
}