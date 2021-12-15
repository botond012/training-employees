#FROM  adoptopenjdk:11-jre-hotspot
#WORKDIR /opt/app
#COPY target/employees-0.0.1-SNAPSHOT.jar employees.jar
#CMD ["java","-jar","employees.jar"]


FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR application
COPY target/employees-0.0.1-SNAPSHOT.jar employees.jar
RUN java -Djarmode=layertools -jar employees.jar extract

FROM  adoptopenjdk:11-jre-hotspot
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]

