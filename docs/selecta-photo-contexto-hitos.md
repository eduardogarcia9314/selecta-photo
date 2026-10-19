# Selecta Photo — Contexto técnico y bitácora del proyecto

**Proyecto:** Selecta Photo  
**Negocio:** Selecta Eventos  
**Tipo de solución:** Monolito modular con JHipster  
**Estado del documento:** Contexto de continuidad para otra IA/desarrollador  
**Última actualización:** 2026-10-19

---

## 1. Objetivo del producto

Selecta Photo es el primer módulo tecnológico de Selecta Eventos.

El MVP debe permitir que:

1. El fotógrafo/administrador cree un álbum.
2. Suba fotografías de forma individual o masiva.
3. El sistema conserve el archivo original.
4. El sistema genere una versión de vista previa con marca de agua.
5. El sistema genere thumbnails livianos para la galería.
6. El sistema entregue un código de acceso al cliente.
7. El cliente acceda al álbum sin una cuenta tradicional, usando el código.
8. El cliente seleccione sus fotografías favoritas.
9. El cliente pueda agregar comentarios sobre fotografías.
10. El fotógrafo vea la selección, comentarios y estado del álbum.
11. El fotógrafo pueda eliminar fotografías cargadas por error.
12. Más adelante, Selecta Photo debe poder integrarse dentro de una infraestructura/ERP mayor de Selecta Eventos.

---

## 2. Principios técnicos acordados

- Se usa **Java + Spring Boot + JHipster**.
- Arquitectura: **monolito modular**, no microservicios.
- Frontend actual: **Angular generado por JHipster**.
- Backend: Spring MVC.
- Autenticación administrativa: JWT de JHipster.
- El cliente tendrá acceso mínimo mediante código de álbum.
- La base de datos **NO almacena las imágenes como BLOB**.
- MariaDB/MySQL almacena solamente metadatos de las fotografías.
- Los archivos se manejan mediante una abstracción `PhotoStorageService`.
- Primera implementación: almacenamiento local.
- Futuro: S3, MinIO u otro object storage sin reescribir la lógica del negocio.
- Deben existir:
  - original,
  - preview,
  - thumbnail.
- Las previews destinadas al cliente deben llevar **marca de agua de Selecta Eventos**.
- El original nunca debe modificarse.
- Ritmo de trabajo acordado: **20 % diseño / 80 % producto funcionando**.
- Evitar sobrearquitectura antes de tener el MVP operativo.

---

## 3. Entorno local

Sistema operativo:

- Windows 10

Herramientas verificadas:

- Java: Oracle JDK 21.0.7
- Node.js: 24.19.0
- Git: 2.36.1.windows.1
- Maven del sistema: 3.9.9
- Maven Wrapper del proyecto: 3.9.16
- IDE: IntelliJ IDEA
- XAMPP instalado
- Base de datos proporcionada por XAMPP: MariaDB
- Usuario de BD local: `root`
- Password local: vacío

Ruta del proyecto:

```text
C:\Users\Eliza\proyectos\selecta-photo
```

Base de datos:

```text
selecta_photo
```

---

## 4. Generación de JHipster

Proyecto generado con:

```text
generator-jhipster 9.2.0
```

Configuración elegida:

- Base name: `selectaPhoto`
- Application type: Monolithic application
- Package: `com.selecta.photo`
- Build tool: Maven
- Spring MVC, no WebFlux
- Authentication: JWT
- Database type: SQL
- Production DB: MySQL
- Development DB: MySQL
- Cache: Caffeine
- Hibernate second-level cache: sí
- Frontend: Angular
- Frontend testing: Cypress
- Admin UI: sí
- i18n: no
- Native language seleccionada durante generación: Spanish
- Sin Kafka
- Sin Pulsar
- Sin Elasticsearch
- Sin WebSockets
- Sin OpenAPI Generator

Durante la generación apareció un warning porque Windows no encontraba `keytool` en PATH. No bloqueó el proyecto.

---

## 5. Configuración de base de datos

Base creada:

```sql
CREATE DATABASE selecta_photo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

`application-dev.yml` fue ajustado para apuntar a:

```yaml
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:mysql://localhost:3306/selecta_photo?useUnicode=true&characterEncoding=utf8&useSSL=false&useLegacyDatetimeCode=false&createDatabaseIfNotExist=true
    username: root
    password:
```

Aunque JHipster está configurado con driver MySQL y XAMPP ejecuta MariaDB, el proyecto ha arrancado correctamente y no se decidió detener el MVP por esto.

---

## 6. Docker Compose deshabilitado en desarrollo

El primer arranque falló porque Spring Boot intentó iniciar Docker Compose y Docker no está instalado/configurado.

Error original:

```text
DockerProcessStartException
Cannot run program "docker": CreateProcess error=2
```

En `application-dev.yml`, dentro de `spring:`, se agregó:

```yaml
docker:
  compose:
    enabled: false
```

Después de esto la aplicación pudo arrancar correctamente.

---

## 7. Primer arranque confirmado

Comando:

```bash
mvnw spring-boot:run
```

Estado confirmado:

```text
Application 'selectaPhoto' is running!
```

Aplicación:

```text
http://localhost:8080
```

---

## 8. Módulo Album implementado

Estructura actual aproximada:

```text
src/main/java/com/selecta/photo/album/
├── domain/
│   └── Album.java
├── repository/
│   └── AlbumRepository.java
├── service/
│   └── AlbumService.java
└── web/
    ├── AlbumResource.java
    └── PublicAlbumResource.java
```

### 8.1 Entidad Album

Campos actuales:

```text
id
name
accessCode
createdAt
```

La entidad utiliza:

```java
@Entity
@Table(name = "album")
```

`createdAt` fue agregado como:

```java
@Column(name = "created_at", nullable = false)
private Instant createdAt = Instant.now();
```

### 8.2 AlbumRepository

Extiende:

```java
JpaRepository<Album, Long>
```

Incluye:

```java
Optional<Album> findByAccessCode(String accessCode);
```

Hubo inicialmente un error porque el archivo se llamó:

```text
albumRepository.java
```

y la interfaz pública:

```text
AlbumRepository
```

Se corrigió el nombre del archivo a:

```text
AlbumRepository.java
```

Después compiló correctamente.

---

## 9. Configuración de escaneo de repositorios

Spring inicialmente no encontró `AlbumRepository`.

Error:

```text
Parameter 0 of constructor in AlbumService required a bean of type
'com.selecta.photo.album.repository.AlbumRepository' that could not be found.
```

La configuración de JPA estaba limitada al paquete original de JHipster.

Se amplió `@EnableJpaRepositories` para cubrir:

```java
com.selecta.photo
```

Después de ese cambio Spring detectó `AlbumRepository`.

---

## 10. AlbumService

Se implementó un servicio que:

- crea álbumes;
- genera un código de acceso;
- guarda mediante `AlbumRepository`;
- permite buscar por código.

Código de acceso actual:

- UUID
- sin guiones
- primeros 8 caracteres
- mayúsculas

Ejemplo generado:

```text
23B11334
```

---

## 11. Liquibase — tabla Album

Se creó changelog para la tabla:

```text
album
```

Campos:

```text
id BIGINT
name VARCHAR(255)
access_code VARCHAR(50) UNIQUE
created_at TIMESTAMP
```

El changelog fue incluido en:

```text
src/main/resources/config/liquibase/master.xml
```

Liquibase ejecutó correctamente y la aplicación arrancó.

---

## 12. Endpoint administrativo para crear álbum

Endpoint:

```http
POST /api/albums
```

Protegido por JWT.

Body:

```json
{
  "name": "Quince años Valentina"
}
```

Para obtener token:

```http
POST /api/authenticate
```

con:

```json
{
  "username": "admin",
  "password": "admin",
  "rememberMe": true
}
```

El `id_token` debe enviarse como Bearer Token.

Primer álbum creado correctamente:

```json
{
  "name": "Quince años Valentina",
  "accessCode": "23B11334",
  "createdAt": "2026-09-16T05:03:01.157760900Z",
  "id": 1
}
```

Este fue el primer registro real creado por la API de Selecta Photo.

---

## 13. Endpoint público de álbum

Se creó:

```text
PublicAlbumResource.java
```

Endpoint:

```http
GET /api/public/albums/{accessCode}
```

Ejemplo:

```http
GET /api/public/albums/23B11334
```

El endpoint devuelve el álbum si existe y `404` si el código no existe.

En `SecurityConfiguration` se permitió:

```java
.requestMatchers("/api/public/**").permitAll()
```

antes de:

```java
.requestMatchers("/api/**").authenticated()
```

Probado correctamente sin JWT.

---

## 14. Flujo ya funcional

Actualmente funciona:

```text
Admin
  ↓
JWT
  ↓
POST /api/albums
  ↓
AlbumService
  ↓
AlbumRepository
  ↓
MariaDB
```

y:

```text
Cliente
  ↓
Código de acceso
  ↓
GET /api/public/albums/{accessCode}
  ↓
Álbum encontrado
```

Por tanto:

- Crear álbum: ✅
- Persistir álbum: ✅
- Generar código: ✅
- Acceder públicamente por código: ✅

---

## 15. Diseño acordado para fotografías

Las fotografías **no deben almacenarse en MariaDB**.

MariaDB guardará metadatos como:

```text
Photo
--------------------------------
id
album_id
original_name
storage_key
preview_key
thumbnail_key
content_type
file_size
uploaded_at
```

Archivos físicos:

```text
selecta-storage/
└── albums/
    └── {albumId}/
        ├── originals/
        ├── previews/
        └── thumbnails/
```

### Original

- Archivo subido por el fotógrafo.
- No se modifica.
- No debe exponerse directamente al cliente.

### Preview

- Resolución suficiente para revisar cómodamente.
- Más liviana que el original.
- Debe incluir **marca de agua Selecta Eventos**.
- Es la imagen grande que verá el cliente.

### Thumbnail

- Imagen pequeña y liviana.
- Utilizada para renderizar rápidamente galerías con 100–500 fotos.
- No necesita calidad de impresión.

---

## 16. Abstracción de almacenamiento acordada

Se implementará aproximadamente:

```java
public interface PhotoStorageService {

    StoredPhoto store(...);

    void delete(...);
}
```

Implementación inicial:

```text
LocalPhotoStorageService
```

Destino local inicial:

```text
C:\selecta-storage\
```

Diseño futuro:

```text
PhotoStorageService
        │
        ├── LocalPhotoStorageService
        ├── S3PhotoStorageService
        └── MinioPhotoStorageService
```

La lógica de negocio no debe conocer detalles del proveedor de almacenamiento.

---

## 17. Carga individual y masiva

Decisión tomada:

**No crear dos motores distintos.**

Debe existir una única lógica de carga:

```text
uploadOne(photo)
```

La carga masiva simplemente invocará el mismo servicio para cada archivo:

```text
uploadMany(files)
  ├── uploadOne(file1)
  ├── uploadOne(file2)
  ├── uploadOne(file3)
  └── ...
```

Endpoints previstos:

```http
POST /api/albums/{albumId}/photos
```

para una fotografía, y:

```http
POST /api/albums/{albumId}/photos/batch
```

para varias.

También es válido evolucionar hacia un endpoint multipart que acepte `List<MultipartFile>` y permita 1..N archivos.

---

## 18. Marca de agua

Requisito confirmado por el usuario.

Reglas:

- El original permanece intacto.
- La preview contiene marca de agua.
- El thumbnail puede derivarse de la preview o generarse aparte.
- Debe ser posible cambiar en el futuro el diseño de la marca.
- No incrustar lógica de marca de agua en el controlador REST.
- Debe pertenecer al servicio/proceso de imágenes.

Ejemplo conceptual:

```text
Original
   │
   ├──> Preview redimensionada + SELECTA EVENTOS
   │
   └──> Thumbnail
```

---

## 19. Dashboard del fotógrafo acordado

El dashboard administrativo deberá permitir como mínimo:

- crear álbum;
- subir una fotografía;
- subir muchas fotografías;
- ver fotografías;
- eliminar fotografías cargadas por error;
- definir fotografía de portada;
- ver código de acceso;
- cambiar/regenerar código;
- activar/desactivar el álbum;
- definir máximo de fotos seleccionables;
- ver cuántas lleva seleccionadas el cliente;
- ver selección del cliente;
- ver comentarios por fotografía;
- saber si el cliente terminó;
- ordenar fotografías;
- obtener/listar únicamente fotografías seleccionadas;
- posteriormente descargar originales seleccionados.

Estados propuestos para álbum:

```text
DRAFT
OPEN
COMPLETED
ARCHIVED
```

---

## 20. Dashboard del cliente acordado

Debe ser deliberadamente simple.

Flujo:

```text
Código del álbum
        ↓
Galería
        ↓
Ver preview
        ↓
Marcar/desmarcar favorita
        ↓
Comentario opcional
        ↓
Ver contador de selección
        ↓
Finalizar selección
```

Ejemplo:

```text
Quince años Valentina

12 / 30 seleccionadas

[foto] ♡
[foto] ♥
[foto] ♡

[Finalizar selección]
```

---

## 21. Entidades futuras previstas

Además de `Album` y `Photo`, se prevén:

### PhotoSelection

Para representar la selección del cliente.

Campos aproximados:

```text
id
photo_id
selected
selected_at
```

### PhotoComment

Para comentarios.

Campos aproximados:

```text
id
photo_id
comment
created_at
```

No implementar todo de golpe. Prioridad inmediata: carga y visualización de imágenes.

---

## 22. Git — disciplina acordada desde este punto

El usuario solicitó comenzar formalmente a registrar los avances en Git.

Antes de continuar se debe ejecutar:

```bash
git status
git log --oneline --decorate -10
git remote -v
```

Esto permitirá verificar si JHipster creó el commit inicial y si existe un remoto.

Los cambios actuales realizados después de la generación deben agruparse en un commit de hito, por ejemplo:

```bash
git add .
git commit -m "feat(album): create album access flow"
```

Después, cada bloque funcional debe tener su propio commit.

Commits propuestos:

```text
feat(album): create album access flow
feat(photo): add photo persistence model
feat(storage): add local photo storage
feat(photo): support single and batch uploads
feat(photo): generate previews thumbnails and watermark
feat(client): add album gallery
feat(selection): add client photo selection
feat(comment): add photo comments
```

No mezclar en un único commit meses de cambios si se puede evitar.

---

## 23. Próximo hito inmediato

Implementar fotografías.

Orden recomendado:

```text
1. Crear Photo
2. Crear changelog Liquibase
3. Crear PhotoRepository
4. Crear PhotoStorageService
5. Crear LocalPhotoStorageService
6. Crear procesador de preview/thumbnail/marca de agua
7. Crear PhotoService
8. Crear carga individual
9. Crear carga masiva usando la misma lógica
10. Probar con Postman
11. Crear endpoints para servir thumbnail/preview
12. Commit
```

Meta del siguiente hito:

```text
Admin crea álbum
      ↓
Sube 1 o N fotografías
      ↓
Se guarda original
      ↓
Se genera preview con marca de agua
      ↓
Se genera thumbnail
      ↓
Se registra Photo en BD
      ↓
API lista las fotografías del álbum
```

---

## 24. Consideraciones importantes para continuar

1. No guardar fotos en BLOB.
2. No devolver originales públicamente.
3. No acoplar la lógica a `C:\selecta-storage`.
4. No duplicar código entre carga individual y masiva.
5. No poner procesamiento de imágenes en el controlador REST.
6. No debilitar toda la seguridad de `/api/**`.
7. Los endpoints administrativos deben continuar autenticados.
8. Solo los endpoints explícitamente públicos deben usar `/api/public/**`.
9. Usar Liquibase para cambios de esquema.
10. Hacer commit después de cada hito funcional.
11. Mantener el MVP como prioridad.
12. La interfaz visual puede evolucionar después de que los flujos backend funcionen.

---

## 25. Estado resumido

| Funcionalidad | Estado |
|---|---|
| Proyecto JHipster | ✅ |
| MariaDB local | ✅ |
| Arranque Spring Boot | ✅ |
| JWT admin | ✅ |
| Crear álbum | ✅ |
| Código de acceso | ✅ |
| Acceso público al álbum | ✅ |
| Persistencia Album | ✅ |
| Git disciplinado por hitos | ⏳ iniciar ahora |
| Entidad Photo | ⏳ |
| Almacenamiento local | ⏳ |
| Carga individual | ⏳ |
| Carga masiva | ⏳ |
| Thumbnail | ⏳ |
| Preview | ⏳ |
| Marca de agua | ⏳ |
| Galería cliente | ⏳ |
| Selección | ⏳ |
| Comentarios | ⏳ |
| Dashboard fotógrafo | ⏳ |

---

## 26. Instrucción para una IA que continúe el proyecto

Continuar desde el **módulo Photo**, no regenerar el proyecto y no sustituir JHipster.

Priorizar código ejecutable.

Antes de cambiar arquitectura, preservar:

```text
com.selecta.photo.album
```

y construir módulos nuevos bajo:

```text
com.selecta.photo.photo
```

o una estructura equivalente por feature.

La siguiente entrega debe permitir probar desde Postman la carga de **una o varias imágenes** contra un álbum existente y verificar:

- registro en MariaDB;
- original en almacenamiento;
- preview con marca de agua;
- thumbnail generado.
