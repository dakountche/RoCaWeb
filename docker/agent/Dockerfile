# Start with Ubuntu base image
FROM ubuntu:latest
LABEL maintainer Djibrilla AMADOU KOUNTCHE <djibrailla.amadoukountche@gmail.com>

# Install the packets for apache and also for building Torch7
RUN apt-get update && apt-get install -y \
    apache2 \
    build-essential \
    git \
    libapache2-mod-security2 \
    libpcre3 \
    libpcre3-dev \
    libssl-dev \
    libzmq3-dev \
    python-dev \
    python-pip \
    python-zmq \
    sudo \ 
    software-properties-common 
 
  
# Run Torch7 installation scripts
RUN git clone https://github.com/torch/distro.git /opt/torch --recursive && cd /opt/torch && \
  bash install-deps && \
  ./install.sh

# Install other dependencies for Lua
RUN /opt/torch/install/bin/luarocks install lrexlib-pcre PCRE_LIBDIR=/lib/x86_64-linux-gnu && \
  /opt/torch/install/bin/luarocks install luasocket-unix

# Install OSBF
COPY ./lua/osbf-lua-2.0.4 /opt/osbf-lua-2.0.4
RUN cd /opt/osbf-lua-2.0.4 && make && make install

# Install moonfilter
ADD ./lua/moonfilter/*.lua /opt/torch/install/share/lua/5.1/

# Create a symbolick link  lua to th
RUN update-alternatives --install /usr/bin/lua lua-interpreter /opt/torch/install/bin/th 90

# Configure apache and modSecurity
RUN a2enmod rewrite && \
    a2enmod proxy && \
    a2enmod proxy_http && \
    a2enmod proxy_html && \
    a2enmod headers && \
    a2enmod xml2enc && \
    a2enmod lua

# Configure mod_status
COPY ./server-status/server-status.lua /opt

# Export environment variables manually
ENV LUA_PATH='/root/.luarocks/share/lua/5.1/?.lua;/root/.luarocks/share/lua/5.1/?/init.lua;/opt/torch/install/share/lua/5.1/?.lua;/opt/torch/install/share/lua/5.1/?/init.lua;./?.lua;/opt/torch/install/share/luajit-2.1.0-beta1/?.lua;/usr/local/share/lua/5.1/?.lua;/usr/local/share/lua/5.1/?/init.lua'
ENV LUA_CPATH='/root/.luarocks/lib/lua/5.1/?.so;/opt/torch/install/lib/lua/5.1/?.so;./?.so;/usr/local/lib/lua/5.1/?.so;/usr/local/lib/lua/5.1/loadall.so'
ENV PATH=/opt/torch/install/bin:$PATH
ENV LD_LIBRARY_PATH=/opt/torch/install/lib:$LD_LIBRARY_PATH
ENV DYLD_LIBRARY_PATH=/opt/torch/install/lib:$DYLD_LIBRARY_PATH
ENV LUA_CPATH='/opt/torch/install/lib/?.so;'$LUA_CPATH

# Set ~/torch as working directory
WORKDIR /opt/torch