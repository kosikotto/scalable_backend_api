🛒 Skálázható E-Commerce Backend API

Ez az alkalmazás egy modern, mikroszerviz-szemléletű, konténerizált REST API backend, amely egy e-commerce (webshop) platform kiszolgálására készült. A projekt fókusza a magas fokú skálázhatóság, a robusztus biztonsági architektúra és a nagy teljesítményű adatkezelés optimalizációja volt.

***

🏗️ Architektúra és Technológiai Stack

A rendszer tervezésekor a vállalati szintű biztonság és a minimális válaszidő (low latency) voltak az elsődleges szempontok:

1. Backend: Java alapú REST API, szigorú bemeneti adatvalidációval és tisztán karbantartható kódbázissal (Lombok, Spring Boot).
2. Biztonság: Keycloak alapú központosított hitelesítés és szerepkör-alapú jogosultságkezelés (RBAC - Role-Based Access Control), amely szigorúan izolálja a publikus végpontokat a védett, specifikus jogosultságot igénylő erőforrásoktól.
3. Adatkezelés: PostgreSQL relációs adatbázis a tranzakcionális és perzisztens adatok biztonságos tárolására.
4. Optimalizálás & Cache: Redis memóriában működő gyorsítótár-réteg. A gyakran lekérdezett erőforrásokat (pl. terméklisták) a rendszer közvetlenül a memóriából szolgálja ki, drasztikusan csökkentve a PostgreSQL adatbázis terhelését (Disk I/O) és biztosítva az intelligens cache-szinkronizációt.
5. Dokumentáció: OpenAPI (Swagger) integráció a végpontok automatizált dokumentálására és interaktív tesztelésére.
6. Infrastruktúra: Teljesen konténerizált Docker ökoszisztéma, ahol a szolgáltatások izolált, dedikált belső bridge hálózaton kommunikálnak egymással.

***

📂 Mappastruktúra

A projekt felépítése és a komponensek elhelyezkedése a következő elrendezést követi:

```text
📁 [Gyökérkönyvtár]
│
├── 📁 Backend
│   └── 📁 demo
│       └── 📁 demo
│           ├── 📁 src               # A Java backend forráskódja
│           ├── 📄 Dockerfile        # A backend image felépítéséhez
│           ├── 📄 pom.xml           # Maven függőségek
│           └── 📄 mvnw              # Maven wrapper
│
└── 📁 Compose
    ├── 📄 .env                  # Környezeti változók (jelszavak, portok)
    └── 📄 docker-compose.yaml   # A teljes infrastruktúra konfigurációja
```


***

🛠️ Telepítés és Inicializálás

A projekt futtatásához Docker Desktop megléte szükséges. A szolgáltatások helyes inicializálásához és az image-ek megfelelő felépítéséhez kövesd az alábbi lépéseket a megadott sorrendben.

1. Környezeti változók beállítása (Opcionális)

    A Compose mappában található docker-compose.yml fájl környezeti változókat használ a biztonság érdekében (pl. ${DB_PASSWORD}, ${KEYCLOAK_ADMIN}).

    Győződj meg róla, hogy a Compose mappádban létezik egy .env fájl, vagy a rendszeredben exportálva vannak az alábbi adatok:

       DB_PASSWORD=SAmsung20020725
       KEYCLOAK_ADMIN=admin
       KEYCLOAK_ADMIN_PASSWORD=admin
       CACHE_HOST=cache_db
       CACHE_PORT=6379

2. A Backend Docker Image felépítése (Build)

   Mielőtt elindítanánk a teljes rendszert, össze kell készítenünk és le kell gyártanunk a Java alkalmazásunk Docker image-ét.

   Nyiss egy terminált (vagy CMD-t), és lépj be a backend projekt mappájába.

   Építsd fel az image-et a Dockerfile alapján (a folyamat automatikusan elvégzi a Maven buildet és a Spring Boot layertools extrakciót a hatékony cache-elés érdekében):

        docker build -t webshop-backend-java .

3. Az Ökoszisztéma elindítása a Docker Compose segítségével

    Miután a webshop-backend-java image elkészült, a Docker Compose képes lesz az összes szolgáltatást egyszerre elindítani a megfelelő függőségi sorrendben (depends_on).

    Lépj át a Compose mappába.

    Indítsd el a teljes konténer-architektúrát a háttérben:

        docker compose up -d

4. Adatbázis ellenőrzése és sémák (Automatikus ORM)

    Mivel a backend alkalmazás Spring Data ORM (Hibernate) réteget használ, az adatbázis tábláinak (pl. webshopproducts és a hozzá tartozó id, product_name mezők) létrehozása, valamint a Keycloak séma inicializálása az első sikeres induláskor automatikusan megtörténik.

    Ha manuálisan szeretnél belépni a PostgreSQL adatbázisba ellenőrzés céljából:

    # Belépés a futó PostgreSQL konténerbe

        docker exec -it postgres bash

    # Csatlakozás a psql klienssel

        psql -h localhost -U postgres

    # Adatbázisok listázása

        \l

    # Csatlakozás a kívánt adatbázishoz

        \c postgres

***

🌐 Elérhetőségek és Portok
A sikeres indítást követően a rendszer egyes komponensei az alábbi helyi címeken és portokon válnak elérhetővé:

1. 🚀 Backend REST API & Swagger UI: http://localhost:8081/swagger-ui.html (Port: 8081)
2. 🔐 Keycloak Admin Console: http://localhost:8080 (Port: 8080)
3. 🐘 PostgreSQL Adatbázis: localhost:5432 (Felhasználó: postgres, Jelszó: SAmsung20020725)
4. 🛑 Redis Cache szerver: localhost:6379 (Belső hálózati elérés a backend számára)
