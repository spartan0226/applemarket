package com.example.four

import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.four.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedItem = intent.getParcelableExtra("selectedItem", MyItem::class.java)

        selectedItem?.let {
            setEachData(it)
        }

        binding.ibBack.setOnClickListener {
            finish()
        }

        binding.tvTempko.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setEachData(mItem: MyItem) {
        binding.ivItem.setImageResource(mItem.aImage)
        binding.tvUser.text = mItem.aUser
        binding.tvLocale.text = mItem.aPlace
        binding.tvTitle.text = mItem.aTitle
        binding.tvContent.text = mItem.aContent
        binding.tvPrice.text = mItem.aPrice
    }
}