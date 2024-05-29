package ro.pub.cs.systems.eim.practicaltest02v5.network
import android.util.Log
import ro.pub.cs.systems.eim.practicaltest02.network.CommunicationThread
import ro.pub.cs.systems.eim.practicaltest02v5.general.Constants
import java.io.IOException
import java.net.ServerSocket

class ServerThread(private val port: Int) : Thread() {

    private var serverSocket: ServerSocket? = null

    init {
        try {
            serverSocket = ServerSocket(port)
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.message)
            ioException.printStackTrace()
        }
    }

    override fun run() {
        try {
            while (!Thread.currentThread().isInterrupted) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a connection...")
                val socket = serverSocket?.accept()
                if (socket != null) {
                    Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from ${socket.inetAddress}:${socket.port}")
                    val communicationThread = CommunicationThread(socket)
                    communicationThread.start()
                }
            }
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.message)
            ioException.printStackTrace()
        } finally {
            try {
                serverSocket?.close()
            } catch (ioException: IOException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.message)
                ioException.printStackTrace()
            }
        }
    }

    fun stopThread() {
        try {
            serverSocket?.close()
            this.interrupt()
        } catch (ioException: IOException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.message)
            ioException.printStackTrace()
        }
    }
}
