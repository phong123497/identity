#get list port in running from docker
docker ps 
#change port 3306 -3307
docker run --name mysql8.0.39 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=12345 -d mysql:8.0.39-debian


# build file , run tai duong dan hien tai 
docker build -t identity-service:0.0.1 .
# check image da tao hay chua 
docker images
# khi kết nối từ một container khác  bên trong docker thì nó vẫn lắng nghe cổng mặc định 3306 (cổng mysql)
docker run --name identity-service --network dev-network -p 8080:8080 -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/identity-service -e SPRING_DATASOURCE_USERNAME=root -e SPRING_DATASOURCE_PASSWORD=12345 identity-service:0.0.1

