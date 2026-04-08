"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";
import { Trophy, Medal, Award } from "lucide-react";

type Usuario = {
  id: string;
  nombre: string;
  apellido: string;
  username: string;
  email: string;
  puntosAcumulados: number;
};


const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
];

export default function RankingPage() {
  const router = useRouter();
  const nombre = useCurrentUser();
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const ENDPOINT = `${BASE_URL}/api/users/ranking`;

    fetch(ENDPOINT)
      .then(res => {
        if (!res.ok) throw new Error();
        return res.json();
      })
      .then((data: Usuario[]) => {
        setUsuarios(data.sort((a, b) => (b.puntosAcumulados || 0) - (a.puntosAcumulados || 0)));
        setError(null);
      })
      .catch(() => {
        setUsuarios([]);
        setError("Backend no disponible — no se pudieron cargar los usuarios.");
      })
      .finally(() => setLoading(false));
  }, []);

  function getMedalIcon(pos: number) {
    if (pos === 1) return <Trophy size={24} color="#ffd700" />;
    if (pos === 2) return <Medal size={24} color="#c0c0c0" />;
    if (pos === 3) return <Award size={24} color="#cd7f32" />;
    return null;
  }

  function getPodiumStyle(pos: number): React.CSSProperties {
    if (pos === 1) return { background: "linear-gradient(135deg, rgba(255,215,0,.15), rgba(255,215,0,.05))", border: "1px solid rgba(255,215,0,.3)" };
    if (pos === 2) return { background: "linear-gradient(135deg, rgba(192,192,192,.15), rgba(192,192,192,.05))", border: "1px solid rgba(192,192,192,.3)" };
    if (pos === 3) return { background: "linear-gradient(135deg, rgba(205,127,50,.15), rgba(205,127,50,.05))", border: "1px solid rgba(205,127,50,.3)" };
    return { background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)" };
  }

  return (
    <main style={{ minHeight: "100vh", background: "#07070f", fontFamily: "'Syne', sans-serif", position: "relative", overflow: "hidden" }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes pulse { 0%,100%{opacity:.3} 50%{opacity:.7} }
        @keyframes shine { 0%{background-position:200%} 100%{background-position:-200%} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .rank-row:hover { background: rgba(100,60,255,.08) !important; transform: translateX(4px); }
        .rank-row { transition: all .2s ease; }
      `}</style>

      <div style={{ position: "fixed", borderRadius: "50%", width: 500, height: 500, background: "radial-gradient(circle, rgba(90,30,200,.22) 0%, transparent 70%)", top: -150, left: -150, animation: "orbFloat 10s ease-in-out infinite alternate", pointerEvents: "none", zIndex: 0 }} />
      <div style={{ position: "fixed", borderRadius: "50%", width: 400, height: 400, background: "radial-gradient(circle, rgba(110,50,255,.18) 0%, transparent 70%)", bottom: -100, right: -100, animation: "orbFloat 10s ease-in-out infinite alternate", animationDelay: "-5s", pointerEvents: "none", zIndex: 0 }} />
      {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width: p.s, height: p.s, left: p.l, animationDuration: p.d, animationDelay: p.dl }} />)}

      <header style={{ position: "relative", zIndex: 10, display: "flex", alignItems: "center", justifyContent: "space-between", padding: "16px 28px", borderBottom: "1px solid rgba(100,60,255,.15)", background: "rgba(7,7,15,.8)", backdropFilter: "blur(10px)" }}>
        <button onClick={() => router.push("/login")} style={{ display: "flex", alignItems: "center", gap: 8, width: "fit-content", padding: "8px 16px", background: "rgba(52, 20, 141, 0.3)", border: "1px solid rgb(99, 60, 255)", borderRadius: 10, color: "rgb(180, 150, 255)", fontFamily: "'Space Mono', monospace", fontSize: 11, letterSpacing: "2px", textTransform: "uppercase", cursor: "pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7" /></svg>
          Volver
        </button>
        <span style={{ fontFamily: "'Space Mono', monospace", fontWeight: 700, fontSize: 16, letterSpacing: 6, color: "#b8a0ff", textShadow: "0 0 20px rgba(150,100,255,.5)", position: "absolute", left: "50%", transform: "translateX(-50%)" }}>DEVHUB</span>
        <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
          <div onClick={() => router.push("/profile")} style={{ display: "flex", alignItems: "center", gap: 8, cursor: "pointer", padding: nombre ? "4px 12px 4px 4px" : "4px", borderRadius: 999, border: "1px solid rgba(100,60,255,.35)", background: "rgba(100,60,255,.05)", transition: "background .2s" }}>
            <div style={{ width: 36, height: 36, borderRadius: "50%", background: "linear-gradient(145deg,#7040ff,#4020b0)", display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0 }}>
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4" /><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" /></svg>
            </div>
            {nombre && <span style={{ fontFamily: "'Space Mono',monospace", fontSize: 11, color: "rgba(200,180,255,.8)", whiteSpace: "nowrap" }}>{nombre}</span>}
          </div>
        </div>
      </header>

      <section style={{ position: "relative", zIndex: 5, padding: "28px 24px", display: "flex", flexDirection: "column", gap: 20, animation: "slideIn .35s ease" }}>
        {error && (
          <div style={{ background: "rgba(200,140,20,.08)", border: "1px solid rgba(200,140,20,.2)", borderRadius: 10, padding: "10px 16px", fontFamily: "'Space Mono', monospace", fontSize: 11, letterSpacing: "1px", color: "rgba(240,190,60,.7)" }}>
            {error}
          </div>
        )}

        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", flexWrap: "wrap", gap: 12 }}>
          <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
            <Trophy size={28} color="#b8a0ff" />
            <span style={{ color: "#ddd0ff", fontSize: 22, fontWeight: 800 }}>Ranking de Usuarios</span>
          </div>
          <span style={{ fontFamily: "'Space Mono', monospace", fontSize: 10, letterSpacing: "2px", textTransform: "uppercase", color: "rgba(159, 130, 255, 0.62)" }}>
            {loading ? "Cargando" : `${usuarios.length} usuario${usuarios.length !== 1 ? "s" : ""}`}
          </span>
        </div>

        <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 16, overflow: "hidden", backdropFilter: "blur(16px)" }}>
          {loading ? (
            [1, 2, 3, 4, 5].map(i => (
              <div key={i} style={{ display: "flex", alignItems: "center", padding: "20px", borderBottom: "1px solid rgba(100,60,255,.1)" }}>
                <div style={{ width: 40, height: 16, borderRadius: 4, background: "rgba(100,60,255,.1)", animation: "pulse 1.5s infinite", marginRight: 20 }} />
                <div style={{ flex: 1, display: "flex", flexDirection: "column", gap: 8 }}>
                  <div style={{ width: "60%", height: 14, borderRadius: 4, background: "rgba(100,60,255,.1)", animation: "pulse 1.5s infinite" }} />
                  <div style={{ width: "30%", height: 10, borderRadius: 4, background: "rgba(100,60,255,.08)", animation: "pulse 1.5s infinite" }} />
                </div>
              </div>
            ))
          ) : usuarios.length === 0 ? (
            <div style={{ padding: 48, textAlign: "center", fontFamily: "'Space Mono', monospace", fontSize: 12, letterSpacing: "2px", textTransform: "uppercase", color: "rgba(140, 110, 200, 0.81)" }}>
              Sin usuarios
            </div>
          ) : (
            usuarios.map((u, i) => {
              const pos = i + 1;
              const podiumStyle = getPodiumStyle(pos);
              return (
                <div key={u.id} className="rank-row" style={{ ...podiumStyle, display: "flex", alignItems: "center", padding: "20px 24px", borderBottom: i < usuarios.length - 1 ? "1px solid rgba(100,60,255,.1)" : "none" }}>
                  <div style={{ minWidth: 60, display: "flex", alignItems: "center", justifyContent: "center", gap: 8 }}>
                    {getMedalIcon(pos)}
                    <span style={{ fontFamily: "'Space Mono', monospace", fontSize: 18, fontWeight: 700, color: pos <= 3 ? "#ffd700" : "rgba(171, 149, 238, 0.9)" }}>
                      #{pos}
                    </span>
                  </div>
                  
                  <div style={{ width: 48, height: 48, borderRadius: "50%", background: "linear-gradient(145deg,#7040ff,#4020b0)", display: "flex", alignItems: "center", justifyContent: "center", marginRight: 16, flexShrink: 0 }}>
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4" /><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" /></svg>
                  </div>

                  <div style={{ flex: 1, display: "flex", flexDirection: "column", gap: 4 }}>
                    <p style={{ color: "#e0d4ff", fontSize: 16, fontWeight: 700, lineHeight: 1.3 }}>
                      {u.nombre} {u.apellido}
                    </p>
                    <span style={{ fontFamily: "'Space Mono',monospace", fontSize: 11, letterSpacing: "1px", color: "rgba(160,130,255,.6)" }}>
                      @{u.username}
                    </span>
                  </div>

                  <div style={{ display: "flex", alignItems: "center", gap: 8, padding: "8px 20px", background: "rgba(255,100,219,.15)", border: "1px solid rgba(255,100,219,.25)", borderRadius: 999 }}>
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#ff64db" strokeWidth="2.5"><path d="M12 2L15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2Z" fill="#ff64db" opacity="0.2"/><path d="M12 2L15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2Z"/></svg>
                    <span style={{ fontFamily: "'Space Mono', monospace", fontSize: 16, fontWeight: 700, color: "#ff64db" }}>
                      {u.puntosAcumulados?.toLocaleString() || 0}
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
