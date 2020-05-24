package com.csvw.myj.smartlight.dao;

import com.csvw.myj.smartlight.Light;

import java.util.List;

/**
 * @ClassName: ILightDao
 * @Description: 数据库接口层
 * @Author: MYJ
 * @CreateDate: 2020/5/24 16:13
 */
public interface ILightDao {
    /**
     * 添加灯
     * @param light
     * @return
     */
    long addLight(Light light);

    /**
     * 删
     * @param id
     * @return
     */
    int delUserById(int id);

    /**
     * 更新
     * @param Light
     * @return
     */
    int updateLight(Light Light);

    /**
     * 查询
     * @param id
     * @return
     */
    Light getLightById(int id);

    /**
     * 获取所有灯
     * @return
     */
    List<Light> listAllLight();
}
