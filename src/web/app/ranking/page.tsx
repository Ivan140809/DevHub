"use client";
import { useState, useEffect, useMemo } from "react";
import { Trophy, Medal, Award } from "lucide-react";
import Navbar from "../components/Navbar";

type RankingBackendUser = {
  position: number;
  username: string;
  email: string;
  totalScore: number;
};

type Usuario = {
  position: number;
  username: string;
  email: string;
  puntosAcumulados: number;
};

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 },
  { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 },
  { l: "80%", d: "10s", dl: "-2s", s: 4 },
];

function mapRankingUser(data: RankingBackendUser): Usuario {
  return {
    position: data.position,
    username: data.username ?? "sin_username",
    email: data.email ?? "",
    puntosAcumulados: data.totalScore ?? 0,
  };
}

export default function RankingPage() {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const nombre = useMemo(() => {
    try {
      const raw = localStorage.getItem("devhub_user");
      if (!raw) return null;
      const u = JSON.parse(raw) as { nombre?: string; username?: string };
      return u.nombre ?? u.username ?? null;
    } catch {
      return null;
    }
  }, []);

  useEffect(() => {
    const baseUrl = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const endpoint = `${baseUrl}/user/ranking?page=0`;

    fetch(endpoint, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((res) => {
        if (!res.ok) {
          throw new Error(`Error ${res.status}`);
        }
        return res.json();
      })
      .then((data: RankingBackendUser[]) => {
        const mappedUsers = data.map(mapRankingUser);
        setUsuarios(mappedUsers);
        setError(null);
      })
      .catch((err: unknown) => {
        console.error("Error cargando ranking:", err);
        setUsuarios([]);
        setError("Backend no disponible — no se pudieron cargar los usuarios.");
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  function getMedalIcon(pos: number) {
    if (pos === 1) return <Trophy size={20} color="#ffd700" />;
    if (pos === 2) return <Medal size={20} color="#c0c0c0" />;
    if (pos === 3) return <Award size={20} color="#cd7f32" />;
    return null;
  }

  function getPodiumStyle(pos: number): React.CSSProperties {
    if (pos === 1) {
      return {
        background:
          "linear-gradient(135deg, rgba(255, 217, 0, 0.33), rgba(255,215,0,.03))",
        border: "1px solid rgba(255,215,0,.25)",
      };
    }

    if (pos === 2) {
      return {
        background:
          "linear-gradient(135deg, rgba(192, 176, 176, 0.35), rgba(192,192,192,.03))",
        border: "1px solid rgba(192,192,192,.25)",
      };
    }

    if (pos === 3) {
      return {
        background:
          "linear-gradient(135deg, rgba(174, 104, 34, 0.35), rgba(205,127,50,.03))",
        border: "1px solid rgba(205,127,50,.25)",
      };
    }

    return {
      background: "rgba(14,10,28,.88)",
      border: "1px solid rgba(100,60,255,.15)",
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
        @keyframes floatUp { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes pulse { 0%,100%{opacity:.25} 50%{opacity:.5} }
        .dh-particle {
          position: fixed;
          border-radius: 50%;
          background: rgba(160,100,255,.7);
          bottom: -10px;
          animation: floatUp linear infinite;
          pointer-events: none;
          z-index: 0;
        }
        .rank-row:hover {
          background: rgba(100,60,255,.12) !important;
          transform: translateX(6px);
          border-color: rgba(100,60,255,.25) !important;
        }
        .rank-row {
          transition: all .25s cubic-bezier(.16,1,.3,1);
        }
      `}</style>

      <div
        style={{
          position: "fixed",
          borderRadius: "50%",
          width: 500,
          height: 500,
          background:
            "radial-gradient(circle, rgba(90,30,200,.22) 0%, transparent 70%)",
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
          background:
            "radial-gradient(circle, rgba(110,50,255,.18) 0%, transparent 70%)",
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

      <Navbar />

      <section
        style={{
          position: "relative",
          zIndex: 5,
          padding: "40px 24px",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 28,
          animation: "slideIn .35s ease",
          maxWidth: 1200,
          margin: "0 auto",
          width: "100%",
        }}
      >
        {error && (
          <div
            style={{
              width: "100%",
              background: "rgba(200,140,20,.08)",
              border: "1px solid rgba(200,140,20,.2)",
              borderRadius: 12,
              padding: "12px 18px",
              fontFamily: "'Space Mono', monospace",
              fontSize: 11,
              letterSpacing: "1px",
              color: "rgba(240,190,60,.7)",
              textAlign: "center",
            }}
          >
            {error}
          </div>
        )}

        <div
          style={{
            width: "100%",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            gap: 16,
            marginBottom: 8,
          }}
        >
          <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
            <Trophy size={32} color="#b8a0ff" strokeWidth={2} />
            <h1
              style={{
                color: "#ddd0ff",
                fontSize: 28,
                fontWeight: 800,
                letterSpacing: 1,
                margin: 0,
              }}
            >
              Ranking de Usuarios
            </h1>
          </div>

          <span
            style={{
              fontFamily: "'Space Mono', monospace",
              fontSize: 11,
              letterSpacing: "2.5px",
              textTransform: "uppercase",
              color: "#bea8f4e4",
            }}
          >
            {loading
              ? "Cargando..."
              : `${usuarios.length} usuario${usuarios.length !== 1 ? "s" : ""} registrado${usuarios.length !== 1 ? "s" : ""}`}
          </span>

          {nombre && !loading && (
            <span
              style={{
                fontFamily: "'Space Mono', monospace",
                fontSize: 11,
                letterSpacing: "1.5px",
                color: "rgba(180,160,255,.7)",
              }}
            >
              Viendo como: {nombre}
            </span>
          )}
        </div>

        <div
          style={{
            width: "100%",
            background: "rgba(14,10,28,.92)",
            border: "1px solid rgba(100,60,255,.2)",
            borderRadius: 18,
            overflow: "hidden",
            backdropFilter: "blur(20px)",
            boxShadow: "0 8px 32px rgba(80,40,200,.15)",
          }}
        >
          {loading ? (
            [1, 2, 3, 4, 5].map((i) => (
              <div
                key={i}
                style={{
                  display: "flex",
                  alignItems: "center",
                  padding: "18px 24px",
                  borderBottom: "1px solid rgba(100,60,255,.08)",
                }}
              >
                <div
                  style={{
                    width: 50,
                    height: 14,
                    borderRadius: 6,
                    background: "rgba(100,60,255,.1)",
                    animation: "pulse 1.5s infinite",
                    marginRight: 20,
                  }}
                />
                <div
                  style={{
                    width: 40,
                    height: 40,
                    borderRadius: "50%",
                    background: "rgba(100,60,255,.1)",
                    animation: "pulse 1.5s infinite",
                    marginRight: 16,
                  }}
                />
                <div
                  style={{
                    flex: 1,
                    display: "flex",
                    flexDirection: "column",
                    gap: 8,
                  }}
                >
                  <div
                    style={{
                      width: "50%",
                      height: 14,
                      borderRadius: 6,
                      background: "rgba(100,60,255,.1)",
                      animation: "pulse 1.5s infinite",
                    }}
                  />
                  <div
                    style={{
                      width: "30%",
                      height: 10,
                      borderRadius: 6,
                      background: "rgba(100,60,255,.06)",
                      animation: "pulse 1.5s infinite",
                    }}
                  />
                </div>
                <div
                  style={{
                    width: 80,
                    height: 32,
                    borderRadius: 999,
                    background: "rgba(100,60,255,.08)",
                    animation: "pulse 1.5s infinite",
                  }}
                />
              </div>
            ))
          ) : usuarios.length === 0 ? (
            <div
              style={{
                padding: 60,
                textAlign: "center",
                fontFamily: "'Space Mono', monospace",
                fontSize: 12,
                letterSpacing: "2px",
                textTransform: "uppercase",
                color: "#bea8f4e4",
              }}
            >
              No hay usuarios en el ranking
            </div>
          ) : (
            usuarios.map((u) => {
              const pos = u.position;
              const podiumStyle = getPodiumStyle(pos);

              return (
                <div
                  key={u.position}
                  className="rank-row"
                  style={{
                    ...podiumStyle,
                    display: "flex",
                    alignItems: "center",
                    padding: "18px 24px",
                    borderBottom:
                      pos < usuarios.length
                        ? "1px solid rgba(100,60,255,.08)"
                        : "none",
                    gap: 16,
                  }}
                >
                  <div
                    style={{
                      minWidth: 70,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "flex-start",
                      gap: 10,
                    }}
                  >
                    {getMedalIcon(pos)}
                    <span
                      style={{
                        fontFamily: "'Space Mono', monospace",
                        fontSize: 16,
                        fontWeight: 700,
                        color:
                          pos === 1
                            ? "#ffd700"
                            : pos === 2
                              ? "#c0c0c0"
                              : pos === 3
                                ? "#cd7f32"
                                : "rgba(171, 149, 238, 0.85)",
                        minWidth: 32,
                      }}
                    >
                      #{pos}
                    </span>
                  </div>

                  <div
                    style={{
                      width: 44,
                      height: 44,
                      borderRadius: "50%",
                      background: "linear-gradient(145deg,#7040ff,#4020b0)",
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      flexShrink: 0,
                      border: "2px solid rgba(112,64,255,.3)",
                    }}
                  >
                    <svg
                      width="18"
                      height="18"
                      viewBox="0 0 24 24"
                      fill="none"
                      stroke="rgba(255,255,255,.9)"
                      strokeWidth="2"
                    >
                      <circle cx="12" cy="8" r="4" />
                      <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
                    </svg>
                  </div>

                  <div
                    style={{
                      flex: 1,
                      display: "flex",
                      flexDirection: "column",
                      gap: 5,
                      minWidth: 0,
                    }}
                  >
                    <p
                      style={{
                        color: "#e0d4ff",
                        fontSize: 15,
                        fontWeight: 700,
                        lineHeight: 1.3,
                        margin: 0,
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap",
                      }}
                    >
                      @{u.username}
                    </p>
                    <span
                      style={{
                        fontFamily: "'Space Mono',monospace",
                        fontSize: 11,
                        letterSpacing: "0.5px",
                        color: "rgba(160,130,255,.55)",
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        whiteSpace: "nowrap",
                      }}
                    >
                      {u.email}
                    </span>
                  </div>

                  <div
                    style={{
                      display: "flex",
                      alignItems: "center",
                      gap: 8,
                      padding: "6px 18px",
                      background: "rgba(203, 46, 246, 0.12)",
                      border: "1px solid rgba(100,216,255,.22)",
                      borderRadius: 999,
                      flexShrink: 0,
                    }}
                  >
                    <svg
                      width="14"
                      height="14"
                      viewBox="0 0 24 24"
                      fill="#c64bfb"
                      stroke="#c64bfb"
                      strokeWidth="2"
                      opacity="0.9"
                    >
                      <path d="M12 2L15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2Z" />
                    </svg>
                    <span
                      style={{
                        fontFamily: "'Space Mono', monospace",
                        fontSize: 15,
                        fontWeight: 700,
                        color: "#cf62fe",
                      }}
                    >
                      {u.puntosAcumulados.toLocaleString()}
                    </span>
                  </div>
                </div>
              );
            })
          )}
        </div>
      </section>
    </main>
  );
}