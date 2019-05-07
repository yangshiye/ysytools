package com.ysy.basetools.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/7/28.
 */
public class DistanceUtil {
    private static Logger LOG = LoggerFactory.getLogger(DistanceUtil.class);

    //地球平均半径
    private static final double EARTH_RADIUS = 6378137;

    //把经纬度转为弧度（°）
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算距离
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static Double calcDistance(Number lng1, Number lat1, Number lng2, Number lat2) {
        if (CommonUtil.anyNull(lng1, lat1, lng2, lat2)) {
            return null;
        }
        return doCalcDistance(lng1.doubleValue(), lat1.doubleValue(), lng2.doubleValue(), lat2.doubleValue());
    }

    /**
     * 计算距离
     *
     * @param lng1
     * @param lat1
     * @param lng2
     * @param lat2
     * @return
     */
    public static Double calcDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
        if (CommonUtil.anyNull(lng1, lat1, lng2, lat2)) {
            return null;
        }
        return doCalcDistance(lng1, lat1, lng2, lat2);
    }

    /**
     * 根据两点间经纬度坐标，计算两点间距离
     *
     * @param lng1 起点经度
     * @param lat1 起点纬度
     * @param lng2 终点经度
     * @param lat2 终点纬度
     * @return 两点间距离，单位为米
     */
    private static double doCalcDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a / 2), 2)
                                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
                )
        );
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 高德地图WebAPI : 地址转化为高德坐标 <br/>
     * String address：高德地图地址
     *
     */
    public static String coordinate(String address) {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("地址转换异常",e);
        }
        String url = "http://restapi.amap.com/v3/geocode/geo?address=" + address
                + "&output=json&key=7486e10d3ca83a934438176cf941df0c";
        String result = getHttpResponse(url);
        JSONObject json1 = JSON.parseObject(result);
        if("1".equals(json1.get("status"))){
            for(Object o:json1.getJSONArray("geocodes")){
                JSONObject jb = (JSONObject)o;
                LOG.info("formatted_address:"+jb.get("formatted_address")+",location:"+jb.get("location"));
                if(!StrUtil.isBlank(jb.get("location").toString())){
                    return jb.get("location").toString();
                }
            }
        }
        return null;
    }

    public static String distance(String origins, String destination) {
        if (origins != null && destination != null) {
            int strategy = 0;
            String url = "http://restapi.amap.com/v3/direction/driving?origin=" + origins + "&destination=" + destination + "&strategy=" + strategy + "&extensions=base&key=7486e10d3ca83a934438176cf941df0c";
            JSONObject jsonobject = JSONObject.parseObject(getHttpResponse(url));
            String distanceString = null;
            if ("1".equals(jsonobject.getString("status"))){
                JSONArray pathArray = jsonobject.getJSONObject("route").getJSONArray("paths");
                distanceString = pathArray.getJSONObject(0).getString("distance");
            } else if ("0".equals(jsonobject.getString("status"))){
                LOG.error("地址错误>>>>>{}", JSONObject.toJSONString(jsonobject));
            }
            return distanceString;
        } else {
            return null;
        }
    }

    public static String getHttpResponse(String allConfigUrl) {
        BufferedReader in = null;
        StringBuffer result = null;
        try {
            // url请求中如果有中文，要在接收方用相应字符转码
            URI uri = new URI(allConfigUrl);
            URL url = uri.toURL();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("Content-type", "text/html");
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.setRequestProperty("contentType", "utf-8");
            connection.connect();
            result = new StringBuffer();
            // 读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            LOG.error("执行http请求"+allConfigUrl+"异常！", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }
}
