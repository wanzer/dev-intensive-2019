package ru.skillbranch.devintensive.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_user_list.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.UserItem

class UserAdapter(var listener: (UserItem) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var items: List<UserItem> = listOf()

    fun updateUserdata(list: List<UserItem>){

        val diffUtilCallBack = object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].id == list[newItemPosition].id

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                items[oldItemPosition].hashCode() == list[newItemPosition].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = list.size
        }

        val diffResult = DiffUtil.calculateDiff(diffUtilCallBack)
        items = list
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent,false)
        return UserViewHolder(inflater)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int)  = holder.bind(items[position], listener)

    inner class UserViewHolder(var contentView: View) : RecyclerView.ViewHolder(contentView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        fun bind(userItem: UserItem, listener: (UserItem) -> Unit){
            if (userItem.avatar != null){
                Glide.with(itemView).load(userItem.avatar).into(iv_user_avatar)
            }else{
                Glide.with(itemView).clear(iv_user_avatar)
            }
            sv_indicator.visibility = if(userItem.isOnline) View.VISIBLE else View.GONE
            tv_user_name.text = userItem.fullName
            iv_check.visibility = if(userItem.isSelected) View.VISIBLE else View.GONE
            tv_user_status.text = userItem.lastActivity
            itemView.setOnClickListener { listener.invoke(userItem) }
        }
    }
}