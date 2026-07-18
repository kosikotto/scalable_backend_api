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

🔐 Keycloak Konfiguráció és Hitelesítés (RBAC)

Az alkalmazás végpontjai (például a POST /add/{product}) szerepkör-alapú jogosultságkezeléssel (RBAC) vannak védve. Ahhoz, hogy a Swagger UI-ból sikeresen tesztelni tudd a végpontokat, a Keycloak admin felületén az alábbi egyszeri beállításokat kell elvégezni.

1. Realm létrehozása

    A backend kód a "webshop" nevű realm-et várja a tokenek hitelesítéséhez.

        Lépj be a Keycloak Admin Console-ba (http://localhost:8080, alapértelmezett belépés: admin / admin).

        A bal felső sarokban kattints a "Keycloak" (vagy master) legördülő menüre.

        Kattints a "Create Realm" gombra.

        Realm name: Írd be, hogy "webshop", majd kattints a Create gombra.

2. Szerepkör (Realm Role) létrehozása

    A rendszer a "postput" szerepkört (Role) vizsgálja az adatmódosító végpontoknál.

        A bal oldali menüben válaszd a "Realm roles" menüpontot.

        Kattints a "Create role" gombra.

        Role name: "postput"

        Kattints a "Save" gombra.

3. Kliens (Client) beállítása a Swaggerhez

   Ahhoz, hogy a Swagger UI tokent tudjon kérni a Keycloaktól, regisztrálnunk kell egy klienst, és engedélyeznünk kell a megfelelő hitelesítési folyamatokat.

        A bal oldali menüben: "Clients" -> "Create client".

        Client ID: "webshop-api" (Ezt kell majd a Swagger login felületén megadni).

        Client authentication: Maradjon kikapcsolva (Public kliensként használjuk).

        Kattints a "Next"-re.

        Valid redirect URIs: Ezt nagyon pontosan kell megadni! Írd be: http://localhost:8081/swagger-ui/* és http://localhost:8081/*

        Web origins: + (vagy http://localhost:8081).

        Kattints a "Save" gombra.

        FONTOS (Direct Access engedélyezése): A sikeres mentés után görgess le a kliens beállításainál a "Capability config" részhez. Keresd meg a "Direct access grants" kapcsolót, és kapcsold BE (a "Standard flow" is maradjon bekapcsolva). Ez teszi lehetővé, hogy a Swagger API-n keresztül jelentkezzen be. Mentsd el a módosításokat!

5. Felhasználók létrehozása és véglegesítése

    A teszteléshez két felhasználót hozunk létre: egy adminisztrátort (aki tud terméket hozzáadni) és egy sima usert (akinek a rendszer megtagadja a hozzáférést).

    ⚠️ Figyelem: A rendszer megköveteli a felhasználói profilok hiánytalan kitöltését, különben "Account is not fully set up" hibát kapunk a Swaggerben!

    A) Jogosult felhasználó (Admin) létrehozása:

       Bal oldali menü: Users -> Add user.
       Username: admin_user
       Email: admin@teszt.hu (Dummy adat kötelező)
       First name: Admin (Dummy adat kötelező)
       Last name: Teszt (Dummy adat kötelező)
       Kattints a "Create" gombra.
    
    A létrehozott felhasználó adatlapján menj a "Credentials" fülre, és kattints a "Set password" gombra.

    Adj meg egy jelszót (pl. 1234), a "Temporary" kapcsolót pedig kapcsold KI!

    Kattints a "Save"-re.

    Menj a "Role mapping" fülre, kattints az "Assign role" gombra, válaszd ki a "postput" szerepkört, és kattints az "Assign"-ra.

    B) Sima felhasználó (Jogosultság nélküli) létrehozása:

        Bal oldali menü: Users -> Add user.

        Username: sima_user

        Töltsd ki a kötelező adatokat (Email, First name, Last name dummy adatokkal).

        Kattints a "Create" gombra.

    "Credentials" fülön adj neki jelszót (pl. 1234, Temporary: KI).

    ❌ Ennek a felhasználónak NE adj semmilyen extra Role-t!

***

🧪 Hitelesítés és Jogosultságok Tesztelése (Swagger UI)

Most, hogy a Keycloak fel van készítve, tesztelhetjük az API működését és a biztonsági korlátozásokat.

1. Nyisd meg a Swaggert: http://localhost:8081/swagger-ui.html
2. Kattints a felső, zöld "Authorize" gombra.
3. A "client_id" mezőbe írd be: webshop-api
4. Teszt 1: Sikeres hozzáférés (Admin)
5. Jelentkezz be az admin_user / 1234 adatokkal.
6. Keresd meg a "POST /add/{product}" végpontot (pl. próbáld meg hozzáadni: szappan).
7. Eredmény: A rendszer be fog engedni, a válaszkód 200 OK.
8. Teszt 2: Megtagadott hozzáférés (Sima User)
9. A Swaggerben kattints a "Logout" gombra, majd az "Authorize"-ra újra.
10. Most jelentkezz be a sima_user / 1234 adatokkal.
11. Próbáld meg ismét meghívni a "POST /add/{product}" végpontot.
12. Eredmény: Mivel a sima user nem rendelkezik a postput szerepkörrel, a Spring Security helyesen blokkolja a kérést, a válaszkód 403 Forbidden lesz! Ezzel bizonyítva, hogy a végpontok védelme tökéletesen működik.

***

🌐 Elérhetőségek és Portok
A sikeres indítást követően a rendszer egyes komponensei az alábbi helyi címeken és portokon válnak elérhetővé:

1. 🚀 Backend REST API & Swagger UI: http://localhost:8081/swagger-ui.html (Port: 8081)
2. 🔐 Keycloak Admin Console: http://localhost:8080 (Port: 8080)
3. 🐘 PostgreSQL Adatbázis: localhost:5432 (Felhasználó: postgres, Jelszó: SAmsung20020725)
4. 🛑 Redis Cache szerver: localhost:6379 (Belső hálózati elérés a backend számára)
