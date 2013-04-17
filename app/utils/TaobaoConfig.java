package utils;


import utils.HMacMD5;

public class TaobaoConfig {
    public static String appkey = "21136607";
    public static String secret = "b43392b7a08581a8916d2f9fa67003db";
 //   public static String timestamp = String.valueOf(System.currentTimeMillis());
    /**
     * $secret.'app_key'.$app_key.'timestamp'.$timestamp.$secret;
     * @return
     */
    public static String getSign(String timestamp){
        String result = null;
        StringBuilder signStr = new StringBuilder();
        signStr.append(secret)
                .append("app_key").append(appkey)
                .append("timestamp").append(timestamp)
                .append(secret);
        try {
            result = byte2hex(HMacMD5.getHmacMd5Bytes(signStr.toString().getBytes("utf-8"), secret.getBytes("utf-8")));
        } catch (Exception ex) {
            throw new java.lang.RuntimeException("sign error !");
        }
        return result;
    }
   /* public static String getOriginalSign(String timestamp){
        String result = null;
        StringBuilder signStr = new StringBuilder();
        signStr.append(secret)
                .append("app_key").append(appkey)
                .append("timestamp").append(timestamp)
                .append(secret);
        try {
            result = signStr.toString();
        } catch (Exception ex) {
            throw new java.lang.RuntimeException("sign error !");
        }
        return result;
    }*/
    private static String byte2hex(byte[] data){
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < data.length; n++) {
            stmp = (java.lang.Integer.toHexString(data[n] & 0XFF));
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }
}
