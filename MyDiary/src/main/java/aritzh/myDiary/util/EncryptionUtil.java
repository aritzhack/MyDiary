package aritzh.myDiary.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Created by aritzh on 23/02/14.
 */
public class EncryptionUtil {

    public static String encrypt(String plainText, String key) {
        try {
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(new byte[]{0}, 1);
            Cipher cipher = Cipher.getInstance("AES");
            PBEKeySpec spec = new PBEKeySpec(key.toCharArray());
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey passwordKey = kf.generateSecret(spec);
            cipher.init(Cipher.ENCRYPT_MODE, passwordKey, pbeParamSpec);

            byte[] utf8 = plainText.getBytes("UTF-8");
            byte[] enc = cipher.doFinal(utf8);

            return new String(Base64.encode(enc, Base64.DEFAULT));
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String plainText, String key) {
        try {
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(new byte[]{0}, 1);
            Cipher cipher = Cipher.getInstance("AES");
            PBEKeySpec spec = new PBEKeySpec(key.toCharArray());
            SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey passwordKey = kf.generateSecret(spec);
            cipher.init(Cipher.DECRYPT_MODE, passwordKey, pbeParamSpec);

            byte[] dec = Base64.decode(plainText, Base64.DEFAULT);
            byte[] utf8 = cipher.doFinal(dec);

            return new String(utf8, "UTF-8");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodePassword(String password) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aResult : result) {
                sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
