package com.example.liuh.eventbus;

/**
 * Created by Administrator on 2016-09-14.
 */
public class BusEventData {

    private float childX;
    private float childY;
    private float radius;

    public int status;

    private float drawViewSize;
    private float distance;

    public BusEventData(float childX, float childY, float radius, float drawViewSize, float distance, int status) {
        this.childX = childX;
        this.childY = childY;
        this.radius = radius;
        this.status = status;
        this.drawViewSize = drawViewSize;
        this.distance = distance;
    }

    public float getChildX() {
        return childX;
    }

    public void setChildX(float childX) {
        this.childX = childX;
    }

    public float getChildY() {
        return childY;
    }

    public void setChildY(float childY) {
        this.childY = childY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public double getDrawViewSize() {
        return drawViewSize;
    }
    public void setDrawViewSize(float drawViewSize) {
        this.drawViewSize = drawViewSize;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
