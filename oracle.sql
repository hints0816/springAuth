-- Create table
create table SYS_CLIENT
(
  CLIENT_ID                      VARCHAR2(50) not null,
  CLIENT_SECRET                  VARCHAR2(200),
  AUTHORIZED_GRANT_TYPES         VARCHAR2(50) not null,
  REDIRECT_URI                   VARCHAR2(100),
  ACCESS_TOKEN_VALIDITY_SECONDS  NUMBER(20) not null,
  REFRESH_TOKEN_VALIDITY_SECONDS NUMBER(20) not null,
  SCOPES                         VARCHAR2(50),
  RESOURCE_ID                    VARCHAR2(10),
  CLIENT_NAME                    VARCHAR2(10),
  APPLICATION_TYPE               VARCHAR2(10),
  INTRODUCTION                   VARCHAR2(10),
  AUTO_APPROVE_SCOPES            VARCHAR2(50),
  CREATE_TIME                    DATE,
  UPDATE_TIME                    DATE
)
tablespace CMDB
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns
comment on column SYS_CLIENT.CLIENT_ID
  is '客户端ID';
comment on column SYS_CLIENT.CLIENT_SECRET
  is '客户端密钥';
comment on column SYS_CLIENT.AUTHORIZED_GRANT_TYPES
  is '授权模式';
comment on column SYS_CLIENT.REDIRECT_URI
  is '回调地址';
comment on column SYS_CLIENT.ACCESS_TOKEN_VALIDITY_SECONDS
  is 'TOKEN失效时长';
comment on column SYS_CLIENT.REFRESH_TOKEN_VALIDITY_SECONDS
  is 'REFRESHTOKEN失效时长';
comment on column SYS_CLIENT.SCOPES
  is '作用域';
comment on column SYS_CLIENT.RESOURCE_ID
  is '资源ID';
comment on column SYS_CLIENT.CLIENT_NAME
  is '客户端名称';
comment on column SYS_CLIENT.APPLICATION_TYPE
  is '应用类型';
comment on column SYS_CLIENT.INTRODUCTION
  is '简介';
comment on column SYS_CLIENT.AUTO_APPROVE_SCOPES
  is '是否显示授权页面';

-- Create table
create table SYS_USER
(
  USER_ID      NUMBER not null,
  USER_NAME    VARCHAR2(200),
  NICK_NAME    VARCHAR2(200) not null,
  EMAIL        VARCHAR2(100),
  PHONE_NUMBER NUMBER(20) not null,
  SEX          NUMBER(20) not null,
  AVATER       VARCHAR2(200),
  PASSWORD     VARCHAR2(200),
  SALT         VARCHAR2(200),
  STATUS       VARCHAR2(200),
  DEL_FLAG     VARCHAR2(200),
  LOGIN_IP     VARCHAR2(200),
  LOGIN_DATE   DATE
)
tablespace CMDB
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns
comment on column SYS_USER.USER_ID
  is '用户ID';
comment on column SYS_USER.USER_NAME
  is '用户名称';
comment on column SYS_USER.NICK_NAME
  is '用户昵称';
comment on column SYS_USER.EMAIL
  is '用户邮箱';
comment on column SYS_USER.PHONE_NUMBER
  is '手机号码';
comment on column SYS_USER.SEX
  is '用户性别';
comment on column SYS_USER.AVATER
  is '用户头像';
comment on column SYS_USER.PASSWORD
  is '密码';
comment on column SYS_USER.SALT
  is '盐加密';
comment on column SYS_USER.STATUS
  is '账号状态';
comment on column SYS_USER.DEL_FLAG
  is '软删除标记';
comment on column SYS_USER.LOGIN_IP
  is '登录IP';
comment on column SYS_USER.LOGIN_DATE
  is '登录时间';
-- Create table
create table SYS_ROLE
(
  ROLE_ID             NUMBER not null,
  ROLE_NAME           VARCHAR2(200),
  ROLE_KEY            VARCHAR2(50) not null,
  ROLE_SORT           VARCHAR2(100),
  DATA_SCOPE          VARCHAR2(20) not null,
  MENU_CHECK_STRICTLY NUMBER(20) not null,
  STATUS              VARCHAR2(50),
  DEL_FLAG            VARCHAR2(10)
)
tablespace CMDB
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns
comment on column SYS_ROLE.ROLE_ID
  is '角色ID';
comment on column SYS_ROLE.ROLE_NAME
  is '角色名称';
comment on column SYS_ROLE.ROLE_KEY
  is '角色标识';
comment on column SYS_ROLE.ROLE_SORT
  is '角色排序';
comment on column SYS_ROLE.DATA_SCOPE
  is '数据范围';
comment on column SYS_ROLE.MENU_CHECK_STRICTLY
  is '菜单是否关联显示';
comment on column SYS_ROLE.STATUS
  is '角色状态';
comment on column SYS_ROLE.DEL_FLAG
  is '软删除';
-- Create/Recreate primary, unique and foreign key constraints
alter table SYS_ROLE
  add constraint ROLE123 primary key (ROLE_ID)
  using index
  tablespace USERS
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create table
create table SYS_USER_ROLE
(
  ROLE_ID VARCHAR2(200),
  USER_ID VARCHAR2(200)
)
tablespace CMDB
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
