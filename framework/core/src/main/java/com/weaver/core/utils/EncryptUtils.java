package com.weaver.core.utils;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;

import java.util.Base64;

/**
 * @Author: zh
 * @CreateTime: 2025-04-20
 * @Description: 加解密工具
 * @Version: 1.0
 */

public class EncryptUtils {
    public static String decryptSm4(String str,String key) {
        byte[] base64Decode = Base64.getDecoder().decode(str);
        SM4 sm4 = SmUtil.sm4(StringUtils.hexToBytes(StringUtils.hex(key)));
        return sm4.decryptStr(new String(base64Decode));
    }
}
