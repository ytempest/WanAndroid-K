package com.ytempest.wanandroid.activity.register

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.base.activity.MvpActivity
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.util.RegexUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.listener.TextWatcherListener
import com.ytempest.wanandroid.utils.SpaceInputFilter
import com.ytempest.wanandroid.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author heqidu
 * @since 21-2-22
 */
@InjectLayout(R.layout.activity_register)
class RegisterActivity : MvpActivity<RegisterPresenter>(), IRegisterView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, ll_register_root)

        val filters = arrayOf(SpaceInputFilter())
        et_register_account.filters = filters
        et_register_pwd.filters = filters
        et_register_pwd_confirm.filters = filters

        val textWatcher = object : TextWatcherListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)
                if (et_register_account.length() > 0 && et_register_pwd.length() > 0 && et_register_pwd_confirm.length() > 0) {
                    view_register.setNormalStatus()
                } else {
                    view_register.setDisableStatus()
                }
            }
        }

        et_register_account.addTextChangedListener(textWatcher)
        et_register_pwd.addTextChangedListener(textWatcher)
        et_register_pwd_confirm.addTextChangedListener(textWatcher)

        showSoftKeyBoard(et_register_account)

        iv_register_back.setOnClickListener { finish() }
        view_register.setOnClickListener {
            val accountMinLen = resources.getInteger(R.integer.account_min_len)
            val account: String = et_register_account.text.toString().trim()
            if (account.length < accountMinLen) {
                showToast(getString(R.string.account_len_limit, accountMinLen))
                return@setOnClickListener
            }

            if (!RegexUtils.isPhone(account) && !RegexUtils.isEmail(account)) {
                showToast(R.string.account_format_err)
                return@setOnClickListener
            }

            val pwdMinLen = resources.getInteger(R.integer.password_min_len)
            val pwd = et_register_pwd.text.toString().trim()
            val confirmPwd: String = et_register_pwd_confirm.text.toString().trim()
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