# RBAC

一个基于事件监听的 RBAC 系统（Spring Boot + Thymeleaf + Spring Data JPA + MySQL）

## **1. 准备 MySQL 数据库**

在 MySQL 中创建数据库 `rbac_demo`
```mysql
CREATE DATABASE rbac_demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

如果用户名/密码不是 `root`/`password`，请修改 `src/main/resources/application.properties` 中对应配置。

## **2. 启动项目**

下载或者clone本项目后，在项目根目录运行：

```bash
mvn -U clean spring-boot:run
```



第一次启动时，会自动初始化示例数据（用户/角色/权限/资源）。看到控制台 `[DataInitializer] 初始化完成` 表示创建成功。

## **3. 登录并测试**

打开浏览器访问 `http://localhost:8080`

使用以下用户：`alice`（管理员）、`bob`（老师）、`carol`（助教）、`dave`（学生），进行切换角色、点击“读取/切换共享/发送消息”等操作，