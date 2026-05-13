# Despliegue — DevHub

## Requisitos

- Docker Desktop instalado y corriendo
- Git
- Acceso al repositorio

## Variables de entorno

Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:

```
MONGO_URI=mongodb+srv://<usuario>:<password>@<cluster>.mongodb.net/DevHubDB
JWT_SECRET=<clave-secreta-jwt>
DOCKER_USERNAME=<usuario-dockerhub>
IMAGE_TAG=latest
```

## Levantar el proyecto localmente

```bash
git clone https://github.com/puj-course/FIS_2610_3513_G1.git
cd FIS_2610_3513_G1
cp .env.example .env
# Completar las variables en .env
docker compose up -d
```

Servicios disponibles:

| Servicio  | URL                   |
|-----------|-----------------------|
| Frontend  | http://localhost:3000 |
| Backend   | http://localhost:8080 |
| MongoDB   | localhost:27017       |

## Red Docker

Los servicios se comunican a través de la red interna `devhubdb-network` definida en `docker-compose.yml`. El frontend llega al backend mediante `http://backend:8080` y el backend a MongoDB mediante el servicio `mongo`.

## Pipeline CI/CD

### CI — `ci.yml`

Se ejecuta en cada push y pull request a `develop` y `main`.

| Job            | Qué hace                                      |
|----------------|-----------------------------------------------|
| Backend Build  | Compila con Maven y corre los tests unitarios |
| Frontend Build | Instala dependencias y compila con Bun        |
| Docker Build   | Construye las imágenes con Docker Compose     |

### CD — `cd.yml`

Se ejecuta automáticamente en cada push a `develop` (merge de PR).

| Paso                    | Descripción                                                          |
|-------------------------|----------------------------------------------------------------------|
| Build images            | Construye las imágenes Docker desde los Dockerfiles                  |
| Push to Docker Hub      | Publica `devhubdb-backend` y `devhubdb-frontend` con tag SHA y `latest` |
| Deploy                  | Ejecuta `docker compose up -d` en el runner local                   |
| Health check            | Verifica que backend y frontend respondan correctamente              |
| Rollback                | Si el health check falla, restaura la versión anterior               |

### Versionamiento de imágenes

Cada imagen se publica en Docker Hub con dos tags:

- `<DOCKER_USERNAME>/devhubdb-backend:<SHA-del-commit>` — versión exacta e inmutable
- `<DOCKER_USERNAME>/devhubdb-backend:latest` — última versión estable

## Secrets requeridos en GitHub

| Secret           | Descripción                        |
|------------------|------------------------------------|
| `DOCKER_USERNAME`| Usuario de Docker Hub              |
| `DOCKER_PASSWORD`| Token de acceso de Docker Hub      |
| `MONGO_URI`      | URI de conexión a MongoDB Atlas    |
| `JWT_SECRET`     | Clave secreta para tokens JWT      |

## Reproducir el despliegue manualmente

```bash
export DOCKER_USERNAME=<tu-usuario>
export IMAGE_TAG=latest
export MONGO_URI=<tu-uri>
export JWT_SECRET=<tu-secret>

docker compose build
docker compose push
docker compose down
docker compose up -d
```
