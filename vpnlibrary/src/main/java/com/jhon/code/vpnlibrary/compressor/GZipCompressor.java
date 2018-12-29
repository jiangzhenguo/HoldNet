package com.jhon.code.vpnlibrary.compressor;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import androidx.annotation.Nullable;

/**
 * creater : Jhon
 * time : 2018/12/21 0021
 */
public class GZipCompressor implements Compressor {
    @Nullable
    @Override
    public byte[] compress(byte[] source) throws Exception {
        if(source == null && source.length == 0 ){
            return source;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(source);
        gzip.close();
        return out.toByteArray();
    }

    @Nullable
    @Override
    public byte[] uncompress(byte[] cipher) throws Exception {
        if (cipher == null || cipher.length == 0) {
            return cipher;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(cipher);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[1024];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        gunzip.close();
        return out.toByteArray();
    }
}
