package org.wirbleibenalle.stressi.util;

/**
 * Created by and on 27.01.17.
 */

public class AnimatorScaleDownHelper {
    private int initWidth;
    private int initHeight;
    private int totalScaledWidth;
    private int totalScaledHeight;
    private float lastPosition;

    public AnimatorScaleDownHelper(int initWidth, int initHeight, int totalScaledWidth, int
        totalScaledHeight) {
        this.initWidth = initWidth;
        this.initHeight = initHeight;
        this.totalScaledHeight = totalScaledHeight;
        this.totalScaledWidth = totalScaledWidth;
        this.lastPosition = -1;
    }

    public int[] getSizeForCurrentValue(float positionCurrent, float maxPosition, float
        minPosition) {
        float middlePosition = getMiddlePosition(maxPosition,minPosition);
        boolean scaleDown = isScalingDown(middlePosition,positionCurrent,maxPosition,minPosition);


        int[] newSize = new int[2];
        float pixPositionW = initWidth / maxPosition;
        float pixPositionH = initHeight / maxPosition;
        newSize[0] = (int)((positionCurrent*2)*initWidth / maxPosition);
        newSize[1] = (int)((positionCurrent*2)*initHeight / maxPosition);

        if(!scaleDown){
            newSize[0] += (int)((positionCurrent-middlePosition)*initWidth / maxPosition);
            newSize[1] += (int)((positionCurrent-middlePosition)*initHeight / maxPosition);
        }
        return newSize;
    }

    public float getMiddlePosition(float maxPosition, float minPosition){
        return (maxPosition-minPosition )/2.0f;
    }

    public boolean isScalingDown(float middlePosition,float positionCurrent, float maxPosition,
                                 float minPosition){
        return lastPosition<0 ? true: positionCurrent<lastPosition &&
            positionCurrent<middlePosition;
    }

    public float getLastPosition() {
        return lastPosition;
    }

    public void reset(){
        lastPosition = -1;
    }
}
