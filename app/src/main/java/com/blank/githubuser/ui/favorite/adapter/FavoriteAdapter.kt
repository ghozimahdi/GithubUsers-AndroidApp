package com.blank.githubuser.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blank.githubuser.data.model.User
import com.blank.githubuser.databinding.ItemUserFavoriteBinding
import com.blank.githubuser.utils.ANONYM_PERSON_ICON
import com.blank.githubuser.utils.noLocation
import com.blank.githubuser.utils.noName
import com.blank.githubuser.utils.setImages
import com.chauthai.swipereveallayout.ViewBinderHelper
import kotlinx.android.synthetic.main.item_user.view.*

class FavoriteAdapter(
    private val data: MutableList<User>,
    private val listenerDelete: (User, (User, Int) -> Unit, Int) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val viewBinderHelper = ViewBinderHelper()
    private lateinit var clicklistener: (user: User, viewArray: Array<View>) -> Unit

    fun add(user: User, position: Int) {
        data.add(position, user)
        notifyItemInserted(position)
        notifyItemRangeChanged(position, data.size)
    }

    private fun create(parent: ViewGroup) = FavoriteViewHolder(
        ItemUserFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    fun clickListener(listener: (user: User, viewArray: Array<View>) -> Unit) {
        this.clicklistener = listener
    }

    private fun removeAt(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, data.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder =
        create(parent)

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = data[position]
        val no_name = holder.itemView.context.noName()
        viewBinderHelper.setOpenOnlyOne(true)
        viewBinderHelper.bind(holder.v.swipelayout, item.name ?: no_name)
        viewBinderHelper.closeLayout(item.name ?: no_name)
        holder.bind(item, position)
    }

    inner class FavoriteViewHolder(
        val v: ItemUserFavoriteBinding
    ) : RecyclerView.ViewHolder(v.root) {

        fun bind(
            user: User,
            position: Int
        ) {
            v.root.ivPhoto.setImages(user.avatarUrl ?: ANONYM_PERSON_ICON)
            v.root.tvTitle.text = user.name ?: v.root.context.noName()
            v.root.tvLocation.text = user.location ?: v.root.context.noLocation()

            v.tvDelete.setOnClickListener {
                removeAt(position)
                listenerDelete(user, this@FavoriteAdapter::add, position)
            }

            v.root.clItemUser.setOnClickListener {
                clicklistener(
                    user, arrayOf(
                        v.root.ivPhoto,
                        v.root.tvTitle,
                        v.root.tvLocation
                    )
                )
            }
        }
    }
}