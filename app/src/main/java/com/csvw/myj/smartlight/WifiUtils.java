package com.csvw.myj.smartlight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: WifiUtils
 * @Description:
 * @Author: MYJ
 * @CreateDate: 2020/6/10 0:43
 */
class WifiUtils {
    private static WifiUtils utils = null;

    public WifiUtils(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    public static WifiUtils getInstance(Context context) {
        if (utils == null) {
            synchronized (WifiUtils.class) {
                if (utils == null) {
                    utils = new WifiUtils(context);
                }
            }
        }
        return utils;
    }

    private WifiManager wifiManager;

    /**
     * wifi是否打开
     * @return
     */
    public boolean isWifiEnable(){
        boolean isEnable = false;
        if (wifiManager != null){
            if (wifiManager.isWifiEnabled()){
                isEnable = true;
            }
        }
        return isEnable;
    }

    /**
     * 打开WiFi
     */
    public void openWifi(){
        if (wifiManager != null && !isWifiEnable()){
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭WiFi
     */
    public void closeWifi(){
        if (wifiManager != null && isWifiEnable()){
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获取WiFi列表
     * @return
     */
    public List<ScanResult> getWifiList(){
        List<ScanResult> resultList = new ArrayList<>();
        if (wifiManager != null && isWifiEnable()){
            resultList.addAll(wifiManager.getScanResults());
        }
        return resultList;
    }

    /**
     * 有密码连接
     * @param ssid
     * @param pws
     */
    public void connectWifiPws(String ssid, String pws){
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        int netId = wifiManager.addNetwork(getWifiConfig(ssid, pws, true));
        wifiManager.enableNetwork(netId, true);
    }

    /**
     * 无密码连接
     * @param ssid
     */
    public void connectWifiNoPws(String ssid){
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        int netId = wifiManager.addNetwork(getWifiConfig(ssid, "", false));
        wifiManager.enableNetwork(netId, true);
    }

    /**
     * wifi设置
     * @param ssid
     * @param pws
     * @param isHasPws
     */
    private WifiConfiguration getWifiConfig(String ssid, String pws, boolean isHasPws){
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        if (isHasPws){
            config.preSharedKey = "\""+pws+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    /* 搜索wifi热点
     */
    private void search() {
        if (!wifiManager.isWifiEnabled()) {
            //开启wifi
            wifiManager.setWifiEnabled(true);
        }
        wifiManager.startScan();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                // wifi已成功扫描到可用wifi。
                List<ScanResult> scanResults = wifiManager.getScanResults();
            }
        }};


}
