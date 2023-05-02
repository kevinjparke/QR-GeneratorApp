package com.example.myapplication.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.data.DataSource
import com.example.myapplication.databinding.ActivityDeleteBinding
import com.example.myapplication.model.QRItem
import org.json.JSONObject

class QRDeleteActivity: AppCompatActivity() {
    companion object{
        const val QRITEM = "qrItem"
        const val POSITION = "position"
    }

    private lateinit var binding: ActivityDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var qrItem = intent.getParcelableExtra<QRItem>(QRITEM)
        var position = intent.extras?.getInt(POSITION)
        binding.filenameTextView.text = qrItem?.filename
        binding.QRCode.setImageBitmap(qrItem?.image)

        binding.button.setOnClickListener {
            DataSource.deleteQRCode(this, qrItem!!.filename, position!!)
            finish()
        }
    }
}