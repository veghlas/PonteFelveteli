# Használj egy alap Spring Boot képet
FROM openjdk:17

# Másold be a projekt forrásfájljait a konténerbe
COPY . /app

# Állítsd be a munkakönyvtárat
WORKDIR /app

# Telepítsd a szükséges függőségeket és buildeld újra az alkalmazást
RUN ./mvnw clean package

# Másold be a generált futtatható JAR állományt a konténerbe
COPY target/backend-0.0.1-SNAPSHOT.jar /app/backend.jar


# Indítsd el az alkalmazást
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]