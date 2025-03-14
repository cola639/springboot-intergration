- ### 📌 **Spring Boot 3.x 整合 Swagger 总结**

  > **背景**
  >  随着前后端分离架构的发展，API 文档成为前后端开发人员沟通的重要桥梁。然而，手动维护 API 文档效率低且易出错。Swagger 提供了一种自动生成、描述、调用和测试 API 的方案，能够极大提高开发效率。

  > **Spring Boot 3.x + JDK 17 的 Swagger 兼容性**

  - 旧版 `Swagger2` 和 `Swagger3` 不兼容 Spring Boot 3.x。
  - **解决方案**：使用 **`springdoc-openapi`** 代替 `Springfox`（即 `swagger-spring-boot-starter`）。
  - 需要使用 **`springdoc-openapi` 2.x 及以上版本**，否则会有兼容性问题。

  ------

  ### 🏗 **整合 Swagger 的步骤**

  #### **1️⃣ 添加 Swagger 依赖**

  在 `pom.xml` 中添加 **`springdoc-openapi`** 依赖：

  ```xml
  <dependency>
      <groupId>org.springdoc</groupId>
      <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
      <version>2.2.0</version> <!-- 确保版本号 >= 2.x -->
  </dependency>
  ```

  > ✅ **注意**：Spring Boot 3.x **不再支持** `Springfox`，请不要使用 `springfox-swagger2`。

  #### **2️⃣ 配置 `application.yml`**

  在 `application.yml` 中添加：

  ```yaml
  spring:
    mvc:
      pathmatch:
        matching-strategy: ant_path_matcher
  ```

  ------

  #### **3️⃣ Swagger 配置类**

  创建 **`OpenAPIConfig.java`**：

  ```java
  package com.example.config;
  
  import io.swagger.v3.oas.models.ExternalDocumentation;
  import io.swagger.v3.oas.models.OpenAPI;
  import io.swagger.v3.oas.models.info.Info;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  
  @Configuration
  public class OpenAPIConfig {
      @Bean
      public OpenAPI openAPI() {
          return new OpenAPI()
                  .info(new Info()
                          .title("Spring Boot 3.x Swagger API 文档")
                          .description("SpringBoot3 集成 Swagger3.0 API 文档")
                          .version("v1"))
                  .externalDocs(new ExternalDocumentation()
                          .description("API 文档地址")
                          .url("/"));
      }
  }
  ```

  ------

  #### **4️⃣ 控制器示例**

  ```java
  package com.example.controller;
  
  import io.swagger.v3.oas.annotations.Operation;
  import io.swagger.v3.oas.annotations.Parameter;
  import io.swagger.v3.oas.annotations.responses.ApiResponse;
  import io.swagger.v3.oas.annotations.tags.Tag;
  import org.springframework.web.bind.annotation.*;
  
  @Tag(name = "用户管理", description = "用户 API")
  @RestController
  @RequestMapping("/user")
  public class UserController {
  
      @Operation(summary = "获取用户信息", description = "根据 ID 获取用户详情")
      @ApiResponse(responseCode = "200", description = "成功")
      @GetMapping("/{id}")
      public String getUser(@Parameter(description = "用户 ID") @PathVariable String id) {
          return "用户 ID：" + id;
      }
  }
  ```

  ------

  #### **5️⃣ 运行并访问 Swagger**

  1. 启动 Spring Boot 项目。

  2. 在浏览器访问 Swagger UI：

     ```
     http://localhost:8080/swagger-ui/index.html
     ```

  3. 你可以在 Swagger UI 中查看和测试 API。

  ------

  ### 🛠 **最终的 `pom.xml`**

  ```xml
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
  
      <groupId>springboot</groupId>
      <artifactId>springboot-mybatis</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>3.1.4</version>
      </parent>
  
      <properties>
          <java.version>17</java.version>
      </properties>
  
      <dependencies>
          <!-- Spring Boot Web -->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
  
          <!-- OpenAPI (Swagger 3) -->
          <dependency>
              <groupId>org.springdoc</groupId>
              <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
              <version>2.2.0</version>
          </dependency>
  
      </dependencies>
  </project>
  ```

  ------

  ### 🔥 **总结**

  ✅ **Spring Boot 3.x + JDK 17 推荐使用 `springdoc-openapi`**，不兼容 `swagger2` 和 `springfox`。
   ✅ 需要 **`springdoc-openapi-starter-webmvc-ui` 2.x 及以上版本**。
   ✅ Swagger API 可视化地址：`http://localhost:8080/swagger-ui/index.html`。
   ✅ 使用 `@Tag`、`@Operation`、`@Parameter` 等新注解替代 `Springfox` 旧注解。

  🚀 这样，你就成功在 Spring Boot 3.x 项目中集成了 **Swagger 3.0 API 文档**！ 🎉