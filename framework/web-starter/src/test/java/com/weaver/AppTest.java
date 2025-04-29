package com.weaver;

import cn.hutool.crypto.SecureUtil;
import org.junit.Test;

import java.security.KeyPair;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testSecurity(){

        KeyPair keyPair = SecureUtil.generateKeyPair("HSA512");
        keyPair.getPrivate().getFormat();
        keyPair.getPublic().getFormat();
    }

}
