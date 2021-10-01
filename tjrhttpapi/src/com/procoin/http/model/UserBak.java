package com.procoin.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;
import com.procoin.http.util.ParcelUtils;

public class UserBak implements TaojinluType, Parcelable {

    // Fields
    private Long userId; // 登录用户ID
    private String userAccount; // 登录用户账号
    private String type; // 登录方式，rr代表是人人网，kx代表开心网，mb代表手机 ,
    // sinawb 代表微薄
    private String name; // 用户名
    private String sex; // 性别，0代表女性，1代表男性
    private String headurl; // 头像路径
    private String headurlLarge; // 大头像路径
    private String selfDescription; // 自我描述
    private String purview; // 1代表仅有自己可以见,2代表好友可见,3代表所有人可见
    private String birthday; // 生日 格式2011-2-1
    private String profession; // 职业
    private String address; // 地址
    private int stockAge; // 股龄
    private int isFirstLogin; // 是否第一次登录,0代表没有登录过，1代表已登录过了
    private String investmentStyle; // 投资风格 1代表短线激进型,2代表中线波段型,3代表长线稳健型
    private Long friendRelationId; // 用户好友关系ID，用于成为好友更新表
    private boolean check;
    private String islook;// 是不是看过这篇文章
    private String tjrApiKey; // 用来验证用户
    private int isVip; // 是不是vip用户 0不是， 1是， 2 待定
    private String vipDesc; //
    private String shiftKey; // 短名称 ，如:A
    private String complaintTip;   //投诉警告

    private String bind_prod_code;//如果不为空说明是网红本人。就可以发动态,已废弃 现在用is_show_dy
    private int is_show_dy;//如果不为空说明是网红本人。就可以发动态

    private int idCertify; //  0：未认证 1：已认证
    private String userRealName; // 真实姓名

    public int getIdCertify() {
        return idCertify;
    }

    public void setIdCertify(int idCertify) {
        this.idCertify = idCertify;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    //    private String verify_url;//认证需要跳转的网页

//    public String getVerify_url() {
//        return verify_url;
//    }
//
//    public void setVerify_url(String verify_url) {
//        this.verify_url = verify_url;
//    }
//
//    public int getVerify_status() {
//        return verify_status;
//    }
//
//    public void setVerify_status(int verify_status) {
//        this.verify_status = verify_status;
//    }
//
//    public String getVerify_msg() {
//        return verify_msg;
//    }
//
//    public void setVerify_msg(String verify_msg) {
//        this.verify_msg = verify_msg;
//    }
//
//    public String getVerify_name() {
//        return verify_name;
//    }
//
//    public void setVerify_name(String verify_name) {
//        this.verify_name = verify_name;
//    }

    public UserBak() {

    }



    public UserBak(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getBind_prod_code() {
        return bind_prod_code;
    }

    public int getIs_show_dy() {
        return is_show_dy;
    }

    public void setIs_show_dy(int is_show_dy) {
        this.is_show_dy = is_show_dy;
    }

    public void setBind_prod_code(String bind_prod_code) {
        this.bind_prod_code = bind_prod_code;
    }

    public String getComplaintTip() {
        return complaintTip;
    }

    public void setComplaintTip(String complaintTip) {
        this.complaintTip = complaintTip;
    }

    public String getShiftKey() {
        return shiftKey;
    }

    public void setShiftKey(String shiftKey) {
        this.shiftKey = shiftKey;
    }

    public String getVipDesc() {
        return vipDesc;
    }

    public void setVipDesc(String vipDesc) {
        this.vipDesc = vipDesc;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getIslook() {
        return islook;
    }

    public void setIslook(String islook) {
        this.islook = islook;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the headurl
     */
    public String getHeadurl() {
        return headurl;
    }

    /**
     * @param headurl the headurl to set
     */
    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    /**
     * @return the selfDescription
     */
    public String getSelfDescription() {
        return selfDescription;
    }

    /**
     * @param selfDescription the selfDescription to set
     */
    public void setSelfDescription(String selfDescription) {
        if ("null".equals(selfDescription)) selfDescription = "";
        this.selfDescription = selfDescription;
    }

    /**
     * @return the purview
     */
    public String getPurview() {
        return purview;
    }

    /**
     * @param purview the purview to set
     */
    public void setPurview(String purview) {
        this.purview = purview;
    }

    /**
     * @return the birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(String birthday) {
        if ("null".equals(birthday)) birthday = "";
        this.birthday = birthday;
    }

    /**
     * @return the profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @param profession the profession to set
     */
    public void setProfession(String profession) {
        if ("null".equals(profession)) profession = "";
        this.profession = profession;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        if ("null".equals(address)) address = "";
        this.address = address;
    }

    /**
     * @return the stockAge
     */
    public int getStockAge() {
        return stockAge;
    }

    /**
     * @param stockAge the stockAge to set
     */
    public void setStockAge(int stockAge) {
        this.stockAge = stockAge;
    }

    public int getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(int isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * @return the investmentStyle
     */
    public String getInvestmentStyle() {
        return investmentStyle;
    }

    /**
     * @param investmentStyle the investmentStyle to set
     */
    public void setInvestmentStyle(String investmentStyle) {
        this.investmentStyle = investmentStyle;
    }

    /**
     * @return the friendRelationId
     */
    public Long getFriendRelationId() {
        return friendRelationId;
    }

    /**
     * @param friendRelationId the friendRelationId to set
     */
    public void setFriendRelationId(Long friendRelationId) {
        this.friendRelationId = friendRelationId;
    }

    /**
     * @return the userAccount
     */
    public String getUserAccount() {
        return userAccount;
    }

    /**
     * @param userAccount the userAccount to set
     */
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    /**
     * 获取是否已经选择
     *
     * @return
     */
    public boolean isCheck() {
        return check;
    }

    /**
     * 是否已经选择
     *
     * @return
     */
    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getTjrApiKey() {
        return tjrApiKey;
    }

    public void setTjrApiKey(String tjrApiKey) {
        this.tjrApiKey = tjrApiKey;
    }

    public String getHeadurlLarge() {
        return headurlLarge;
    }

    public void setHeadurlLarge(String headurlLarge) {
        this.headurlLarge = headurlLarge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private UserBak(Parcel in) {
        userId = in.readLong();
        userAccount = ParcelUtils.readStringFromParcel(in);
        type = ParcelUtils.readStringFromParcel(in);
        name = ParcelUtils.readStringFromParcel(in);
        sex = ParcelUtils.readStringFromParcel(in);
        headurl = ParcelUtils.readStringFromParcel(in);
        headurlLarge = ParcelUtils.readStringFromParcel(in);
        selfDescription = ParcelUtils.readStringFromParcel(in);
        purview = ParcelUtils.readStringFromParcel(in);
        birthday = ParcelUtils.readStringFromParcel(in);
        profession = ParcelUtils.readStringFromParcel(in);
        address = ParcelUtils.readStringFromParcel(in);
        stockAge = in.readInt();
        investmentStyle = ParcelUtils.readStringFromParcel(in);
        check = in.readInt() == 1;
        islook = ParcelUtils.readStringFromParcel(in);
        tjrApiKey = ParcelUtils.readStringFromParcel(in);
        isVip = in.readInt();
        shiftKey = ParcelUtils.readStringFromParcel(in);
        bind_prod_code= ParcelUtils.readStringFromParcel(in);
        is_show_dy= in.readInt();

        idCertify=in.readInt();
        userRealName = ParcelUtils.readStringFromParcel(in);

//        verify_status= in.readInt();
//        verify_msg= ParcelUtils.readStringFromParcel(in);
//        verify_name= ParcelUtils.readStringFromParcel(in);


    }



    public static final Creator<UserBak> CREATOR = new Creator<UserBak>() {
        public UserBak createFromParcel(Parcel in) {
            return new UserBak(in);
        }

        @Override
        public UserBak[] newArray(int size) {
            return new UserBak[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(userId == null ? 0 : userId);
        ParcelUtils.writeStringToParcel(out, userAccount);
        ParcelUtils.writeStringToParcel(out, type);
        ParcelUtils.writeStringToParcel(out, name);
        ParcelUtils.writeStringToParcel(out, sex);
        ParcelUtils.writeStringToParcel(out, headurl);
        ParcelUtils.writeStringToParcel(out, headurlLarge);
        ParcelUtils.writeStringToParcel(out, selfDescription);
        ParcelUtils.writeStringToParcel(out, purview);
        ParcelUtils.writeStringToParcel(out, birthday);
        ParcelUtils.writeStringToParcel(out, profession);
        ParcelUtils.writeStringToParcel(out, address);
        out.writeInt(stockAge);
        ParcelUtils.writeStringToParcel(out, investmentStyle);
        out.writeInt(check ? 1 : 0);
        ParcelUtils.writeStringToParcel(out, islook);
        ParcelUtils.writeStringToParcel(out, tjrApiKey);
        out.writeInt(isVip);
        ParcelUtils.writeStringToParcel(out, shiftKey);

        ParcelUtils.writeStringToParcel(out, bind_prod_code);
        out.writeInt(is_show_dy);

        out.writeInt(idCertify);
        ParcelUtils.writeStringToParcel(out, userRealName);

//        out.writeInt(verify_status);
//        ParcelUtils.writeStringToParcel(out, verify_msg);
//        ParcelUtils.writeStringToParcel(out, verify_name);

    }
    @Override
    public String toString() {
        return "User [userId=" + userId + ", userAccount=" + userAccount + ", type=" + type + ", name=" + name + ", sex=" + sex + ", headurl=" + headurl + ", selfDescription=" + selfDescription + ", purview=" + purview + ", birthday=" + birthday + ", profession=" + profession + ", address=" + address + ", stockAge=" + stockAge + ", investmentStyle=" + investmentStyle + ", friendRelationId=" + friendRelationId + ", check=" + check + ", islook=" + islook + ", tjrApiKey=" + tjrApiKey + "]";
    }

}
