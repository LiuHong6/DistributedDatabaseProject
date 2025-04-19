# 分布式数据库项目  
# Distributed Database Project

## 项目概述  
## Project Overview  
这是一个基于 Java RMI 技术的分布式数据库应用，实现了一个员工管理系统。该系统允许多个客户端同时访问并操作共享数据库，同时提供了事务管理和并发控制，确保数据的一致性和完整性。  
This is a distributed database application based on Java RMI technology, implementing an employee management system. The system allows multiple clients to simultaneously access and manipulate a shared database, with transaction management and concurrency control to ensure data consistency and integrity.

## 功能特点  
## Features  
- 员工数据的增删改查操作  
  CRUD operations on employee data  
- 分布式架构，基于 RMI 实现客户端-服务器通信  
  Distributed architecture with client-server communication via RMI  
- 事务处理和并发控制  
  Transaction processing and concurrency control  
- 多客户端同时操作支持  
  Support for simultaneous operations by multiple clients  
- 命令行界面操作  
  Command-line interface for operations  
- 数据过滤、排序功能  
  Data filtering and sorting capabilities

## 技术栈  
## Technology Stack  
- Java 17  
- Maven（项目管理） / Maven (Project Management)  
- SQLite（数据库） / SQLite (Database)  
- Java RMI（远程方法调用） / Java RMI (Remote Method Invocation)

## 系统架构  
## System Architecture  
系统采用三层架构：  
The system adopts a three-tier architecture:  
1. **客户端**：提供用户界面，通过 RMI 与服务器通信  
   **Client**: Provides user interface and communicates with the server via RMI  
2. **服务器**：处理客户端请求，实现业务逻辑  
   **Server**: Handles client requests and implements business logic  
3. **数据库**：SQLite 数据库，存储员工信息  
   **Database**: SQLite database for storing employee information

## 环境要求  
## Environment Requirements  
- JDK 17 及以上 / JDK 17 or above  
- Maven 3.8 及以上 / Maven 3.8 or above

## 安装与使用  
## Installation & Usage

### 1. 克隆仓库  
### 1. Clone the Repository  
```bash
git clone [仓库URL]
cd DistributedDatabaseProject
```

### 2. 构建项目  
### 2. Build the Project  
```bash
mvn clean install
```

### 3. 运行 RMI 注册表  
### 3. Run RMI Registry  
```bash
java -cp target/demo-1.0-SNAPSHOT.jar com.example.RMIRegistryStarter
```

### 4. 启动服务器  
### 4. Start the Server  
```bash
java -cp target/demo-1.0-SNAPSHOT.jar com.example.EmpServer
```

### 5. 运行客户端应用  
### 5. Run the Client Application  
```bash
java -cp target/demo-1.0-SNAPSHOT.jar com.example.EmpDBConsoleApp
```

### 6. 测试并发事务处理  
### 6. Test Concurrent Transactions  
```bash
java -cp target/demo-1.0-SNAPSHOT.jar com.example.ConcurrentTransactionTest
```

## 主要类说明  
## Key Class Descriptions  
- `EmpDBConsoleApp`：提供命令行界面的客户端应用  
  `EmpDBConsoleApp`: CLI-based client application  
- `EmpServer`：RMI 服务器实现  
  `EmpServer`: RMI server implementation  
- `EmpRemote`：远程接口定义  
  `EmpRemote`: Remote interface definition  
- `EmpRemoteImpl`：远程接口实现  
  `EmpRemoteImpl`: Remote interface implementation  
- `EMPDAO`：数据访问对象，处理数据库操作  
  `EMPDAO`: Data Access Object for database operations  
- `DBConnection`：数据库连接管理  
  `DBConnection`: Manages database connections  
- `TransactionClient`：事务处理客户端  
  `TransactionClient`: Client for transaction operations  
- `ConcurrentTransactionTest`：测试并发事务处理  
  `ConcurrentTransactionTest`: Class for testing concurrent transactions

## 事务并发控制  
## Transaction & Concurrency Control  
项目实现了基本的事务隔离级别和并发控制机制，确保在多客户端同时操作时保持数据一致性：  
The project implements basic transaction isolation levels and concurrency control mechanisms to ensure data consistency during concurrent client operations:  
- 读取一致性 / Read consistency  
- 写入隔离 / Write isolation  
- 死锁检测与处理 / Deadlock detection and handling  
- 事务回滚 / Transaction rollback
