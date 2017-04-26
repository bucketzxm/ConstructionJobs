package com.workerassistant.CityPick;

import com.workerassistant.CustomUI.IndexBar.bean.BaseIndexPinyinBean;

import java.util.List;

public class CityListHeaderBean extends BaseIndexPinyinBean {
    private List<String> cityList;
    //悬停ItemDecoration显示的Tag
    private String suspensionTag;

    public CityListHeaderBean() {
    }

    public CityListHeaderBean(List<String> cityList, String suspensionTag, String indexBarTag) {
        this.cityList = cityList;
        this.suspensionTag = suspensionTag;
        this.setBaseIndexTag(indexBarTag);
    }

    public List<String> getCityList() {
        return cityList;
    }

    public CityListHeaderBean setCityList(List<String> cityList) {
        this.cityList = cityList;
        return this;
    }

    public CityListHeaderBean setSuspensionTag(String suspensionTag) {
        this.suspensionTag = suspensionTag;
        return this;
    }

    @Override
    public String getTarget() {
        return null;
    }

    @Override
    public boolean isNeedToPinyin() {
        return false;
    }

    @Override
    public String getSuspensionTag() {
        return suspensionTag;
    }


}