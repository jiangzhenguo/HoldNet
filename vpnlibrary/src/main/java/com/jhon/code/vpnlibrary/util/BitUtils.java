package com.jhon.code.vpnlibrary.util;

/**
 * creater : Jhon
 * time : 2018/12/12 0012
 */
public class BitUtils {

    public static short getUnsignedByte(byte value)
    {
        return (short)(value & 0xFF);
    }

    public static int getUnsignedShort(short value)
    {
        return value & 0xFFFF;
    }

    public static long getUnsignedInt(int value)
    {
        return value & 0xFFFFFFFFL;
    }

    public static int bytesToInt(byte[] bytes)
    {
        int addr = bytes[3] & 0xFF;
        addr |= ((bytes[2] << 8) & 0xFF00);
        addr |= ((bytes[1] << 16) & 0xFF0000);
        addr |= ((bytes[0] << 24) & 0xFF000000);
        return addr;
    }

    public static int ipStringToInt(String ip) {
        String[] arrayStrings = ip.split("\\.");
        int r = (Integer.parseInt(arrayStrings[0]) << 24)
                | (Integer.parseInt(arrayStrings[1]) << 16)
                | (Integer.parseInt(arrayStrings[2]) << 8)
                | (Integer.parseInt(arrayStrings[3]));
        return r;
    }
}
