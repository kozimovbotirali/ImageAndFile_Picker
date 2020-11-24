package com.example.pickerimagevideoandfiles.presentation.ui.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDecoder
import com.bumptech.glide.load.resource.bitmap.VideoBitmapDecoder
import com.example.pickerimagevideoandfiles.R
import com.example.pickerimagevideoandfiles.data.source.local.room.entities.FileData
import com.example.pickerimagevideoandfiles.utils.SingleBlock
import com.example.pickerimagevideoandfiles.utils.bindItem
import com.example.pickerimagevideoandfiles.utils.inflate
import kotlinx.android.synthetic.main.item_media.view.*

class MediaAdapter :
    ListAdapter<FileData, MediaAdapter.VH>(FileData.ITEM_CALLBACK) {
    private var listenerAdd: (() -> Unit)? = null
    private var listenerRemove: SingleBlock<FileData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent.inflate(R.layout.item_media))
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = currentList.size

    fun setOnAddListener(block: (() -> Unit)?) {
        listenerAdd = block
    }

    fun setOnItemRemoveListener(block: SingleBlock<FileData>) {
        listenerRemove = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            Log.d("ttt", "$adapterPosition ${currentList.size}")
            itemView.image?.setOnClickListener {
                if (adapterPosition == currentList.size - 1) {
                    Log.d("ttt", "Add")
                    listenerAdd?.invoke()
                }
            }
            itemView.btn_remove?.apply {
                setOnClickListener {
                    listenerRemove?.invoke(currentList[adapterPosition])
                }
            }
        }

        fun bind() = bindItem {
            if (adapterPosition != currentList.size - 1) {
                layout.visibility = View.VISIBLE
                btn_remove.visibility = View.VISIBLE
                val d = currentList[adapterPosition]
                val path = d.path
                val mimeTypeMap = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(path))

                val bitmapPool = Glide.get(context).bitmapPool
                val microSecond = 2000000
                val videoBitmapDecoder = VideoBitmapDecoder(microSecond)
                val fileDescriptorBitmapDecoder = FileDescriptorBitmapDecoder(
                    videoBitmapDecoder,
                    bitmapPool,
                    DecodeFormat.PREFER_ARGB_8888
                )
                if (mimeTypeMap!!.startsWith("image")) {
                    Glide.with(context)
                        .load(path)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(R.drawable.ic_baseline_broken_image_24)
                        .centerCrop()
                        .into(image)
                    btn_play.visibility = View.GONE
                    /*val bitmap = BitmapFactory.decodeFile(path)
                    image.setImageBitmap(bitmap)*/
                } else {
                    Glide.with(context).load(path)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(R.drawable.ic_baseline_slow_motion_video_24)
                        .centerCrop()
                        .videoDecoder(fileDescriptorBitmapDecoder)
                        .into(image)

                    btn_play.visibility = View.VISIBLE
                }
            } else {
                btn_remove.visibility = View.GONE
                if (currentList[adapterPosition].hide)
                    layout.visibility = View.GONE
            }
        }
    }
}