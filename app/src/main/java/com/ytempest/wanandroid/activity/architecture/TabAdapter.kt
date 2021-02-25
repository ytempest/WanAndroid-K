package com.ytempest.wanandroid.activity.architecture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ytempest.variety.BaseAdapter
import com.ytempest.variety.ColorTrackTextView
import com.ytempest.variety.TabGroup
import com.ytempest.variety.indicator.DynamicIndicator
import com.ytempest.variety.indicator.IIndicatorDecorator
import com.ytempest.variety.tab.ITabDecorator
import com.ytempest.variety.tab.TrackTabDecorator
import com.ytempest.wanandroid.R

/**
 * @author heqidu
 * @since 21-2-22
 */
class TabAdapter(list: List<String>) : BaseAdapter<String>(list) {

    override fun getTabDecorator(): ITabDecorator<String> {
        return object : TrackTabDecorator() {
            override fun createTabView(inflater: LayoutInflater, parent: TabGroup, position: Int): View {
                val view = super.createTabView(inflater, parent, position) as ColorTrackTextView
                view.setOriginColor(view.resources.getColor(R.color.common_light_gray_text))
                view.setChangeColor(view.resources.getColor(R.color.arch_indicator))
                return view
            }
        }
    }

    override fun getIndicatorDecorator(): IIndicatorDecorator {
        return object : DynamicIndicator() {
            override fun createIndicatorView(inflater: LayoutInflater, parent: ViewGroup): View {
                return inflater.inflate(R.layout.indicator_arch_article, parent, false)
            }
        }
    }
}