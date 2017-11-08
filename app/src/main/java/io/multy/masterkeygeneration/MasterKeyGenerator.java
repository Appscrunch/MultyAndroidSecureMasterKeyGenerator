/**
 * Copyright 2017 Idealnaya rabota LLC
 * Licensed under Multy.io license.
 * See LICENSE for details
 */
package io.multy.masterkeygeneration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.provider.Settings;
import android.util.Base64;

import com.google.android.gms.iid.InstanceID;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;

public class MasterKeyGenerator {

    private static byte[] getInstanceID(Context context){
        return InstanceID.getInstance(context).getId().getBytes();
    }

    @SuppressLint("HardwareIds")
    private static byte[] getSecureID(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID).getBytes();
    }

    @SuppressLint("PackageManagerGetSignatures")
    private static byte[] getKeystoreFingerprint(Context context){
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance(context.getString(R.string.sha));
                md.update(signature.toByteArray());
                return md.digest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateKey(Context context){
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(getInstanceID(context));
            outputStream.write(getKeystoreFingerprint(context));
            outputStream.write(getSecureID(context));
            return Base64.encodeToString(outputStream.toByteArray(), 0);
        } catch (IOException e) {
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

}