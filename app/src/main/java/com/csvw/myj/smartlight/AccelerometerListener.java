package com.csvw.myj.smartlight;

/**
 * @ClassName: AccelerometerListener
 * @Description:
 * @Author: MYJ
 * @CreateDate: 2020/8/11 12:55
 */
public interface AccelerometerListener {
    public void onAccelerationChanged(float x, float y, float z);
    public void onShake(float force);
}
