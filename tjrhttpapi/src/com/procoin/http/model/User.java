package com.procoin.http.model;

import com.procoin.http.base.TaojinluType;

public class User implements TaojinluType {

    public long synTime;
    public long userId;
    public long roomId;
    public int synVersion;
    public int sex;
    public int isLock;
    public int passErrNum;
    public int otcCertify;
    public int otcIssue;
    public int idCertify;//  0：未认证 1：已认证
    public String synSign;
    public String phone;



    public String countryCode;
    public String userName;
    public String headUrl;
    public String bgUrl;
    public String userPass;
    public String birthday;
    public String describes;
    public String createTime;
    public String lastIp;
    public String lastLogin;
    public String regPlatform;
    public String payPass;//不为空说明已经设置了交易密码
    public String userRealName; // 真实姓名

    public String shiftKey; // 短名称 ，如:A

    public User() {
    }

    public User(long userId, String userName,String headUrl) {
        this.userId = userId;
        this.userName = userName;
        this.headUrl=headUrl;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getRoomId() {
        return roomId;
    }

    public long getSynTime() {
        return synTime;
    }

    public void setSynTime(long synTime) {
        this.synTime = synTime;
    }

    public void setSynSign(String synSign) {
        this.synSign = synSign;
    }

    public void setSynVersion(int synVersion) {
        this.synVersion = synVersion;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setDescribes(String describes) {
        this.describes = describes;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setPassErrNum(int passErrNum) {
        this.passErrNum = passErrNum;
    }

    public void setRegPlatform(String regPlatform) {
        this.regPlatform = regPlatform;
    }

    public void setPayPass(String payPass) {
        this.payPass = payPass;
    }

    public void setOtcCertify(int otcCertify) {
        this.otcCertify = otcCertify;
    }

    public void setOtcIssue(int otcIssue) {
        this.otcIssue = otcIssue;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public void setIdCertify(int idCertify) {
        this.idCertify = idCertify;
    }

    public String getSynSign() {
        return synSign;
    }

    public int getSynVersion() {
        return synVersion;
    }

    public long getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setBgUrl(String url) {
        bgUrl = url;
    }

    public String getBgUrl() {
        return bgUrl;
    }

    public String getUserPass() {
        return userPass;
    }

    public int getSex() {
        return sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDescribes() {
        return describes;
    }

    public int getIsLock() {
        return isLock;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getLastIp() {
        return lastIp;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public int getPassErrNum() {
        return passErrNum;
    }

    public String getRegPlatform() {
        return regPlatform;
    }

    public String getPayPass() {
        return payPass;
    }

    public int getOtcCertify() {
        return otcCertify;
    }

    public int getOtcIssue() {
        return otcIssue;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public int getIdCertify() {
        return idCertify;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
