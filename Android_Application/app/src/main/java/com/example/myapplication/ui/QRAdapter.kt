package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.DataSource
import com.example.myapplication.model.QRItem

class QRAdapter(private var qrList: MutableList<QRItem>) : RecyclerView.Adapter<QRAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_qr_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val qrItem = qrList[position]
        holder.filenameTextView.text = qrItem.filename
        holder.imageView.setImageBitmap(qrItem.image)

        val context = holder.itemView.context

        holder.imageView.setOnClickListener {
            val intent = Intent(context, QRDeleteActivity::class.java)
            intent.putExtra(QRDeleteActivity.QRITEM, qrItem)
            intent.putExtra(QRDeleteActivity.POSITION, position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return qrList.size
    }

    fun updateList(newList: MutableList<QRItem>) {
        qrList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.qr_itm_thumbnail)
        var filenameTextView: TextView = itemView.findViewById(R.id.qr_itm_filename)
    }
}