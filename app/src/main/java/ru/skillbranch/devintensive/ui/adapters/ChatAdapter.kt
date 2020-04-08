package ru.skillbranch.devintensive.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_single.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem

class ChatAdapter(var listener: (ChatItem)-> Unit) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var chatItems: List<ChatItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_chat_single, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chatItems.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatItems[position], listener)
    }

    fun updateData(chatItems: List<ChatItem>) {

        Log.d("M_Chat_adapter", "update data adapter - new data ${chatItems.size} hash : ${chatItems.hashCode()}" +
        " old data ${this.chatItems.size} hash : ${chatItems.hashCode()}")

        val diffUtilCallBack = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@ChatAdapter.chatItems[oldItemPosition] == chatItems[newItemPosition]

            override fun getOldListSize(): Int = this@ChatAdapter.chatItems.size

            override fun getNewListSize(): Int = chatItems.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                this@ChatAdapter.chatItems[oldItemPosition].hashCode() == chatItems[newItemPosition].hashCode()
        }

        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        this.chatItems = chatItems
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer,
        ChatItemTouchHelperCallBack.ItemTouchViewHolder {
        override val containerView: View?
            get() = itemView

        fun bind(chatItem: ChatItem, listener: (ChatItem) -> Unit){

            if(chatItem.avatar == null){
                //set initials to custom avatar view
            }else{
                //set avatar from net
            }

            sv_indicator.visibility = if (chatItem.isOnline) View.VISIBLE else View.GONE
            with(tv_date_single){
                visibility = if(chatItem.lastMessageDate != null) View.VISIBLE else View.GONE
                text = chatItem.lastMessageDate
            }
            with(tv_counter_single){
                visibility = if(chatItem.messageCount > 0) View.VISIBLE else View.GONE
                text = chatItem.messageCount.toString()

            }
            tv_title_single.text = chatItem.title
            tv_message_single.text = chatItem.shortDescription

            itemView.setOnClickListener {
                listener.invoke(chatItem)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(Color.WHITE)
        }
    }
}