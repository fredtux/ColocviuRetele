FROM ubuntu:20.04

RUN apt-get update && apt-get install dnsutils iputils-ping iproute2 -y
COPY ./dummy/resolv.conf /etc/resolv.conf