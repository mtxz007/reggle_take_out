package com.mtxz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mtxz.common.R;
import com.mtxz.entity.User;
import com.mtxz.service.UserService;
import com.mtxz.utils.SMSUtils;
import com.mtxz.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * author: ylb
 * Date: 2022/4/17
 * Time: 22:29
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 接收手机号发送验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        //获取手机号
        String phone = user.getPhone();
        //验证手机号
        if (StringUtils.isNotEmpty(phone) && SMSUtils.isPhone(phone)) {
            //生成随机4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code:{}", code);
            //调用阿里云API发送短信  调试慎用,0.045人民币一条!!!
//            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            //将生成的验证码保存到Session
            session.setAttribute(phone, code);
            return R.success("手机验证码发送成功");
        }

        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info("map:{}", map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取用户输入验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        Object codeSession = session.getAttribute(phone);
        //进行验证码的比对
        if (codeSession != null && codeSession.equals(code)) {
            //判断是否为新用户
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            //新用户则自动注册
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //保存登录状态
            session.setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("验证码错误，登录失败");
    }

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");

        return R.success("您已退出登录");
    }
}
