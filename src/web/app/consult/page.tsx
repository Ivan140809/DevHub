"use client";
import React, { useState } from "react";
import { LogOut, Search, ChevronRight } from "lucide-react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";

const Particles = [
  {l:"5%",d:"12s",dl:"0s",s:3},{l:"15%",d:"9s",dl:"-2s",s:2},
  {l:"25%",d:"14s",dl:"-4s",s:4},{l:"35%",d:"10s",dl:"-1s",s:2},
  {l:"45%",d:"11s",dl:"-6s",s:3},{l:"55%",d:"16s",dl:"-3s",s:5},
  {l:"65%",d:"8s",dl:"-7s",s:2},{l:"72%",d:"13s",dl:"-5s",s:3},
  {l:"80%",d:"10s",dl:"-2s",s:4},{l:"88%",d:"15s",dl:"-8s",s:2},
  {l:"93%",d:"9s",dl:"-1s",s:3},{l:"10%",d:"12s",dl:"-4s",s:2},
];

type Pregunta = { id: number; pregunta: string; categoria: string; dificultad: string };

const PREGUNTAS: Pregunta[] = [
  { id: 1, pregunta: "¿Cuáles son las principales características de Docker?",  categoria: "Backend",    dificultad: "Media"   },
  { id: 2, pregunta: "¿Qué lenguaje se usa en machine learning?",               categoria: "Backend",    dificultad: "Fácil"   },
  { id: 3, pregunta: "¿Cómo funciona el Virtual DOM en React?",                 categoria: "Frontend",   dificultad: "Media"   },
  { id: 4, pregunta: "¿Qué es la complejidad algorítmica Big O?",               categoria: "Algoritmos", dificultad: "Difícil" },
  { id: 5, pregunta: "¿Diferencia entre REST y GraphQL?",                       categoria: "Backend",    dificultad: "Media"   },
  { id: 6, pregunta: "¿Qué es un closure en JavaScript?",                       categoria: "Frontend",   dificultad: "Fácil"   },
];

function diffStyle(d: string): React.CSSProperties {
  if (d === "Fácil")   return { background:"rgba(30,160,100,.12)",  color:"rgba(80,220,150,.8)",   border:"1px solid rgba(30,160,100,.2)" };
  if (d === "Difícil") return { background:"rgba(200,40,40,.1)",    color:"rgba(240,100,100,.8)",  border:"1px solid rgba(200,40,40,.2)" };
  return                      { background:"rgba(200,140,20,.1)",   color:"rgba(240,190,60,.8)",   border:"1px solid rgba(200,140,20,.2)" };
}

export default function QuestionListPage() {
  const router = useRouter();
  const nombre = useCurrentUser();
  function handleProfile() {
    const raw = localStorage.getItem("devhub_user");
    router.push(raw ? "/profile" : "/login");
  }
  const [search, setSearch]   = useState("");

  const filtered = PREGUNTAS.filter(p =>
    p.pregunta.toLowerCase().includes(search.toLowerCase()) ||
    p.categoria.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne',sans-serif", position:"relative", overflow:"hidden", display:"flex", flexDirection:"column" }}>

      <>
        <div style={{ position:"absolute", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none" }} />
        <div style={{ position:"absolute", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none" }} />
        <div style={{ position:"absolute", borderRadius:"50%", width:280, height:280, background:"radial-gradient(circle,rgba(70,20,160,.18) 0%,transparent 70%)", top:"35%", left:"52%", animation:"orbFloat 14s ease-in-out infinite alternate", animationDelay:"-3s", pointerEvents:"none" }} />
        <div style={{ position:"absolute", inset:0, pointerEvents:"none", zIndex:1 }}>
          {Particles.map((p, i) => (
            <div key={i} style={{ position:"absolute", borderRadius:"50%", width:p.s, height:p.s, background:"rgba(160,100,255,.7)", left:p.l, bottom:-10, animation:`floatUp ${p.d} linear ${p.dl} infinite` }} />
          ))}
        </div>
      </>

      {/* Header */}
      <header style={{ position:"relative", zIndex:5, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.7)", backdropFilter:"blur(10px)" }}>
        <div style={iconBtn}><LogOut size={15} color="#b8a0ff" /></div>
        <span style={{ fontFamily:"'Space Mono',monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>DEVHUB</span>
        <div style={{ display:"flex", alignItems:"center", gap:14 }}>
          <div onClick={handleProfile} style={{ display:"flex", alignItems:"center", gap:8, cursor:"pointer", padding: nombre ? "4px 12px 4px 4px" : "4px", borderRadius:999, border:"1px solid rgba(100,60,255,.35)", background:"rgba(100,60,255,.05)", transition:"background .2s" }}>
            <div style={{ width:26, height:26, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
            </div>
            {nombre && <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(200,180,255,.8)", whiteSpace:"nowrap" }}>{nombre}</span>}
          </div>
        </div>
      </header>

      {/* Body */}
      <section style={{ flex:1, padding:"28px 24px", position:"relative", zIndex:5, display:"flex", flexDirection:"column", gap:20 }}>

        {/* Toolbar */}
        <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between" }}>
          <span style={{ color:"#ddd0ff", fontSize:16, fontWeight:800, letterSpacing:.5 }}>Preguntas disponibles</span>
          <div style={{ display:"flex", alignItems:"center", gap:10 }}>
            <button style={{ height:36, padding:"0 20px", background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Syne',sans-serif", fontSize:11, fontWeight:800, letterSpacing:3, textTransform:"uppercase", cursor:"pointer", boxShadow:"0 4px 16px rgba(90,40,220,.35)" }}>
              Filtrar
            </button>
            <div style={{ position:"relative", display:"flex", alignItems:"center" }}>
              <Search size={14} style={{ position:"absolute", left:11, color:"rgba(160,130,255,.5)", pointerEvents:"none" }} />
              <input
                type="text" value={search}
                onChange={(e) => setSearch(e.target.value)}
                placeholder="Buscar pregunta..."
                style={{ height:36, width:220, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.2)", borderRadius:10, padding:"0 14px 0 34px", fontFamily:"'Space Mono',monospace", fontSize:12, color:"#ddd0ff", outline:"none" }}
              />
            </div>
          </div>
        </div>

        {/* Contador */}
        <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:2, textTransform:"uppercase", color:"rgba(160,130,255,.35)" }}>
          {filtered.length} pregunta{filtered.length !== 1 ? "s" : ""}
        </span>

        {/* Tabla */}
        <div style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.2)", borderRadius:16, overflow:"hidden", backdropFilter:"blur(16px)", boxShadow:"0 20px 60px rgba(60,20,180,.15)" }}>
          {filtered.length === 0 ? (
            <div style={{ padding:48, textAlign:"center", fontFamily:"'Space Mono',monospace", fontSize:12, letterSpacing:2, textTransform:"uppercase", color:"rgba(140,110,200,.3)" }}>
              Sin resultados
            </div>
          ) : filtered.map((p, i) => (
            <div key={p.id} style={{ display:"flex", alignItems:"stretch", borderBottom: i < filtered.length - 1 ? "1px solid rgba(100,60,255,.1)" : "none", cursor:"pointer" }}>
              {/* Número */}
              <div style={{ minWidth:64, width:64, borderRight:"1px solid rgba(100,60,255,.1)", display:"flex", alignItems:"center", justifyContent:"center", fontFamily:"'Space Mono',monospace", fontSize:15, fontWeight:700, color:"rgba(140,100,255,.5)" }}>
                {String(p.id).padStart(2, "0")}
              </div>
              {/* Contenido */}
              <div style={{ flex:1, padding:"18px 22px", display:"flex", flexDirection:"column", gap:10 }}>
                <p style={{ color:"#e0d4ff", fontSize:14, fontWeight:700, lineHeight:1.5 }}>{p.pregunta}</p>
                <div style={{ display:"flex", gap:8, flexWrap:"wrap" }}>
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:1.5, textTransform:"uppercase", padding:"3px 12px", borderRadius:999, fontWeight:700, background:"rgba(100,60,255,.15)", color:"rgba(180,150,255,.7)", border:"1px solid rgba(100,60,255,.2)" }}>
                    {p.categoria}
                  </span>
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:1.5, textTransform:"uppercase", padding:"3px 12px", borderRadius:999, fontWeight:700, ...diffStyle(p.dificultad) }}>
                    {p.dificultad}
                  </span>
                </div>
              </div>
              {/* Flecha */}
              <div style={{ display:"flex", alignItems:"center", padding:"0 18px", color:"rgba(120,80,255,.35)" }}>
                <ChevronRight size={16} />
              </div>
            </div>
          ))}
        </div>
      </section>
    </main>
  );
}

const iconBtn: React.CSSProperties = {
  width:34, height:34, borderRadius:"50%",
  border:"1px solid rgba(100,60,255,.35)",
  display:"flex", alignItems:"center", justifyContent:"center",
  cursor:"pointer", background:"rgba(100,60,255,.05)",
};
