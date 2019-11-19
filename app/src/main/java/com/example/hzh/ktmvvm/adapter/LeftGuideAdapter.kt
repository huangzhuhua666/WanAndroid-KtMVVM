package com.example.hzh.ktmvvm.adapter

import android.graphics.Color
import com.example.hzh.ktmvvm.R
import com.example.hzh.ktmvvm.data.bean.Guide
import com.example.hzh.library.adapter.DBViewHolder
import com.example.hzh.library.adapter.SimpleBindingAdapter

/**
 * Create by hzh on 2019/11/13.
 */
class LeftGuideAdapter : SimpleBindingAdapter<Guide>(R.layout.item_guide_left) {

    private var checkPosition = 0

    override fun convert(helper: DBViewHolder, item: Guide) {
        super.convert(helper, item)
        helper.itemView.setBackgroundColor(
            if (helper.adapterPosition == checkPosition) mContext.resources.getColor(R.color.color_bbdefb)
            else Color.WHITE
        )
    }

    fun changeCheckPosition(position: Int) {
        checkPosition = position
        notifyDataSetChanged()
    }
}