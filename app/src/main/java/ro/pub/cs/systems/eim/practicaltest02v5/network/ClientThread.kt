package ro.pub.cs.systems.eim.practicaltest02v5.network

import android.util.Log
import android.widget.TextView
import ro.pub.cs.systems.eim.practicaltest02v5.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v5.general.Utilities
import ro.pub.cs.systems.eim.practicaltest02v5.model.TimezoneInfo
import java.io.IOException
import java.net.Socket

class ClientThread(
    private val address: String,
    private val port: Int,
    private val requestType: String,
    private val keyRequest: String,
    private val valueRequest: String,
    private val resultTextView: TextView
) : Thread() {

    private var socket: Socket? = null

    override fun run() {
        try {
            socket = Socket(address, port)
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!")
                return
            }

            val bufferedReader = Utilities.getReader(socket!!)
            val printWriter = Utilities.getWriter(socket!!)

            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!")
                return
            }

            printWriter.println(requestType)
            printWriter.flush()
            printWriter.println(keyRequest)
            printWriter.flush()
            printWriter.println(valueRequest)
            printWriter.flush()

            var threadResponse: String?
            while (bufferedReader.readLine().also { threadResponse = it } != null) {
                val finalizedResponse = threadResponse

                TimezoneInfo.fromString(threadResponse!!)


                if (requestType == Constants.GET) {
                    Log.d(Constants.LOG_MESSAGE_CLIENT, threadResponse.toString())
                    val res = TimezoneInfo.fromString(threadResponse!!).getValue(keyRequest)
                    if (res == null) {
                        resultTextView.post {
                            resultTextView.text = "No information available"
                        }
                    } else {
                        resultTextView.post {
                            resultTextView.text = res.toString()
                        }
                    }
                }

//                Log.d(Constants.LOG_MESSAGE_CLIENT, threadResponse.toString())
//                resultTextView.post {
//                    resultTextView.text = finalizedResponse
//                }
            }
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.message)
            ioException.printStackTrace()
        } finally {
            socket?.close()
        }
    }
}
