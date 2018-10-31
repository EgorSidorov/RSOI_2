# Deploying a Spring Boot project as WAR on tomcat

This is an example of how to package and deploy a Spring Boot project as WAR on Tomcat.

By default, this project can be run as an executable jar file deployable to Spring Boot's embedded tomcat.

In order to package it as an executable war file, we have to do the following changes:

First Download Apache Maven 3 and set the M2_HOME

Then we create a standard Spring Boot web project:

https://github.com/ManfredWind/deploying-spring-boot-as-war-on-tomcat/commit/02c0732e930f2b92c5aafb57646b1b987e9a92a8

- Change Packaging type in pom.xml from jar to war

- Add dependency spring-boot-starter-tomcat and set the scope as provided. This is necessary to omit embedded tomcat in order to avoid conflict.

- In your @SpringBootApplication configuration class, extend the SpringBootServletInitializer class to override the configure(SpringApplicationBuilder application) method. This is necessary to make it an executable war file.

From docs: https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/support/SpringBootServletInitializer.html

"An opinionated WebApplicationInitializer to run a SpringApplication from a traditional WAR deployment. Binds Servlet, Filter and ServletContextInitializer beans from the application context to the servlet container."

Now you can create a controller to test the web application and use maven to package the war file with the command:

- mvn clean package

Once the war file has been successfully generated (compiled and built) you can deploy it to tomcat.


## Deploying to Tomcat

All changes:

https://github.com/ManfredWind/deploying-spring-boot-as-war-on-tomcat/tree/deploy-to-tomcat

Download Apache Tomcat 9

There are a numerous ways to deploy locally to Tomcat (copy/paste, ANT script, custom shell script, IDE, maven plugin, etc. )

The easiest would be with your IDE by just adding Tomcat as a "Server" and then right click project -> "Run as" - > "Run on Server"

My favorite is the maven-tomcat-plugin which gladly still works for Tomcat 9. It let's me externalize my tomcat and run it as a separate service instead of using the IDE for it.


All we need to do is the following:

- Change your settings.xml in your .m2 directory (if not present, then create one).

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>${user.home}/.m2/repository</localRepository>

<!-- add these config -->
   <servers>
        <server>
                <id>localtomcat</id>
                <username>user</username>
                <password>password</password>
        </server>
   </servers>

</settings>
```

- Add the following lines add the end of your tomcat-users.xml located in your tomcat's conf directory:

```
  <!-- Add these lines at the bottom -->
  <role rolename="manager"/>
  <user username="user" password="password" roles="manager"/>

</tomcat-users>
```

- Add the plugin to your project's pom.xml:

```
<build>
  <finalName>example</finalName>
  <plugins>
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
        <plugin>
          <groupId>org.apache.tomcat.maven</groupId>
          <artifactId>tomcat7-maven-plugin</artifactId>
          <version>2.2</version>
      <configuration>
         <url>http://localhost:8080/manager/text</url>
         <server>localtomcat</server>
         <path>/example</path>
       </configuration>
        </plugin>
  </plugins>
</build>
```

- Start your tomcat

- Execute maven command for your project:  mvn clean tomcat7:redeploy

- Go to http://localhost:8080/example

- Verify your controller output

VOILA!


More info at Spring docs:

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file
