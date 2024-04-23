package com.example.four

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.four.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        val dataList = DataList.dataSet

        val adapter = MyAdapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )

        adapter.itemClick = object : MyAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                val selectedItem = DataList.dataSet[position]
                intent.putExtra("selectedItem", selectedItem)
                startActivity(intent)
                }
            }

        binding.btnPlace.setOnClickListener {
            return@setOnClickListener
        }

        binding.btnBell.setOnClickListener {
            bellCheck()
            bellRang()
        }
    }

    private fun bellRang() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channelId = "AppleMarket"
            val channelName = "AppleMarket Channel"
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {

                description = "Channel for AppleMarket"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM).build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channelId)

        } else {
            builder = NotificationCompat.Builder(this)
        }

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cart)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        builder.run {
            setSmallIcon(R.drawable.cart)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림")
            setContentText("설정한 키워드에 대한 알림이 도착했습니다!")
        }
        manager.notify(11, builder.build())
    }

    private fun bellCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)
            }
        }
    }

    private var onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(enabled = true) {
            override fun handleOnBackPressed() {
                showClosingDialog()
            }
        }

    fun showClosingDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("종료")
        builder.setMessage("정말 종료하시겠습니까?")
        builder.setIcon(R.drawable.bubble_chat)

        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> finish()

                    DialogInterface.BUTTON_NEGATIVE -> return@onClick
                }
            }
        }

        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", listener)

        builder.show()
    }
}
