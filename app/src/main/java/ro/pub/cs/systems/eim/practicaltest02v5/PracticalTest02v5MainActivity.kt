package ro.pub.cs.systems.eim.practicaltest02v5

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ro.pub.cs.systems.eim.practicaltest02v5.databinding.ActivityPracticalTest02v5MainBinding
import ro.pub.cs.systems.eim.practicaltest02v5.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v5.network.ClientThread
import ro.pub.cs.systems.eim.practicaltest02v5.network.ServerThread

class PracticalTest02v5MainActivity : AppCompatActivity() {

    private var serverThread: ServerThread? = null
    private var clientThread: ClientThread? = null
    private lateinit var binding: ActivityPracticalTest02v5MainBinding
    private lateinit var hashMap: ExpiringHashMap<String, String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityPracticalTest02v5MainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hashMap = ExpiringHashMap(10000)



//
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.connectButton.setOnClickListener {
            val serverPort = binding.serverPortEditText.text.toString()
            if (serverPort.isNotEmpty()) {
                serverThread = ServerThread(serverPort.toInt())
                serverThread?.start()
            } else {
                Toast.makeText(this, "Server port should be filled!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getButton.setOnClickListener {
            val clientAddress = binding.clientAddressEditText.text.toString()
            val clientPort = binding.clientPortEditText.text.toString()
            val requestType = "${Constants.GET}"
            val key = binding.keyEditTextText.text.toString()
            val value = binding.valEditText.text.toString()
//            val informationType = binding.informationTypeSpinner.selectedItem.toString()

            if (clientAddress.isNotEmpty() && clientPort.isNotEmpty() && key.isNotEmpty()) {
                clientThread = ClientThread(clientAddress, clientPort.toInt(), requestType, key, value, binding.resultTextView, hashMap)
                clientThread?.start()
            } else {
                Toast.makeText(this, "Client parameters should be filled!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.putButton.setOnClickListener {
            val clientAddress = binding.clientAddressEditText.text.toString()
            val clientPort = binding.clientPortEditText.text.toString()
            val requestType = "${Constants.PUT}"
            val key = binding.keyEditTextText.text.toString()
            val value = binding.valEditText.text.toString()
//            val informationType = binding.informationTypeSpinner.selectedItem.toString()

            if (clientAddress.isNotEmpty() && clientPort.isNotEmpty() && key.isNotEmpty()) {
                clientThread = ClientThread(clientAddress, clientPort.toInt(), requestType, key, value, binding.resultTextView, hashMap)
                clientThread?.start()
            } else {
                Toast.makeText(this, "Client parameters should be filled!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroy() {
        serverThread?.stopThread()
        super.onDestroy()
    }
}