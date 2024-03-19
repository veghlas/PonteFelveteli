# Használj egy Java alapú képet
FROM openjdk:17

# Másoljuk az alkalmazás jar állományát a konténerbe
COPY target/myapp.jar /app/myapp.jar

# Másoljuk az adatbázis feltöltő szkriptet a konténerbe
COPY data_uploader.jar /app/data_uploader.jar

# Másoljuk a várakozási szkriptet a konténerbe
COPY wait-for-it.sh /app/wait-for-it.sh

# Futtassuk az adatbázis feltöltő szkriptet csak akkor, ha az adatbázis szolgáltatás elérhető
CMD ["./wait-for-it.sh", "db:5432", "--", "java", "-jar", "/app/data_uploader.jar"]