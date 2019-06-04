package chen.vike.c680.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import chen.vike.c680.main.MyApplication
import chen.vike.c680.views.CustomToast

/**
 * <pre>
 *
 *     author: Hy
 *     time  : 2019/04/19  上午 11:38
 *     desc  : 网络状态监听Receiver
 *
 * </pre>
 */

/**
 * TYPE_NONE        = -1
 * TYPE_MOBILE      = 0
 * TYPE_WIFI        = 1
 * TYPE_MOBILE_MMS  = 2
 * TYPE_MOBILE_SUPL = 3
 * TYPE_MOBILE_DUN  = 4
 * TYPE_MOBILE_HIPRI = 5
 * TYPE_WIMAX       = 6
 * TYPE_BLUETOOTH   = 7
 * TYPE_DUMMY       = 8
 * TYPE_ETHERNET    = 9
 * TYPE_MOBILE_FOTA = 10
 * TYPE_MOBILE_IMS  = 11
 * TYPE_MOBILE_CBS  = 12
 * TYPE_WIFI_P2P    = 13
 * TYPE_MOBILE_IA = 14
 * TYPE_MOBILE_EMERGENCY = 15
 * TYPE_PROXY = 16
 * TYPE_VPN = 17
 */
class NetReceiver: BroadcastReceiver() {
    override fun onReceive(contex: Context?, intent: Intent?) {
        val com = contex?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = com.activeNetworkInfo
        if (ni != null && ni.isAvailable) {
            when (ni.typeName) {
                "MOBILE" -> CustomToast.showToast(MyApplication.INSTANCE, "你当前网络为移动网络，请注意流量的使用", Toast.LENGTH_SHORT)
                "WIFI" -> CustomToast.showToast(MyApplication.INSTANCE, "你当前网络为WIFI网络，请放心使用", Toast.LENGTH_SHORT)
            }
            //                CustomToast.showToast(getApplicationContext(), "连接模式==========================" + ni.getTypeName(), Toast.LENGTH_SHORT);
        } else {
            CustomToast.showToast(MyApplication.INSTANCE, "当前无网络，请检查后重试!", Toast.LENGTH_SHORT)
        }
    }
}