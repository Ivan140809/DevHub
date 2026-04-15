"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import Navbar from "../../components/Navbar";

type Dificultad = "EASY" | "MEDIUM" | "HARD";
type Categoria =
  | "FrontEnd"
  | "BackEnd"
  | "DB"
  | "IA"
  | "Ciberseguridad"
  | "Estructuras";

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "80%", d: "10s", dl: "-2s", s: 4 },
];

export default function AddQuestionPage() {
  const router = useRouter();

  const [titulo, setTitulo] = useState("");
  const [enunciado, setEnunciado] = useState("");
  const [opciones, setOpciones] = useState(["", "", ""]);
  const [correcta, setCorrecta] = useState<number | null>(null);
  const [dificultad, setDificultad] = useState<Dificultad | null>(null);
  const [categoria, setCategoria] = useState<Categoria | null>(null);
  const [loading, setLoading] = useState(false);
  const [mounted, setMounted] = useState(false);

  useEffect(() => setMounted(true), []);

  const letters = ["A", "B", "C"];

  const categoryMap: Record<Categoria, string> = {
    FrontEnd: "FRONTEND",
    BackEnd: "BACKEND",
    DB: "DB",
    IA: "IA",
    Ciberseguridad: "CIBERSEGURIDAD",
    Estructuras: "ESTRUCTURAS",
  };

  function updateOpt(i: number, val: string) {
    const next = [...opciones];
    next[i] = val;
    setOpciones(next);
  }

  async function submit() {
    if (
      !titulo.trim() ||
      !enunciado.trim() ||
      opciones.some((o) => !o.trim()) ||
      correcta === null ||
      !dificultad ||
      !categoria
    ) {
      alert("Completa todos los campos, selecciona categoría, dificultad y la respuesta correcta.");
      return;
    }

    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

    const body = {
  title: titulo,
  statement: enunciado,
  category: categoryMap[categoria],
  difficulty: dificultad,
  options: opciones.map((texto, i) => ({
    text: texto,
    correct: i === correcta,
  })),
};

    setLoading(true);

    try {
      const token = localStorage.getItem("token");

      const res = await fetch(`${BASE_URL}/questions`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: JSON.stringify(body),
      });

      if (!res.ok) {
        const errorText = await res.text();
        console.error("ERROR BACKEND:", errorText);
        throw new Error(`Error ${res.status}`);
      }

      router.push("/question");
    } catch (error) {
      console.error("Error al guardar pregunta:", error);
      alert("No se pudo guardar la pregunta.");
    } finally {
      setLoading(false);
    }
  }

  const difColors: Record<Dificultad, React.CSSProperties> = {
    EASY: {
      background: "rgba(30,160,100,.12)",
      color: "rgba(80,220,150,.8)",
      border: "1px solid rgba(30,160,100,.2)",
    },
    MEDIUM: {
      background: "rgba(200,140,20,.1)",
      color: "rgba(240,190,60,.8)",
      border: "1px solid rgba(200,140,20,.2)",
    },
    HARD: {
      background: "rgba(200,40,40,.1)",
      color: "rgba(240,100,100,.8)",
      border: "1px solid rgba(200,40,40,.2)",
    },
  };

  const catColors: Record<Categoria, React.CSSProperties> = {
    FrontEnd: {
      background: "rgba(160, 60, 30, 0.12)",
      color: "rgba(220, 150, 80, 0.8)",
      border: "1px solid rgba(160, 99, 30, 0.3)",
    },
    BackEnd: {
      background: "rgba(179, 200, 40, 0.1)",
      color: "rgba(240, 226, 100, 0.8)",
      border: "1px solid rgba(168, 200, 40, 0.3)",
    },
    DB: {
      background: "rgba(100,100,200,.1)",
      color: "rgba(128, 246, 112, 0.8)",
      border: "1px solid rgba(100, 200, 112, 0.3)",
    },
    IA: {
      background: "rgba(200,100,200,.1)",
      color: "rgba(106, 223, 250, 0.8)",
      border: "1px solid rgba(39, 186, 235, 0.3)",
    },
    Ciberseguridad: {
      background: "rgba(160, 60, 30, 0.12)",
      color: "rgba(115, 1, 255, 0.8)",
      border: "1px solid rgba(144, 34, 227, 0.3)",
    },
    Estructuras: {
      background: "rgba(160, 60, 30, 0.12)",
      color: "rgba(249, 90, 238, 0.8)",
      border: "1px solid rgba(217, 34, 238, 0.3)",
    },
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

        @keyframes slideIn {
          0% { opacity: 0; transform: translateY(28px); }
          100% { opacity: 1; transform: translateY(0); }
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

        .fta:focus,
        .finp:focus {
          border-color: rgba(100,60,255,.5) !important;
          outline: none;
        }
      `}</style>

      {mounted && (
        <>
          <div
            style={{
              position: "fixed",
              borderRadius: "50%",
              width: 500,
              height: 500,
              background: "radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)",
              top: -150,
              left: -150,
              animation: "orbFloat 10s ease-in-out infinite alternate",
              pointerEvents: "none",
              zIndex: 0,
            }}
          />
          <div
            style={{
              position: "fixed",
              borderRadius: "50%",
              width: 400,
              height: 400,
              background: "radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)",
              bottom: -100,
              right: -100,
              animation: "orbFloat 10s ease-in-out infinite alternate",
              animationDelay: "-5s",
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
        </>
      )}

      <Navbar />

      <section
        style={{
          position: "relative",
          zIndex: 5,
          padding: "28px 24px",
          display: "flex",
          flexDirection: "column",
          gap: 20,
          animation: "slideIn .35s ease",
        }}
      >
        <div
          style={{
            background: "rgba(14,10,28,.88)",
            border: "1px solid rgba(100,60,255,.2)",
            borderRadius: 20,
            padding: 28,
            backdropFilter: "blur(16px)",
            display: "flex",
            flexDirection: "column",
            gap: 24,
          }}
        >
          <div>
            <label
              style={{
                fontWeight: 700,
                fontSize: 14,
                color: "#ddd0ff",
                display: "block",
                marginBottom: 6,
              }}
            >
              Título
              <span
                style={{
                  display: "block",
                  fontSize: 12,
                  color: "rgba(159, 130, 255, 0.87)",
                  fontWeight: 400,
                  marginTop: 2,
                  fontFamily: "'Space Mono', monospace",
                }}
              >
                Título corto de la pregunta.
              </span>
            </label>
            <input
              className="finp"
              type="text"
              value={titulo}
              onChange={(e) => setTitulo(e.target.value)}
              placeholder="Características de Docker"
              style={{
                width: "100%",
                height: 38,
                background: "rgba(255,255,255,.04)",
                border: "1px solid rgba(100,60,255,.2)",
                borderRadius: 12,
                padding: "0 16px",
                fontFamily: "'Syne', sans-serif",
                fontSize: 14,
                color: "#e0d4ff",
              }}
            />
          </div>

          <div>
            <label
              style={{
                fontWeight: 700,
                fontSize: 14,
                color: "#ddd0ff",
                display: "block",
                marginBottom: 6,
              }}
            >
              Enunciado
              <span
                style={{
                  display: "block",
                  fontSize: 12,
                  color: "rgba(159, 130, 255, 0.87)",
                  fontWeight: 400,
                  marginTop: 2,
                  fontFamily: "'Space Mono', monospace",
                }}
              >
                Escribe la pregunta completa que deseas agregar.
              </span>
            </label>
            <textarea
              className="fta"
              value={enunciado}
              onChange={(e) => setEnunciado(e.target.value)}
              placeholder="¿Cuál es la salida de console.log(typeof null)?"
              style={{
                width: "100%",
                height: 80,
                background: "rgba(255,255,255,.04)",
                border: "1px solid rgba(100,60,255,.2)",
                borderRadius: 12,
                padding: "12px 16px",
                fontFamily: "'Syne', sans-serif",
                fontSize: 14,
                color: "#e0d4ff",
                resize: "vertical",
                lineHeight: 1.6,
              }}
            />
          </div>

          <div style={{ display: "flex", gap: 32, flexWrap: "wrap" }}>
            <div style={{ flex: 1, minWidth: 220 }}>
              <label
                style={{
                  fontWeight: 700,
                  fontSize: 14,
                  color: "#ddd0ff",
                  display: "block",
                  marginBottom: 10,
                }}
              >
                Opciones de respuesta
                <span
                  style={{
                    display: "block",
                    fontSize: 12,
                    color: "rgba(159, 130, 255, 0.87)",
                    fontWeight: 400,
                    marginTop: 2,
                    fontFamily: "'Space Mono', monospace",
                  }}
                >
                  Haz clic en la letra para marcarla como correcta.
                </span>
              </label>

              <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                {opciones.map((opt, i) => (
                  <div
                    key={i}
                    style={{ display: "flex", alignItems: "center", gap: 12, cursor: "pointer" }}
                    onClick={() => setCorrecta(i)}
                  >
                    <div
                      style={{
                        width: 36,
                        height: 36,
                        borderRadius: "50%",
                        border: "1.5px solid",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        fontFamily: "'Space Mono', monospace",
                        fontSize: 13,
                        fontWeight: 700,
                        flexShrink: 0,
                        transition: "all .15s",
                        ...(correcta === i
                          ? {
                              background: "rgba(30,160,100,.12)",
                              borderColor: "rgba(80,220,150,.8)",
                              color: "#fefeff",
                            }
                          : {
                              borderColor: "rgba(100,60,255,.3)",
                              color: "rgba(236, 230, 254, 0.93)",
                              background: "transparent",
                            }),
                      }}
                    >
                      {letters[i]}
                    </div>

                    <input
                      className="finp"
                      type="text"
                      value={opt}
                      onChange={(e) => updateOpt(i, e.target.value)}
                      onClick={(e) => e.stopPropagation()}
                      placeholder={`Opción ${letters[i]}`}
                      style={{
                        flex: 1,
                        height: 38,
                        background: "rgba(255,255,255,.04)",
                        border: `1px solid ${
                          correcta === i ? "rgba(100,60,255,.4)" : "rgba(100,60,255,.2)"
                        }`,
                        borderRadius: 999,
                        padding: "0 16px",
                        fontFamily: "'Syne', sans-serif",
                        fontSize: 13,
                        color: "#e0d4ff",
                        transition: "border-color .2s",
                      }}
                    />

                    {correcta === i && (
                      <span
                        style={{
                          fontFamily: "'Space Mono', monospace",
                          fontSize: 10,
                          letterSpacing: "1.5px",
                          textTransform: "uppercase",
                          color: "rgba(80,220,150,.8)",
                          flexShrink: 0,
                        }}
                      >
                        correcta
                      </span>
                    )}
                  </div>
                ))}
              </div>
            </div>

            <div style={{ display: "flex", flexDirection: "column", gap: 20, minWidth: 200 }}>
              <div>
                <label
                  style={{
                    fontWeight: 700,
                    fontSize: 14,
                    color: "#ddd0ff",
                    display: "block",
                    marginBottom: 6,
                  }}
                >
                  Categoría
                </label>

                <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                  {(
                    ["FrontEnd", "BackEnd", "DB", "IA", "Ciberseguridad", "Estructuras"] as Categoria[]
                  ).map((d) => (
                    <div
                      key={d}
                      onClick={() => setCategoria(d)}
                      style={{
                        display: "flex",
                        alignItems: "center",
                        cursor: "pointer",
                        padding: "6px 16px",
                        border: "1px solid",
                        borderRadius: 999,
                        fontSize: 13,
                        transition: "all .15s",
                        marginBottom: 8,
                        fontWeight: categoria === d ? 700 : 400,
                        ...(categoria === d
                          ? catColors[d]
                          : {
                              background: "rgba(255,255,255,.03)",
                              borderColor: "rgba(100,60,255,.25)",
                              color: "rgba(193, 173, 244, 0.94)",
                            }),
                      }}
                    >
                      {d}
                    </div>
                  ))}
                </div>
              </div>

              <div>
                <label
                  style={{
                    fontWeight: 700,
                    fontSize: 14,
                    color: "#ddd0ff",
                    display: "block",
                    marginBottom: 10,
                  }}
                >
                  Dificultad
                </label>

                <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                  {(["EASY", "MEDIUM", "HARD"] as Dificultad[]).map((d) => (
                    <div
                      key={d}
                      onClick={() => setDificultad(d)}
                      style={{
                        display: "flex",
                        alignItems: "center",
                        cursor: "pointer",
                        padding: "6px 16px",
                        border: "1px solid",
                        borderRadius: 999,
                        fontSize: 13,
                        transition: "all .15s",
                        fontWeight: dificultad === d ? 700 : 400,
                        ...(dificultad === d
                          ? difColors[d]
                          : {
                              background: "rgba(255,255,255,.03)",
                              borderColor: "rgba(100,60,255,.25)",
                              color: "rgba(193, 173, 244, 0.94)",
                            }),
                      }}
                    >
                      {d === "EASY" ? "Fácil" : d === "HARD" ? "Difícil" : "Media"}
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          <div style={{ display: "flex", justifyContent: "center", marginTop: 8 }}>
            <button
              onClick={submit}
              disabled={loading}
              style={{
                height: 40,
                padding: "0 32px",
                background: loading
                  ? "rgba(100,60,255,.3)"
                  : "linear-gradient(135deg,#7040ff,#5020e0)",
                border: "none",
                borderRadius: 10,
                color: "white",
                fontFamily: "'Syne', sans-serif",
                fontSize: 11,
                fontWeight: 800,
                letterSpacing: "4px",
                textTransform: "uppercase",
                cursor: loading ? "not-allowed" : "pointer",
                boxShadow: "0 4px 16px rgba(90,40,220,.35)",
              }}
            >
              {loading ? "Guardando" : "Agregar pregunta"}
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}