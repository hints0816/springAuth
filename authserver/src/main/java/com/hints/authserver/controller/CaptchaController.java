package com.hints.authserver.controller;

import com.google.code.kaptcha.Producer;
import com.hints.authserver.constant.SecurityConstants;
import com.hints.authserver.utils.Base64;
import com.hints.authserver.utils.IdUtils;
import com.hints.authserver.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码操作处理
 * 
 */
@RestController
public class CaptchaController
{
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    @Autowired
    private RedisCache redisCache;
    
    /**
     * 生成验证码
     */
    @GetMapping("/captchaImage")
    public HashMap<String, Object> getCode(HttpServletResponse response) throws IOException
    {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = SecurityConstants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null, code = null;
        BufferedImage image = null;

        // 生成验证码
        String capText = captchaProducerMath.createText();
        capStr = capText.substring(0, capText.lastIndexOf("@"));
        code = capText.substring(capText.lastIndexOf("@") + 1);
        image = captchaProducerMath.createImage(capStr);


        redisCache.setCacheObject(verifyKey, code, SecurityConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try
        {
            ImageIO.write(image, "jpg", os);
        }
        catch (IOException e)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("code", code);
            map.put("msg", e.getMessage());
            return map;
        }

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("uuid", uuid);
        map.put("img", Base64.encode(os.toByteArray()));
        return map;
    }
}
