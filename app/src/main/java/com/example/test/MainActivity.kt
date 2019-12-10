package com.example.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.messages.Message
import com.google.android.gms.nearby.messages.MessageListener
import kotlinx.android.synthetic.main.activity_main.*

//spotify
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

class MainActivity : AppCompatActivity() {
    private var messageListener: MessageListener? = null
    private var message: Message? = null

    private val clientId = "e0a722ced90449768ad5bc5f85561c14"
    private val redirectUri = "http://com.example.test/callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null


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
        //w przypadku udanego połączenia z innym telefonem można wysłać tutaj wstępną wiadomość

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

        val connectionParams = ConnectionParams.Builder(clientId)
                .setRedirectUri(redirectUri)
                .showAuthView(true)
                .build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                Log.d("MainActivity", "Connected! Yay!")
                // Now you can start interacting with App Remote
                connected()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)
                // Something went wrong when attempting to connect! Handle errors here
            }
        })

    }

    private fun connected() {
        // Then we will write some more code here.
        spotifyAppRemote?.let {
            // Then we will write some more code here.
            val playlistURI = "spotify:playlist:37i9dQZF1DX2sUQwD7tbmL"
            it.playerApi.play(playlistURI)
        }
    }

    public override fun onStop() {
        //Toast.makeText(this@MainActivity, "onstop", Toast.LENGTH_SHORT).show()
        super.onStop()
        //Nearby.getMessagesClient(this).unpublish(message!!)
        //Nearby.getMessagesClient(this).unsubscribe(messageListener!!)
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}