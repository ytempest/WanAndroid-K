package com.ytempest.wanandroid.http.bean;

import androidx.annotation.IntDef;

import com.ytempest.wanandroid.utils.JSON;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author heqidu
 * @since 2020/12/16
 */
public class ArticleDetailBean {
    private long articleId;
    private String title;
    private String url;
    private boolean isCollected;
    private String author;

    private ArticleDetailBean(@Source int source, long articleId, String author, String title, String url, boolean isCollected) {
        this.source = source;
        this.articleId = articleId;
        this.author = author;
        this.title = title;
        this.url = url;
        this.isCollected = isCollected;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /*Ext*/

    @Source
    private final int source;

    @Source
    public int getSource() {
        return source;
    }

    public boolean isShowCollect() {
        return source == Source.HOME || source == Source.KNOWLEDGE;
    }

    public String toJson() {
        return JSON.toJson(this);
    }

    public static ArticleDetailBean from(String json) {
        return JSON.from(json, ArticleDetailBean.class);
    }

    public static ArticleDetailBean from(HomeArticleBean.DatasBean bean) {
        return new ArticleDetailBean(Source.HOME, bean.getId(), bean.getAuthor(), bean.getTitle(), bean.getLink(), bean.isCollect());
    }

    public static ArticleDetailBean from(ArchitectureContentBean.DatasBean bean) {
        return new ArticleDetailBean(Source.KNOWLEDGE, bean.getId(), bean.getAuthor(), bean.getTitle(), bean.getLink(), bean.isCollect());
    }

    public static ArticleDetailBean from(ProjectContentBean.DatasBean bean) {
        return new ArticleDetailBean(Source.PROJECT, bean.getId(), bean.getAuthor(), bean.getTitle(), bean.getLink(), bean.isCollect());
    }

    public static ArticleDetailBean from(NavigationListBean.ArticlesBean bean) {
        return new ArticleDetailBean(Source.NAVIGATION, bean.getId(), bean.getAuthor(), bean.getTitle(), bean.getLink(), bean.isCollect());
    }

    @IntDef({
            Source.HOME,
            Source.KNOWLEDGE,
            Source.PROJECT,
            Source.NAVIGATION,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
        int HOME = 1;
        int KNOWLEDGE = 2;
        int PROJECT = 3;
        int NAVIGATION = 4;
    }
}
