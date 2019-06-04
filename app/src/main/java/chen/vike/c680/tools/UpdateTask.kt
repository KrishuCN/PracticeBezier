package chen.vike.c680.tools

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.support.v4.content.FileProvider
import chen.vike.c680.main.BaseActivity

import com.blankj.utilcode.util.ToastUtils
import com.hjq.permissions.OnPermission
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import chen.vike.c680.main.MainActivity
import chen.vike.c680.main.MyApplication
import chen.vike.c680.views.MyProgressDialog

import java.io.File

/**
 * Created by lht on 2017/3/2.
 */

class UpdateTask : DownLoadTask.DownlaodListener {
    private var context: Activity? = null
    private var file: File? = null
    private var url: String? = null
    private val THREAD_NUM = 5
    private var mProgressDialog: MyProgressDialog? = null
    private var progressVaue: Int = 0

    private var flag = true

    constructor(context: BaseActivity, url: String) {
        this.context = context
        this.url = url
        checkNetAndDown(context)
    }

    constructor(context: BaseStatusBarActivity, url: String) {
        this.context = context
        this.url = url
        checkNetAndDown(context)
    }


    /**
     * 检查网络并下载
     */
    private fun checkNetAndDown(context: Context) {
        if (LhtTool.getNetworkType(context) == 0) {
            AlertDialog.Builder(context).setTitle("温馨提醒")
                    .setMessage("亲，您当前使用的是手机网络是否继续下载?").setCancelable(false)
                    .setPositiveButton("继续下载") { dialog, which -> downApk() }.setNegativeButton("以后再说") { dialog, which -> }.show()
        } else {
            downApk()
        }
    }


    /**
     * 进入主页
     */
    private fun gotoHome() {
        val mIntent = Intent()
        mIntent.setClass(context!!, MainActivity::class.java)
        context!!.startActivity(mIntent)
        context!!.finish()
    }

    /**
     * 从服务器下载新的Apk
     */
    private fun downApk() {
        initProgressDialog()
        file = File(Environment.getExternalStorageDirectory(), "m680com.apk")
        val downLoadTask = DownLoadTask(url, file!!.absolutePath, THREAD_NUM)
        downLoadTask.setListener(this)
        ThreadPoolManager.instance.addTask(downLoadTask)
    }

    /**
     * 安装Apk
     */
    private fun installApk() {
        var intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (MyApplication.INSTANCE.packageManager.canRequestPackageInstalls()) {
                    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    val contentUri = FileProvider.getUriForFile(context!!, "com.vikecn" + ".provider", file!!)
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
                } else {
                    if (!XXPermissions.isHasPermission(context, Permission.REQUEST_INSTALL_PACKAGES)) {
                        XXPermissions.with(context)
                                .permission(Permission.REQUEST_INSTALL_PACKAGES)
                                .request(object : OnPermission {
                                    override fun noPermission(denied: MutableList<String>?, quick: Boolean) {
                                        //跳转至“安装未知应用”权限界面，引导用户开启权限
                                        val selfPackageUri = Uri.parse("package:" + context!!.packageName)
                                        intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri)
                                        context!!.startActivityForResult(intent, REQUEST_CODE_UNKNOWN_APP)
                                    }

                                    override fun hasPermission(granted: MutableList<String>?, isAll: Boolean) {

                                    }
                                })
                    }
                }
            }
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context!!, "com.vikecn" + ".provider", file!!)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context?.startActivity(intent)
        context?.finish()
    }
    /**
     * 初始化进度条
     */
    private fun initProgressDialog() {
        mProgressDialog = MyProgressDialog(context)// 进度条初始化
        //        mProgressDialog.setCancelable(false);
        mProgressDialog!!.setCanceledOnTouchOutside(false)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        mProgressDialog!!.setMessage("更新下载中……")
        mProgressDialog!!.show()
        mProgressDialog!!.setOnCancelListener { ToastUtils.showShort("下载取消") }
    }


    override fun update(total: Int, len: Int, threadid: Int) {
        if (flag) {
            mProgressDialog!!.max = total
            flag = false
        }
        progressVaue += len
        mProgressDialog!!.progress = progressVaue
    }

    override fun downLoadFinish(totalSucess: Int) {

        if (totalSucess == THREAD_NUM) {
            installApk()
        } else {
            ToastUtils.showShort("下载失败")
        }

    }

    override fun downLoadError(type: Int) {

        ToastUtils.showShort("下载失败")
    }

    companion object {
        private const val REQUEST_CODE_UNKNOWN_APP = 0x100
    }
}
