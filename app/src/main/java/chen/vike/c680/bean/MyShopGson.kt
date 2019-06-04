package chen.vike.c680.bean

/**
 * Created by lht on 2017/3/18.
 */

class MyShopGson {


    /**
     * shopInfo : {"city":"太原","type2_f":"0,390,0","shop_des":"x时间财富网第一批优质店铺，我们名誉好，效率高，威客的英文Witkey是由wit智慧、key钥匙两个单词组成，也是The key of wisdom 的缩写，是指那些通过互联网把自己的智慧、知识、能力、经验转换成实际收益的人，他们在互联网上通过解决科学，技术，工作，生活，学习中的问题从而让知识、智慧、经验、技能体现经济价值。","sheng_f":"25","userid":"583067","shop_name":"gguu111","shi_f":"263","shop_type":"12","type1_f":"0,4,0","province":"山西"}
     * class_list : [{"classname":"策划文案,品牌故事","classid":"4,390"}]
     */

    var shopInfo: ShopInfoBean? = null
    var class_list: List<ClassListBean>? = null

    class ShopInfoBean {
        /**
         * city : 太原
         * type2_f : 0,390,0
         * shop_des : x时间财富网第一批优质店铺，我们名誉好，效率高，威客的英文Witkey是由wit智慧、key钥匙两个单词组成，也是The key of wisdom 的缩写，是指那些通过互联网把自己的智慧、知识、能力、经验转换成实际收益的人，他们在互联网上通过解决科学，技术，工作，生活，学习中的问题从而让知识、智慧、经验、技能体现经济价值。
         * sheng_f : 25
         * userid : 583067
         * shop_name : gguu111
         * shi_f : 263
         * shop_type : 12
         * type1_f : 0,4,0
         * province : 山西
         */

        var city: String? = null
        var type2_f: String? = null
        var shop_des: String? = null
        var sheng_f: String? = null
        var userid: String? = null
        var shop_name: String? = null
        var shi_f: String? = null
        var shop_type: String? = null
        var type1_f: String? = null
        var province: String? = null
    }

    class ClassListBean {
        /**
         * classname : 策划文案,品牌故事
         * classid : 4,390
         */

        var classname: String? = null
        var classid: String? = null
    }
}
