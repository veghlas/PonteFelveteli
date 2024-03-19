# Használj egy Java alapú képet
FROM openjdk:17

# Másoljuk az alkalmazás jar állományát a konténerbe
COPY target/myapp.jar /app/myapp.jar

# Másoljuk az adatbázis feltöltő szkriptet a konténerbe
COPY data_uploader.jar /app/data_uploader.jar

# Futtassuk az adatbázis feltöltő szkriptet a konténer indításakor
CMD ["java", "-jar", "/app/myapp.jar"]