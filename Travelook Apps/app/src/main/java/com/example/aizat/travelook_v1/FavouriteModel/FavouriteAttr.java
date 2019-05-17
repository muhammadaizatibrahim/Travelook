package com.example.aizat.travelook_v1.FavouriteModel;

public class FavouriteAttr {
    public String placeId;
    public String url;
    public String name;
    public String addr;
    public String phoneNum;
    public String website;
    public String favId;
    public String cuid;

    public FavouriteAttr() {
    }

    public FavouriteAttr(String placeId, String url, String name, String addr, String phoneNum, String website, String favId,String cuid) {
        this.placeId = placeId;
        this.url = url;
        this.name = name;
        this.addr = addr;
        this.phoneNum = phoneNum;
        this.website = website;
        this.favId = favId;
        this.cuid = cuid;
    }


    public String getPlaceId() { return placeId; }

    public void setPlaceId(String placeId) { this.placeId = placeId; }


    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }


    public String getName() { return name; }

    public void setName(String name) { this.name = name; }


    public String getAddr() { return addr; }

    public void setAddr(String addr) { this.addr = addr; }


    public String getPhoneNum() { return phoneNum; }

    public void setPhoneNum(String phoneNum) { this.phoneNum = phoneNum; }


    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }


    public String getFavId() { return favId; }

    public void setFavId(String favId) { this.favId = favId; }


    public String getCuid() { return cuid; }

    public void setCuid(String cuid) { this.cuid = cuid; }
}
