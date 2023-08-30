package ru.olegsvs.wolsettings

import android.os.StrictMode
import android.util.Log
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class WolUtils {
    companion object {
        fun wakeUp(
            ip: String,
            mac: String
        ) {
            DatagramSocket().use { socket ->
                try {
                    val macBytes = getMacBytes(mac)
                    val bytes = ByteArray(6 + 16 * macBytes.size)
                    for (i in 0..5) {
                        bytes[i] = 0xff.toByte()
                    }
                    var i = 6
                    while (i < bytes.size) {
                        System.arraycopy(macBytes, 0, bytes, i, macBytes.size)
                        i += macBytes.size
                    }
                    val address: InetAddress = InetAddress.getByName(ip)
                    val packet = DatagramPacket(bytes, bytes.size, address, 9)
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    socket.send(packet)
                    Log.i("WoL", "Wake-on-LAN packet sent.")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        @Throws(IllegalArgumentException::class)
        private fun getMacBytes(macStr: String): ByteArray {
            val bytes = ByteArray(6)
            val hex = macStr.split(":").toTypedArray()
            require(hex.size == 6) { "Invalid MAC address." }
            try {
                for (i in 0..5) {
                    bytes[i] = hex[i].toInt(16).toByte()
                }
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Invalid hex digit in MAC address. ${e.toString()}")
            }
            return bytes
        }
    }
}