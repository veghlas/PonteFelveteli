# PonteFelveteli

Ez a projekt egy példa Spring Boot alkalmazásra, amelyben lehetőség van elmenteni kapcsolattartók adatait. Mint például név, cím adatok, TAJ szám, adószám, telefonszámok. Admin jogosultsággal érhető el egy kapcsolattartó hozzáadása név és jelszó párossal, illetve a kapcsolattartók listázása.
Az adatok frissítését illetve a saját user törlését bármely felhasználó elérheti. A jogosoultságok kezelését MethodSecurty-vel valósítottam meg. 

//JAVA JDK 17 / SPRING BOOT 3.2.3//

Sping security-t használok az applikációban kiegészítve JWT tokennel. Itt kapunk egy ACCESS és egy REFRESH token-t, amelyet a kliens tárol. 
A jelszavak BCryptPasswordEncoder-rel vannak hashelve.

2 fő domain találjható az AppUser és a hozzátartozó Address. A telefonszámok CollectionTable-ben vannak tárolva. Minden jogosultsághoz egy ENUM érték tartozik.

A DTO osztályokban valósítottam meg az input adatok validációját. 

A programban saját Exception osztályokat is hoztam létre.

Az Postgres adatbázis verziókövetésére a Luiqibase-t implementáltam.


A Java17 / Spring Boot 3.2.3használatával készült az applikáció.

Telepítés és Indítás
1. Először is, telepítened kell a Docker alkalmazást a gépedre. Ehhez keresd fel a Docker hivatalos weboldalát és kövesd az ott található telepítési útmutatót az operációs rendszerednek megfelelően.

2. Miután sikeresen telepítetted a Docker-t, töltsd le a projektet a következő parancs segítségével:

   git clone https://github.com/veghlas/PonteFelveteli.git

3. Most navigálj a letöltött projekt mappájába:

    cd projekt

4. Most futtasd a következő parancsot a Docker indításához:

   docker-compose up --build -d
   
Ez a parancs elindítja a Docker konténert a projekt számára, és futtatja az alkalmazást.

6. Ha az alkalmazás sikeresen elindult, nyisd meg a postmant, és és már indíthatod is az API hívásokat.

A végpontok dokumentációja az alábbi linken elérhető, a program futása közben (SWAGGER implementáció):

   localhost:8080/swagger-ui.html

A programban található integritás és egység teszt is egyaránt.

