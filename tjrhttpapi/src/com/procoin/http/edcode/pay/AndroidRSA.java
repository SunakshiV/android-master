//package com.cropyme.http.edcode.pay;
//
//import java.security.KeyFactory;
//import java.security.PublicKey;
//import java.security.spec.X509EncodedKeySpec;
//
//import javax.crypto.Cipher;
//
//public class AndroidRSA {
//    private static final String TJR_RAS_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGDMIqUc+DtRagoASOoL+Q/JC1R+3XQWlpFuvk6i4CmejBbJGPiYY8AU2frS25smVfpZdUH4nuNvOqThEJQWWfEaAPxdOZdvPonseJx11+yJoqB+yaLperUI0gavZvk4jkfZYTqSVEhU60a4zcqMLQtghrAco39tnMotgVP+BNwwIDAQAB";
//
//    private static final String ANOTHER_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4d4PkqATbHEi0nor5tFX6XrJSkQsHqXfJVxBIG5PPVyySKWX1YszmbpR6AVkgYczpF3swGOV0Nu1yiz9QoEq0VhexH95Z4GTVHEAMrJ/IcWeAknwY29wofd8GSwVUcTlI07EY0ENKXVCCu35awJBVhgoK0VJ+Tno6MgVCCz5ETQIDAQAB";
//
//    private static final String TJR_RSA_PUBLIC_TRADE_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDhAkZVx6CMI/CxjzBLiP5L0FsGIjdLpFQUOPcDTUQMQ606wrUUhGAlavfR9FuTp1bGk3WpjU5NMua/ZAhv/YWu+hmEtx0f4h3QXSG7OBwt5TxIEsiK5ziMq9XGq1JUv+YU9vwWspTSwJVdKviJTyqEfFgNoNVygsrPK0lPF0bX8wIDAQAB";
//
//    private static final String TJR_RSA_PUBLIC_WEIPAN_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDe0S5epmnVv6rumb529F34MNSQLJWhEoBbLtUhAHkH9yJz0CPSIEPrMpGky4EH9zAVx5pKjJd9wi99F9h1KuwvqPm892SbMEVyOuEQUNcZQNXYEMSMQLnDO18xX5Ah3xf6uR42nqZ8KbhI3riNzQvkOz4D8gN0r9AdV5QIhX08rwIDAQAB";
//
//    /**
//     * String to hold name of the encryption algorithm.
//     */
//    private static final String ALGORITHM = "RSA";
//
//
//    public static String encrypt(String text) {
//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
////            byte[] encodedKey = Base64.decode(TJR_RAS_PUBLIC_KEY);
//            byte[] encodedKey = Base64.decode(ANOTHER_PUBLIC_KEY);
//            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
//            // get an RSA cipher object and print the provider
//            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            // encrypt the plain text using the public key
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] signed = cipher.doFinal(text.getBytes());
//            return Base64.encode(signed);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String encryptForTrade(String text) {
//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
//            byte[] encodedKey = Base64.decode(TJR_RSA_PUBLIC_TRADE_KEY);
//            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
//            // get an RSA cipher object and print the provider
//            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            // encrypt the plain text using the public key
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] signed = cipher.doFinal(text.getBytes());
//            return Base64.encode(signed);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static String encryptForWeipan(String text) {
//        try {
//            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
//            byte[] encodedKey = Base64.decode(TJR_RSA_PUBLIC_WEIPAN_KEY);
//            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
//            // get an RSA cipher object and print the provider
//            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//            // encrypt the plain text using the public key
//            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//            byte[] signed = cipher.doFinal(text.getBytes());
//            return Base64.encode(signed);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//}
