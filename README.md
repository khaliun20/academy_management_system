# Academy Management System

## Overview 

This is an academic enrollment and attendance recording system built over two months with the help of three other teammates of mine. We went through three different evoliutions of the project each time adding new features and capabilities. 

The goal of the project was to learn how to design and implement software that is extendable, scalable, and maintainable, hence the three evolutions! 

The project is made up of three independent modules (admin, server, client) though for the purposes of consolidation, we have chosen to have them live on the same repository. 

  1. Firstly, the admin module with GUI is built for the schools admins for the purposes of adding new student/teachers to the academy and managing student enrollment to courses. 

  2. The client module with GUI is used by either teacher or students. Some available features include emailing attendance reports, viewing attendance reports and taking attendance, changing password and username. 

  3. The last module is the server module which was built modeling the MVC architecture. Clients connect to the server using TCP sockets communicate and indirectly interact with the database. For security purposes, the client sends request to update or view the database and the server processes requests by connecting with the database.  

Tech Used: Java, JDBC, TCP server/client sockets, JavaFX, MySQL, Gradle, Clover, CI/CD, VMs for doployment

## Contributions

This project was done with my three other teammates. I took charge of the following! 

1. Design and implement the client/server architecture
2. Design and implement the client/server communication protocol 
3. Model and View of the server side MVC architecture
4. I also took part in the overall design processes for our models! 
