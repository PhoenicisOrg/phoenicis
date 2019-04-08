FROM gitpod/workspace-full-vnc:latest

USER gitpod

# activate java 11. It's already installed in the base image.
RUN  bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && sdk default java 11.0.2-open"