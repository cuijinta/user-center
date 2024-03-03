# 用户中心

## 项目目标

完整了解做项目的流程和思路分析，接触一些企业级的开发技术， 快速搭建项目的前后端基本框架。

## 项目流程

需求分析 => 设计（概要设计、详细设计）=> 技术选型 =>

初始化 / 引入需要的技术 => 写 Demo => 写代码（实现业务逻辑） => 测试（单元测试）=> 代码提交 / 代码评审 => 部署=> 发布

## 需求分析

1. **登录 / 注册**
2. **用户管理（仅管理员可见）对用户的查询或者修改**
3. 用户校验（ **仅星球用户** ）

## 技术选型

前端：三件套 + React + 组件库 Ant Design + Umi + Ant Design Pro（现成的管理系统）



后端：

- java
- spring（依赖注入框架，帮助你管理 Java 对象，集成一些其他的内容）
- springmvc（web 框架，提供接口访问、restful接口等能力）
- mybatis（Java 操作数据库的框架，持久层框架，对 jdbc 的封装）
- mybatis-plus（对 mybatis 的增强，不用写 sql 也能实现增删改查）
- springboot（**快速启动** / 快速集成项目。不用自己管理 spring 配置，不用自己整合各种框架）
- junit 单元测试库
- mysql

部署：服务器 / 容器（平台）

## 计划

1. 初始化项目

   1. 前端初始化     20 min

      1. 初始化项目 ✔
      2. 引入一些组件之类的 ✔
      3. 框架介绍 / 瘦身 ✔

   2. 后端初始化  20 min

      1. 准备环境（MySQL 之类的）验证 MySQL 是否安装成功 - 连接一下 ✔
      2. 初始化后端项目，引入框架（整合框架）✔

      

2. **登录 / 注册** 

   1. 前端
   2. 后端

3. 用户管理（仅管理员可见）

   1. 前端
   2. 后端





## 笔记



三种初始化 Java 项目的方式

1. GitHub 搜现成的代码
2. SpringBoot 官方的模板生成器（https://start.spring.io/）
3. 直接在 IDEA 开发工具中生成  ✔

如果要引入 java 的包，可以去 maven 中心仓库寻找（http://mvnrepository.com/）

## 数据库设计

用户表：

id(主键)： bigint

userAccount  varchar 账号

userName:昵称 varchar

avatarUrl: 头像 varchar

gender: 性别 tinyint

password: 密码 varchar

phone: 电话 varchar

email：邮箱 varchar

userStatus  用户状态 （比如被封号）tinyint 0 1



createTime 创建时间 （数据创建时间） datetime

updateTime 更新时间  （数据更新时间） datetime

idDelete 是否删除  0 1（逻辑删除） tinyint（在配置文件有说明并在实体字段上加@TableLogic 注解，表示逻辑删除字段）

```
username
userAcco
avatarUr
gender  
userPass
phone   
createTi
updateTi
userStat
isDelete
email   
userRole
```

```mysql
create table user
(
    id           bigint                             not null
        primary key,
    username     varchar(256)                       null comment '用户昵称',
    userAccount  varchar(256)                       null comment '账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '用户性别',
    userPassword varchar(512)                       null comment '用户密码',
    phone        varchar(512)                       null comment '电话',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    userStatus   int      default 0                 null comment ' 用户状态 0 - 正常 ',
    isDelete     tinyint  default 0                 null comment '是否删除',
    email        varchar(512)                       null comment '邮箱',
    userRole     int      default 0                 null
)
    comment '用户表';
```



## 登录注册

### 后端

1. 实现基本的数据库操作（操作user表）

2. 模型user对象 => 和数据库的关系

3. 使用自动生成器（mybatisX插件），自动根据数据库生成domain实体对象、mapper（操作数据库得对象）、mapper.xml(定义了mapper对象和数据库关联，可以在里面写SQL)、service（包含常用得增删改查）、service实现

4. 新增内容：

   新增获取用户的登录态（session中取出），**获取当前登录用户信息接口**

#### 注册逻辑

1. 用户在前端输入账户和密码，以及校验码（todo）

2. 校验用户的账户、密码、校验密码是否符合规则

   1. 账户名**不小于**6位
   2. 密码**不小于**8位
   3. 账户名不能重复
   4. 账户不包含特殊字符
   5. 密码和校验密码必须相同

3. 对密码进行**加密**（密码 **一定** 不能明文存储到数据库中）

   使用spring自带的 `DigestUtils.md5DigestAsHex(userPassword.getBytes());`

4. 向数据库插入用户数据

#### 登录逻辑

1. **请求响应**

   > 接收参数：账号、密码
   >
   > 请求类型：POST（请求参数可能会很长时不建议用GET）       
   >
   > 请求体：JSON 格式的数据
   >
   > 返回值：用户信息（**脱敏**）

2. **逻辑**

   1. 校验密码是否输入正确，跟数据库中的账户名和密码去比对(格式校验与注册相同)
   2. 返回脱敏后的用户信息,防止数据库中的字段泄露给前端
   3. 记录用户的登录态（session），将其保存在服务器上（用后端Springboot框架封装的服务器tomcat去保存）
   4. 返回脱敏后的用户信息

   

   #### 控制层 Controller 封装请求

   application.yml 指定接口全局 api

   > ```yml
   > servlet:
   > context-path: /api
   > ```

   

   > @RestController 适用于编写 restful 风格的 api，返回值默认为 json 类型

   controller 层倾向于对请求体参数的校验，不涉及业务逻辑本身（越少越好）

   service 层是对业务逻辑的校验（有可能被 controller 之外的类调用）

   

   > 如何知道是哪个用户登录了？
   >
   > 1. 连接服务器端后，得到一个session1 状态，返回给前端
   > 2. 登录成功后，得到了登录成功的session，在该session中保存相应的变量，（比如用户信息）返回给前端一个设置cookie的“信号”
   > 3. 前端收到后端的“信号”，设置cookie，保存到浏览器中
   > 4. 前端再次请求的时候（相同的域名），在请求头中携带cookie去请求
   > 5. 后端拿到前端传来的cookie，就能找到对应的session
   > 6. 后端从该session中可以取出基于该session存储的变量（用户的登录信息、登录名）



#### 用户管理

> 写接口之前一定要鉴权！！！至少在业务当中进行身份的判断，鉴权时需要添加一个userRole字段到数据库中（0-普通用户  1-管理员）

1. 查询用户

   1. 根据用户名查询

      先鉴权（只有管理员才能查询），有权限就返回脱敏后的用户列表

   2. 根据id删除用户（mybatisplus的remove方法默认是逻辑删除）

      先鉴权（只有管理员才能删除），无权限直接返回false



### 前端

修改前端样式

删除多余代码

##### Ant Design Pro （Umi 框架的）

- app.tsx ：项目全局入口文件，定义了整个项目中使用的公共数据（比如用户信息）
- access.ts : 控制用户的访问权限

##### 流程

首次访问页面（刷新页面）， 进入app.tsx, 执行 getInitialState 方法，该方法的返回值就是全局可用的状态值。

```tsx
export default function access(initialState: { currentUser?: API.CurrentUser  | undefined}) {
  const { currentUser } = initialState || {};
  return {
    canAdmin: currentUser && currentUser.userRole === 1,
  };
}
```



#### ProCpmmponents 高级表单

1. 通过 columns 定义表格有哪些列

2. columns 的属性

   - dataIndex 对应返回数据对象的属性

   - title 表格列名

   - ellipsis 是否缩略 

     > ellipsis: true

   - valueType 声明这一列的字段类型

#### 前后端交互

前端需要向后端发送请求

1. ajax来请求后端
2. axios封装了ajax
3. request是ant design又封装了一次
4. 追踪request源码，用到了umi插件，requestConfig是一个配置



#### 代理

正向代理：替客户端向服务器发送请求

反向代理：替服务器接收来自客户端的请求

怎么搞代理？

Nginx 服务器

Node.js 服务器



### 注册功能前端开发

复制登录组件代码直接用，修改组件名称为Register

设置注册路由：（config包下的routes.ts文件中添加注册路由）

```js
path: '/user',
        layout: false,
        routes: [
            {
                path: '/user', routes: [
                    {name: '登录', path: '/user/login', component: './user/Login'},
                    {name: '注册', path: '/user/register', component: './user/Register'}
                ]
            },
            {component: './404'},
        ],
```

但是，发现地址栏路径为/user/register时，页面会自动跳转到登录页面，检查发现在app.tsx中有拦截的逻辑，即当前用户没有登录时跳转到登录页面，修改后：

```js
const { location } = history;
  const whiteList = ['/user/register', loginPath];
  if(whiteList.includes(location.pathname)) {
    return;
  }
  // 如果没有登录，重定向到 login
  if (!initialState?.currentUser && location.pathname !== loginPath) {
    history.push(loginPath);
  }
},
```

接下来是注册页面修改



### 用户注销

​	后端，关键在于将 session 中的用户登录态移除

```java
public Integer userLogout(HttpServletRequest request) {
    request.getSession().removeAttribute(USER_LOGIN_STATUS);
    return 1;
}
```

前端，注意请求地址

```ts
/** 退出登录接口 POST /api/user/logout */
export async function outLogin(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}
```

#### 添加用户校验功能

> 用户自己填编号， 
>
> 后台校验编号：长度校验、唯一性校验
>
> 前端补充输入框，适配后端



### 后端优化

1. 统一返回结果

   返回结果应该容易区分响应的状态

   ```json
   {
       "code":0 //业务状态码
       "data":{}
   	"message":"ok"
   }
   ```

   自定义错误码

2. 封装全局异常处理器

   - 封装定义业务异常类

     - 相对于java自带的异常类，添加了字段和方法，更灵活
     - 自定义构造函数， 更灵活、快捷的设置字段

   - 编写全局异常处理器

     1. 捕获代码中的所有业务异常，集中处理，让前端得到更详细的业务报错/信息

     2. 同时屏蔽项目框架本身的异常（不暴露服务器本身的状态）

     3. 可以集中处理和记录日志

        实现：

        1. spring AOP ，在调用方法前后进行额外的处理

3. 全局请求日志和登录校验 todo

### 前端优化

1. 对接后端的返回值，接收data

   在`typings.d.ts`中 定义统一响应结果类型：

   ```tsx
   /**
      * 对接后端的通用返回类
      */
     type Result<T> = {
       code: number,
       data: T,
       message: string,
       description: string,
     }
   ```

   

2. 定义全局响应拦截器，统一取出data，避免代码冗余，提高可维护性。   参考：https://blog.csdn.net/huantai3334/article/details/116780020

自定义全局响应拦截器,对通用响应data进行 拦截并处理：

`globalRequest`:

```tsx
const res = await response.clone().json();

    if(res.code === 20000) {
        return res.data;
    }

    if(res.data === 40100) {   //未登录
        message.error('请先登录');
        history.replace({ //跳转到指定的地址
            pathname: '/user/login',
            search: stringify({
                redirect: location.pathname,
            }),
        });
    }else {
        message.error(res.description)
    }
    return res.data;
```

实现：参考请求工具（axios、react）的官方文档的介绍

### 部署上线

1. #### 多环境

   参考文章：[多环境设计-CSDN博客](https://blog.csdn.net/weixin_41701290/article/details/120173283)

   本地开发： localhost(127.0.0.1)

   多环境：指同一套项目代码在不同的阶段需要根据实际情况来调整配置并且部署到不同的机器上。

   > 优点：
   >
   > - 每个环境互不影响
   >
   > - 区分不同的阶段：开发/测试/生产
   >
   > - 对项目进行优化：
   >
   >   本地日志级别
   >
   >   精简依赖，节省项目体积
   >
   >   项目的环境/参数可以调整，比如JVM参数
   >
   > 多环境就是针对不同环境做不同的事情。

   ##### 多环境分类：

   1. 本地环境（本地电脑）localhost
   2. 开发环境：（远程开发），开发人员共连同一台机器，方便协作开发开发
   3. 测试环境(测试)/测试产品
   4. 预发布环境：和正式环境一致，正式数据库，更严谨，排查问题
   5. 正式环境（线上，公开对外访问的项目）：尽量不要改动，保证上线前的代码是"完美"运行
   6. 沙箱环境

   ##### 多环境实践（前端）：

   - 请求地址

     - 开发环境：localhost:8000

     - 线上环境：user_backend-nav.cn

   ```tsx
   startFront(env) {
   if(env === 'prod') {
   	 //不输出注释
   	 //项目优化
    	//修改请求地址
   	} else {
   	//保持本地开发逻辑
   	}
   }
   ```

   使用了umi框架，build时会自动传入  `process.env.NODE_ENV`参数，`process.env.NODE_ENV`参数为development

   - 启动方式

     - 开发环境： `npm run start `(本地启动，监听端口、自动更新)
     - 线上环境：`npm run build`(项目构建打包)，可以使用 serve工具启动 （安装：`npm install -g serve`

   - 项目配置

     不同项目（框架）都会有不同的配置文件，umi的配置文件是 config，可以在配置文件后添加对应的环境名称后缀来区分开发环境和生产环境。参考文档[部署 (umijs.org)](https://v3.umijs.org/zh-CN/docs/deployment)

     - 生产环境：config.dev.ts
     - 生产环境：confi.prod.ts
     - 公共配置：config.ts不带后缀

   ##### 后端多环境

   springboot 项目，通过 application.yml 添加不同的后缀来区分配置文件，可以在启动项目时传入环境变量：

   `java -jar xxx(jar包) --spring.profiles.active=prod`

   不同环境体现在：

   - 依赖的环境地址
     - 数据库地址
     - 缓存地址
     - 消息队列地址
     - 项目端口号
   - 服务器配置

2. #### 部署上线

   - 原始前端/后端项目

     安装 nginx  : [Linux系统下安装配置nginx（保姆级教程）_linux安装nginx-CSDN博客](https://blog.csdn.net/qq_65732918/article/details/131862373)

     ```bash
     curl -o nginx-1.21.6.tar.gz http://nginx.org/download/nginx-1.21.6.tar.gz
     
     tar -zxvf nginx-1.21.6.tar.gz
     
     cd nginx-1.21.6
     
        37  2022-04-17 23:30:09 yum install pcre pcre-devel -y
        39  2022-04-17 23:30:59 yum install openssl openssl-devel -y
        41  2022-04-17 23:31:57 ./configure --with-http_ssl_module --with-http_v2_module --with-stream
        42  2022-04-17 23:32:13 make
        43  2022-04-17 23:32:54 make install
        48  2022-04-17 23:33:40 ls /usr/local/nginx/sbin/nginx
        vim /etc/profile
       在最后一行添加：export PATH=$PATH:/usr/local/nginx/sbin	
       
       nginx
       
       netstat -ntlp 查看启动情况
     ```

     注意 nginx 权限， 在配置文件中添加 `user root`指定用户

   - 宝塔 Linux

   - 容器

   - 容器平台
