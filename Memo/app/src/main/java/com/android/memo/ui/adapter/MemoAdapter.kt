package com.android.memo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.memo.R
import com.android.memo.data.Memo
import com.android.memo.util.ploadImg
import kotlinx.android.synthetic.main.item_memo.view.*

class MemoAdapter(
    private val memos: ArrayList<Memo>
) : RecyclerView.Adapter<MemoAdapter.ViewHolder>() {

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun getItemCount(): Int = memos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        memos[position].let { memo ->
            with(holder) {
                titleTv.text = memo.title
                contentTv.text = memo.content
                dateTv.text = memo.date
                if (memo.isChecked) {
                    checkIb.setImageResource(R.drawable.checked_star)
                } else {
                    checkIb.setImageResource(R.drawable.unchecked_star)
                }
                if (memo.image.isNullOrEmpty()) {
                    imageIv.ploadImg(memo.image)
                }
                this.itemView.setOnClickListener { v ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener?.onItemClick(v, pos)
                    }
                }
                this.itemView.setOnLongClickListener { v ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener?.onItemLongClick(v, pos)
                    }
                    false
                }
                checkIb.setOnClickListener { v ->
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener?.onCheckClick(v, pos)
                    }
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, position: Int)
        fun onItemLongClick(v: View, position: Int)
        fun onCheckClick(v: View, position: Int)
    }


    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
    ) {
        val titleTv = itemView.title_tv
        val contentTv = itemView.content_tv
        val dateTv = itemView.date_tv
        val imageIv = itemView.image_iv
        val checkIb = itemView.imgbtn_check
    }

}