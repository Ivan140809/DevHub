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

- Cliente web desarrollado en Next.js
- API REST desarrollada en Spring Boot
- Persistencia de datos en MongoDB
- Contenerización completa mediante Docker
- Orquestación de servicios con Docker Compose

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
│   ├── deploy.sh
│   └── test.sh
├── src/
│   ├── main/                # Backend (Spring Boot)
│   │   ├── java/
│   │   └── resources/
│   │       └── application.properties
│   ├── test/
│   └── web/                 # Frontend (Next.js + Bun)
│       ├── app/
│       ├── public/
│       │   ├── file.svg
│       │   ├── globe.svg
│       │   ├── next.svg
│       │   ├── vercel.svg
│       │   └── window.svg
│       ├── package.json
│       ├── next.config.ts
│       ├── tsconfig.json
│       ├── eslint.config.mjs
│       ├── postcss.config.mjs
│       ├── bun.lock
├── assets/
├── temp/
├── .mvn/
├── .gitattributes
├── .gitignore
├── README.md
├── LICENSE
├── pom.xml
├── docker-compose.yml
├── env.example
├── mvnw
├── mvnw.cmd
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
git clone https://github.com/organizacion/proyecto.git
cd proyecto
```

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

## 📚 Contexto Académico

Proyecto desarrollado en el marco de la asignatura:

- **Asignatura:** Fundamentos de Ingeniería de Software
- **Docente:** Luis Gabriel Moreno Sandoval, PhD
- **Contacto**: morenoluis@javeriana.edu.co

## 📩 Contacto
### Equipo de desarrollo 

**Iván Santiago Lastra**  
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana. 

📧 ivan.lastra@javeriana.edu.co  

**Ana Maria Murcia Gomez**  
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana.

📧 murcia-ana@javeriana.edu.co

**Richard Manuel Castillo Pesca**  
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana.

📧 r-castillo@javeriana.edu.co

**Lorenzo Ramirez Calderon**  
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana.

📧 lorenzo.ramirezc@javeriana.edu.co

**Adam Kalel Ordoñez Herrera**  
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana. 

📧 adordonez@javeriana.edu.co

**Lucas Fuentes Sanchez**  
Estudiante de Ingeniería de Sistemas, Pontificia Universidad Javeriana. 

📧 lucas.fuentes@javeriana.edu.co


