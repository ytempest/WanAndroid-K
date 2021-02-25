package com.ytempest.wanandroid.activity.register

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.ytempest.tool.util.RegexUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.activity.MvpActivity
import com.ytempest.wanandroid.databinding.ActivityRegisterBinding
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.listener.TextWatcherListener
import com.ytempest.wanandroid.utils.SpaceInputFilter
import com.ytempest.wanandroid.utils.StatusBarUtil

/**
 * @author heqidu
 * @since 21-2-22
 */
class RegisterActivity : MvpActivity<RegisterPresenter, ActivityRegisterBinding>(), IRegisterView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, binding.root)

        val filters = arrayOf(SpaceInputFilter())
        val textWatcher = object : TextWatcherListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (binding.accountEt.length() > 0 && binding.pwdEt.length() > 0 && binding.pwdConfirmEt.length() > 0) {
                    binding.registerBtn.setNormalStatus()
                } else {
                    binding.registerBtn.setDisableStatus()
                }
            }
        }

        arrayOf(binding.accountEt, binding.pwdEt, binding.pwdConfirmEt).forEach {
            it.filters = filters
            it.addTextChangedListener(textWatcher)
        }

        showSoftKeyBoard(binding.accountEt)

        binding.backView.setOnClickListener { finish() }
        binding.registerBtn.setOnClickListener {
            val accountMinLen = resources.getInteger(R.integer.account_min_len)
            val account: String = binding.accountEt.text.toString().trim()
            if (account.length < accountMinLen) {
                showToast(getString(R.string.account_len_limit, accountMinLen))
                return@setOnClickListener
            }

            if (!RegexUtils.isPhone(account) && !RegexUtils.isEmail(account)) {
                showToast(R.string.account_format_err)
                return@setOnClickListener
            }

            val pwdMinLen = resources.getInteger(R.integer.password_min_len)
            val pwd = binding.pwdEt.text.toString().trim()
            val confirmPwd: String = binding.pwdConfirmEt.text.toString().trim()
            if (pwd.length < pwdMinLen || confirmPwd.length < pwdMinLen) {
                showToast(getString(R.string.pwd_len_limit, pwdMinLen))
                return@setOnClickListener
            }

            if (pwd != confirmPwd) {
                showToast(R.string.pwd_not_match)
                return@setOnClickListener
            }

            mPresenter.register(account, pwd, confirmPwd)
        }
    }

    /**
     * 自动对焦到输入框，并打开软键盘
     */
    private fun showSoftKeyBoard(editText: EditText) {
        // 延迟200毫秒弹出，以防止自动弹出失效
        editText.postDelayed({
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            inputMethodManager?.let {
                editText.requestFocus()
                inputMethodManager.showSoftInput(editText, 0)
            }
        }, 200)
    }

    override fun onRegisterSuccess(loginBean: LoginBean) {
        showToast(R.string.register_success)
        finish()
    }

    override fun onRegisterFail(code: Int, throwable: Throwable) = when (code) {
        ErrCode.NET_ERR -> showToast(R.string.net_err)
        ErrCode.DATA_ERR -> showToast(R.string.account_pwd_err)
        ErrCode.SRC_ERR -> showToast(throwable.message)
        else -> showToast(R.string.unknown_err)
    }

}