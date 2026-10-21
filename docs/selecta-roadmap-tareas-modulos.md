# Selecta — Roadmap general, módulos y tabla de tareas

**Proyecto principal actual:** Selecta Photo  
**Negocio:** Selecta Eventos  
**Objetivo general:** construir una base tecnológica propia que empiece con la selección de fotografías y pueda crecer progresivamente hasta convertirse en la infraestructura/ERP de Selecta Eventos.

> Estados: [x] Terminado · [ ] Pendiente · [~] En progreso · [!] Requiere decisión

## 1. Visión inicial discutida

Selecta Eventos maneja actividades de fotografía, video, producción de eventos, DJ, maestro de ceremonia, recepción, decoración, mesas de dulces, coctelería, alquileres y venta de artículos para eventos.

Entre los alquileres discutidos están vestidos de quinceañera, vestidos de gala, trajes para hombre, vestidos de primera comunión, mobiliario decorativo, sillas, mesas, números luminosos, sonido, fuente de chocolate, cofres, centros de mesa y mantelería.

Entre los productos de venta se mencionaron bisutería, números inflables para fotos, bengalas, arreglos florales, globos y artículos para hora loca.

La necesidad inicial priorizada fue resolver un problema concreto: que Selecta pueda subir un álbum de aproximadamente 100–250 fotos y que el cliente seleccione sus favoritas sin depender de plataformas gratuitas limitadas.

---

# 2. Selecta Photo — tareas

## 2.1 Infraestructura base

| Estado | Tarea | Detalle |
|---|---|---|
| [x] | Crear proyecto JHipster | Monolito modular con Java, Spring Boot, Angular y JWT |
| [x] | Configurar Java | JDK 21 |
| [x] | Configurar Node.js | Node 24 |
| [x] | Configurar Maven Wrapper | Maven 3.9.x |
| [x] | Configurar IntelliJ | IDE principal |
| [x] | Configurar MariaDB local | Base `selecta_photo` con XAMPP |
| [x] | Configurar datasource | `application-dev.yml` |
| [x] | Deshabilitar Docker Compose en dev | Docker no requerido para arrancar |
| [x] | Arrancar aplicación | `localhost:8080` |
| [x] | Crear repositorio Git local | Historial local |
| [x] | Conectar GitHub remoto | `eduardogarcia9314/selecta-photo` |
| [x] | Primer push a GitHub | `main` sigue `origin/main` |
| [~] | Corregir Husky/lint-staged | Queda un fallo ocasional con stash temporal |
| [ ] | Definir estrategia de ramas | Puede hacerse cuando el equipo/proyecto crezca |
| [ ] | Crear entorno productivo | Servidor, dominio, storage y BD |

## 2.2 Álbumes

| Estado | Tarea | Detalle |
|---|---|---|
| [x] | Crear entidad `Album` | `id`, `name`, `accessCode`, `createdAt` |
| [x] | Crear `AlbumRepository` | Búsqueda por código |
| [x] | Crear `AlbumService` | Crear y consultar álbum |
| [x] | Generar código de acceso | Código aleatorio de 8 caracteres |
| [x] | Crear tabla `album` con Liquibase | Persistencia |
| [x] | Endpoint administrativo | `POST /api/albums` |
| [x] | Proteger creación con JWT | Admin autenticado |
| [x] | Endpoint público por código | `GET /api/public/albums/{accessCode}` |
| [x] | Permitir `/api/public/**` sin JWT | Seguridad configurada |
| [ ] | Editar nombre del álbum | UI fotógrafo |
| [ ] | Regenerar código | Invalidar código anterior |
| [ ] | Activar/desactivar álbum | Control de acceso |
| [ ] | Estado del álbum | `DRAFT`, `OPEN`, `COMPLETED`, `ARCHIVED` |
| [ ] | Límite de selección | Ej. 30 fotos |
| [ ] | Portada del álbum | Elegida por fotógrafo |
| [ ] | Fecha de vencimiento | Opcional |
| [ ] | Asociar cliente | Para integración con CRM |

## 2.3 Fotografías

| Estado | Tarea | Detalle |
|---|---|---|
| [x] | Crear entidad `Photo` | Metadata |
| [x] | Crear `PhotoRepository` | Consultas por álbum |
| [x] | Crear tabla `photo` con Liquibase | FK hacia `album` |
| [x] | Crear `PhotoStorageService` | Abstracción |
| [x] | Crear `LocalPhotoStorageService` | Storage local |
| [x] | Guardar original | Sin modificar |
| [x] | Generar preview | Versión para cliente |
| [x] | Generar thumbnail | Galería rápida |
| [x] | Marca de agua inicial | Texto `SELECTA EVENTOS` |
| [x] | Carga individual | Multipart |
| [x] | Carga masiva | Misma lógica, varios `files` |
| [x] | Listar fotos del álbum | API admin |
| [x] | Servir thumbnail | API admin |
| [x] | Servir preview | API admin |
| [x] | No exponer originales | Rutas internas privadas |
| [x] | Protección básica de rutas | Evitar path traversal |
| [~] | Endpoints públicos de fotos | Código en proceso/prueba |
| [ ] | Eliminar foto | Borrar BD + original + preview + thumbnail |
| [ ] | Reordenar fotos | Manual o automático |
| [ ] | Foto de portada | Una por álbum |
| [ ] | Barra de progreso de carga | Especialmente para lotes grandes |
| [ ] | Reintento de fallidas | Evitar repetir todo el lote |
| [ ] | Detección de duplicados | Hash u otra estrategia |
| [ ] | Soporte HEIC/HEIF | Útil para iPhone |
| [ ] | Soporte WebP | Opcional |
| [ ] | Corregir orientación EXIF | Evitar fotos giradas |
| [ ] | Logo real como watermark | Reemplazar texto |
| [ ] | Opacidad/posición configurable | Watermark flexible |
| [ ] | Migrar a S3/MinIO | Producción |
| [ ] | Backups automáticos | Originales |

## 2.4 Galería del cliente

| Estado | Tarea |
|---|---|
| [x] | Acceso por código |
| [~] | Consultar fotos públicamente |
| [ ] | Pantalla Angular para ingresar código |
| [ ] | Mostrar nombre del álbum |
| [ ] | Grid de thumbnails |
| [ ] | Abrir preview grande |
| [ ] | Navegar anterior/siguiente |
| [ ] | Marcar/desmarcar favorita |
| [ ] | Contador `seleccionadas / límite` |
| [ ] | Bloquear exceso de selección |
| [ ] | Comentario por foto |
| [ ] | Vista de solo seleccionadas |
| [ ] | Finalizar selección |
| [ ] | Confirmación final |
| [ ] | Diseño responsive |
| [ ] | Lazy loading |
| [ ] | Branding Selecta |

## 2.5 Selección y comentarios

| Estado | Tarea |
|---|---|
| [ ] | Crear `PhotoSelection` |
| [ ] | Guardar fecha de selección |
| [ ] | Endpoint seleccionar |
| [ ] | Endpoint deseleccionar |
| [ ] | Crear `PhotoComment` |
| [ ] | Crear/editar/eliminar comentario |
| [ ] | Mostrar comentarios al fotógrafo |
| [ ] | Registrar selección final |
| [ ] | Permitir reapertura por fotógrafo |
| [ ] | Exportar lista seleccionada |
| [ ] | Descargar originales seleccionados |
| [ ] | Preparar flujo de impresión/álbum físico |

## 2.6 Dashboard del fotógrafo

| Estado | Tarea |
|---|---|
| [x] | Autenticación administrativa |
| [ ] | Home/resumen |
| [ ] | Listado de álbumes |
| [ ] | Crear álbum desde UI |
| [ ] | Administrar álbum |
| [ ] | Subir una foto |
| [ ] | Subir lote drag & drop |
| [ ] | Ver progreso de carga |
| [ ] | Ver fotos |
| [ ] | Eliminar foto errónea |
| [ ] | Filtrar seleccionadas |
| [ ] | Ver comentarios |
| [ ] | Ver progreso del cliente |
| [ ] | Cambiar límite |
| [ ] | Cambiar portada |
| [ ] | Regenerar código |
| [ ] | Archivar álbum |
| [ ] | Descargar seleccionadas |
| [ ] | Buscar álbum |
| [ ] | Filtrar por estado |
| [ ] | Historial básico de actividad |
| [ ] | Papelera temporal de fotos eliminadas |

---

# 3. Arquitectura acordada

| Estado | Decisión |
|---|---|
| [x] | Monolito modular |
| [x] | Java + Spring Boot + JHipster |
| [x] | Angular integrado |
| [x] | JWT para administración |
| [x] | Acceso cliente mediante código |
| [x] | Liquibase |
| [x] | MariaDB/MySQL |
| [x] | No guardar fotos como BLOB |
| [x] | Original, preview y thumbnail separados |
| [x] | Watermark sobre preview |
| [x] | `PhotoStorageService` desacoplado |
| [x] | Storage local para MVP |
| [ ] | Object storage en producción |
| [ ] | Hosting |
| [ ] | HTTPS y dominio |
| [ ] | Backups |
| [ ] | Monitoreo/logs |
| [ ] | CI/CD |
| [ ] | Tests automáticos |

---

# 4. Git y calidad

| Estado | Tarea |
|---|---|
| [x] | Git inicializado |
| [x] | Commit inicial JHipster |
| [x] | GitHub remoto configurado |
| [x] | Push funcional |
| [~] | Husky/lint-staged |
| [ ] | Corregir fallo de stash de lint-staged |
| [ ] | Mantener commits por hito |
| [ ] | README específico |
| [ ] | Diagrama de arquitectura |
| [ ] | GitHub Actions |
| [ ] | Tests en push |
| [ ] | Releases/tags |

---

# 5. Seguridad y privacidad

| Estado | Tarea |
|---|---|
| [x] | Proteger endpoints administrativos |
| [x] | Exponer solo `/api/public/**` sin JWT |
| [x] | No exponer rutas físicas |
| [x] | No exponer originales |
| [x] | Validar root de storage |
| [ ] | Validar MIME real |
| [ ] | Ajustar límites de tamaño |
| [ ] | Rate limiting por código |
| [ ] | Caducidad/regeneración de código |
| [ ] | Logs de acceso |
| [ ] | Política de privacidad |
| [ ] | Consentimiento para tratamiento de imágenes |
| [ ] | Política de retención/borrado |
| [ ] | HTTPS |
| [ ] | Gestión de secretos |

---

# 6. Otros módulos proyectados para Selecta Eventos

## 6.1 CRM / Clientes

| Estado | Tarea |
|---|---|
| [ ] | Registro de clientes |
| [ ] | Datos de contacto |
| [ ] | Historial de eventos |
| [ ] | Historial de compras/alquileres |
| [ ] | Álbumes asociados |
| [ ] | Notas internas |
| [ ] | Oportunidades comerciales |
| [ ] | Segmentación |
| [ ] | Consentimientos |

## 6.2 Cotizaciones, ventas y contratos

| Estado | Tarea |
|---|---|
| [ ] | Crear cotizaciones |
| [ ] | Paquetes de servicios |
| [ ] | Productos adicionales |
| [ ] | Descuentos |
| [ ] | Aprobación |
| [ ] | Contratos |
| [ ] | Aceptación/firma digital |
| [ ] | Control de abonos |
| [ ] | Saldos pendientes |
| [ ] | Estado comercial |

## 6.3 Agenda y producción de eventos

| Estado | Tarea |
|---|---|
| [ ] | Calendario |
| [ ] | Reservar fecha |
| [ ] | Lugar |
| [ ] | Montaje |
| [ ] | Hora del evento |
| [ ] | Desmontaje |
| [ ] | Checklist |
| [ ] | Asignar fotógrafo |
| [ ] | Asignar DJ |
| [ ] | Asignar maestro de ceremonia |
| [ ] | Asignar decoración |
| [ ] | Asignar transporte |
| [ ] | Alertas |

## 6.4 Inventario y alquileres

| Estado | Tarea |
|---|---|
| [ ] | Catálogo de activos |
| [ ] | Cantidades disponibles |
| [ ] | Reserva por fecha |
| [ ] | Evitar doble alquiler |
| [ ] | Entrega/devolución |
| [ ] | Estado físico |
| [ ] | Daños |
| [ ] | Depósitos |
| [ ] | Mantenimiento |
| [ ] | Limpieza |
| [ ] | Ubicación |
| [ ] | Historial |

## 6.5 Inventario de productos de venta

| Estado | Tarea |
|---|---|
| [ ] | Catálogo |
| [ ] | Stock |
| [ ] | Entradas/salidas |
| [ ] | Costos |
| [ ] | Precio de venta |
| [ ] | Alertas de stock |
| [ ] | Historial de ventas |
| [ ] | Ajustes |

## 6.6 Proveedores y compras

| Estado | Tarea |
|---|---|
| [ ] | Registro de proveedores |
| [ ] | Productos/servicios |
| [ ] | Comparar precios |
| [ ] | Órdenes de compra |
| [ ] | Cuentas por pagar |
| [ ] | Historial |
| [ ] | Evaluación del proveedor |

## 6.7 Finanzas / Caja

| Estado | Tarea |
|---|---|
| [ ] | Ingresos |
| [ ] | Egresos |
| [ ] | Abonos |
| [ ] | Saldos |
| [ ] | Caja diaria |
| [ ] | Cuentas por cobrar |
| [ ] | Cuentas por pagar |
| [ ] | Rentabilidad por evento |
| [ ] | Rentabilidad por servicio |
| [ ] | Reportes |
| [ ] | Integración futura con facturación electrónica |

## 6.8 Personal / Recursos Humanos

| Estado | Tarea |
|---|---|
| [ ] | Colaboradores |
| [ ] | Roles |
| [ ] | Disponibilidad |
| [ ] | Asignación a eventos |
| [ ] | Pagos por evento |
| [ ] | Documentos |
| [ ] | Personal freelance |
| [ ] | Historial de trabajos |

## 6.9 Portal del cliente

| Estado | Tarea |
|---|---|
| [ ] | Ver cotización |
| [ ] | Aceptar propuesta |
| [ ] | Ver contrato |
| [ ] | Ver pagos |
| [ ] | Consultar evento |
| [ ] | Seleccionar fotos |
| [ ] | Descargar entregables |
| [ ] | Consultar alquileres |
| [ ] | Solicitudes |
| [ ] | Estado del servicio |

## 6.10 Reportes y analítica

| Estado | Tarea |
|---|---|
| [ ] | Ventas por mes |
| [ ] | Eventos por mes |
| [ ] | Servicios más vendidos |
| [ ] | Alquileres frecuentes |
| [ ] | Productos más vendidos |
| [ ] | Clientes recurrentes |
| [ ] | Ingresos por línea |
| [ ] | Gastos/margen por evento |
| [ ] | Uso de inventario |
| [ ] | Métricas de Selecta Photo |

---

# 7. Módulos adicionales sugeridos

## 7.1 Selecta Delivery

- [ ] Entrega digital de fotos finales
- [ ] Entrega de videos
- [ ] Links con expiración
- [ ] Descargas protegidas
- [ ] Alta/baja resolución
- [ ] Historial de entregas

## 7.2 Selecta Studio

- [ ] Estado de edición
- [ ] Asignar editor
- [ ] Pendiente / editando / revisión / terminado
- [ ] Versionado
- [ ] Observaciones
- [ ] Fecha prometida
- [ ] Aprobación del cliente

## 7.3 Selecta Notifications

- [ ] Correos automáticos
- [ ] WhatsApp futuro
- [ ] Recordatorio de selección
- [ ] Aviso de álbum listo
- [ ] Aviso al fotógrafo cuando el cliente finaliza
- [ ] Recordatorios de pago
- [ ] Recordatorios de evento
- [ ] Recordatorios de devolución

## 7.4 Selecta Catalog / Web comercial

- [ ] Catálogo público de servicios
- [ ] Catálogo de alquileres
- [ ] Catálogo de productos
- [ ] Portafolio
- [ ] Formulario de cotización
- [ ] Leads
- [ ] SEO
- [ ] Disponibilidad aproximada

## 7.5 Selecta Operations

- [ ] Checklist por evento
- [ ] Equipo/material requerido
- [ ] Responsable
- [ ] Vehículo/transporte
- [ ] Hora de salida
- [ ] Confirmación de montaje
- [ ] Confirmación de desmontaje
- [ ] Incidencias
- [ ] Evidencias

---

# 8. Prioridad sugerida

| Prioridad | Módulo / hito |
|---|---|
| 1 | Selecta Photo MVP |
| 2 | Dashboards cliente y fotógrafo |
| 3 | Selección y comentarios |
| 4 | Entrega/producción de fotos |
| 5 | CRM |
| 6 | Agenda/Eventos |
| 7 | Cotizaciones/Contratos |
| 8 | Inventario/Alquileres |
| 9 | Finanzas |
| 10 | Proveedores/Compras |
| 11 | RRHH |
| 12 | Analítica |

---

# 9. Próximos hitos inmediatos de Selecta Photo

| Estado | Hito |
|---|---|
| [~] | Terminar/probar endpoints públicos de fotos |
| [ ] | Commit y push |
| [ ] | Pantalla Angular para código |
| [ ] | Galería de thumbnails |
| [ ] | Preview con marca de agua |
| [ ] | `PhotoSelection` |
| [ ] | Favoritas |
| [ ] | Contador |
| [ ] | `PhotoComment` |
| [ ] | Finalizar selección |
| [ ] | Dashboard básico del fotógrafo |
| [ ] | Ver selección del cliente |
| [ ] | Eliminar fotos erróneas |
| [ ] | Descargar seleccionadas |
| [ ] | Cerrar MVP v1 |

---

# 10. Definición de MVP v1 terminado

- [ ] El fotógrafo inicia sesión.
- [ ] Crea un álbum desde la interfaz.
- [ ] Sube 1 o muchas fotos.
- [ ] Ve las fotos cargadas.
- [ ] Elimina una foto errónea.
- [ ] Define límite de selección.
- [ ] Entrega un código al cliente.
- [ ] El cliente entra con el código.
- [ ] Ve thumbnails.
- [ ] Abre previews con marca de agua.
- [ ] Selecciona/desselecciona favoritas.
- [ ] Escribe comentarios.
- [ ] Ve contador.
- [ ] Finaliza selección.
- [ ] El fotógrafo ve la selección final.
- [ ] Filtra seleccionadas.
- [ ] Descarga/identifica originales elegidos.
- [ ] Todo persiste.
- [ ] Existe backup razonable.
- [ ] Está desplegado en Internet.

---

# 11. Regla de trabajo acordada

**20 % diseño / 80 % producto funcionando.**

Cada hito terminado:

```bash
git status
git add .
git commit -m "tipo(modulo): descripcion"
git push
```

---

# 12. Visión de largo plazo

```text
Selecta
│
├── Selecta Photo
│   ├── Albums
│   ├── Photos
│   ├── Selections
│   └── Delivery
│
├── CRM
├── Eventos / Agenda
├── Cotizaciones / Contratos
├── Inventario
│   ├── Alquileres
│   └── Ventas
├── Proveedores / Compras
├── Finanzas
├── Personal
├── Portal Cliente
├── Notificaciones
└── Reportes / Analítica
```

La prioridad sigue siendo **terminar Selecta Photo como producto real y usado**, y después expandir sobre esa base.
