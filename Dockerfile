# Használj egy alap Spring Boot képet
FROM openjdk:17

# Másold be a projekt forrásfájljait a konténerbe
COPY . /app

# Állítsd be a munkakönyvtárat
WORKDIR /app

# Telepítsd a szükséges függőségeket és buildeld újra az alkalmazást
RUN ./mvnw clean package

# Másold be a generált futtatható JAR állományt a konténerbe
COPY target/pontefelveteli.jar /app/pontefelveteli.jar

# Indítsd el az alkalmazást
CMD ["java", "-jar", "target/pontefelveteli.jar"]