package com.ytempest.wanandroid.activity.architecture

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.architecture.content.ArchArticleFrag
import com.ytempest.wanandroid.base.activity.MVVMActivity
import com.ytempest.wanandroid.base.createViewModel
import com.ytempest.wanandroid.databinding.ActivityArchitectureBinding
import com.ytempest.wanandroid.ext.getStringSafe
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import com.ytempest.wanandroid.utils.CoreFragPagerAdapter
import com.ytempest.wanandroid.utils.JSON

/**
 * @author heqidu
 * @since 21-2-22
 */
class ArchitectureActivity() : MVVMActivity<ActivityArchitectureBinding>(), IArchitectureView {

    override val viewModel by lazy { createViewModel<ArchitectureViewModel>() }

    companion object {
        private const val KEY_ARCHITECTURE_DATA = "architecture_data"

        fun start(context: Context?, bean: KnowledgeArchitectureBean) {
            val intent = Intent(context, ArchitectureActivity::class.java)
            intent.putExtra(KEY_ARCHITECTURE_DATA, JSON.toJson(bean))
            ActivityLauncher.startActivity(context, intent)
        }
    }

    private lateinit var mBean: KnowledgeArchitectureBean

    override fun onViewCreated() {
        val json = intent.getStringSafe(KEY_ARCHITECTURE_DATA, null)
        mBean = JSON.from(json, KnowledgeArchitectureBean::class.java) as KnowledgeArchitectureBean
        if (mBean == null) {
            showToast(R.string.get_data_fail)
            return
        }

        mBean.children?.let {
            val tabs = ArrayList<String>()
            it.forEach { children -> tabs.add(children.name) }
            binding.indicatorView.setupWithViewPager(binding.contentView)
            binding.indicatorView.adapter = TabAdapter(tabs)
            binding.contentView.adapter = object : CoreFragPagerAdapter<KnowledgeArchitectureBean.Children>(
                    supportFragmentManager, it) {
                override fun onCreateFragment(data: KnowledgeArchitectureBean.Children, pos: Int): Fragment {
                    return ArchArticleFrag.newInstance(data)
                }

            }
        }
    }
}