# Usa una imagen base de Ubuntu
FROM ubuntu:22.04
WORKDIR /app

# Actualiza el índice de paquetes e instala dependencias necesarias
RUN apt-get update \
    && apt-get install -y \
       wget \
       gnupg \
       openssh-server \
       coreutils \
       libgcc-s1 \
       libc6 \
       && adduser --disabled-password --gecos "" marsol \
       && mkdir /var/run/sshd \
       && echo "marsol:123456" | chpasswd

# Instala OpenJDK 17
RUN wget https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_linux-x64_bin.tar.gz \
    && tar -xvf openjdk-17.* \
    && mv jdk-17.0.2 /usr/local/openjdk-17



# Configura variables de entorno para Java
ENV JAVA_HOME=/usr/local/openjdk-17
ENV PATH=$JAVA_HOME/bin:$PATH

# Crea directorios necesarios
RUN mkdir ./pendings

# Expone puertos
EXPOSE 22 7001 80

COPY target/Sync-1.0-SNAPSHOT.jar /app/Sync-1.0.jar
COPY target/classes /app/classes/
COPY ./application-linux.properties /app/application.properties
# Copia la biblioteca nativa al contenedor
COPY ./src/main/resources/libSyncSDK.so /usr/lib/libSyncSDK.so

RUN rm openjdk-17.0.2_linux-x64_bin.tar.gz

CMD tail -f /dev/null
#CMD ["sh","-c","sshd -D  & java -jar /app/Sync-1.0.jar"]