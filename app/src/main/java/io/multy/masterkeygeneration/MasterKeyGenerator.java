/**
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */
package io.multy.masterkeygeneration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.iid.InstanceID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MasterKeyGenerator {

    private static byte[] getInstanceID(Context context){
        return InstanceID.getInstance(context).getId().getBytes();
    }

    @SuppressLint("HardwareIds")
    private static byte[] getSecureID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes();
    }

    /**
     * Provide PIN or password if user set it. Method will be used after alpha release
     * for better secure protection, but it returns empty value for now.
     *
     * @return zero if no password or PIN, otherwise bytes of password or PIN.
     */
    private static byte[] getUserSecurePassword(){
            return new byte[0];
    }

    public static byte[] generateKey(Context context){
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(getInstanceID(context));
            outputStream.write(getUserSecurePassword());
            outputStream.write(getSecureID(context));
            return calculateHMAC(context, outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static byte[] calculateHMAC(Context context, byte[] key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, context.getString(R.string.hmac_sha_512));
        Mac mac = Mac.getInstance(context.getString(R.string.hmac_sha_512));
        mac.init(secretKeySpec);
        return mac.doFinal();
    }

}