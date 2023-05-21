## SpringBoot整合Mybatis

### 依赖导入

````xml
        <dependencies>
<!--            SpringBoot依赖-->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
<!--            Mybatis依赖-->
            <dependency>
                <groupId>org.mybatis.spring.boot</groupId>
                <artifactId>mybatis-spring-boot-starter</artifactId>
                <version>2.1.4</version>
            </dependency>
<!--            数据库连接-->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>5.1.6</version>
            </dependency>
<!--            德鲁伊的连接池依赖-->
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>1.2.4</version>
            </dependency>
<!--            lombok依赖-->
            <dependency>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </dependency>
 <!--          日志门面-->
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-api</artifactId>
                <version>1.7.30</version>
            </dependency>
  <!--         log4j日志 -->
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>1.2.17</version>
            </dependency>
        </dependencies>
````

同时在Maven中检查一下依赖是否完全导入

![依赖检查](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520123710333.png)



### 配置信息(application.yml)

我们在resources目录下创建application.yml的文件

![image-20230520123908536](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520123908536.png)

在application.yml文件中我们编写配置信息：

````yaml
#端口号
server:
  port: 8888
#spring中datasource配置
spring:
  datasource:
#    我们选择我们的德鲁伊数据源
    type: com.alibaba.druid.pool.DruidDataSource
#    这里我们填写我们的数据库用户名和密码
    username: root
    password: 123456
#    这里填写我们的url地址,注意:不同版本的mysql在url‘?’后拼接的字符是不一样的,这里我的mysql版本是5.7的
    # 8.0版本以上的url是 jdbc:mysql://localhost:3306/ibcompany?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT
    # 解释: 端口号是3306 对应数据库是ibcompany
    url: jdbc:mysql://localhost:3306/ibcompany?characterEncoding=utf8
    driver-class-name: com.mysql.jdbc.Driver
mybatis:
  # mapper映射文件所在的包
  mapper-locations: classpath:/mapper/*.xml
  # 实体类所在的包
  type-aliases-package: com.lty.enity
  configuration:
    # 将下划线映射成驼峰
    map-underscore-to-camel-case: true
    # 开启延迟加载
    lazy-loading-enabled: true
    # 按需加载关闭
    aggressive-lazy-loading: false
    # 这里开启打印日志,他会帮助我们生成对应的sql语句
    log-impl: org.apache.ibatis.logging.log4j.Log4jImpl
````

**注意:**

一定要先确认自己的Mysql版本号再去填写对应的*url*和*driver-class-name* 

补充：怎么查看自己的mysql版本号：

1. win+r 输入cmd + 回车
2. 在控制台输入mysql --version
3. ![image-20230520172305349](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520172305349.png)

同时添加log4j的配置文件

![image-20230520130342942](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520130342942.png)

````xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
    <appender name="STDOUT" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="UTF-8" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%-5p %d{MM-dd HH:mm:ss,SSS}
%m (%F:%L) \n" />
        </layout>
    </appender>
    <logger name="java.sql">
        <level value="debug" />
    </logger>
    <logger name="org.apache.ibatis">
        <level value="info" />
    </logger>
    <root>
        <level value="debug" />
        <appender-ref ref="STDOUT" />
    </root>
</log4j:configuration>
````

### 代码分层

![image-20230520130625712](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520130625712.png)

controller: 控制层负责编写控制用户访问代码

dao: 负责与数据库的交互

enity: 各个对象的实体层

service: 业务层

   impl: 业务层的主要实现

mapper: 每个实体类对应的mapper文件

### 数据库(建库建表语句)

​     建表的sql如下

```mysql
CREATE DATABASE ibcompany;

CREATE TABLE `ib_user`(
`id` INT(25) PRIMARY KEY,
`name` VARCHAR(255),
`dept_name` VARCHAR(255),
`address` VARCHAR(255)
)CHARACTER SET = utf8;

CREATE TABLE `ib_dept`(
`id` INT(25) PRIMARY KEY NOT NULL AUTO_INCREMENT,
`dept_name` VARCHAR(255),
`company_number` INT(25)
)CHARACTER SET = utf8;

CREATE TABLE `ib_admin`(
`id` INT(25) PRIMARY KEY AUTO_INCREMENT,
`username` VARCHAR(255) NOT NULL,
`password` VARCHAR(255) NOT NULL
)CHARACTER SET = utf8;
```

​     表中的各种信息大家可以自行填写

###      各层代码

#### enity:

我们根据数据库中表中的字段属性去实体类中编写对应的实体对象

````java
@Data // lombok提供的注解 自动生成getter,setter方法
@AllArgsConstructor //所有的有参构造器
@NoArgsConstructor  //所有的无参构造器
public class User {
    private Integer id;
    private String name;
    private String deptName;
    private String address;
}
````

#### dao:

dao层所放置的代码是我们与数据库之间进行数据交互的代码。

注意我们的dao层需要的两个注解:

@Mapper:

> Mapper注解用于标识与Mybatis对应的Mapper映射文件。Mybatis 会有一个拦截器，会自动的把 @Mapper 注解的接口生成**动态代理类**

@Repository

> 该注解则是将我们的dao层注入到IOC容器中去。当我们在某些地方如Service层需要调用dao层时，我们只需要通过@Autowired或者@Resource注解声明一下就可以直接进行调用。

````java
@Mapper
@Repository
public interface UserDao {
       //获取所有的用户信息
       public List<User> getAllUser();
}

````

当我们写完对应的dao层代码，我们会发现它有红线提示。

![提示](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520160013974.png)

这里的红线提示是指我们没有与之对应的Mapper映射文件,因此我们需要在resources下的Mapper中去创建对应的UserMapper.xml。

UserMapper.xml

````xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.lty.dao.UserDao">

    <select id="getAllUser" resultType="com.lty.enity.User">
        select * from ib_user;
    </select>
</mapper>
````

**注意:**

- 这里的namespace必须要对应到dao层。
- select标签中的id必须是对应的dao层的接口名字，如果名字不一致会导致匹配不上。resultType就是我们返回的数据类型。
- 在select标签中我们就开始写我们对应的sql语句。

#### service:

![image-20230520155024519](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520155024519.png)

我们service层一般是一个接口对应的一个实现类。并且实现类和接口分开进行放置。

service层的接口:

```java
public interface UserService {
    List<User> getAllUser();
}
```

对应的实现类:

@Service

将我们的Service也注入到IOC容器中，也交给IOC容器进行管理，方便在后边Controller层中的使用。

```java
@Service
public class UserServiceImpl implements UserService {
    //注入我们的dao层,与数据库之间进行交互
    @Autowired
    private UserDao userDao;

    /**
     * 得到所有的用户信息
     * @return 返回的是一个List的User集合
     */
    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }
}
```

在实现类中我们可以看到service层中注入了dao层与数据库进行数据访问。

#### controller:

@RestController：

这个注解当我们ctrl+B进入就可以看到是@Controller + @ResponseBody 注解的合成注解

@ResponseBody：该注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据。

@Controller：则是用来标识这Controller控制层。

![image-20230520173146874](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520173146874.png)

@RequestMapping：

请求路径：当我们在类上使用这个注解，就表示这个在这个类中所有请求前面都必须带有这个请求路径。

```java
@RestController
@RequestMapping("/user")
public class UserController{
    @Autowired
    private UserService userService;

    @RequestMapping("/getAllUser")
    public List<User> getAllUser(){
        return userService.getAllUser();
    }
}
```

例如我们刚写好的一个控制层接口我们只需要访问 http://localhost:8888/user/getAllUser 即可。

因此我们这个Controller层就已经完成了。

### 测试

我们可以进行测试：

这里我使用的是postman。

![success](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520173912009.png)

我们可以看到已经发送成功，并且拿到了我们所需要的数据。

![控制台](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520174012814.png)

在控制台也打印出了我们的查询语句。

这样我们的SpringBoot与Mybatis的初级整合也就完成了！

## Mybatis分页查询和模糊查询

### 分页查询:

先引入Mybatis的pageHelper的依赖

````xml
<!--            引入Mybatis的Pagehelper的依赖-->
            <dependency>
                <groupId>com.github.pagehelper</groupId>
                <artifactId>pagehelper-spring-boot-starter</artifactId>
                <version>1.4.5</version>
            </dependency>
````

当我们引入这个pageHelper后，我们就得到了一个分页对象PageInfo

从中我们可以看出PageInfo拥有很多的属性

![image-20230520204714434](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520204714434.png)

我们用的比较多的

> pageNum: 起始的页码
>
> pageSize: 每页的数量

因此我们需要定义一个自己的分页数据PageQuery

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery {
    //起始的页码 默认为第一页
    private Integer pageNum = 1;
    //每页显示的个数为3个信息
    private Integer pageSize = 3;
}

```

使用方法,我们只需要将我们定义的pageQuery对象传入即可,返回的对象是一个PageInfo对象

UserService:

```java
    PageInfo<User> getAllUserByPage(PageQuery pageQuery);
```



UserServiceImpl:

```java
    /**
     * 通过分页插件pageHepler进行分页
     * @return 返回的是PageInfo
     */
    @Override
    public PageInfo<User> getAllUserByPage(PageQuery pageQuery) {
        //先开启分页
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        //返回的是一个PageInfo对象
        return new PageInfo<User>(userDao.getAllUserByPage(pageQuery));
    }
```

> 注意:
>
> 在返回之前我们必须用PageHelper的startPage方法将我们的分页数据传入进去，只有这样分页才会生效。

Controller层:

```java
    @RequestMapping("/getAllUserByPage")
    public List<User> getAllUserByPage(PageQuery pageQuery){
        PageInfo<User> allUserByPage = userService.getAllUserByPage(pageQuery);
        //拿到pageInfo中的分页过后集合
        List<User> list = allUserByPage.getList();
        return list;
    }
```

#### 测试:

![image-20230520205930212](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520205930212.png)

IDEA控制台:

![image-20230520210004318](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520210004318.png)

当我们想查看第二页，我们只需要在postman中输入pageNum=2,就可以查看到第二页。

### 模糊查询:

dao层:

```java
       //根据用户用户名进行模糊查询
       public List<User> getAllUserByPageAndName(PageQuery pageQuery,@Param("name") String name);
```

对应的UserMapper.xml

```xml
    <select id="getAllUserByPageAndName" resultType="com.lty.enity.User">
        select * from ib_user
             <where>
                 <if test="name != null and name != ''">
                     and name like '%${name}%'
                 </if>
             </where>
    </select>
```

> 这里我们用到了Mybatis的动态sql，当我们传入的name为null或者为空字符。这样就直接查询所有用户。

controller:

```java
    @RequestMapping("/getAllUserByPageAndName")
    public List<User> getAllUserByPageAndName(PageQuery pageQuery,String name){
        PageInfo<User> allUserByPage = userService.getAllUserByPageAndName(pageQuery,name);
        List<User> list = allUserByPage.getList();
        return list;
    }
```

其他层的代码大同小异。

#### 测试:

![image-20230520212242478](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230520212242478.png)

## Mybatis的分布查询

不知道当时我们查询所有用户时大家注意到每我们查出的用户信息中deptName总是null的。那是因为我们的数据库中user表内没有deptName的这个内容。deptName的内容在dept表中。因此我们要关联这两张表。

### 多对一:

这里我们采用**分布查询**的方式。

1. 我们先在UserMapper.xml中的getAllUser进行修改，因为是分布查询，我们的主要思路是先查询User表，拿到User表中deptId，再根据deptId到dept表中进行查询到对应的deptName

   ```xml
       <resultMap id="getAllUserMap" type="User">
           <result property="id" column="id"></result>
           <result property="deptId" column="dept_id"></result>
           <!--        其中的deptName是多对一的关系，这里我们采用association-->
           <!--        select中我们写的是下一个查询的路径，也就是查询dept表的方法-->
           <association property="deptName" select="com.lty.dao.DeptDao.getDeptName" column="dept_id"></association>
       </resultMap>
   
       <select id="getAllUser" resultMap="getAllUserMap">
           select * from ib_user;
       </select>
   ```

   

2. 我们创建1个DeptDao,在DeptDao中写查询deptName的方法getDeptName();

   DeptDao:

   ```java
   @Mapper
   @Repository
   public interface DeptDao {
       String getDeptName(@Param("deptId") Integer id);
   }
   
   ```

   DeptMapper.xml:

   ```xml
       <select id="getDeptName" resultType="string">
           select dept_name
           from ib_dept
           where id = #{deptId};
       </select>
   ```

   **注意:DeptDao中的方法路径必须和UserMapper中的select对应。**

   

   ### 测试:

   这里我们进行测试:

   ![image-20230521132501430](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521132501430.png)

   可以看到这个测试出来的数据就比较全面。

### 一对多:

​    一个部门对应这多个User，因此我们这种情况属于一对多的关系。

   这里我们所用的标签就为collection

1. 我们在Dept实体类中加入 users;

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class Dept {
       private Integer id;
       private String deptName;
       private Integer companyNumber;
       private List<User> users;
   }
   ```

2. DeptDao

   ```java
   Dept getDeptUsers(@Param("deptId") Integer id);
   ```

3. DeptMapper.xml

   注意：是collection标签

   ```xml
   <resultMap id="getUsers" type="Dept">
       <collection property="users" select="com.lty.dao.UserDao.getUsers" column="id"></collection>
   </resultMap>
   <select id="getDeptUsers" resultMap="getUsers">
       select * from ib_dept
       where id = #{deptId};
   </select>
   ```

4. UserDao

   ```java
   List<User> getUsers(@Param("deptId") Integer id);
   ```

5. UserMapper.xml:

   ```xml
   <select id="getUsers" resultType="User">
        select * from ib_user
           where dept_id = #{deptId}
   </select>
   ```

   测试:

   ![image-20230521135304233](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521135304233.png)

我们也可以看控制台的查询语句（2句sql）

![image-20230521135337342](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521135337342.png)

## Mybatis的其他语句

### Update:

UserDao:

```java
int UpdateUserById(@Param("id") Integer id, @Param("address") String address);
```

UserMapper.xml:

```xml
<update id="UpdateUserById">
    update ib_user
    set address = #{address}
    where id = #{id};
</update>
```

**注意:标签为update**

![image-20230521140323065](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521140323065.png)

### delete:

UserDao:
```java
       int deleteById(@Param("id") Integer id);
```

UserMapper.xml:

```xml
<delete id="deleteById">
    delete from ib_user
      where id = #{id};
</delete>
```

![image-20230521140830773](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521140830773.png)

控制台(可见执行了delete语句):

![image-20230521140857758](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521140857758.png)

### Insert:

UserDao:

```java
int insertUser(@Param("user") User user);
```

UserMapper.xml

```xml
<insert id="insertUser">
    insert into ib_user
    values (null,#{user.name},#{user.address},#{user.deptId});
</insert>
```

从控制台可以看到添加成功！

![image-20230521141815784](C:\Users\刘泰源\AppData\Roaming\Typora\typora-user-images\image-20230521141815784.png)

## 小结

这篇博客有点长，博主个人觉得写的还是挺细的，希望能对初学者有帮助。同时这篇博客的源码也会同步到博主的github上。希望大家多点点赞和关注。LeetCode每日一题还是会更的，只是最近事太多了。

