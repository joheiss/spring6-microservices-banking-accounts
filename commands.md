### build docker image
docker build . -t joheiss/sb3-accounts:v1

### show images
docker images

### inspect image <image-id>
docker inspect image 4236f8a634fd

### run docker container - detached mode
docker run -d -p 8091:8091 joheiss/sb3-accounts:v1

# run the same image on a different local port
docker run -d -p 8096:8091 joheiss/sb3-accounts:v1

### show running containers
docker ps

### show docker logs <container-id>
docker logs f804a93108b1

### start existing docker container <container-id>
docker start f804a93108b1

### stop running container <container-id>
docker stop f804a93108b1

### delete a docker container
docker rm <container-id>

### delete all stopped containers
docker container prune

### delete all (unused) images
docker image prune

### delete all unused docker stuff
docker system prune

### open a command shell in docker container
docker exec -t <container-id> sh

### build image using buildbacks
mvn spring-boot:build-image

### push docker image to docker hub
docker image push docker.io/joheiss/sb3-accounts:v1

### start all microservices via docker compose
docker compose up -d

### stop all containers - and delete them
docker compose down

### stop containers - without deleting them
docker compose stop

### start existing containers
docker compose start

### run RabbitMQ
# latest RabbitMQ 3.13
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13-management

### start mysql container for accountsdb
docker run -p 3306:3306 --name accountsdb -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=accountsdb -d mysql