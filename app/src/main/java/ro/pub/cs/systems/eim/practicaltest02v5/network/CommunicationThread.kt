package ro.pub.cs.systems.eim.practicaltest02.network

import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import ro.pub.cs.systems.eim.practicaltest02v5.general.Constants
import ro.pub.cs.systems.eim.practicaltest02v5.general.Utilities
import ro.pub.cs.systems.eim.practicaltest02v5.model.TimezoneInfo
import java.io.IOException
import java.net.Socket

class CommunicationThread(private val socket: Socket) : Thread() {

    override fun run() {
        try {
            val bufferedReader = Utilities.getReader(socket)
            val printWriter = Utilities.getWriter(socket)

            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!")
                return
            }

            val requestType = bufferedReader.readLine()
            Log.d(Constants.LOG_MESSAGE_SERVER, "Got request type: $requestType")
            val keyRequest = bufferedReader.readLine()
            Log.d(Constants.LOG_MESSAGE_SERVER, "Got key: $keyRequest")
            val valueRequest = bufferedReader.readLine()
            Log.d(Constants.LOG_MESSAGE_SERVER, "Got val: $valueRequest")


            if (requestType.isNullOrEmpty() || keyRequest.isNullOrEmpty() || valueRequest.isNullOrEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client!")
                return
            }

            val client = OkHttpClient()
            val url = "${Constants.WEB_SERVICE_ADDRESS}"
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (responseBody != null) {
                val serverResponse = Gson().fromJson(responseBody, TimezoneInfo::class.java)
                Log.d(Constants.LOG_MESSAGE_SERVER, "Server response: $serverResponse")



                val finalRes = serverResponse.toString()


                printWriter.println(finalRes)
                printWriter.flush()
            } else {
                printWriter.println("No weather information available")
                printWriter.flush()
            }
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.message)
            ioException.printStackTrace()
        } finally {
            socket.close()
        }
    }
}
