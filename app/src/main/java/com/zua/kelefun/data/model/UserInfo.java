package com.zua.kelefun.data.model;

/**
 * 饭否用户信息
 *
 * @author liukaiyang
 * @since 2017/2/6 18:02
 */

public class UserInfo extends BaseModel {
    //用户姓名
    private String name;
    //用户昵称
    private String screenName;
    //用户地址
    private String location;
    //用户性别
    private String gender;
    //用户生日
    private String birthday;
    //用户自述
    private String description;
    //用户头像地址
    private String profileImageUrl;
    //用户高清头像地址
    private String profileImageUrlLarge;
    //用户页面地址
    private String url;
    //用户是否设置隐私保护
    private boolean protect;
    //粉丝数
    private int followersCount;
    //好友数
    private int friendsCount;
    //收藏消息数
    private int favouritesCount;
    //消息数
    private int statusesCount;
    //该用户是否被当前登录用户关注
    private boolean following;
    //当前登录用户是否已对该用户发出关注请求
    private boolean notifications;
    //用户注册时间
    private String createdAt;
    //
    private String utcOffset;
    //用户用户自定义页面背景颜色
    private String profileBackgroundColor;
    //用户用户自定义文字颜色
    private String profileTextColor;
    //用户用户自定义链接颜色
    private String profileLinkColor;
    //用户用户自定义侧边栏颜色
    private String profileSidebarFillColor;
    //用户用户自定义侧边栏边框颜色
    private String profileSidebarBorderColor;
    //用户用户自定义背景图片地址
    private String profileBackgroundImageUrl;
    //是否平铺用户用户自定义背景图片地址
    private boolean profileBackgroundTile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getProfileImageUrlLarge() {
        return profileImageUrlLarge;
    }

    public void setProfileImageUrlLarge(String profileImageUrlLarge) {
        this.profileImageUrlLarge = profileImageUrlLarge;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(int statusesCount) {
        this.statusesCount = statusesCount;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isProtect() {
        return protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(String utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    public void setProfileBackgroundColor(String profileBackgroundColor) {
        this.profileBackgroundColor = profileBackgroundColor;
    }

    public String getProfileTextColor() {
        return profileTextColor;
    }

    public void setProfileTextColor(String profileTextColor) {
        this.profileTextColor = profileTextColor;
    }

    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    public void setProfileLinkColor(String profileLinkColor) {
        this.profileLinkColor = profileLinkColor;
    }

    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    public void setProfileSidebarFillColor(String profileSidebarFillColor) {
        this.profileSidebarFillColor = profileSidebarFillColor;
    }

    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    public void setProfileSidebarBorderColor(String profileSidebarBorderColor) {
        this.profileSidebarBorderColor = profileSidebarBorderColor;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public boolean isProfileBackgroundTile() {
        return profileBackgroundTile;
    }

    public void setProfileBackgroundTile(boolean profileBackgroundTile) {
        this.profileBackgroundTile = profileBackgroundTile;
    }

}
