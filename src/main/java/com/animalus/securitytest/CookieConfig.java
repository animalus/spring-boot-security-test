package com.animalus.securitytest;

public class CookieConfig {
    private String cipherKey;
    private Integer rememberMeDays;
    private String sameSiteOption; // NONE, LAX, STRICT

    public String getCipherKey() {
        return cipherKey;
    }

    public void setCipherKey(String cipherKey) {
        this.cipherKey = cipherKey;
    }

    public Integer getRememberMeDays() {
        return rememberMeDays;
    }

    public void setRememberMeDays(Integer rememberMeDays) {
        this.rememberMeDays = rememberMeDays;
    }

    public String getSameSiteOption() {
        return sameSiteOption;
    }

    public void setSameSiteOption(String sameSiteOption) {
        this.sameSiteOption = sameSiteOption;
    }

}
