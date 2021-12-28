package com.example.familysecurity.Detection;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetectionViewModel extends ViewModel {
    private MutableLiveData<DetectionData> detectionMutableLiveData;
    private MutableLiveData<List<byte[]>> imgDetectionList;

    public MutableLiveData<DetectionData> getDetectionMutableLiveData() {
        if (detectionMutableLiveData == null) {
            this.detectionMutableLiveData = new MutableLiveData<>();
            this.detectionMutableLiveData.setValue(null);
        }
        return this.detectionMutableLiveData;
    }

    public MutableLiveData<List<byte[]>> getImgDetectionList() {
        if (imgDetectionList == null) {
            this.imgDetectionList = new MutableLiveData<>();
            this.imgDetectionList.setValue(new ArrayList<>());
        }
        return this.imgDetectionList;
    }

}
