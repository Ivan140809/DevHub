"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import Navbar from "../../components/Navbar";

type OpcionDTO = {
  text: string;
  correct: boolean;
};

type Pregunta = {
  id: string;
  title: string;
  statement: string;
  category: string;
  difficulty: "EASY" | "MEDIUM" | "HARD";
  options: OpcionDTO[];
};

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 },
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 },
];

function diffLabel(d: string) {
  if (d === "EASY") return "Fácil";
  if (d === "HARD") return "Difícil";
  return "Media";
}

function diffStyle(d: string): React.CSSProperties {
  if (d === "EASY") {
    return {
      background: "rgba(30,160,100,.12)",
      color: "rgba(80,220,150,.8)",
      border: "1px solid rgba(30,160,100,.2)",
    };
  }
  if (d === "HARD") {
    return {
      background: "rgba(200,40,40,.1)",
      color: "rgba(240,100,100,.8)",
      border: "1px solid rgba(200,40,40,.2)",
    };
  }
  return {
    background: "rgba(200,140,20,.1)",
    color: "rgba(240,190,60,.8)",
    border: "1px solid rgba(200,140,20,.2)",
  };
}

const letters = ["A", "B", "C", "D", "E"];

export default function QuestionDetailPage() {
  const router = useRouter();
  const params = useParams();
  const id = params?.id as string;

  const [pregunta, setPregunta] = useState<Pregunta | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selected, setSelected] = useState<number | null>(null);
  const [answered, setAnswered] = useState(false);
  const [confirming, setConfirming] = useState(false);
  const [isCorrect, setIsCorrect] = useState<boolean | null>(null);
  const [answerError, setAnswerError] = useState<string | null>(null);
  const [comentario, setComentario] = useState("");
  const [rating, setRating] = useState<number>(5);
  const [comentarioEnviado, setComentarioEnviado] = useState(false);
  const [comentarioLoading, setComentarioLoading] = useState(false);
  const [comentarioError, setComentarioError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;

    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const ENDPOINT = `${BASE_URL}/questions/${id}`;

    fetch(ENDPOINT)
      .then((res) => {
        if (!res.ok) throw new Error(`Error ${res.status}`);
        return res.json();
      })
      .then((data: Pregunta) => {
        setSelected(null);
        setAnswered(false);
        setPregunta(data);
        setError(null);
      })
      .catch((error) => {
        console.error("Error fetching question:", error);
        setError("No se pudo cargar la pregunta. Verifica tu conexión.");
        setPregunta(null);
      })
      .finally(() => setLoading(false));
  }, [id]);

  const correctaIdx = pregunta?.options.findIndex((o) => o.correct) ?? -1;

  async function enviarComentario() {
    if (!comentario.trim() || !id) return;

    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const token = localStorage.getItem("token");

    setComentarioLoading(true);
    setComentarioError(null);

    try {
      const res = await fetch(`${BASE_URL}/questions/${id}/reviews`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
        body: JSON.stringify({ comment: comentario, rating }),
      });

      if (!res.ok) {
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || "Error al enviar comentario");
      }

      setComentarioEnviado(true);
      setComentario("");
    } catch (error: Error | unknown) {
      const message = error instanceof Error ? error.message : "No se pudo enviar el comentario. Intenta de nuevo.";
      setComentarioError(message || "No se pudo enviar el comentario. Intenta de nuevo.");
    } finally {
      setComentarioLoading(false);
    }
  }

  async function confirmarRespuesta() {
    if (selected === null || !id) return;

    const token = localStorage.getItem("token");
    if (!token) {
      setAnswerError("Necesitas iniciar sesión para responder.");
      return;
    }

    setAnswerError(null);
    setConfirming(true);

    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const requestUrl = `${BASE_URL}/questions/${id}/answer`;
    const requestBody = {
      questionId: id,
      selectedOption: pregunta?.options[selected]?.text,
    };
    console.log("POST answer request:", requestUrl, requestBody);

    try {
      const res = await fetch(requestUrl, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(requestBody),
      });

      if (!res.ok) {
        if (res.status === 401) {
          setAnswerError("Debes iniciar sesión para enviar la respuesta.");
          return;
        }
        if (res.status === 404) {
          setAnswerError("Pregunta no encontrada o no disponible.");
          return;
        }
        const errorData = await res.json().catch(() => ({}));
        throw new Error(errorData.message || `Error ${res.status}`);
      }

      const result = await res.json();
      setIsCorrect(result.correct);
      setAnswered(true);

      const storedUser = localStorage.getItem("devhub_user");
      if (storedUser) {
        try {
          const parsed = JSON.parse(storedUser);
          const currentScore = result.totalScore ?? parsed.totalScore ?? parsed.puntosAcumulados ?? 0;
          parsed.totalScore = currentScore;
          parsed.puntosAcumulados = currentScore;
          localStorage.setItem("devhub_user", JSON.stringify(parsed));
          window.dispatchEvent(new Event("devhub-user-updated"));
          window.dispatchEvent(new Event("devhub-profile-refresh"));
        } catch (e) {
          console.error("Error actualizando puntos en localStorage:", e);
        }
      }
    } catch (error: Error | unknown) {
      const message = error instanceof Error ? error.message : "No se pudo enviar la respuesta. Intenta de nuevo.";
      setAnswerError(message || "No se pudo enviar la respuesta. Intenta de nuevo.");
      console.error("Error enviando respuesta:", error);
    } finally {
      setConfirming(false);
    }
  }

  function optStyle(i: number): React.CSSProperties {
    if (!answered) {
      return selected === i
        ? {
            background: "rgba(100,60,255,.2)",
            borderColor: "rgba(140,80,255,.6)",
            transform: "translateX(6px) scale(1.01)",
            boxShadow: "0 0 20px rgba(120,60,255,.3), inset 0 0 20px rgba(100,60,255,.08)",
          }
        : {
            background: "rgba(255,255,255,.03)",
            borderColor: "rgba(100,60,255,.2)",
          };
    }

    if (i === correctaIdx) {
      return {
        background: "rgba(30,160,100,.15)",
        borderColor: "rgba(80,200,130,.5)",
      };
    }

    if (i === selected) {
      return {
        background: "rgba(200,40,40,.1)",
        borderColor: "rgba(220,80,80,.4)",
      };
    }

    return {
      background: "rgba(255,255,255,.03)",
      borderColor: "rgba(100,60,255,.1)",
    };
  }

  function letterStyle(i: number): React.CSSProperties {
    if (!answered) {
      return {
        borderColor: "rgba(100,60,255,.4)",
        color: "#b8a0ff",
        background: "transparent",
      };
    }

    if (i === correctaIdx) {
      return {
        borderColor: "rgba(80,200,130,.6)",
        color: "rgba(80,220,150,.9)",
        background: "rgba(30,160,100,.15)",
      };
    }

    if (i === selected) {
      return {
        borderColor: "rgba(220,80,80,.5)",
        color: "rgba(240,100,100,.8)",
        background: "transparent",
      };
    }

    return {
      borderColor: "rgba(100,60,255,.2)",
      color: "rgba(140,100,255,.4)",
      background: "transparent",
    };
  }

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
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp  { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn  { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes popIn    { 0%{opacity:0;transform:scale(.92)} 100%{opacity:1;transform:scale(1)} }
        @keyframes pulse    { 0%,100%{opacity:.3} 50%{opacity:.7} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .opt-btn { transition: all .2s cubic-bezier(.34,1.56,.64,1) !important; }
        .opt-btn:hover:not(:disabled) { transform: translateX(4px); }
      `}</style>

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
            style={{ width: p.s, height: p.s, left: p.l, animationDuration: p.d, animationDelay: p.dl }}
          />
        ))}
      </>

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
        {error && (
          <div
            style={{
              background: "rgba(200,140,20,.08)",
              border: "1px solid rgba(200,140,20,.2)",
              borderRadius: 10,
              padding: "10px 16px",
              fontFamily: "'Space Mono', monospace",
              fontSize: 11,
              color: "rgba(240,190,60,.7)",
            }}
          >
            {error}
          </div>
        )}

        {loading ? (
          <div
            style={{
              background: "rgba(14,10,28,.88)",
              border: "1px solid rgba(100,60,255,.2)",
              borderRadius: 20,
              padding: 24,
              display: "flex",
              flexDirection: "column",
              gap: 16,
            }}
          >
            <div style={{ display: "flex", gap: 16 }}>
              <div
                style={{
                  width: 50,
                  height: 40,
                  borderRadius: 4,
                  background: "rgba(100,60,255,.1)",
                  animation: "pulse 1.5s infinite",
                }}
              />
              <div
                style={{
                  flex: 1,
                  height: 40,
                  borderRadius: 4,
                  background: "rgba(100,60,255,.1)",
                  animation: "pulse 1.5s infinite",
                }}
              />
            </div>
            {[1, 2, 3].map((i) => (
              <div
                key={i}
                style={{
                  height: 56,
                  borderRadius: 14,
                  background: "rgba(100,60,255,.07)",
                  animation: "pulse 1.5s infinite",
                }}
              />
            ))}
          </div>
        ) : (
          pregunta && (
            <>
              <div
                style={{
                  background: "rgba(14,10,28,.88)",
                  border: "1px solid rgba(100,60,255,.2)",
                  borderRadius: 20,
                  padding: 24,
                  backdropFilter: "blur(16px)",
                  display: "flex",
                  flexDirection: "column",
                  gap: 16,
                }}
              >
                <div style={{ display: "flex", alignItems: "flex-start", gap: 16 }}>
                  <span
                    style={{
                      fontFamily: "'Space Mono', monospace",
                      fontSize: 38,
                      fontWeight: 700,
                      color: "rgba(140,100,255,.2)",
                      lineHeight: 1,
                      flexShrink: 0,
                    }}
                  >
                    {String(pregunta.id).slice(-2).padStart(2, "0")}
                  </span>
                  <p style={{ color: "#e0d4ff", fontSize: 19, fontWeight: 800, lineHeight: 1.4, flex: 1 }}>
                    {pregunta.statement}
                  </p>
                </div>

                <div style={{ height: 1, background: "rgba(100,60,255,.15)" }} />

                <div style={{ display: "flex", gap: 10, flexWrap: "wrap", alignItems: "center" }}>
                  <span
                    style={{
                      fontFamily: "'Space Mono', monospace",
                      fontSize: 10,
                      letterSpacing: "2px",
                      textTransform: "uppercase",
                      color: "rgba(186, 166, 250, 0.4)",
                    }}
                  >
                    Categoría
                  </span>

                  <span
                    style={{
                      fontFamily: "'Space Mono', monospace",
                      fontSize: 10,
                      letterSpacing: "1.5px",
                      textTransform: "uppercase",
                      padding: "3px 12px",
                      borderRadius: 999,
                      fontWeight: 700,
                      background: "rgba(100,60,255,.15)",
                      color: "rgba(180,150,255,.7)",
                      border: "1px solid rgba(100,60,255,.2)",
                    }}
                  >
                    {pregunta.category}
                  </span>

                  <span
                    style={{
                      fontFamily: "'Space Mono', monospace",
                      fontSize: 10,
                      letterSpacing: "2px",
                      textTransform: "uppercase",
                      color: "rgba(186, 166, 250, 0.4)",
                      marginLeft: 8,
                    }}
                  >
                    Dificultad
                  </span>

                  <span
                    style={{
                      fontFamily: "'Space Mono', monospace",
                      fontSize: 10,
                      letterSpacing: "1.5px",
                      textTransform: "uppercase",
                      padding: "3px 12px",
                      borderRadius: 999,
                      fontWeight: 700,
                      ...diffStyle(pregunta.difficulty),
                    }}
                  >
                    {diffLabel(pregunta.difficulty)}
                  </span>
                </div>
              </div>

              <span
                style={{
                  fontFamily: "'Space Mono', monospace",
                  fontSize: 10,
                  letterSpacing: "3px",
                  textTransform: "uppercase",
                  color: "rgba(231, 224, 255, 0.4)",
                }}
              >
                Selecciona la respuesta correcta
              </span>

              <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
                {pregunta.options.map((opt, i) => (
                  <button
                    key={i}
                    className="opt-btn"
                    disabled={answered}
                    onClick={() => setSelected(i)}
                    style={{
                      width: "100%",
                      display: "flex",
                      alignItems: "center",
                      gap: 16,
                      padding: "14px 20px",
                      border: "1px solid",
                      borderRadius: 14,
                      cursor: answered ? "default" : "pointer",
                      textAlign: "left",
                      animation: `popIn .3s ease ${i * 0.07}s both`,
                      ...optStyle(i),
                    }}
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
                        ...letterStyle(i),
                      }}
                    >
                      {letters[i]}
                    </div>

                    <span style={{ color: "#ddd0ff", fontSize: 14, fontWeight: 700, lineHeight: 1.4, flex: 1 }}>
                      {opt.text}
                    </span>

                    {answered && i === correctaIdx && (
                      <span
                        style={{
                          fontFamily: "'Space Mono', monospace",
                          fontSize: 10,
                          letterSpacing: "1px",
                          textTransform: "uppercase",
                          padding: "3px 10px",
                          borderRadius: 999,
                          background: "rgba(30,160,100,.2)",
                          color: "rgba(80,220,150,.9)",
                          border: "1px solid rgba(30,160,100,.25)",
                          flexShrink: 0,
                        }}
                      >
                        Correcta
                      </span>
                    )}

                    {answered && i === selected && i !== correctaIdx && (
                      <span
                        style={{
                          fontFamily: "'Space Mono', monospace",
                          fontSize: 10,
                          letterSpacing: "1px",
                          textTransform: "uppercase",
                          padding: "3px 10px",
                          borderRadius: 999,
                          background: "rgba(200,40,40,.12)",
                          color: "rgba(240,100,100,.8)",
                          border: "1px solid rgba(200,40,40,.2)",
                          flexShrink: 0,
                        }}
                      >
                        Incorrecta
                      </span>
                    )}
                  </button>
                ))}
              </div>

              {selected !== null && !answered && (
                <button
                  onClick={confirmarRespuesta}
                  disabled={confirming}
                  style={{
                    alignSelf: "center",
                    height: 40,
                    padding: "0 32px",
                    background: confirming ? "rgba(100,60,255,.2)" : "linear-gradient(135deg,#7040ff,#5020e0)",
                    border: "none",
                    borderRadius: 12,
                    color: "white",
                    fontFamily: "'Syne', sans-serif",
                    fontSize: 12,
                    fontWeight: 800,
                    letterSpacing: "2px",
                    textTransform: "uppercase",
                    cursor: confirming ? "not-allowed" : "pointer",
                    boxShadow: "0 4px 16px rgba(90,40,220,.25)",
                  }}
                >
                  {confirming ? "Enviando..." : "Confirmar Respuesta"}
                </button>
              )}

              {answerError && (
                <span
                  style={{
                    marginTop: 10,
                    color: "rgba(240,100,100,.85)",
                    fontFamily: "'Space Mono', monospace",
                    fontSize: 12,
                  }}
                >
                  {answerError}
                </span>
              )}

              {answered && (
                <div
                  style={{
                    borderRadius: 14,
                    padding: "16px 20px",
                    display: "flex",
                    flexDirection: "column",
                    gap: 6,
                    ...(isCorrect
                      ? {
                          background: "rgba(30,160,100,.1)",
                          border: "1px solid rgba(30,160,100,.2)",
                        }
                      : {
                          background: "rgba(200,40,40,.08)",
                          border: "1px solid rgba(200,40,40,.2)",
                        }),
                  }}
                >
                  <span
                    style={{
                      fontWeight: 800,
                      fontSize: 15,
                      color: isCorrect ? "rgba(80,220,150,.9)" : "rgba(240,100,100,.8)",
                    }}
                  >
                    {isCorrect ? "¡Correcto!" : "Incorrecto"}
                  </span>

                  <span style={{ fontSize: 13, color: "rgba(200,180,255,.65)", lineHeight: 1.6 }}>
                    {isCorrect
                      ? "Muy bien. Respondiste correctamente."
                      : `La respuesta correcta era: ${pregunta.options[correctaIdx]?.text}`}
                  </span>
                </div>
              )}

              <div
                style={{
                  background: "rgba(14,10,28,.88)",
                  border: "1px solid rgba(100,60,255,.2)",
                  borderRadius: 20,
                  padding: 24,
                  backdropFilter: "blur(16px)",
                  display: "flex",
                  flexDirection: "column",
                  gap: 14,
                }}
              >
                <span
                  style={{
                    fontFamily: "'Space Mono', monospace",
                    fontSize: 10,
                    letterSpacing: "2px",
                    textTransform: "uppercase",
                    color: "rgba(160,130,255,.5)",
                  }}
                >
                  Deja un comentario
                </span>

                {comentarioEnviado ? (
                  <div
                    style={{
                      padding: "12px 16px",
                      borderRadius: 12,
                      background: "rgba(30,160,100,.08)",
                      border: "1px solid rgba(30,160,100,.2)",
                      fontFamily: "'Space Mono', monospace",
                      fontSize: 12,
                      color: "rgba(80,220,150,.8)",
                    }}
                  >
                    ✓ Comentario enviado
                  </div>
                ) : (
                  <div style={{ display: "flex", flexDirection: "column", gap: 10 }}>
                    <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
                      <span
                        style={{
                          fontFamily: "'Space Mono', monospace",
                          fontSize: 10,
                          letterSpacing: "1.5px",
                          textTransform: "uppercase",
                          color: "rgba(160,130,255,.5)",
                        }}
                      >
                        Rating
                      </span>

                      {[1, 2, 3, 4, 5].map((n) => (
                        <span
                          key={n}
                          onClick={() => setRating(n)}
                          style={{
                            fontSize: 18,
                            cursor: "pointer",
                            opacity: n <= rating ? 1 : 0.25,
                            transition: "opacity .15s",
                          }}
                        >
                          ★
                        </span>
                      ))}
                    </div>

                    <textarea
                      value={comentario}
                      onChange={(e) => setComentario(e.target.value)}
                      placeholder="Escribe tu comentario sobre esta pregunta..."
                      rows={3}
                      style={{
                        width: "100%",
                        background: "rgba(255,255,255,.04)",
                        border: "1px solid rgba(100,60,255,.2)",
                        borderRadius: 12,
                        padding: "12px 16px",
                        fontFamily: "'Syne', sans-serif",
                        fontSize: 13,
                        color: "#e0d4ff",
                        resize: "vertical",
                        lineHeight: 1.6,
                        outline: "none",
                      }}
                    />

                    {comentarioError && (
                      <span
                        style={{
                          fontFamily: "'Space Mono', monospace",
                          fontSize: 11,
                          color: "rgba(240,100,100,.8)",
                        }}
                      >
                        {comentarioError}
                      </span>
                    )}

                    <button
                      onClick={enviarComentario}
                      disabled={!comentario.trim() || comentarioLoading}
                      style={{
                        alignSelf: "flex-end",
                        height: 36,
                        padding: "0 24px",
                        background: !comentario.trim()
                          ? "rgba(100,60,255,.2)"
                          : "linear-gradient(135deg,#7040ff,#5020e0)",
                        border: "none",
                        borderRadius: 10,
                        color: "white",
                        fontFamily: "'Syne', sans-serif",
                        fontSize: 11,
                        fontWeight: 800,
                        letterSpacing: "3px",
                        textTransform: "uppercase",
                        cursor: !comentario.trim() ? "not-allowed" : "pointer",
                        boxShadow: "0 4px 16px rgba(90,40,220,.25)",
                      }}
                    >
                      {comentarioLoading ? "Enviando..." : "Enviar"}
                    </button>
                  </div>
                )}
              </div>

              <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
                <button
                  onClick={() => router.push("/question")}
                  style={{
                    height: 40,
                    padding: "0 20px",
                    background: "rgba(100,60,255,.08)",
                    border: "1px solid rgba(100,60,255,.25)",
                    borderRadius: 10,
                    color: "rgba(180,150,255,.8)",
                    fontFamily: "'Syne', sans-serif",
                    fontSize: 11,
                    fontWeight: 800,
                    letterSpacing: "3px",
                    textTransform: "uppercase",
                    cursor: "pointer",
                  }}
                >
                  Volver al listado
                </button>
              </div>
            </>
          )
        )}
      </section>
    </main>
  );
}