package com.ytempest.wanandroid.activity.article;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.tool.helper.ActivityLauncher;
import com.ytempest.tool.util.IntentUtils;
import com.ytempest.tool.util.LogUtils;
import com.ytempest.tool.util.NetUtils;
import com.ytempest.tool.util.web.WebUtils;
import com.ytempest.tool.util.web.WebViewClientWrapper;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.activity.login.LoginActivity;
import com.ytempest.wanandroid.base.activity.MvpActivity;
import com.ytempest.wanandroid.helper.ArticleDetailHelper;
import com.ytempest.wanandroid.http.bean.ArticleDetailBean;
import com.ytempest.wanandroid.utils.JSON;
import com.ytempest.wanandroid.utils.StatusBarUtil;
import com.ytempest.wanandroid.utils.Utils;
import com.ytempest.wanandroid.widget.MaskLayout;

import butterknife.BindView;

/**
 * @author heqidu
 * @since 2020/12/15
 */
@InjectLayout(R.layout.activity_article_detail)
public class ArticleDetailActivity extends MvpActivity<ArticleDetailPresenter> implements IArticleDetailContract.View {

    private static final String TAG = ArticleDetailActivity.class.getSimpleName();
    private static final String KEY_ARTICLE_DETAIL = "key_article_detail";
    private static final int MAX_PROGRESS = 100;

    public static void start(Context context, ArticleDetailBean bean) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(KEY_ARTICLE_DETAIL, bean.toJson());
        ActivityLauncher.startActivity(context, intent);
    }

    @BindView(R.id.toolbar_article_detail)
    Toolbar mToolbar;
    @BindView(R.id.view_article_detail_mask)
    MaskLayout mMaskLayout;
    @BindView(R.id.view_article_detail_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.webView_article_detail_content)
    WebView mWebView;
    private ArticleDetailBean mArticleDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, mToolbar);
        String json = IntentUtils.getString(getIntent(), KEY_ARTICLE_DETAIL, null);
        mArticleDetail = ArticleDetailBean.from(json);
        LogUtils.d(TAG, "onCreate: 文章信息：" + JSON.toJson(mArticleDetail));

        // 标题要在setSupportActionBar()前设置
        mToolbar.setTitle(mArticleDetail.getTitle());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());

        mProgressBar.setVisibility(View.INVISIBLE);
        mProgressBar.setMax(MAX_PROGRESS);
        WebUtils.initWebView(this, mWebView);
        mWebView.setWebViewClient(new WebViewClientWrapper() {
            // TODO  heqidu: 待优化
            private boolean isLoadErr;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                isLoadErr = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.INVISIBLE);
                if (isLoadErr) {
                    mMaskLayout.showMask();
                } else {
                    mMaskLayout.hideMask();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isLoadErr = true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isLoadErr = true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                super.onProgressChanged(view, progress);
                mProgressBar.setProgress(progress);
            }
        });

        mMaskLayout.setMaskActionListener(new MaskLayout.SimpleMaskActionListener() {
            @Override
            public void onMaskCreated(View maskView) {
                super.onMaskCreated(maskView);
                maskView.findViewById(R.id.tv_net_error_retry)
                        .setOnClickListener(v -> loadArticle(true));
                maskView.findViewById(R.id.tv_net_error_setting_net)
                        .setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_SETTINGS)));
            }
        });

        loadArticle(false);
    }

    private void loadArticle(boolean forceRefresh) {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            LogUtils.d(TAG, "loadArticle: 正在加载");
            return;
        }
        mProgressBar.setProgress(0);
        mProgressBar.setVisibility(View.VISIBLE);
        if (forceRefresh) {
            mWebView.reload();
        } else {
            mWebView.loadUrl(mArticleDetail.getUrl());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_detail, menu);
        initArticleCollect(menu);
        Utils.enableMenuShowIcon(menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initArticleCollect(Menu menu) {
        if (mArticleDetail.isShowCollect()) {
            refreshCollectArticleView(mArticleDetail.isCollected());
        } else {
            menu.findItem(R.id.item_article_detail_collect).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_article_detail_collect:
                onArticleCollectClick(item);
                break;

            case R.id.item_article_detail_refresh:
                if (NetUtils.isNetAvailable(getContext())) {
                    loadArticle(true);
                } else {
                    showToast(R.string.net_err);
                }
                break;

            case R.id.item_article_detail_share:
                String articleUrl = mArticleDetail.getUrl();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_from, getString(R.string.app_name), articleUrl));
                intent.setType("text/plain");
                startActivity(intent);
                break;

            case R.id.item_article_detail_browser:
                Uri url = Uri.parse(mArticleDetail.getUrl());
                ActivityLauncher.startActivity(this, new Intent(Intent.ACTION_VIEW, url));
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onArticleCollectClick(MenuItem item) {
        if (mPresenter.isUserLogin()) {
            boolean isCollected = !item.isChecked();
            mPresenter.updateArticleCollectStatus(isCollected, mArticleDetail.getArticleId());

        } else {
            ActivityLauncher.startActivity(this, new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void onArticleCollectSuccess(boolean isCollect, long articleId) {
        refreshCollectArticleView(isCollect);
    }

    private void refreshCollectArticleView(boolean isCollect) {
        mArticleDetail.setCollected(isCollect);
        MenuItem item = mToolbar.getMenu().findItem(R.id.item_article_detail_collect);
        item.setIcon(isCollect ? R.drawable.ic_collect_select : R.drawable.ic_collect_normal);
        item.setChecked(isCollect);
        ArticleDetailHelper.getInstance().postUpdate(mArticleDetail);
    }

    @Override
    public void onArticleCollectFail(boolean isCollect, long articleId, int errCode) {
        showToast(isCollect ? R.string.collect_fail : R.string.cancel_fail);
    }

    @Override
    protected void onDestroy() {
        mWebView.stopLoading();
        mWebView.destroy();
        super.onDestroy();
    }
}
