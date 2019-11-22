package org.chromium.chrome.browser.ntp.sponsored;

public class BackgroundImage {
    private int imageDrawable;
    private int centerPoint;
    private ImageCredit imageCredit;

    public BackgroundImage(int imageDrawable, int centerPoint, ImageCredit imageCredit) {
        this.imageDrawable = imageDrawable;
        this.centerPoint = centerPoint;
        this.imageCredit = imageCredit;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public int getCenterPoint() {
        return centerPoint;
    }

    public ImageCredit getImageCredit() {
        return imageCredit;
    }
}
