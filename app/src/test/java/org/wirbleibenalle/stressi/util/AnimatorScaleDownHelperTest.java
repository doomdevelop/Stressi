package org.wirbleibenalle.stressi.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by and on 27.01.17.
 */
@RunWith(MockitoJUnitRunner.class)
public class AnimatorScaleDownHelperTest {
    int initW = 100;
    int initH = 50;
    int totalScaleW = 10;
    int totalScaleH = 5;
    AnimatorScaleDownHelper animatorScaleDownHelper;

    @Before
    public void init() {
        animatorScaleDownHelper = new AnimatorScaleDownHelper(initW, initH, totalScaleW, totalScaleH);
    }

    @Test
    public void getSizeForCurrentValue_scaleDown() throws Exception {
        int[] scaledValues = animatorScaleDownHelper.getSizeForCurrentValue(0.25f, 1.0f, 0.0f);
        assertNotNull(scaledValues);
        float scaledW = ((float) initW) * 0.5f;
        assertEquals(scaledW, scaledValues[0], 0.001);

        scaledValues = animatorScaleDownHelper.getSizeForCurrentValue(0.5f, 1.0f, 0.0f);
        assertNotNull(scaledValues);
        scaledW = ((float) initW) * 0.0f;
        assertEquals(scaledW, scaledValues[0], 0.001);
    }

    @Test
    public void getSizeForCurrentValue_scaleDownAndUp() throws Exception {
        int[] scaledValues = animatorScaleDownHelper.getSizeForCurrentValue(0.25f, 1.0f, 0.0f);
        assertNotNull(scaledValues);
        float scaledW = ((float) initW) * 0.5f;
        assertEquals(scaledW, scaledValues[0], 0.001);
        scaledValues = animatorScaleDownHelper.getSizeForCurrentValue(0.25f, 1.0f, 0.0f);
    }

    @Test
    public void testGetMiddlePosition() {
        float middlePosition = animatorScaleDownHelper.getMiddlePosition(100.0f, 50.0f);
        assertEquals(25, middlePosition, 0.0001);

        middlePosition = animatorScaleDownHelper.getMiddlePosition(1.0f, 0.0f);
        assertEquals(0.5f, middlePosition, 0.0001);
    }

    @Test
    public void testIsScalingDown() {
        float middlePosition = animatorScaleDownHelper.getMiddlePosition(1.0f, 0.0f);

        boolean isScalingDown = animatorScaleDownHelper.isScalingDown(middlePosition,0.1f, 0.1f,0.0f);
        assertTrue(isScalingDown);
        isScalingDown = animatorScaleDownHelper.isScalingDown(middlePosition,0.1f, 0.2f,0.0f);
        assertTrue(isScalingDown);
        isScalingDown = animatorScaleDownHelper.isScalingDown(middlePosition,0.1f, 0.3f,0.0f);
        assertTrue(isScalingDown);
        isScalingDown = animatorScaleDownHelper.isScalingDown(middlePosition,0.1f, 0.2f,0.0f);
        assertFalse(isScalingDown);
    }

    @Test
    public void testReset(){
        int[] scaledValues = animatorScaleDownHelper.getSizeForCurrentValue(0.25f, 1.0f, 0.0f);
        assertNotNull(scaledValues);
        float scaledW = ((float) initW) * 0.5f;
        assertEquals(scaledW, scaledValues[0], 0.001);
        float lastPosition = animatorScaleDownHelper.getLastPosition();
        assertEquals(0.25f,lastPosition,0.001);
        animatorScaleDownHelper.reset();
        assertTrue(animatorScaleDownHelper.getLastPosition()<0);


    }
}