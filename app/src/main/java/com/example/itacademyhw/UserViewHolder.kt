package com.example.itacademyhw

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itacademyhw.room.model.UserEntity

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val userItemView: TextView = itemView.findViewById(R.id.textView)
    private val userImageView: ImageView = itemView.findViewById(R.id.userImageView)

    fun bind(user: UserEntity) {
        userItemView.text = "${user.firstName} ${user.lastName}"
        if (user.photoName.isNotEmpty()) {
            val file = itemView.context
                .filesDir
                .listFiles()
                ?.filter { it.canRead() && it.isFile && it.name.endsWith(JPG_EXTENSION) }
                ?.map { file ->
                    val bytes = file.readBytes()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    ImageItem.Internal(file.name, bmp)
                }
                ?.find {
                    it.fileName == user.photoName
                }
            if (file != null) {
                userImageView.setImageBitmap(file.bitmap)
            }
        }
    }

    companion object {
        private const val JPG_EXTENSION = ".jpg"
        
        fun create(parent: ViewGroup): UserViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
            return UserViewHolder(view)
        }
    }
}