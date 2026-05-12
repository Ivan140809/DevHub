"use client";

import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import {
  Trophy,
  Book,
  Award,
  CloudLightning,
  LogOut,
  ChevronRight,
  Brain,
  Target,
  BarChart3,
  UserCircle2,
  Swords,
  Medal,
} from "lucide-react";

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 },
  { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 },
  { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "35%", d: "11s", dl: "-3s", s: 2 },
  { l: "45%", d: "14s", dl: "-6s", s: 3 },
  { l: "72%", d: "13s", dl: "-5s", s: 3 },
  { l: "88%", d: "15s", dl: "-8s", s: 2 },
  { l: "93%", d: "9s", dl: "-1s", s: 3 },
  { l: "10%", d: "12s", dl: "-4s", s: 2 },
];

const FEATURES = [
  {
    icon: <CloudLightning />,
    title: "Preguntas Técnicas Reales",
    desc: "Practica con preguntas usadas en procesos de selección reales en empresas de tecnología.",
  },
  {
    icon: <Award />,
    title: "Autoevaluación Continua",
    desc: "Mide tu progreso con métricas claras y descubre exactamente dónde mejorar.",
  },
  {
    icon: <Trophy />,
    title: "Ranking Competitivo",
    desc: "Compite con otros estudiantes y profesionales para mantenerte motivado.",
  },
  {
    icon: <Book />,
    title: "Preparación Focalizada",
    desc: "Filtra por categoría y dificultad para entrenar exactamente lo que necesitas.",
  },
];

const SUBMODULES = [
  {
    id: 1,
    icon: <Brain size={26} />,
    title: "Simulador de Entrevistas",
    shortDesc: "Resuelve preguntas técnicas como en una entrevista real.",
    longDesc:
      "Enfréntate a escenarios similares a procesos reales de selección. Practica lógica, estructuras de datos, backend, frontend y fundamentos técnicos.",
    path: "/question",
    cta: "Ir a practicar",
  },
  {
    id: 2,
    icon: <BarChart3 size={26} />,
    title: "Seguimiento de Progreso",
    shortDesc: "Visualiza tu rendimiento y tus áreas de mejora.",
    longDesc:
      "Consulta tu avance por categorías, dificultad y desempeño general para enfocar mejor tu preparación y detectar debilidades rápidamente.",
    path: "/profile",
    cta: "Ver progreso",
  },
  {
    id: 3,
    icon: <Swords size={26} />,
    title: "Ranking y Competencia",
    shortDesc: "Compite con otros usuarios y mantén tu motivación.",
    longDesc:
      "Compara tu desempeño con otros estudiantes y profesionales. Usa el ranking como incentivo para practicar de manera constante.",
    path: "/ranking",
    cta: "Ver ranking",
  },
  {
    id: 4,
    icon: <Target size={26} />,
    title: "Entrenamiento Personalizado",
    shortDesc: "Enfócate solo en lo que necesitas mejorar.",
    longDesc:
      "Filtra preguntas por categoría y dificultad para crear una ruta de preparación más precisa según tus objetivos laborales.",
    path: "/filter",
    cta: "Explorar módulos",
  },
  {
    id: 5,
    icon: <UserCircle2 size={26} />,
    title: "Perfil Profesional",
    shortDesc: "Gestiona tu cuenta y revisa tus estadísticas.",
    longDesc:
      "Consulta tu perfil, tu avance acumulado, tus resultados y mantén organizada toda tu experiencia dentro de la plataforma.",
    path: "/profile",
    cta: "Ir al perfil",
  },
  {
    id: 6,
    icon: <Medal size={26} />,
    title: "Retos por Nivel",
    shortDesc: "Pon a prueba tus conocimientos según tu dificultad.",
    longDesc:
      "Supera retos fáciles, medios y difíciles para fortalecer progresivamente tus habilidades y ganar confianza en entrevistas técnicas.",
    path: "/filter",
    cta: "Comenzar reto",
  },
];

const BUTTON_STYLES = {
  primary: {
    height: 46,
    padding: "0 20px",
    background: "rgba(52, 26, 122, 0.3)",
    border: "1px solid rgb(99, 60, 255)",
    borderRadius: 10,
    color: "rgb(180, 150, 255)",
    fontFamily: "'Syne', sans-serif",
    fontSize: 14,
    fontWeight: 800,
    letterSpacing: "3px",
    textTransform: "uppercase" as const,
    cursor: "pointer",
    boxShadow: "0 4px 16px rgba(91, 40, 220, 0.75)",
    width: "fit-content",
  },
  header: {
    height: 36,
    padding: "0 20px",
    background: "linear-gradient(135deg,#7040ff,#5020e0)",
    border: "none",
    borderRadius: 10,
    color: "white",
    fontFamily: "'Space Mono',monospace",
    fontSize: 11,
    letterSpacing: "2px",
    textTransform: "uppercase" as const,
    cursor: "pointer",
    boxShadow: "0 4px 16px rgba(90,40,220,.35)",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
};

export default function HomePage() {
  const router = useRouter();

  const [visible, setVisible] = useState(false);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [username, setUsername] = useState("Usuario");
  const [openModule, setOpenModule] = useState<number | null>(null);

  useEffect(() => {
    const raw = localStorage.getItem("devhub_user");
    const token = localStorage.getItem("token");

    if (raw && token) {
      try {
        const parsedUser = JSON.parse(raw);
        setIsAuthenticated(true);
        setUsername(parsedUser.username || "Usuario");
      } catch (error) {
        console.error("Error al parsear devhub_user:", error);
        setIsAuthenticated(false);
        setUsername("Usuario");
      }
    } else {
      setIsAuthenticated(false);
      setUsername("Usuario");
    }

    const timer = setTimeout(() => setVisible(true), 100);
    return () => clearTimeout(timer);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("devhub_user");
    localStorage.removeItem("token");
    setIsAuthenticated(false);
    setUsername("Usuario");
    router.push("/");
  };

  const navigateTo = (path: string) => {
    router.push(path);
  };

  const toggleModule = (id: number) => {
    setOpenModule((prev) => (prev === id ? null : id));
  };

  return (
    <main
      style={{
        minHeight: "100vh",
        background: "#07070f",
        fontFamily: "'Syne', sans-serif",
        position: "relative",
        overflow: "hidden",
      }}
    >
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');

        @keyframes orbFloat {
          0% { transform: translate(0,0); }
          100% { transform: translate(20px,20px); }
        }

        @keyframes floatUp {
          0% { transform: translateY(0); opacity: .7; }
          100% { transform: translateY(-100vh); opacity: 0; }
        }

        @keyframes fadeUp {
          0% { opacity: 0; transform: translateY(32px); }
          100% { opacity: 1; transform: translateY(0); }
        }

        @keyframes shimmer {
          0% { background-position: 200% center; }
          100% { background-position: -200% center; }
        }

        .dh-particle {
          position: fixed;
          border-radius: 50%;
          background: rgba(160,100,255,.7);
          bottom: -10px;
          animation: floatUp linear infinite;
          pointer-events: none;
          z-index: 0;
        }

        .feat-card {
          transition: all .3s cubic-bezier(.16,1,.3,1);
        }

        .feat-card:hover {
          transform: translateY(-6px) !important;
          border-color: rgba(100,60,255,.4) !important;
        }

        .cta-btn {
          transition: all .2s ease;
        }

        .cta-btn:hover {
          transform: scale(1.04);
          box-shadow: 0 8px 32px rgba(112,64,255,.5) !important;
        }

        .sub-card {
          transition: all .28s ease;
        }

        .sub-card:hover {
          transform: translateY(-4px);
          border-color: rgba(120, 80, 255, 0.45) !important;
          box-shadow: 0 10px 30px rgba(70, 30, 180, 0.22);
        }

        .sub-arrow {
          transition: transform .25s ease;
        }

        .sub-arrow.open {
          transform: rotate(90deg);
        }

        .sub-content {
          overflow: hidden;
          transition: all .3s ease;
        }
      `}</style>

      <div
        style={{
          position: "fixed",
          borderRadius: "50%",
          width: 600,
          height: 600,
          background: "radial-gradient(circle,rgba(90,30,200,.2) 0%,transparent 70%)",
          top: -200,
          left: -200,
          animation: "orbFloat 10s ease-in-out infinite alternate",
          pointerEvents: "none",
          zIndex: 0,
        }}
      />
      <div
        style={{
          position: "fixed",
          borderRadius: "50%",
          width: 500,
          height: 500,
          background: "radial-gradient(circle,rgba(110,50,255,.15) 0%,transparent 70%)",
          bottom: -150,
          right: -150,
          animation: "orbFloat 10s ease-in-out infinite alternate",
          animationDelay: "-5s",
          pointerEvents: "none",
          zIndex: 0,
        }}
      />
      <div
        style={{
          position: "fixed",
          borderRadius: "50%",
          width: 300,
          height: 300,
          background: "radial-gradient(circle,rgba(80,20,180,.15) 0%,transparent 70%)",
          top: "40%",
          left: "60%",
          animation: "orbFloat 14s ease-in-out infinite alternate",
          animationDelay: "-3s",
          pointerEvents: "none",
          zIndex: 0,
        }}
      />

      {PARTICLES.map((p, i) => (
        <div
          key={i}
          className="dh-particle"
          style={{
            width: p.s,
            height: p.s,
            left: p.l,
            animationDuration: p.d,
            animationDelay: p.dl,
          }}
        />
      ))}

      <Navbar />

      <header
        style={{
          position: "relative",
          zIndex: 10,
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          padding: "16px 28px",
          borderBottom: "1px solid rgba(100,60,255,.15)",
          background: "rgba(7,7,15,.8)",
          backdropFilter: "blur(10px)",
        }}
      >
        <button
          onClick={() => navigateTo("/FAQ")}
          className="cta-btn"
          style={BUTTON_STYLES.header}
        >
          Preguntas Frecuentes
        </button>

        <span
          style={{
            fontFamily: "'Space Mono',monospace",
            fontWeight: 700,
            fontSize: 16,
            letterSpacing: 6,
            color: "#b8a0ff",
            textShadow: "0 0 20px rgba(150,100,255,.5)",
          }}
        >
          {" "}
        </span>

        <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
          {isAuthenticated ? (
            <>
              <span
                style={{
                  color: "rgba(180,150,255,.8)",
                  fontFamily: "'Space Mono',monospace",
                  fontSize: 12,
                }}
              >
                {username}
              </span>

              <button
                onClick={() => navigateTo("/profile")}
                className="cta-btn"
                style={BUTTON_STYLES.header}
              >
                Mi Perfil
              </button>

              <button
                onClick={handleLogout}
                className="cta-btn"
                style={{
                  ...BUTTON_STYLES.header,
                  background: "rgba(200, 60, 60, 0.3)",
                }}
              >
                <LogOut size={14} style={{ marginRight: 6 }} />
                Salir
              </button>
            </>
          ) : (
            <>
              <button
                onClick={() => navigateTo("/login")}
                className="cta-btn"
                style={BUTTON_STYLES.header}
              >
                Iniciar Sesión
              </button>

              <button
                onClick={() => navigateTo("/register")}
                className="cta-btn"
                style={BUTTON_STYLES.header}
              >
                Registrarse
              </button>
            </>
          )}
        </div>
      </header>

      <section
        style={{
          position: "relative",
          zIndex: 5,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          marginTop: -40,
          padding: "80px 24px 60px",
          textAlign: "center",
          animation: visible ? "fadeUp .8s ease both" : "none",
        }}
      >
        <div style={{ marginBottom: 16, animation: "fadeUp .8s .1s ease both" }}>
          <h1
            style={{
              fontSize: "clamp(36px, 6vw, 72px)",
              fontWeight: 800,
              margin: 0,
              lineHeight: 1.1,
              background:
                "linear-gradient(135deg, #ffffff 0%, #b8a0ff 40%, #7040ff 100%)",
              backgroundSize: "200% auto",
              WebkitBackgroundClip: "text",
              WebkitTextFillColor: "transparent",
              backgroundClip: "text",
              animation: "shimmer 7s linear infinite",
            }}
          >
            DevHub
          </h1>
        </div>

        <p
          style={{
            fontFamily: "'Space Mono',monospace",
            fontSize: 13,
            letterSpacing: "3px",
            textTransform: "uppercase",
            color: "rgb(208, 197, 250)",
            marginBottom: 24,
            animation: "fadeUp .8s .2s ease both",
          }}
        >
          Entrena hoy, destaca mañana.
        </p>

        <p
          style={{
            maxWidth: 620,
            fontSize: 16,
            lineHeight: 1.8,
            color: "rgb(172, 150, 248)",
            marginBottom: 40,
            animation: "fadeUp .8s .3s ease both",
          }}
        >
          La plataforma especializada en preparación de entrevistas técnicas para
          ingeniería de sistemas y tecnología. Practica, rompe tus limites, mide
          tu progreso y llega listo a tu próxima oportunidad laboral.
        </p>

        <div
          style={{
            display: "flex",
            gap: 14,
            flexWrap: "wrap",
            justifyContent: "center",
            animation: "fadeUp .8s .4s ease both",
          }}
        >
          {isAuthenticated ? (
            <>
              <button
                onClick={() => navigateTo("/question")}
                className="cta-btn"
                style={BUTTON_STYLES.primary}
              >
                Practicar Preguntas
              </button>

              <button
                onClick={() => navigateTo("/ranking")}
                className="cta-btn"
                style={BUTTON_STYLES.primary}
              >
                Ver Ranking
              </button>
            </>
          ) : (
            <>
              <button
                onClick={() => navigateTo("/register")}
                className="cta-btn"
                style={BUTTON_STYLES.primary}
              >
                Empezar Gratis
              </button>

              <button
                onClick={() => navigateTo("/question")}
                className="cta-btn"
                style={BUTTON_STYLES.primary}
              >
                Ver Preguntas
              </button>
            </>
          )}
        </div>

        <div
          style={{
            display: "flex",
            gap: 24,
            marginTop: 52,
            flexWrap: "wrap",
            justifyContent: "center",
            animation: "fadeUp .8s .5s ease both",
          }}
        >
          {[["Preguntas", "100+"], ["Categorías", "7"], ["Eficiencia", "100%"]].map(
            ([label, val]) => (
              <div
                key={label}
                style={{
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  gap: 4,
                  padding: "14px 24px",
                  background: "rgba(21, 4, 90, 0.55)",
                  border: "1px solid rgba(99, 60, 255, 0.54)",
                  borderRadius: 14,
                  backdropFilter: "blur(10px)",
                }}
              >
                <span style={{ fontSize: 22, fontWeight: 900, color: "#ddd0ff" }}>
                  {val}
                </span>
                <span
                  style={{
                    fontFamily: "'Space Mono',monospace",
                    fontSize: 9,
                    letterSpacing: "2px",
                    textTransform: "uppercase",
                    color: "rgba(159, 130, 255, 0.92)",
                  }}
                >
                  {label}
                </span>
              </div>
            )
          )}
        </div>
      </section>

      <div
        style={{
          position: "relative",
          zIndex: 5,
          height: 1,
          background:
            "linear-gradient(90deg,transparent,rgba(100,60,255,.3),transparent)",
          margin: "0 24px",
        }}
      />

      <section
        style={{
          position: "relative",
          zIndex: 5,
          padding: "60px 24px 80px",
          maxWidth: 1100,
          margin: "0 auto",
        }}
      >
        <div style={{ textAlign: "center", marginBottom: 48 }}>
          <h2
            style={{
              color: "#ddd0ff",
              fontSize: 28,
              fontWeight: 800,
              margin: 0,
            }}
          >
            Todo lo que necesitas para prepararte
          </h2>
        </div>

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(240px, 1fr))",
            gap: 20,
          }}
        >
          {FEATURES.map((f, i) => (
            <div
              key={f.title}
              className="feat-card"
              style={{
                background: "rgba(14,10,28,.88)",
                border: "1px solid rgba(100,60,255,.18)",
                borderRadius: 18,
                padding: "28px 24px",
                backdropFilter: "blur(16px)",
                animation: `fadeUp .6s ${0.1 * i + 0.2}s ease both`,
              }}
            >
              <div style={{ color: "#9973fb", fontSize: 32, marginBottom: 16 }}>
                {f.icon}
              </div>
              <h3
                style={{
                  color: "#ddd0ff",
                  fontSize: 15,
                  fontWeight: 800,
                  marginBottom: 10,
                  letterSpacing: 0.3,
                }}
              >
                {f.title}
              </h3>
              <p
                style={{
                  color: "rgba(180,160,255,.55)",
                  fontSize: 13,
                  lineHeight: 1.7,
                  margin: 0,
                }}
              >
                {f.desc}
              </p>
            </div>
          ))}
        </div>
      </section>

      <div
        style={{
          position: "relative",
          zIndex: 5,
          height: 1,
          background:
            "linear-gradient(90deg,transparent,rgba(100,60,255,.3),transparent)",
          margin: "0 24px",
        }}
      />

      <section
        style={{
          position: "relative",
          zIndex: 5,
          padding: "60px 24px 100px",
          maxWidth: 1150,
          margin: "0 auto",
        }}
      >
        <div style={{ textAlign: "center", marginBottom: 20 }}>
          <h2
            style={{
              color: "#ddd0ff",
              fontSize: 30,
              fontWeight: 800,
              margin: 0,
            }}
          >
            Explora los submódulos
          </h2>
        </div>

        <p
          style={{
            textAlign: "center",
            maxWidth: 760,
            margin: "0 auto 40px",
            color: "rgba(190,170,255,.7)",
            lineHeight: 1.8,
            fontSize: 15,
          }}
        >
          Haz clic en cada tarjeta para ver más información y acceder
          rápidamente a las funciones principales de la plataforma.
        </p>

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
            gap: 20,
          }}
        >
          {SUBMODULES.map((module, index) => {
            const isOpen = openModule === module.id;

            return (
              <div
                key={module.id}
                className="sub-card"
                style={{
                  background: "rgba(14,10,28,.9)",
                  border: "1px solid rgba(100,60,255,.2)",
                  borderRadius: 20,
                  padding: 20,
                  backdropFilter: "blur(16px)",
                  animation: `fadeUp .6s ${0.1 * index + 0.15}s ease both`,
                }}
              >
                <button
                  onClick={() => toggleModule(module.id)}
                  style={{
                    width: "100%",
                    background: "transparent",
                    border: "none",
                    cursor: "pointer",
                    textAlign: "left",
                    padding: 0,
                  }}
                >
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "flex-start",
                      gap: 12,
                    }}
                  >
                    <div style={{ display: "flex", gap: 14, alignItems: "flex-start" }}>
                      <div
                        style={{
                          minWidth: 52,
                          minHeight: 52,
                          display: "flex",
                          alignItems: "center",
                          justifyContent: "center",
                          borderRadius: 14,
                          background: "rgba(92, 52, 210, 0.18)",
                          color: "#a884ff",
                          border: "1px solid rgba(120,80,255,.25)",
                        }}
                      >
                        {module.icon}
                      </div>

                      <div>
                        <h3
                          style={{
                            color: "#e3d7ff",
                            fontSize: 17,
                            margin: "0 0 8px 0",
                            fontWeight: 800,
                          }}
                        >
                          {module.title}
                        </h3>

                        <p
                          style={{
                            margin: 0,
                            color: "rgba(188,168,255,.62)",
                            fontSize: 13,
                            lineHeight: 1.7,
                          }}
                        >
                          {module.shortDesc}
                        </p>
                      </div>
                    </div>

                    <ChevronRight
                      size={20}
                      className={`sub-arrow ${isOpen ? "open" : ""}`}
                      style={{ color: "#b092ff", marginTop: 4 }}
                    />
                  </div>
                </button>

                <div
                  className="sub-content"
                  style={{
                    maxHeight: isOpen ? 220 : 0,
                    opacity: isOpen ? 1 : 0,
                    marginTop: isOpen ? 18 : 0,
                    paddingTop: isOpen ? 18 : 0,
                    borderTop: isOpen
                      ? "1px solid rgba(100,60,255,.18)"
                      : "1px solid transparent",
                  }}
                >
                  <p
                    style={{
                      margin: "0 0 18px 0",
                      color: "rgba(215,205,255,.72)",
                      fontSize: 14,
                      lineHeight: 1.8,
                    }}
                  >
                    {module.longDesc}
                  </p>

                  <button
                    onClick={() => navigateTo(module.path)}
                    className="cta-btn"
                    style={{
                      ...BUTTON_STYLES.header,
                      width: "100%",
                      height: 42,
                      fontSize: 10,
                    }}
                  >
                    {module.cta}
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      </section>
    </main>
  );
}