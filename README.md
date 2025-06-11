## 第一部分：环境准备与工具配置

在开始之前，请确保你已经安装了以下核心软件：
- **JDK 17**: Java Development Kit
- **Visual Studio Code**: 代码编辑器

接下来，我们需要为 VSCode 安装和配置必要的插件和命令行工具：

### 1. 安装 Node.js 和 npm
Vue3 开发需要 Node.js 环境：
1. 访问 [Node.js 官网](https://nodejs.org/) 下载并安装 LTS（长期支持）版本（安装过程会自动包含 npm）
2. 安装后打开终端（或 Windows 的 PowerShell/CMD）验证安装：
```bash
node -v
npm -v
```

### 2. 安装 Vue CLI

内容：Vue CLI 是一个用于快速搭建 Vue 项目的命令行工具。

在终端中运行以下命令进行全局安装：

```bash

npm install -g @vue/cli

```

### 3. 配置 VSCode 插件

内容：打开 VSCode，进入插件市场（侧边栏的方块图标），安装以下插件：

- Volar: Vue 3 的官方推荐插件，提供语法高亮、智能提示等功能。（如果安装了 Vetur，请禁用它以避免冲突）。

- Extension Pack for Java: 由 Microsoft 提供的 Java 开发扩展包，包含了运行、调试、Maven/Gradle 支持等一系列功能。

- Spring Boot Extension Pack: 强烈推荐，它能极大地简化 Spring Boot 应用的创建和管理。


## 第二部分：创建“正版”应用 (App1)
我们将首先创建一个功能正常的、前后端分离的登录应用。  
**后端 (Legit-Backend)**: 运行在 http://localhost:9090  
**前端 (Legit-Frontend)**: 运行在 http://localhost:8080  

### 1. 创建后端项目 (Legit-Backend)
我们将使用 Spring Boot 来快速构建后端。  

**创建项目**:  
1. 在 VSCode 中，按 `Ctrl+Shift+P` 打开命令面板。  
2. 输入 `Spring Initializr: Create a Maven Project...`。  
3. Spring Boot Version: 选择一个稳定版 (如 3.x.x)。  
4. Language: Java。  
5. Group Id: `com.example`。  
6. Artifact Id: `legit-backend`。  
7. Packaging Type: Jar。  
8. Java Version: 17。  
9. Dependencies: 搜索并选择 **Spring Web** 和 **Lombok**。  
10. 选择一个文件夹来存放项目。VSCode 会自动生成并打开项目。  

**项目结构**:  
legit-backend/
├── src/main/java/com/example/legitbackend/
│ ├── dto/
│ │ └── LoginRequest.java // DTO: 数据传输对象
│ ├── controller/
│ │ └── AuthController.java // 控制器
│ └── LegitBackendApplication.java
├── src/main/resources/
│ └── application.properties
└── pom.xml




