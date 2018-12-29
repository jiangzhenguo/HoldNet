package com.jhon.code.vpnlibrary.compressor;


import androidx.annotation.Nullable;

/**
 * creater : Jhon
 * time : 2018/12/21 0021
 */
public interface Compressor {

    @Nullable
    byte[] compress(byte[] source) throws Exception;

    @Nullable
    byte[] uncompress(byte[] cipher) throws Exception;
}
