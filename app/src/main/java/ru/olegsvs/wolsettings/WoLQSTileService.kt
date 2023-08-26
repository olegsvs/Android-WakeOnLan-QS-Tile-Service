package ru.olegsvs.wolsettings

import android.os.Build
import android.preference.PreferenceManager
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.N)
class WoLQSTileService : TileService() {
    override fun onClick() {
        super.onClick()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val dstAddress = prefs.getString("DST_MAC", "2C:F0:5D:87:9B:A1")!!
        val dstGateway = prefs.getString("GATEWAY_IP", "192.168.1.255")!!
        WolUtils.wakeUp(dstGateway, dstAddress)
    }
}