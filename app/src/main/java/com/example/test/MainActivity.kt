package com.example.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mMessageListener: MessageListener? = null
    private var mMessage: Message? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMessageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                Log.d("berichtfound", "Found message: " + String(message.content))
                val messageWasReceived = String(message.content).trim()
                receiveText.text = String(message.content)
                Toast.makeText(this@MainActivity, "Message received! $messageWasReceived", Toast.LENGTH_LONG).show()
            }

            override fun onLost(message: Message) {
                Log.d("berichtlost", "Lost sight of message: " + String(message.content))
                receiveText.text = message.content.toString()
            }
        }
        mMessage = Message("Hello World".toByteArray())

        sendButton.setOnClickListener{
            //val textFromInput = Message(messageInput.toString().toByteArray())
            mMessage = Message("Deze app werkt eindelijk".toByteArray())
            Nearby.getMessagesClient(this).publish(mMessage!!)
            Toast.makeText(this@MainActivity, mMessage.toString(), Toast.LENGTH_SHORT).show()
            Log.d("messagelog", "message: ${mMessage.toString()}")
        }

    }

    public override fun onStart() {
        super.onStart()
        Toast.makeText(this@MainActivity, "onStart", Toast.LENGTH_SHORT).show()
        Nearby.getMessagesClient(this).publish(mMessage!!)
        Nearby.getMessagesClient(this).subscribe(mMessageListener!!)
    }

    public override fun onStop() {
        Toast.makeText(this@MainActivity, "onStop", Toast.LENGTH_SHORT).show()
        Nearby.getMessagesClient(this).unpublish(mMessage!!)
        Nearby.getMessagesClient(this).unsubscribe(mMessageListener!!)
        super.onStop()
    }
}