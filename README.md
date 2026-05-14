<p align="center">
  <img src="./assets/devhub-logo.png" alt="DevHub Logo" width="640">
</p>

# DEVHUB 💻

> "Entrena con propósito, destaca con confianza."
> 
## 📖 Descripción
- DEVHUB es una plataforma web especializada en preparación de entrevistas técnicas para ingeniería de sistemas y tecnologías de la información.

- La plataforma facilita la práctica de preguntas técnicas, el fortalecimiento de fundamentos y la familiarización con dinámicas reales de procesos de selección laboral.

- Además, proporciona un entorno digital de entrenamiento que promueve la práctica constante, la autoevaluación y el aprendizaje colaborativo entre estudiantes y profesionales.

- DEVHUB surge como respuesta a la brecha existente entre la formación universitaria y las competencias que exige el mercado laboral, donde las entrevistas técnicas representan un filtro determinante para acceder a oportunidades profesionales.

- La plataforma busca fortalecer las habilidades técnicas de los usuarios y mejorar su empleabilidad en el sector tecnológico, contribuyendo a una preparación integral alineada con procesos reales de selección.
## 👥 Equipo del Proyecto

| Nombre | Rol Scrum | GitHub |
|--------|-----------|--------|
| Lorenzo Ramírez | Scrum Master | https://github.com/lorenzoramirez-lrc |
| Adam Kalel Ordoñez | Product Owner | https://github.com/KALEL2006 |
| Richard Castillo | Sprint Planner | https://github.com/RichardCastillo-jpg |
| Iván Santiago Lastra | Configuration Manager | https://github.com/Ivan140809 |
| Ana María Murcia | QA Lead | https://github.com/ana2320-ux |
| Lucas Fuentes | DevOps Engineer | https://github.com/brewLux |

## 🛠 Tecnologías Utilizadas

- **Frontend:** Next.js 16 + Bun
- **Backend:** Java 17 – Spring Boot
- **Base de Datos:** MongoDB
- **Contenerización:** Docker + Docker Compose
- **CI/CD:** GitHub Actions
- **Control de versiones:** Git (GitFlow).
- **Arquitectura de repositorio:** Monorepo Fullstack

## 🏗 Arquitectura del Sistema

El proyecto sigue una arquitectura de **monorepo fullstack** con tres servicios desacoplados orquestados por Docker Compose:

```
Cliente (Next.js :3000) → API REST (Spring Boot :8080) → MongoDB (:27017)
```

- Cliente web desarrollado en Next.js
- API REST desarrollada en Spring Boot
- Persistencia de datos en MongoDB Atlas
- Autenticación stateless mediante JWT + Spring Security
- Contenerización completa mediante Docker
- Orquestación de servicios con Docker Compose

### Patrones de Diseño Implementados

| Patrón | Módulo | Descripción |
|--------|--------|-------------|
| Builder | `builder/` | Construcción de objetos `Progress` |
| Observer | `observer/` | Notificaciones de eventos del sistema |
| Strategy | `strategy/` | Lógica de evaluación intercambiable |
| Factory Method | `factorymethod/` | Creación de tipos de usuario |
| Facade | `facade/` | Simplificación de subsistemas complejos |
| Composite | `model/CommentComposite` | Árbol de comentarios y respuestas |

## 📂 Estructura del Proyecto

```
FIS_2610_3513_G1/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── workflows/
│       ├── ci.yml
│       └── cd.yml
├── conf/
├── docs/
│   ├── api/
│   ├── architecture/
│   └── user_guide/
├── jupyter/
│   ├── notebooks/
│   │   ├── exploration.ipynb
│   │   └── analysis.ipynb
│   └── datasets/
│       ├── data1.csv
│       └── data2.csv
├── scripts/
│   ├── setup.sh
│   ├── setup-respuestas.sh
│   ├── setup-reviews.sh
│   └── setup_comments.sh
├── src/
│   ├── main/
│   │   ├── java/com/skillstack/devhub/
│   │   │   ├── builder/           # Patrón Builder
│   │   │   ├── config/            # SecurityConfig
│   │   │   ├── controller/        # REST controllers
│   │   │   ├── dto/               # Data Transfer Objects
│   │   │   ├── exception/         # Excepciones personalizadas
│   │   │   ├── facade/            # Patron Facade
│   │   │   ├── factorymethod/     # Patron Factory Method
│   │   │   ├── handler/           # Global exception handler
│   │   │   ├── model/             # Entidades de dominio
│   │   │   ├── observer/          # Patron Observer
│   │   │   ├── repository/        # Repositorios MongoDB
│   │   │   ├── security/          # JWT y filtros
│   │   │   ├── service/           # Lógica de negocio
│   │   │   └── strategy/          # Patrón Strategy
│   │   ├── resources/
│   │   │   └── application.properties 
│   │   └── Dockerfile
│   ├── test/                      # Pruebas unitarias e integración
│   └── web/                       # Frontend Next.js + Bun
│       ├── app/
│       ├── public/
│       ├── package.json
│       ├── next.config.ts
│       ├── tsconfig.json
│       └── Dockerfile
├── assets/                        # Logo e imágenes del proyecto
├── temp/
├── .mvn/
├── .gitattributes
├── .gitignore
├── README.md
├── LICENSE
├── pom.xml
├── docker-compose.yml
├── env.example
├── sonar-project.properties
├── mvnw
└── mvnw.cmd
```

## 🚀 Instalación y Ejecución

### 🔹 Requisitos

- Docker y Docker Compose
- Git
- Java 17+
- Next.js instalado
- Bun (para desarrollo local frontend)

### 🔹 Clonar el repositorio

```bash
git clone https://github.com/puj-course/FIS_2610_3513_G1.git
cd FIS_2610_3513_G1
```

### 🔹 Configurar variables de entorno

```bash
cp env.example .env
# Edita .env con tus credenciales
```

| Variable | Descripción |
|----------|-------------|
| `MONGO_ROOT_USERNAME` | Usuario administrador de MongoDB |
| `MONGO_ROOT_PASSWORD` | Contraseña de MongoDB |
| `MONGO_URI` | URI de conexión a MongoDB Atlas |
| `JWT_SECRET` | Clave secreta para firmar tokens JWT |
| `DOCKER_USERNAME` | Usuario de Docker Hub (para CI/CD) |


### 🔹 Ejecución con Docker

```bash
docker-compose up --build
```

**Servicios disponibles:**
- Frontend → http://localhost:3000
- Backend → http://localhost:8080
- MongoDB → puerto 27017

### 🔹 Ejecución de pruebas

**Backend:**
```bash
mvn test
```
**Frontend:**
```bash
bun run dev
```


**Con Docker:**
```bash
docker-compose run backend mvn test
```

---

## 🌐 API REST – Endpoints principales

| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/auth/register` | Registro de usuario |
| `POST` | `/auth/login` | Inicio de sesión (retorna JWT) |
| `POST` | `/auth/forgot-password` | Solicitud de recuperación de contraseña |
| `POST` | `/auth/reset-password` | Restablecer contraseña |
| `GET` | `/questions` | Listar preguntas |
| `POST` | `/questions` | Crear pregunta (admin) |
| `GET` | `/questions/{id}` | Obtener pregunta por ID |
| `GET` | `/users/{id}` | Obtener perfil de usuario |
| `PUT` | `/users/{id}` | Actualizar perfil |
| `GET` | `/statistics/ranking` | Ranking de usuarios |
| `POST` | `/comments` | Agregar comentario |
| `POST` | `/comments/{id}/replies` | Responder a un comentario |

---

## ⚙️ CI/CD y Calidad de Código

Flujos automatizados en GitHub Actions:

| Workflow | Descripción |
|----------|-------------|
| `ci.yml` | Integración continua: compila y ejecuta pruebas |
| `cd.yml` | Despliegue continuo a producción |
| `build.yml` | Construcción y publicación de imágenes Docker |
| `quality-metrics.yml` | Análisis de calidad con SonarCloud |
| `test-report.yml` | Reporte de cobertura con JaCoCo |
| `hu-metrics.yml` | Métricas de historias de usuario |
| `sprint-report.yml` | Reporte automático por sprint |
| `daily-scrum.yaml` | Automatización de Daily Scrum |

**SonarCloud:** [puj-course_FIS_2610_3513_G1](https://sonarcloud.io/project/overview?id=puj-course_FIS_2610_3513_G1)

---

## 📚 Contexto Académico

Proyecto desarrollado en el marco de la asignatura:

| Campo | Detalle |
|-------|---------|
| Asignatura | Fundamentos de Ingeniería de Software |
| Código | FIS 2610 – Grupo 3513 G1 |
| Institución | Pontificia Universidad Javeriana |
| Docente | Luis Gabriel Moreno Sandoval, PhD |
| Contacto | morenoluis@javeriana.edu.co |

## 📩 Contacto
### Equipo de desarrollo

**Lorenzo Ramírez Calderón**
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana
📧 lorenzo.ramirezc@javeriana.edu.co

**Adam Kalel Ordoñez Herrera**
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana
📧 adordonez@javeriana.edu.co

**Richard Manuel Castillo Pesca**
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana
📧 r-castillo@javeriana.edu.co

**Iván Santiago Lastra**
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana
📧 ivan.lastra@javeriana.edu.co

**Ana María Murcia Gómez**
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana
📧 murcia-ana@javeriana.edu.co

**Lucas Fuentes Sánchez**
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana
📧 lucas.fuentes@javeriana.edu.co

---

## 📄 Licencia

Proyecto desarrollado con fines académicos en el marco de la asignatura Fundamentos de Ingeniería de Software — Pontificia Universidad Javeriana, 2025.

Ver [LICENSE](./LICENSE) para más detalles.


