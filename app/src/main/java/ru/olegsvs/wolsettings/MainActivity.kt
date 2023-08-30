package ru.olegsvs.wolsettings

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ru.olegsvs.wolsettings.ui.theme.WolSettingsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WolSettingsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    screen()
                }
            }
        }
    }
}

@Composable
fun screen() {
    Column() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
        var dstAddress by rememberSaveable { mutableStateOf(prefs.getString("DST_MAC", "2C:F0:5D:87:9B:A1")) }
        var ip by rememberSaveable { mutableStateOf(prefs.getString("GATEWAY_IP", "192.168.1.255")) }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = dstAddress!!,
            onValueChange = {
                dstAddress = it
            },
            label = { Text("DST MAC ADDRESS") }
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = ip!!,
            onValueChange = {
                ip = it
            },
            label = { Text("LAN GATEWAY IP") }
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                prefs.edit().putString("DST_MAC", dstAddress).putString("GATEWAY_IP", ip).apply()
            }) {
            Text("SAVE")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val prefsDST = prefs.getString("DST_MAC", "")
                val prefsIP = prefs.getString("GATEWAY_IP", "")
                if (prefsIP != null && prefsIP.isNotEmpty() && prefsDST != null && prefsDST.isNotEmpty()) {
                    WolUtils.wakeUp(prefsIP, prefsDST)
                }
            }) {
            Text("TEST")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WolSettingsTheme(darkTheme = true) {
        screen()
    }
}