package chen.vike.c680.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import chen.vike.c680.tools.BaseStatusBarActivity
import com.lht.vike.a680_v1.R
import kotterknife.bindView

/**
 * Created by lht on 2018/7/31.
 * 修改店铺名称
 */

class ChangeShopNameActivity : BaseStatusBarActivity() {
    private val eidtDianpuName: EditText by bindView(R.id.eidt_dianpu_name)
    private val xiugaiNameEnterBtn: Button by bindView(R.id.xiugai_name_enter_btn)
    private var context: Context? = null
    private var name: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xiugai_dianpu_name)
        context = this
        name = intent.getStringExtra("dianpuname")
        eidtDianpuName.setText(name)
        xiugaiNameEnterBtn.setOnClickListener {
            val intent = Intent()
            intent.putExtra("dianpuname", eidtDianpuName.text.toString())
            setResult(789, intent)
            finish()
        }
    }
}
