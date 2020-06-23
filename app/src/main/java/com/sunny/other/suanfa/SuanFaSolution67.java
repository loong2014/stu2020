package com.sunny.other.suanfa;

/**
 * Created by zhangxin17 on 2020-04-24
 * 67. 二进制求和
 */
public class SuanFaSolution67 {


    public static void main(String[] args) {
        String out = addBinary("100", "110010");
        System.out.println("out :" + out);
    }


    public static String addBinary(String longStr, String shortStr) {
        if (shortStr.length() > longStr.length()) {
            return addBinary(shortStr, longStr);
        }

        int dif = longStr.length() - shortStr.length();
        char[] outChars = new char[longStr.length()];

        boolean hasBitUp = false;
        //
        for (int i = shortStr.length() - 1; i >= 0; i--) {
            char ca = longStr.charAt(i + dif);
            char cb = shortStr.charAt(i);
            if (ca == '1' && cb == '1') {
                if (hasBitUp) {
                    outChars[i + dif] = '1';
                } else {
                    outChars[i + dif] = '0';
                }
                hasBitUp = true;

            } else if (ca == '1' || cb == '1') {
                if (hasBitUp) {
                    outChars[i + dif] = '0';
                    hasBitUp = true;
                } else {
                    outChars[i + dif] = '1';
                    hasBitUp = false;
                }
            } else {
                if (hasBitUp) {
                    outChars[i + dif] = '1';
                } else {
                    outChars[i + dif] = '0';
                }
                hasBitUp = false;
            }
        }

        //
        for (int i = dif - 1; i >= 0; i--) {
            if (longStr.charAt(i) == '1') {
                if (hasBitUp) {
                    outChars[i] = '0';
                    hasBitUp = true;
                } else {
                    outChars[i] = '1';
                }

            } else {
                if (hasBitUp) {
                    outChars[i] = '1';
                    hasBitUp = false;
                } else {
                    outChars[i] = '0';
                }
            }
        }

        //
        if (hasBitUp) {
            return "1" + new String(outChars);
        } else {
            return new String(outChars);
        }

    }


    public static String addBinary2(String a, String b) {
        String longStr;
        String shortStr;
        int dif = a.length() - b.length();
        if (dif < 0) {
            dif = -dif;
            longStr = b;
            shortStr = a;
        } else {
            longStr = a;
            shortStr = b;
        }

        char[] outChars = new char[longStr.length()];

        boolean hasBitUp = false;
        //
        for (int i = shortStr.length() - 1; i >= 0; i--) {
            char ca = longStr.charAt(i + dif);
            char cb = shortStr.charAt(i);
            if (ca == '1' && cb == '1') {
                if (hasBitUp) {
                    outChars[i + dif] = '1';
                } else {
                    outChars[i + dif] = '0';
                }
                hasBitUp = true;

            } else if (ca == '1' || cb == '1') {
                if (hasBitUp) {
                    outChars[i + dif] = '0';
                    hasBitUp = true;
                } else {
                    outChars[i + dif] = '1';
                    hasBitUp = false;
                }
            } else {
                if (hasBitUp) {
                    outChars[i + dif] = '1';
                } else {
                    outChars[i + dif] = '0';
                }
                hasBitUp = false;
            }
        }

        //
        for (int i = dif - 1; i >= 0; i--) {
            if (longStr.charAt(i) == '1') {
                if (hasBitUp) {
                    outChars[i] = '0';
                    hasBitUp = true;
                } else {
                    outChars[i] = '1';
                }

            } else {
                if (hasBitUp) {
                    outChars[i] = '1';
                    hasBitUp = false;
                } else {
                    outChars[i] = '0';
                }
            }
        }

        //
        if (hasBitUp) {
            return "1" + new String(outChars);
        } else {
            return new String(outChars);
        }

    }

}
