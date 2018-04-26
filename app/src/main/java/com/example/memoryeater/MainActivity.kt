package com.example.memoryeater

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val mData = mutableListOf<String>()
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var tvOut: TextView
    var eater : EaterClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        eat_button.setOnClickListener { eatMemory() }
        release_button.setOnClickListener { releaseMemory() }

        tvOut = findViewById(R.id.tvOut)
        updateDisplay()
    }

    override fun onDestroy() {
        eater?.cancel(true)
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun eatMemory() {
        eater?.cancel(true)
        eater = EaterClass()
        eater?.execute()
    }

    private fun releaseMemory() {
        eater?.cancel(true)
        Thread.sleep(1000)
        mData.clear()
        updateDisplay()
    }

    @SuppressLint("StaticFieldLeak")
    inner class EaterClass : AsyncTask<Void, String, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            for (i in 1..1000) {
                for (j in 1..10000) {
                    mData.add("Item $i:$j")
                }
                runOnUiThread({
                    updateDisplay()
                })
                try {
                    Thread.sleep(500)
                    if (isCancelled) break
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
            updateDisplay()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateDisplay() {
        tvOut.text = "Number of items: ${mData.size}"
    }
}
