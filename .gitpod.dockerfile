FROM gitpod/workspace-full-vnc:latest

USER root
RUN dpkg --add-architecture i386 \
  && apt-get update \
  && apt-get install -y \
    cabextract \
    libxext6 \
    libxext6:i386 \
    libfreetype6 \
    libfreetype6:i386 \
    fakeroot \
&& apt-get clean && rm -rf /var/cache/apt/* && rm -rf /var/lib/apt/lists/* && rm -rf /tmp/*

USER gitpod
# activate java 11. It's already installed in the base image.
RUN  bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk install java"
