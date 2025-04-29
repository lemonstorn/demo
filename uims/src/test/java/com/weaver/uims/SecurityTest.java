package com.weaver.uims;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.weaver.core.utils.StringUtils;
import org.junit.Test;

import java.util.Base64;

/**
 * @Author: zh
 * @CreateTime: 2025-04-20
 * @Description:
 * @Version: 1.0
 */

public class SecurityTest {
    @Test
    public void testSm4(){
        byte[] base64Decode = Base64.getDecoder().decode("MTEyOTdlYmUxZGI1ZTBkMGQzOWM0ZTNkNzQ3YWU2YWY=");
        SM4 sm4 = SmUtil.sm4(StringUtils.hexToBytes(StringUtils.hex("weaver2025_onDev")));
        System.out.println(sm4.decryptStr(new String(base64Decode)));
    }
}
