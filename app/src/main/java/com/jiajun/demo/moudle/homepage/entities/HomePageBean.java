package com.jiajun.demo.moudle.homepage.entities;

import java.util.List;

/**
 * Created by dan on 2017/11/20/020.
 */

public class HomePageBean {


    /**
     * imgList : [{"url":"http://www.163.com","imgUrl":"http://admintest.implus100.com:80/file/manager/20161130160810511.png","desc":"http://admintest.implus100.com:80OK"}]
     * share_title : 广福E宝
     * share_url : http://www.implus100.com/agent/interface/app_share.jsp?appShorName=ax
     * share_image_url : http://www.implus100.com/agent/interface/images/logo_ax.png
     * share_content : 广福E宝，买保险算佣金简单快捷。更多服务等着您。
     * insure_car_url : http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp
     * host_all_url : http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?userIds=297ebe0e55dd69430155de1e99db006e
     * policyMenu : [{"name":"智能报价","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon01.png"},{"name":"人工报价","url":"http://admintest.implus100.com:80/agent-new/vip_insure/vip_bill.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon02.png"},{"name":"车险订单","url":"http://admintest.implus100.com:80/agent-new/order/order_list.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon03.png"},{"name":"个险订单","url":"http://admintest.implus100.com:80/agent-new/order/order_list.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon04.png"},{"name":"工具箱","url":"","imgUrl":"/agent-new/weixin/images/xb_v2_icon05.png"},{"name":"团队管理","url":"http://admintest.implus100.com:80//agent/member_center/my_branch.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon06.png"},{"name":"业绩","url":"http://admintest.implus100.com:80/agent-new/order/order_list.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon07.png"},{"name":"钱包","url":"http://admintest.implus100.com:80/agent-new/weixin/wallet.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon08.png"},{"name":" 续费提醒","url":"http://admintest.implus100.com:80/agent-new/order/order_list.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon09.png"},{"name":" 知识吧","url":"http://admintest.implus100.com:80//agent/member_center/my_branch.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon010.png"},{"name":"微店","url":"http://admintest.implus100.com:80/agent-new/handle_new/home.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon011.png"},{"name":"优惠券","url":"http://admintest.implus100.com:80//agent-new/coupon/coupon_list.jsp?userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_icon012.png"}]
     * hostMenu : [{"title":"费改后车主最高可享3.8折","description":"万元1小时赔付  理赔网点最多","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=101&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_hot01.png","logo":"/agent-new/weixin/images/picc.png"},{"title":"费改后车主最高可享3.8折","description":"万元1小时赔付  理赔网点最多","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_v2_hot02.png","logo":"/agent-new/weixin/images/ans.png"}]
     * newMenu : [{"title":"新品上市","description":"本月新品","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_tj01.png"},{"title":"意外险","description":"排行榜","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_tj02.png"},{"title":"少儿险","description":"排行榜","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_tj03.png"},{"title":"健康险","description":"排行榜","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_tj04.png"},{"title":"旅游险","description":"排行榜","url":"http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e","imgUrl":"/agent-new/weixin/images/xb_tj04.png"}]
     */

    private String share_title;
    private String share_url;
    private String share_image_url;
    private String share_content;
    private String insure_car_url;
    private String host_all_url;
    private List<ImgListBean> imgList;
    private List<PolicyMenuBean> policyMenu;
    private List<HostMenuBean> hostMenu;
    private List<NewMenuBean> newMenu;

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getShare_image_url() {
        return share_image_url;
    }

    public void setShare_image_url(String share_image_url) {
        this.share_image_url = share_image_url;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getInsure_car_url() {
        return insure_car_url;
    }

    public void setInsure_car_url(String insure_car_url) {
        this.insure_car_url = insure_car_url;
    }

    public String getHost_all_url() {
        return host_all_url;
    }

    public void setHost_all_url(String host_all_url) {
        this.host_all_url = host_all_url;
    }

    public List<ImgListBean> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImgListBean> imgList) {
        this.imgList = imgList;
    }

    public List<PolicyMenuBean> getPolicyMenu() {
        return policyMenu;
    }

    public void setPolicyMenu(List<PolicyMenuBean> policyMenu) {
        this.policyMenu = policyMenu;
    }

    public List<HostMenuBean> getHostMenu() {
        return hostMenu;
    }

    public void setHostMenu(List<HostMenuBean> hostMenu) {
        this.hostMenu = hostMenu;
    }

    public List<NewMenuBean> getNewMenu() {
        return newMenu;
    }

    public void setNewMenu(List<NewMenuBean> newMenu) {
        this.newMenu = newMenu;
    }

    public static class ImgListBean {
        /**
         * url : http://www.163.com
         * imgUrl : http://admintest.implus100.com:80/file/manager/20161130160810511.png
         * desc : http://admintest.implus100.com:80OK
         */

        private String url;
        private String imgUrl;
        private String desc;

        public ImgListBean(String url, String imgUrl, String desc) {
            this.url = url;
            this.imgUrl = imgUrl;
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class PolicyMenuBean {
        /**
         * name : 智能报价
         * url : http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?userIds=297ebe0e55dd69430155de1e99db006e
         * imgUrl : /agent-new/weixin/images/xb_v2_icon01.png
         */

        private String name;
        private String url;
        private String imgUrl;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class HostMenuBean {
        /**
         * title : 费改后车主最高可享3.8折
         * description : 万元1小时赔付  理赔网点最多
         * url : http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=101&userIds=297ebe0e55dd69430155de1e99db006e
         * imgUrl : /agent-new/weixin/images/xb_v2_hot01.png
         * logo : /agent-new/weixin/images/picc.png
         */

        private String title;
        private String description;
        private String url;
        private String imgUrl;
        private String logo;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }

    public static class NewMenuBean {
        /**
         * title : 新品上市
         * description : 本月新品
         * url : http://admintest.implus100.com:80/agent-new/handle_new/insure_page.jsp?product_code=8&userIds=297ebe0e55dd69430155de1e99db006e
         * imgUrl : /agent-new/weixin/images/xb_tj01.png
         */

        private String title;
        private String description;
        private String url;
        private String imgUrl;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
