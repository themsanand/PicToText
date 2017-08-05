package com.example.hopef.testproject;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

/**
 * Created by hopef on 8/3/2017.
 */

public class OCRProcessor implements Detector.Processor<TextBlock> {

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        Log.e("TAG!", "RECEIVING");
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); i++) {
            TextBlock item = items.valueAt(i);
            Log.e("TAG!", item.getValue());
        }
    }

    @Override
    public void release() {
    }
}
