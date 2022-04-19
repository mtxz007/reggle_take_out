package com.mtxz.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信发送工具类
 */
public class SMSUtils {

    /**
     * 发送短信
     *
     * @param signName     签名
     * @param templateCode 模板
     * @param phoneNumbers 手机号
     * @param param        参数
     */
    public static void sendMessage(String signName, String templateCode, String phoneNumbers, String param) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tQnCWgPKAmJTbM15Xac", "oVCN2er0UiEAQPWZCsQVT43Pv3zR2Y");
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId("cn-hangzhou");
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\"" + param + "\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println("短信发送成功");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证手机号格式
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        String regex = "^(13[0-9]{9})|(15[0-9]{9})|(17[0-9]{9})|(18[0-9]{9})|(19[0-9]{9})$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
