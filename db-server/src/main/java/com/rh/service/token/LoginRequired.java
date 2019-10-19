package com.rh.service.token;

import java.lang.annotation.*;

 /* @Description: 在需要登录验证的Controller的方法上使用此注解 */
@Target({ElementType.METHOD})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface LoginRequired {
}