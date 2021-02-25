package com.ytempest.wanandroid.activity.architecture

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.ytempest.layoutinjector.annotation.InjectLayout
import com.ytempest.tool.helper.ActivityLauncher
import com.ytempest.wanandroid.R
import com.ytempest.wanandroid.activity.architecture.content.ArchArticleFrag
import com.ytempest.wanandroid.base.activity.LoaderActivity
import com.ytempest.wanandroid.ext.getStringSafe
import com.ytempest.wanandroid.http.bean.KnowledgeArchitectureBean
import com.ytempest.wanandroid.utils.CoreFragPagerAdapter
import com.ytempest.wanandroid.utils.JSON
import kotlinx.android.synthetic.main.activity_architecture.*

/**
 * @author heqidu
 * @since 21-2-22
 */
@InjectLayout(R.layout.activity_architecture)
class ArchitectureActivity : LoaderActivity<ArchitecturePresenter>(), IArchitectureView {

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
        super.onViewCreated()
        val json = intent.getStringSafe(KEY_ARCHITECTURE_DATA, null)
        mBean = JSON.from(json, KnowledgeArchitectureBean::class.java) as KnowledgeArchitectureBean
        if (mBean == null) {
            showToast(R.string.get_data_fail)
            return
        }

        val tabs = ArrayList<String>()
        mBean.children?.let {
            for (child in it) {
                tabs.add(child.name)
            }
            group_arch_indicator.setupWithViewPager(group_arch_content)
            group_arch_indicator.adapter = TabAdapter(tabs)
            group_arch_content.adapter = object : CoreFragPagerAdapter<KnowledgeArchitectureBean.Children>(
                    supportFragmentManager, mBean.children) {
                override fun onCreateFragment(data: KnowledgeArchitectureBean.Children, pos: Int): Fragment {
                    return ArchArticleFrag.newInstance(data)
                }

            }
        }
    }
}