package com.example.pickerimagevideoandfiles.presentation.ui.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pickerimagevideoandfiles.R
import com.example.pickerimagevideoandfiles.data.source.local.room.entities.FileData
import com.example.pickerimagevideoandfiles.utils.SingleBlock
import com.example.pickerimagevideoandfiles.utils.bindItem
import com.example.pickerimagevideoandfiles.utils.inflate
import kotlinx.android.synthetic.main.item_file.view.*
import java.io.File

class FileAdapter :
    ListAdapter<FileData, FileAdapter.VH>(FileData.ITEM_CALLBACK) {
    private var listenerRemove: SingleBlock<FileData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent.inflate(R.layout.item_file))
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind()

    override fun getItemCount(): Int = currentList.size

    fun setOnItemRemoveListener(block: SingleBlock<FileData>) {
        listenerRemove = block
    }

    fun insert(data: FileData) {
        val ls = currentList.toMutableList()
        ls.add(data)
        submitList(ls)
    }

    fun delete(data: FileData) {
        val ls = currentList.toMutableList()
        val pos = ls.indexOfFirst { data.id == it.id }
        ls.removeAt(pos)
        submitList(ls)
    }

    @Suppress("DEPRECATION")
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        init {
            itemView.btn_file_remove?.apply {
                setOnClickListener {
                    listenerRemove?.invoke(currentList[adapterPosition])
                    delete(currentList[adapterPosition])
                }
            }
        }

        fun bind() = bindItem {
            val d = currentList[adapterPosition]
            val f = File(d.path)
            text_file_name.text = f.name

            val mimeTypeMap = MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(d.path))
            val image = when {
                d.path.endsWith(".pdf") -> R.drawable.ic_file_pdf
                (mimeTypeMap
                    ?: "").startsWith("video") -> R.drawable.ic_baseline_slow_motion_video_24
                (mimeTypeMap ?: "").startsWith("image") -> R.drawable.ic_baseline_broken_image_24
                d.path.endsWith(".doc") -> R.drawable.ic_baseline_text_format_24
                d.path.endsWith(".docx") -> R.drawable.ic_baseline_text_format_24
                d.path.endsWith(".txt") -> R.drawable.ic_baseline_text_format_24
                d.path.endsWith(".apk") -> R.drawable.ic_baseline_android_24
                d.path.endsWith(".mp3") -> R.drawable.ic_file_music
                d.path.endsWith(".m4p") -> R.drawable.ic_file_music
                else -> R.drawable.ic_baseline_attach_file_24
            }

            btn_file_type.setImageResource(image)
        }
    }
}