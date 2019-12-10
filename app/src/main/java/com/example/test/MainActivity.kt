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
    private var messageListener: MessageListener? = null
    private var message: Message? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageListener = object : MessageListener() {
            override fun onFound(message: Message) {
                var receivedMessage = String(message.content).trim()
                receiveText.text = receivedMessage
                Toast.makeText(this@MainActivity, "received: $receivedMessage", Toast.LENGTH_LONG).show()
                Log.d("messagefound", "Found message: " + String(message.content))
            }

            override fun onLost(message: Message) {
                Log.d("messagelost", "Lost sight of message: " + String(message.content))
            }
        }

        //mMessage = Message("Hello World".toByteArray())
        //hier kan een eerste bericht komen als er succesvolle verbinding is met een andere telefoon

        sendButton.setOnClickListener{
            message = Message(messageInput.text.toString().toByteArray())
            Nearby.getMessagesClient(this).publish(message!!)
            Toast.makeText(this@MainActivity, messageInput.text, Toast.LENGTH_SHORT).show()
            Log.d("messagesend", "send message: " + messageInput.text)
        }

    }

    public override fun onStart() {
        super.onStart()
        //Toast.makeText(this@MainActivity, "onstart", Toast.LENGTH_SHORT).show()
        //Nearby.getMessagesClient(this).publish(mMessage!!)
        Nearby.getMessagesClient(this).subscribe(messageListener!!)
    }

    public override fun onStop() {
        //Toast.makeText(this@MainActivity, "onstop", Toast.LENGTH_SHORT).show()
        Nearby.getMessagesClient(this).unpublish(message!!)
        Nearby.getMessagesClient(this).unsubscribe(messageListener!!)
        super.onStop()
    }
}