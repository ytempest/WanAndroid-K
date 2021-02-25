package com.ytempest.wanandroid.activity.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ytempest.banner.AbsBannerBinder
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.http.bean.BannerBean
import com.ytempest.wanandroid.utils.ImgLoader

/**
 * @author heqidu
 * @since 21-2-9
 */
class HomeBannerBinder : AbsBannerBinder<BannerBean>(), View.OnClickListener {

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, data: BannerBean, position: Int): View {
        val view = inflater.inflate(R.layout.item_banner_content, container, false)
        ImgLoader.loadTo(view.findViewById(R.id.iv_item_banner), data.imagePath)
        view.tag = data
        view.setOnClickListener(this)
        return view
    }

    override fun onCreateTitleView(inflater: LayoutInflater, container: ViewGroup): View? =
            inflater.inflate(R.layout.item_banner_title, container, false)

    override fun onUpdateTitleView(view: View?, data: BannerBean, position: Int, count: Int) {
        view?.let {
            view.findViewById<TextView>(R.id.tv_banner_title_text).text = data.title
            val group = view.findViewById<ViewGroup>(R.id.group_item_banner_title)
            for (idx in 0 until group.childCount) {
                group.getChildAt(idx).isSelected = (idx == position)
            }
        }
    }

    override fun onClick(view: View) {
        val data = view.tag as BannerBean

    }
}