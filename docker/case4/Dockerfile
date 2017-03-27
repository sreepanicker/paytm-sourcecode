# Docker file for Case 4
# getting my base java 8 with centos image
FROM sreepanicker/java:8 
#Maintainer of the file 
MAINTAINER Sreejithu Panicker "sree.panicker@outlook.com"
#Creating the project folder
RUN mkdir /var/javaapp
#copying the war file
COPY paytmnoplogin.war /var/javaapp
#copying the jar file
COPY paytmserver-1.1.0.jar /var/javaapp
#Expose port
EXPOSE 80
#moving to work directory
WORKDIR /var/javaapp
#Runing the java application
CMD java -jar paytmserver-1.1.0.jar