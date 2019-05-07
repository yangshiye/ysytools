package com.ysy.basetools.util;

import com.ysy.basetools.log.LogUtil;
import org.slf4j.Logger;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/1/30.
 */
public final class IPUtil {

    public static final String INNER_IP_PATTERN = "((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
            + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
            + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";

    private static final Logger LOGGER = LogUtil.LOG;

    public static final String LOCAL_IP = initIP();

    public static final String LOCAL_HOST = initLocalHostName();

    /**
     * 获取本机ip地址
     */
    public static String getLocalIp() {
        return LOCAL_IP;
    }

    /**
     * 获取本地机器名
     */
    public static String getLocalHostName() {
        return LOCAL_HOST;
    }

    public static String initLocalHostName() {
        String hostName = System.getenv("COMPUTERNAME");
        if (hostName == null) {
            try {
                hostName = (InetAddress.getLocalHost()).getHostName();
            } catch (UnknownHostException e) {
                LOGGER.error("get hostname error", e);
                String host = e.getMessage();
                if (host != null) {
                    int colon = host.indexOf(':');
                    if (colon > 0) {
                        hostName = host.substring(0, colon);
                    }
                }
            }
        }
        LOGGER.info("get local hostName ：" + hostName);
        return hostName;
    }

    /**
     * 判断是否是内网IP
     *
     * @param ip
     * @return
     */
    public static boolean isInnerIP(String ip) {
        if (ip == null) {
            return false;
        } else if ("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)) {
            return true;
        }
        Pattern reg = Pattern.compile(INNER_IP_PATTERN);
        Matcher match = reg.matcher(ip);

        return match.find();
    }

    public static String initIP() {
        try {
            //根据网卡取本机配置的IP
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            String ip = null;
            a:
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    InetAddress ipObj = ips.nextElement();
                    if (ipObj.isSiteLocalAddress()) {
                        ip = ipObj.getHostAddress();
                        break a;
                    }
                }
            }
            return ip;
        } catch (Exception e) {
            LOGGER.error("initIP IS ERROR!", e);
            return null;
        }
    }
}
