package com.ytempest.wanandroid.activity.register

import android.view.View
import com.ytempest.tool.util.RegexUtils
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.base.activity.MVVMActivity
import com.ytempest.wanandroid.base.createViewModel
import com.ytempest.wanandroid.base.vm.EntityObserver
import com.ytempest.wanandroid.databinding.ActivityRegisterBinding
import com.ytempest.wanandroid.http.ErrCode
import com.ytempest.wanandroid.http.bean.LoginBean
import com.ytempest.wanandroid.listener.TextWatcherListener
import com.ytempest.wanandroid.utils.CommonUtils
import com.ytempest.wanandroid.utils.SpaceInputFilter
import com.ytempest.wanandroid.utils.StatusBarUtil

/**
 * @author heqidu
 * @since 21-2-22
 */
class RegisterActivity : MVVMActivity<ActivityRegisterBinding>(), IRegisterView, View.OnClickListener {

    override val viewModel by lazy { createViewModel<RegisterViewModel>() }

    override fun onViewCreated() {
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, binding.root)

        initViews()
        initData()
    }

    private fun initViews() {
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

        CommonUtils.showSoftKeyBoard(binding.accountEt)

        binding.backView.setOnClickListener { finish() }
        binding.registerBtn.setOnClickListener(this)
    }

    private fun initData() {
        viewModel.registerResult.observe(this, EntityObserver(
                onSuccess = { entity ->
                    onRegisterSuccess(entity.data)
                },
                onFail = { entity ->
                    onRegisterFail(entity.code, entity.throwable)
                },
                onComplete = {
                    stopLoading()
                }
        ))
    }

    override fun onRegisterSuccess(loginBean: LoginBean) {
        showToast(R.string.register_success)
        finish()
    }

    override fun onRegisterFail(@ErrCode code: Int, throwable: Throwable?) = when (code) {
        ErrCode.NET_ERR -> showToast(R.string.net_err)
        ErrCode.DATA_ERR -> showToast(R.string.account_pwd_err)
        ErrCode.SRC_ERR -> showToast(throwable?.message)
        else -> showToast(R.string.unknown_err)
    }

    override fun onClick(v: View) {
        when (v) {
            binding.registerBtn -> {
                onRegisterConfirmClick()
            }
        }
    }

    private fun onRegisterConfirmClick() {
        val accountMinLen = resources.getInteger(R.integer.account_min_len)
        val account: String = binding.accountEt.text.toString().trim()
        if (account.length < accountMinLen) {
            showToast(getString(R.string.account_len_limit, accountMinLen))
            return
        }

        if (!RegexUtils.isPhone(account) && !RegexUtils.isEmail(account)) {
            showToast(R.string.account_format_err)
            return
        }

        val pwdMinLen = resources.getInteger(R.integer.password_min_len)
        val pwd = binding.pwdEt.text.toString().trim()
        val confirmPwd: String = binding.pwdConfirmEt.text.toString().trim()
        if (pwd.length < pwdMinLen || confirmPwd.length < pwdMinLen) {
            showToast(getString(R.string.pwd_len_limit, pwdMinLen))
            return
        }

        if (pwd != confirmPwd) {
            showToast(R.string.pwd_not_match)
            return
        }

        showLoading()
        viewModel.register(account, pwd, confirmPwd)
    }

}