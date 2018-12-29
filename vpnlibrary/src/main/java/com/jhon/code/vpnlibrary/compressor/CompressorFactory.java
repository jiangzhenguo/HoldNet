package com.jhon.code.vpnlibrary.compressor;


import androidx.annotation.Nullable;

/**
 * creater : Jhon
 * time : 2018/12/21 0021
 */
public class CompressorFactory {

    public static final String METHOD_GZIP = "gzip";

    @Nullable
    public static Compressor getCompressor(String method) {
        Compressor compressor = null;
        if (method != null) {
            if (method.trim().equals(METHOD_GZIP)) {
                compressor = new GZipCompressor();
            }
        }

        return compressor;
    }
}
