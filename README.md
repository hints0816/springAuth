
## 介绍

`Auth2Connect` 一款以第三方应用接入的第三方平台

- 支持`JWT`授权码登录模式。

- 自定义多个组件实现验证码登录多模式登录。

- 包含`JAVA`客户端开发样例。

  ![Snipaste_2022-03-09_17-43-06](img\Snipaste_2022-03-09_17-43-06.png)

  ![Snipaste_2022-03-09_17-43-16](img\Snipaste_2022-03-09_17-43-16.png)

  ![Snipaste_2022-03-09_17-43-32](img\Snipaste_2022-03-09_17-43-32.png)

  ![Snipaste_2022-03-09_17-44-14](img\Snipaste_2022-03-09_17-44-14.png)

  ![Snipaste_2022-03-09_17-44-57](img\Snipaste_2022-03-09_17-44-57.png)

  ![Snipaste_2022-03-09_17-45-27](img\Snipaste_2022-03-09_17-45-27.png)

  
> 在使用或开发过程中有任何疑问都可[联系我](#联系作者)。

## 系统架构

- `Spring Oauth2`整合spring。
- 安全框架：`Spring Security`。
- 前端框架：`thymeleaf`。
- 缓存：`Redis`。
- 持久化存储：`Mysql8`。
- 持久层框架：`NutDao`。

### 认证模块

- 自定义`Spring Security`内登录接口和登录参数。
- 自定义`AuthenticationToken`。
- 自定义授权码长度。
- 自定义`OAuth2RequestFactory`，重写无Scope的默认规则。
- 自定义`TokenGranter`，新增授权方式，可与新增的AuthenticationToken绑定。
- 自定义`TokenEnhancer`，自定义JWT信息体。
- 自定义`AuthenticationFailureHandler`。
- 自定义`AuthenticationSuccessHandler`。
- 自定义`customAuthenticationProvider`。
> provider->providerManager->filter
> provider->CustomizeUsernamePasswordAuthenticationToken
- 第三方信息持久化和缓存

### 资源模块

- 自定义`AccessDecisionManager`。
- 自定义`FilterInvocationSecurityMetadataSource`。
- 自定义拒绝策略。


## 联系作者
- [1152536969@qq.com](mailto:1152536969@qq.com)
