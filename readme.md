# **Crypto Wallet Management Application**
### **Prerequisites**
- **Java 17**
- **Apache Maven**
- **Docker**
### **Instructions**
#### Clone the repository
``` bash
git clone https://github.com/your-repository/crypto-wallet-management.git
cd crypto-wallet-management
```
#### Build the project
``` bash
mvn clean install
```
#### Start the application
runs on a docker container on ports 8080 for the application and 3306 for mysql
``` bash
docker-compose up --build
```

The application will start on `http://localhost:8080` , Once the application is running, visit the link in the browser:
``` 
   http://localhost:8080/swagger-ui/index.html
```
