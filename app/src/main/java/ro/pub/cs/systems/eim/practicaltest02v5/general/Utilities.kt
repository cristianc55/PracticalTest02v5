package ro.pub.cs.systems.eim.practicaltest02v5.general
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket

object Utilities {
    fun getReader(socket: Socket): BufferedReader? {
        return try {
            BufferedReader(InputStreamReader(socket.getInputStream()))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getWriter(socket: Socket): PrintWriter? {
        return try {
            PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isNumeric(str: String): Boolean {
        return try {
            str.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}
