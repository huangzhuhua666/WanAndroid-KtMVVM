package com.example.hzh.ktmvvm.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Guide
import com.example.hzh.ktmvvm.data.bean.SubGuide
import com.example.hzh.library.adapter.DBViewHolder
import com.example.hzh.library.adapter.ItemClickPresenter
import com.example.hzh.library.adapter.SimpleBindingAdapter
import com.google.android.flexbox.FlexboxLayoutManager

/**
 * Create by hzh on 2019/11/19.
 */
class RightGuideAdapter(private val action: ((View, SubGuide, Int) -> Unit)) :
    SimpleBindingAdapter<Guide>(R.layout.item_guide_right_head) {

    override fun convert(helper: DBViewHolder, item: Guide) {
        super.convert(helper, item)
        helper.getView<RecyclerView>(R.id.rvInner).run {
            layoutManager = FlexboxLayoutManager(mContext)
            adapter = SimpleBindingAdapter<SubGuide>(R.layout.item_guide_right_child).also {
                it.setNewData(item.articles)
                it.mPresenter = object : ItemClickPresenter<SubGuide> {
                    override fun onItemClick(view: View, item: SubGuide, position: Int) {
                        action.invoke(view, item, position)
                    }
                }
            }
        }
    }
}