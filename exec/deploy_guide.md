# 공습 배포 가이드

### 1. Dependency

- **BE**
  - `Intellij`: Ultimate 2021.3.1
  - `JAVA` : java-1.8.0-openjdk 1.8.0.312
  - `Spring Boot`: 2.6.3
  
  - `MYSQL`: 8.0
  
- **FE**
  - `VScode`: version 1.64
  - `Node.js`: v16.13.2
  
  - `axios`: ^0.25.0
  
  - `react`: ^17.0.2
  
  - `react-redux`: ^7.2.6
  
  - `react-router-dom`: ^6.2.1
  
  - `yarn`: ^1.22.17
  
  - `sass`: 1.49.7
  
- **Deploy**
  - `NGINX`:  nginx/1.18.0
  - `gitlab`
  - `docker`:  20.10.12

### 2. Env Variable for build

- **BE**

  - `application.properties`

    ```java
    # database
    spring.datasource.url=jdbc:mysql://i6a301.p.ssafy.io:3306/gongsp?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.hikari.username=superwaffle
    spring.datasource.hikari.password=superGongsp!6
    
    # jwt
    jwt.secret=dyAeHubOOc8KaOfYB6XEQoEj1QzRlVgtjNL8PYs1A1tymZvvqkcEU7L1imkKHeDa
        
    # mail
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=gongsp.sw
    spring.mail.password=superGongsp!6
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    
    ## for SSL
    server.port = 8080
    server.http.port = 8082
    server.ssl.enable = true
        
    # The format used for the keystore. It could be set to JKS in case it is a JKS file
    server.ssl.key-store-type = PKCS12
        
    # The path to the keystore containing the certificate
    server.ssl.key-store = classpath:keystore/keystore.p12
        
    # The password used to generate the certificate
    server.ssl.key-store-password = gongsp6
    
    # openvidu
    openvidu.url: https://i6a301.p.ssafy.io:8443
    openvidu.secret: superGongsp6
        
    
    # servlet
    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=10MB
    spring.servlet.multipart.location=C:\\ssafy\\gonsp\\images
    spring.servlet.multipart.enabled=true
    ```

- **FE**

  - `.env`

    ```
    REACT_APP_SERVER_URL="https:/i6a301.p.ssafy.io:8080"
    ```



### 3. Remarks for Deploy

- We set up DB and Client(by NGINX) on EC2 and seperated Server through Docker. By doing so, we was possible to gain the advantage of operating them independently on one machine.

### 4. etc

- [ERDcloud](https://www.erdcloud.com/team/kpsf5vvWrFLF3QoHj)
- [Figma](https://www.figma.com/file/5ySXykH6zs0BEXyhOMNASk/%EA%B3%B5%EC%8A%B5)