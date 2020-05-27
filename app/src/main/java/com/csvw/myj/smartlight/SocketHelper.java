package com.csvw.myj.smartlight;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @ClassName: SocketHelper
 * @Description:
 * @Author: MYJ
 * @CreateDate: 2020/5/26 11:21
 */
public class SocketHelper {



    public void commCheck(final PrintWriter pw) throws IOException {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                pw.println("comm check");
                pw.flush();
            }
        },0,1000);
    }

    /**
     * 接收的byte[]数据打印成字符串
     * @param buffer
     * @param length
     * @return
     */
    public String byte2hex(byte[] buffer,int length){
        String h = "";
        for (int i =0; i<length;i++){
            String temp = Integer.toHexString(buffer[i]&0xFF);
            if (temp.length()==1){
                temp = "0"+temp;
            }
//            if (i%8==0){
//                h=h+"|";
//            }
            h= h + " " + temp;
        }
        return  h;
    }

//    public String arrPrint(String s){
//        String[] strArr =  s.split(" ");
////        int[][] arr = new int[][];
//        for(int i=0;i<20;i++){
//            for(int j=0;j<8;j++){
//                for (int len=0;len<strArr.length;len++){
//                    arr[i][j]= Integer.parseInt(strArr[len]);
//                }
//
//            }
//        }
//        String arrPrint =  arr.toString();
//        return arrPrint;
//    }

    /**
     * 将接收的数据放入二维数组中
     * @param b 存放属性的二维数组
     * @param bytes 接收到的数据
     * @return
     */

    public static void attrsArray(byte[][] b,byte[] bytes){
            for (int x = 0;x<20;x++){
                for(int y= 0;y<8;y++){
                    b[x][y] = bytes[x*8+y];
                }
            }
    }

    public  static  byte[] attrsArray2D21D(byte[][] b){
        byte[] bytes = new byte[160];
        for (int x = 0;x<20;x++){
            for(int y= 0;y<8;y++){
                bytes[x*8+y]=b[x][y];
            }
        }
        return bytes;
    }
    /**
     * 1byte转int
     * @param b
     * @return
     */
    public static int byte2int(byte b){
        int x =b & 0xff;
        return x;
    }
//    /**
//     *
//     * @param bytes
//     * @param size
//     * @return
//     */
//    public byte[] splitByes(byte[] bytes,int size){
//        double splitLengh = Double.parseDouble(size+"");
//        int arrayLength = (int) Math.ceil(bytes.length/splitLengh);
//        byte[] result = new byte[arrayLength];
//        int from, to;
//        for (int i = 0;1<arrayLength;i++){
//            from = (int)(i*splitLengh);
//            to = (int) (from+splitLengh);
//            if(to>bytes.length){to = bytes.length;}
//            result[i] = Arrays.copyOfRange(bytes,from,to);
//        }
//
//        return result;
//    }
}
