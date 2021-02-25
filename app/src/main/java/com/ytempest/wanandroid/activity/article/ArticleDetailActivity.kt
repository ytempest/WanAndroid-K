package com.ytempest.wanandroid.activity.article

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.tool.util.LogUtils
import com.ytempest.tool.util.NetUtils
import com.ytempest.tool.util.web.WebUtils
import com.ytempest.tool.util.web.WebViewClientWrapper
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.login.LoginActivity
import com.ytempest.wanandroid.base.activity.MvpActivity
import com.ytempest.wanandroid.ext.getStringSafe
import com.ytempest.wanandroid.helper.ArticleDetailHelper
import com.ytempest.wanandroid.http.bean.ArticleDetailBean
import com.ytempest.wanandroid.utils.JSON
import com.ytempest.wanandroid.utils.StatusBarUtil
import com.ytempest.wanandroid.utils.Utils
import com.ytempest.wanandroid.widget.MaskLayout.SimpleMaskActionListener
import kotlinx.android.synthetic.main.activity_article_detail.*

/**
 * @author heqidu
 * @since 21-2-22
 */
@InjectLayout(R.layout.activity_article_detail)
class ArticleDetailActivity : MvpActivity<ArticleDetailPresenter>(), IArticleDetailView {

    private val TAG = ArticleDetailActivity::class.qualifiedName

    companion object {
        private const val KEY_ARTICLE_DETAIL = "key_article_detail"
        private const val MAX_PROGRESS = 100

        fun start(context: Context?, bean: ArticleDetailBean) {
            val intent = Intent(context, ArticleDetailActivity::class.java)
            intent.putExtra(KEY_ARTICLE_DETAIL, bean.toJson())
            ActivityLauncher.startActivity(context, intent)
        }
    }

    private lateinit var mArticleDetail: ArticleDetailBean


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar_article_detail)
        val json = intent.getStringSafe(KEY_ARTICLE_DETAIL, null)

        val detail = ArticleDetailBean.from(json)
        if (detail == null) {
            showToast(R.string.get_data_fail)
            return
        }
        mArticleDetail = detail
        LogUtils.d(TAG, "onCreate: 文章信息：" + JSON.toJson(mArticleDetail))

        // 标题要在setSupportActionBar()前设置
        toolbar_article_detail.title = mArticleDetail.title
        setSupportActionBar(toolbar_article_detail)
        toolbar_article_detail.setNavigationOnClickListener { finish() }

        view_article_detail_progress.visibility = View.INVISIBLE
        view_article_detail_progress.max = MAX_PROGRESS

        WebUtils.initWebView(this, webView_article_detail_content)
        webView_article_detail_content.webViewClient = object : WebViewClientWrapper() {
            private var isLoadErr: Boolean = false

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                isLoadErr = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view_article_detail_progress.visibility = View.INVISIBLE
                if (isLoadErr) view_article_detail_mask.showMask()
                else view_article_detail_mask.hideMask()
            }


            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                isLoadErr = true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                isLoadErr = true
            }
        }
        webView_article_detail_content.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                view_article_detail_progress.progress = newProgress
            }
        }

        view_article_detail_mask.setMaskActionListener(object : SimpleMaskActionListener() {
            override fun onMaskCreated(maskView: View) {
                super.onMaskCreated(maskView)
                maskView.findViewById<View>(R.id.tv_net_error_retry)
                        .setOnClickListener { loadArticle(true) }
                maskView.findViewById<View>(R.id.tv_net_error_setting_net)
                        .setOnClickListener { startActivity(Intent(Settings.ACTION_SETTINGS)) }
            }
        })


        loadArticle(false)
    }

    private fun loadArticle(forceRefresh: Boolean) {
        if (view_article_detail_progress.visibility == View.VISIBLE) {
            LogUtils.d(TAG, "loadArticle: 正在加载")
            return
        }

        view_article_detail_progress.progress = 0
        view_article_detail_progress.visibility = View.VISIBLE
        if (forceRefresh) webView_article_detail_content.reload()
        else webView_article_detail_content.loadUrl(mArticleDetail.url)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_article_detail, menu)
        initArticleCollect(menu)
        Utils.enableMenuShowIcon(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun initArticleCollect(menu: Menu) {
        if (mArticleDetail.isShowCollect()) {
            refreshCollectArticleView(mArticleDetail.isCollected)
        } else {
            menu.findItem(R.id.item_article_detail_collect).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_article_detail_collect -> onArticleCollectClick(item)
            R.id.item_article_detail_refresh -> {
                if (NetUtils.isNetAvailable(this)) loadArticle(true)
                else showToast(R.string.net_err)
            }
            R.id.item_article_detail_share -> {
                val articleUrl = mArticleDetail.url
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_from, getString(R.string.app_name), articleUrl))
                intent.type = "text/plain"
                startActivity(intent)
            }
            R.id.item_article_detail_browser -> {
                val url = Uri.parse(mArticleDetail.url)
                ActivityLauncher.startActivity(this, Intent(Intent.ACTION_VIEW, url))
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onArticleCollectClick(item: MenuItem) {
        if (mPresenter.isUserLogin()) {
            val isCollected = !item.isChecked
            mPresenter.updateArticleCollectStatus(isCollected, mArticleDetail.articleId)
        } else {
            ActivityLauncher.startActivity(this, Intent(this, LoginActivity::class.java))
        }
    }

    private fun refreshCollectArticleView(isCollect: Boolean) {
        mArticleDetail.isCollected = isCollect
        val item: MenuItem = toolbar_article_detail.menu.findItem(R.id.item_article_detail_collect)
        item.setIcon(if (isCollect) R.drawable.ic_collect_select else R.drawable.ic_collect_normal)
        item.isChecked = isCollect
        ArticleDetailHelper.instance.postUpdate(mArticleDetail)
    }

    override fun onArticleCollectSuccess(isCollect: Boolean, articleId: Long) {
        refreshCollectArticleView(isCollect)
    }

    override fun onArticleCollectFail(isCollect: Boolean, articleId: Long, errCode: Int) {
        showToast(if (isCollect) R.string.collect_fail else R.string.cancel_fail)
    }

    override fun onDestroy() {
        webView_article_detail_content.stopLoading()
        webView_article_detail_content.destroy()
        super.onDestroy()
    }
}