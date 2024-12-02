package com.razorpaypoc.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class RazorpaySignatureUtil {

    public boolean verifyRazorpaySignature(String orderId, String paymentId, String razorpaySignature) {
        String generatedSignature = "";
        try {
            String message = orderId + "|" + paymentId;
            String secret = "YOUR_RAZORPAY_SECRET"; // Fetch from properties or environment variable
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            generatedSignature = Hex.encodeHexString(sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return generatedSignature.equals(razorpaySignature);
    }

	private static String hmacSHA256(String data, String secret) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		return new String(Hex.encodeHex(sha256_HMAC.doFinal(data.getBytes())));
	}
}
