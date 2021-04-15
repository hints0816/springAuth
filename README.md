
## 介绍

`Auth2Connect` 一款以第三方应用接入的第三方平台

- 支持`JWT`授权码登录模式。
- 自定义多个组件实现验证码登录多模式登录。
- 包含`JAVA`客户端开发样例。

> 在使用或开发过程中有任何疑问都可[联系我](#联系作者)。

## 系统架构

- 注册中心：`Spring Eureka`。
- `Spring Oauth2`整合spring。
- 安全框架：`Spring Security`。
- 前端框架：`thymeleaf`。
- 缓存：`Redis`。
- 持久化存储：`Mysql8`。
- 持久层框架：`NutDao`。

### 认证模块

- 自定义Spring Security内登录接口和登录参数。
- 自定义AuthenticationToken。
- 自定义授权码长度。
- 自定义OAuth2RequestFactory，重写无Scope的默认规则。
- 自定义TokenGranter，新增授权方式，可与新增的AuthenticationToken绑定。
- 自定义TokenEnhancer，自定义JWT信息体。
- 自定义AuthenticationFailureHandler。
- 自定义AuthenticationSuccessHandler。
- 自定义customAuthenticationProvider。
> provider->providerManager->filter
> provider->CustomizeUsernamePasswordAuthenticationToken
- 第三方信息持久化和缓存

### 资源模块

- 自定义AccessDecisionManager。
- 自定义FilterInvocationSecurityMetadataSource。
- 自定义拒绝策略。

## 快速启动

```shell

```

### 部署

```shell

```

### 启动客户端

```shell

```

## 联系作者
- [1152536969@qq.com](mailto:1152536969@qq.com)
