package com.bjut.cyl.kfyrip.bean;

import java.util.List;

/**
 * 作者：haoran   on https://github.com/woaigmz 2017/6/5.
 * 邮箱：1549112908@qq.com
 * 说明：
 */

public class Photo {

    /**
     * code : 210
     * message : 图片获取成功
     * result : [{"id":"1","title":"专利","picture":"images_infor/b_zl.png"},{"id":"2","title":"立项到款","picture":"images_infor/b_ht2.png"},{"id":"3","title":"报奖","picture":"images_infor/b_bj.png"}]
     */

    public int code;
    public String message;
    public List<ResultBean> result;


    public static class ResultBean {
        /**
         * id : 1
         * title : 专利
         * picture : images_infor/b_zl.png
         */

        public String id;
        public String title;
        public String picture;


    }
}
