package com.bashkanov.wifianalyzator;

public class Wifi {
    private String mSsid;
    private Integer mFrequency;
    private Integer mSignalpower;
    private String mBssid;

    public Wifi(String ssid, Integer frequency, Integer signalPower, String bSsid) {
        this.mSsid = ssid;
        this.mFrequency = frequency;
        this.mSignalpower = signalPower;
        this.mBssid = bSsid;
    }

    public String getSsid() {
        return mSsid;
    }

    public void setSsid(String ssid) {
        this.mSsid = ssid;
    }

    public Integer getFrequency() {
        return mFrequency;
    }

    public void setFrequency(Integer frequency) {
        this.mFrequency = frequency;
    }

    public Integer getSignalPower() {
        return mSignalpower;
    }

    public void setSignalPower(Integer signalPower) {
        this.mSignalpower = signalPower;
    }

    public String getBssid() {
        return mBssid;
    }

    public void setBssid(String bssid) {
        this.mBssid = bssid;
    }
}