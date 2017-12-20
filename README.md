## A Very Simple Vaadin Login Demo

### Technologies used:

* Java (1.8.0_144)
* Maven (3.3.9)
* MySQL (5.7.19)
* Spring Boot
* Vaadin

### Preliminary Remarks

This small application shows password hashing by BCrypt within the framework of Spring Boot.<br>
The design and implementation owe to several tutorials and other sources as acknowledged<br>
in the references section below. In particular, if you are interested in building a similar<br>
application by yourself, I would suggest starting with Alejandro's introductory tutorial<br>
https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-, but you need<br>
to add Spring Security for BCrypt when generating a project template by Spring Boot Initializer.


### How to Run the Application

1. Run MySQL and create a new database called "simple_login".
1. Add a new table called "user" as follows:<br>
CREATE TABLE user(user_name VARCHAR(50) PRIMARY KEY, email VARCHAR(50) NOT NULL, password VARCHAR(255) NOT NULL);<br>
1. Edit the application.properties file in the resources directory of the source code by adding<br>
your MySQL credentials (and changing the default port if necessary).<br>
1. Build the source code with Maven and then run the project jar file generated.<br>
Alternatively, a simpler way is to just open the source code on IntelliJ and Click 'Run'.<br>
1. Once Tomcat starts to run, go to localhost:8080, and click 'New User?' to sign up.<br>
1. After registering the new user, sign in with the credentials.<br>

For sign-up,<br>

![sign_up](/md_images/sign_up.PNG)

For sign-in,<br>

![sign_in](/md_images/sign_in.PNG)

![main](/md_images/main.PNG)

If you do "select" on the MySQL shell console, you will see that the password is in fact encrypted.<br>

![password_hashing](/md_images/password_hashing.PNG)

### References

For the basic design, Alejandro Duarte's Vaadin tutorials:<br>
https://vaadin.com/blog/implementing-remember-me-with-vaadin<br>
https://vaadin.com/blog/building-a-web-ui-for-mysql-databases-in-plain-java-<br>

For more detailed introduction on Vaadin Binder, Kirill Bulatov's tutorial:<br>
https://vaadin.com/blog/15624687<br>

For Spring configuration of authentication,<br>
eparvan's post: https://stackoverflow.com/questions/42562893/could-not-autowire-no-beans-of-userdetailservice-type-found<br>
Roland Ewald's post: https://stackoverflow.com/questions/35912404/spring-boot-security-with-vaadin-login<br>
Eugen Paraschiv's tutorial: http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt<br>
Spring Documentation: https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html

 

  
  

   

   
   

           
               

          

