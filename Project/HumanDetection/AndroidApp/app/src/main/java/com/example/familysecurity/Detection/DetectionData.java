package com.example.familysecurity.Detection;

public class DetectionData {
    public DetectionData(byte[] src, String name, String date, String time) {
        this.src = src;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public byte[] src;
    public String name;
    public String date;
    public String time;
}
