package com.weaver.web.config;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.weaver.core.constants.Sym;
import com.weaver.core.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@Component
@ConfigurationProperties(prefix = "system.login")
public class WeaverSecurityProperties {
    // 是否需要认证登陆
    private Boolean need = false;
    // 是否开启单点登陆
    private Boolean single = false;
    // 会话有效时间 单位秒
    private Integer time = 3600*8;
    // 登陆uri
    private String loginUri = "/login";
    // 使用会话方式
    private String use = "session";
    // 404页面uri
    private String notFindUri = "/404";
    // 静态地址
    private String staticPaths;
    // 外部静态资源（公网项目勿用）
    private String staticOutPaths;
    // JWT 请求头
    private String tokenHeader = "Authorization";
    // JWT 默认前缀
    private String tokenPrefix = "Brear";
    //
    private String jwtSecret = "Weaver_login_salt";
    // JWT Rsa公钥存储地址
    private String jwtRsaPath = "/app/";
    // JWT 加密方式  建议使用rsa加密保证安全
    private String jwtSignWith = "HS512";
    // 公钥
    private PublicKey publicKey;
    // 私钥
    private PrivateKey privateKey;

    public static final String RSA_PUBLIC_FILENAME = "/rsa.pub";
    public static final String RSA_PRIVATE_FILENAME = "/rsa.pri";

    @PostConstruct
    public void init() {
        try {
            if (!Sym.RSA.equals(jwtSignWith)) {
                return;
            }
//			RSA加密自动生成相关加密信息
            String publicKeyPath = jwtRsaPath + RSA_PUBLIC_FILENAME;
            String privateKeyPath = jwtRsaPath + RSA_PRIVATE_FILENAME;
            File pubKey = new File(publicKeyPath);
            File priKey = new File(privateKeyPath);
            if (!pubKey.exists() || !priKey.exists()) {
                // 生成公钥和私钥
                KeyPair pair = SecureUtil.generateKeyPair("RSA", 1024, jwtSecret.getBytes(StandardCharsets.UTF_8));
                String privateKey = pair.getPrivate().getFormat();
                String publicKey = pair.getPublic().getFormat();
                FileUtils.writeFile(publicKeyPath, publicKey);
                FileUtils.writeFile(privateKeyPath, privateKey);
            }
            RSA rsa = new RSA(FileUtils.readFile(pubKey), FileUtils.readFile(priKey));
            // 获取公钥和私钥
            this.publicKey = rsa.getPublicKey();
            this.privateKey = rsa.getPrivateKey();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
