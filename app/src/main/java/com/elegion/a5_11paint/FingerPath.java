package com.elegion.a5_11paint;

import android.graphics.Path;

public class FingerPath {

    public int mColor;
    public int mStrokeWidth;
    public Path mPath;

    public FingerPath(int color, int strokeWidth, Path path){
        mColor = color;
        mStrokeWidth = strokeWidth;
        mPath = path;
    }

}
