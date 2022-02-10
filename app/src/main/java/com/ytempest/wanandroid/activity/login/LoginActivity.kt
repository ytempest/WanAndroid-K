package com.ytempest.wanandroid.activity.login

import android.text.InputFilter
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.tool.util.RegexUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.register.RegisterActivity
import com.ytempest.wanandroid.base.activity.MVVMActivity
import com.ytempest.wanandroid.base.createViewModel
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.ActivityLoginBinding
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.listener.PasswordStatusChangeListener
import com.ytempest.wanandroid.listener.TextWatcherListener
import com.ytempest.wanandroid.utils.SpaceInputFilter

/**
 * @author heqidu
 * @since 21-2-22
 */

class LoginActivity : MVVMActivity<ActivityLoginBinding>(), ILoginView {

    override val viewModel by lazy { createViewModel<LoginViewModel>() }

    override fun onViewCreated() {
        binding.apply {
            pwdStatusView.setOnClickListener(PasswordStatusChangeListener(passwordEt))

            // EditText空格过滤器
            val filters = arrayOf<InputFilter>(SpaceInputFilter())
            accountEt.filters = filters
            passwordEt.filters = filters

            // 输入监听
            val textWatcher = object : TextWatcherListener() {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    super.onTextChanged(s, start, before, count)
                    if (accountEt.length() > 0 && passwordEt.length() > 0) {
                        confirmBtn.setNormalStatus()
                    } else {
                        confirmBtn.setDisableStatus()
                    }
                }
            }
            accountEt.addTextChangedListener(textWatcher)
            passwordEt.addTextChangedListener(textWatcher)

            confirmBtn.setOnClickListener {
                val account = accountEt.text.toString().trim()
                if (!RegexUtils.isPhone(account) && !RegexUtils.isEmail(account)) {
                    showToast(R.string.account_format_err)
                    return@setOnClickListener
                }

                val pwd = passwordEt.text.toString()
                showLoading()
                viewModel.login(account, pwd)
            }
            registerBtn.setOnClickListener { ActivityLauncher.startActivity(this@LoginActivity, RegisterActivity::class.java) }
        }

        viewModel.loginResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    onLoginSuccess(entity.data)
                },
                onFail = { entity ->
                    onLoginFail(entity.code, entity.throwable)
                },
                onComplete = {
                    stopLoading()
                }))
    }

    override fun onLoginSuccess(loginBean: LoginBean) {
        finish()
    }

    override fun onLoginFail(code: Int, throwable: Throwable?) = when (code) {
        ErrCode.NET_ERR -> showToast(R.string.net_err)
        ErrCode.DATA_ERR -> showToast(R.string.account_pwd_err)
        ErrCode.SRC_ERR -> showToast(throwable?.message)
        else -> showToast(R.string.unknown_err)
    }
}