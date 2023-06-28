FROM ubuntu:22.04

RUN apt-get update && apt-get install dnsutils iputils-ping iproute2 libc6-x32 libc6-i386 wget -y
COPY ./dummy/resolv.conf /etc/resolv.conf

WORKDIR /app

# Sursa de inspiratie: https://linuxhint.com/install-java-ubuntu-22-04/
RUN wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.deb
RUN apt install ./jdk-17_linux-x64_bin.deb -y
RUN ln -s /usr/lib/jvm/jdk-17/bin/* /usr/bin
RUN rm jdk-17_linux-x64_bin.deb