package chen.vike.c680.tools

import android.text.TextUtils
import okhttp3.*
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.ArrayList
import java.util.concurrent.TimeUnit

/**
 * Created by lht on 2017/2/25.
 */

class OkhttpTool {

    init {
        okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout((30 * 1000).toLong(), TimeUnit.SECONDS)
                .writeTimeout((10 * 1000).toLong(), TimeUnit.SECONDS)
                .readTimeout((10 * 1000).toLong(), TimeUnit.SECONDS)
                .addNetworkInterceptor(NetInterceptor())
                //                .cookieJar(new CookieJar() {
                //                    @Override
                //                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                //                        //保存cookie信息,密码，表单等信息
                //                    }
                //
                //                    @Override
                //                    public List<Cookie> loadForRequest(HttpUrl url) {
                //                        //从保存位置读取，注意此处不能为空，否则会导致空指针
                //
                //                        return new ArrayList<Cookie>();
                //                    }
                //                })
                .build()

    }


    operator fun get(url: String, callback: Callback) {

        request = Request.Builder()
                .url(url)
                .get()
                .build()

        okHttpClient!!.newCall(request!!).enqueue(callback)
    }


    fun post(url: String, map: MutableMap<String, Any>?, callback: Callback) {

        val formBody = FormBody.Builder()
//        var mutableMap: MutableMap<String,Any> =
        if (null != map) {
            for ((key, value) in map) {
                try {
                    if (value.toString().isEmpty()){
                        map[key] = "null"
                    }
                    val o = URLEncoder.encode(value.toString(), "GBK")
                    formBody.add(key, o)
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
        }

        request = Request.Builder()
                .url(url)
                .post(formBody.build())
                .build()

        okHttpClient!!.newCall(request!!).enqueue(callback)

    }


    //表单访问
    fun httpPost(url: String, map: Map<String, Any>?, callback: Callback) {
        val formBody = FormBody.Builder()

        if (null != map) {
            for ((key, value) in map) {
                try {
                    val o = URLEncoder.encode(value.toString(), "GBK")
                    formBody.add(key, o)
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }

            }
        }

        request = Request.Builder()
                .url(url)
                .post(formBody.build())
                .build()

        okHttpClient!!.newCall(request!!).enqueue(callback)


    }

    //单张图片上传
    fun upLoadImage(url: String, f: File, callback: Callback) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        builder.addFormDataPart("imgFile", "imgFile.jpg", RequestBody.create(MediaType.parse("image/*"), f))

        request = Request.Builder()
                .url(url)
                .post(builder.build())
                .build()

        okHttpClient!!.newCall(request!!).enqueue(callback)

    }

    /**
     * 多张图片上传
     * @param url
     * @param fileList
     * @param callback
     */
    fun updateMoreImg(url: String, fileList: ArrayList<File>, maps: Map<String, Any>, callback: Callback) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        for (key in maps.keys) {
            builder.addFormDataPart(key, maps[key]!!.toString() + "")
        }
        for (file in fileList) {
            if (file.exists()) {
                builder.addFormDataPart("imgFile", file.name, RequestBody.create(MediaType.parse("image/*"), file))
            }
        }
        request = Request.Builder()
                .url(url)
                .post(builder.build())
                .build()
        okHttpClient!!.newCall(request!!).enqueue(callback)
    }

    /**
     * 上传json语句.
     *
     * @param url
     * @param json
     * @param callback
     */
    fun uploadJson(url: String, json: String, callback: Callback) {
        val requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
        okHttpClient!!.newCall(request!!).enqueue(callback)

    }

    /**
     * 取消请求
     * 注：在实际中用户点得快的话几个页面反复切换会报错 java.io.IOException: Canceled
     * 此方法保险起见暂时不用
     */
    fun cancelRequest() {
        okHttpClient?.dispatcher()?.cancelAll()
    }


    /**
     * 改掉消息头中的KeepAlive为Close
     * 为了避免频繁的发起Http请求时OkHttp出错
     * 使用缓存
     */
    class NetInterceptor : Interceptor{

        //设缓存有效期为一个星期
        private val CACHE_STALE_SEC = (60 * 60 * 24 * 7).toLong()

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
//                    .addHeader("Connection","close")
                    .build()
            val response = chain.proceed(request)

            var cacheControl = request.cacheControl().toString()
            if (TextUtils.isEmpty(cacheControl)){
                cacheControl = "public, max-age=60"
            }
            return response.newBuilder()
                    .header("Cache-Control",cacheControl)
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                    .header("Connection","keep-alive")
                    .removeHeader("Pragma")
                    .build()
        }
    }




    companion object {

        private var okhttpTool: OkhttpTool? = null
        private var okHttpClient: OkHttpClient? = null
        private var request: Request? = null


        fun getOkhttpTool(): OkhttpTool {

            if (null == okhttpTool) {

                synchronized(OkhttpTool::class.java) {
                    if (null == okhttpTool) {
                        okhttpTool = OkhttpTool()
                    }
                }
            }
            return okhttpTool!!
        }
    }
}
