package com.lespayne.smartwear.model;

public class SelfBean {
    private String leftTip;
    private String rightContent;
    private boolean isSex;

    public boolean isSex() {
        return isSex;
    }

    public void setSex(boolean sex) {
        isSex = sex;
    }

    public String getLeftTip() {
        return leftTip;
    }

    public void setLeftTip(String leftTip) {
        this.leftTip = leftTip;
    }

    public String getRightContent() {
        return rightContent;
    }

    public void setRightContent(String rightContent) {
        this.rightContent = rightContent;
    }
}
