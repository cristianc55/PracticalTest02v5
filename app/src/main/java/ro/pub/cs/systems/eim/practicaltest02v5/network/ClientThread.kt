package ro.pub.cs.systems.eim.practicaltest02v5.network

import android.util.Log
import android.widget.TextView
import ro.pub.cs.systems.eim.practicaltest02v5.ExpiringHashMap
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
    private val resultTextView: TextView,
    private val hashMap: ExpiringHashMap<String, String>
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
                    val res = TimezoneInfo.fromString(threadResponse!!).datetime

                    val res2 = hashMap.get("1")
//                    val res2 = hashMap[keyRequest]
                    if (res2 == null) {
                        resultTextView.post {
                            resultTextView.text = "No information available"
                        }
                    } else {
                        resultTextView.post {
                            resultTextView.text = res2.toString()
                        }
                    }
                }

                if (requestType == Constants.PUT) {
                    Log.d(Constants.LOG_MESSAGE_CLIENT, threadResponse.toString())
                    val time = TimezoneInfo.fromString(threadResponse!!).datetime
                    val valueHash = "$valueRequest" + "$time"
                    hashMap.put(keyRequest, valueHash, 10000)
                    Log.d("INSERTED", "$keyRequest $valueHash")
                    Log.d("SIZE_HASH", hashMap.size().toString())
//                    hashMap[keyRequest] = valueHash


//                    if (res == null) {
//                        resultTextView.post {
//                            resultTextView.text = "No information available"
//                        }
//                    } else {
//                        resultTextView.post {
//                            resultTextView.text = res.toString()
//                        }
//                    }
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
