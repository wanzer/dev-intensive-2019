package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatItemTouchHelperCallBack(
    var adapter: ChatAdapter,
    var swipeListener: (ChatItem)-> Unit
) : ItemTouchHelper.Callback(){

    private var backRect = RectF()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var iconBounds = Rect()

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (viewHolder is ItemTouchViewHolder){
            makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.START)
        }else{
            makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START)
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipeListener.invoke(adapter.chatItems[viewHolder.adapterPosition])
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder is ItemTouchViewHolder){
            viewHolder.onItemSelected()
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is ItemTouchViewHolder) viewHolder.onItemCleared()
        super.clearView(recyclerView, viewHolder)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            val itemView = viewHolder.itemView
            drawBackground(c, itemView, dX)
            drawIcon(c, itemView, dX)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawIcon(canvas: Canvas, itemView : View, dx: Float) {
        val icon = itemView.resources.getDrawable(R.drawable.ic_archive_white, itemView.context.theme)
        val iconSize = itemView.resources.getDimensionPixelSize(R.dimen.icon_size)
        val space = itemView.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)

        val margin = (itemView.bottom - itemView.top - iconSize) / 2
        //set bounds to icon
        with(iconBounds){
            left = itemView.right + dx.toInt() + space
            top = itemView.top + margin
            right= itemView.right + dx.toInt() + iconSize + space
            bottom = itemView.bottom - margin
        }

        icon.bounds = iconBounds
        icon.draw(canvas)
    }

    private fun drawBackground(canvas: Canvas, itemView : View, dx: Float) {
        with(backRect){
            left = itemView.left.toFloat()
            right = itemView.right.toFloat()
            top = itemView.top.toFloat()
            bottom = itemView.bottom.toFloat()
        }

        with(paint){
            color = itemView.resources.getColor(R.color.color_primary_dark, itemView.context.theme)
        }

        canvas.drawRect(backRect, paint)
    }

    interface ItemTouchViewHolder {
        fun onItemSelected()
        fun onItemCleared()
    }
}