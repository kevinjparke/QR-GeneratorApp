package com.example.myapplication.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.DataSource

class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: QRAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stringTextEdit = binding.stringToGenerate
        val filenameTextEdit = binding.filename
        val detail = binding.detail

        DataSource.loadQrCodes(this)
        adapter = QRAdapter(DataSource.qrCodes)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        //Notify Activity of change of loading
        val handler = Handler()
        handler.postDelayed({
            adapter.notifyDataSetChanged()
        }, 10000)

        binding.generateButton.setOnClickListener{
            DataSource.addQRCode(this,
                stringTextEdit.text.toString(),
                filenameTextEdit.text.toString(),
                detail.text.toString().toInt())
            adapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.updateList(DataSource.qrCodes)
    }
}