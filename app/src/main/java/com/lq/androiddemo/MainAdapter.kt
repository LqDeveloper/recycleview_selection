package com.lq.androiddemo

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.lq.androiddemo.databinding.LayoutItemBinding


class MainAdapter(var tracker: SelectionTracker<Long>? = null) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    init {
        //设置对每个项目维持唯一稳定的标识符ID：
        setHasStableIds(true)
    }


    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: LayoutItemBinding = LayoutItemBinding.bind(itemView)

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false);
        return MainViewHolder(view);
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.binding.title.text = "这是第${position}行"
        tracker?.let {
            holder.itemView.isActivated = it.isSelected(position.toLong())
        }
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun getItemId(position: Int): Long = position.toLong()
}

class MyItemDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as MainAdapter.MainViewHolder)
                .getItemDetails()
        }
        return null
    }
}

class MyItemKeyProvider(private val recyclerView: RecyclerView) :
    ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {

    override fun getKey(position: Int): Long? {
        return recyclerView.adapter?.getItemId(position)
    }

    override fun getPosition(key: Long): Int {
        val viewHolder = recyclerView.findViewHolderForItemId(key)
        return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
    }
}


