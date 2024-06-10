### build docker image
docker build . -t joheiss/sb3-accounts:v1

### show images
docker images

### inspect image <image-id>
docker inspect image 4236f8a634fd

### run docker container - detached mode
docker run -d -p 8091:8091 joheiss/sb3-accounts:v1

# run the same image on a differend local port
docker run -d -p 8096:8091 joheiss/sb3-accounts:v1

### show running containers
docker ps

### show docker logs <container-id>
docker logs f804a93108b1

### start existing docker container <container-id>
docker start f804a93108b1

### stop running container <container-id>
docker stop f804a93108b1

### build image using buildbacks
mvn spring-boot:build-image

### push docker image to docker hub
docker image push docker.io/joheiss/sb3-accounts:v1
