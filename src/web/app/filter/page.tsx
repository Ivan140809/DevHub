"use client";
import React from "react";
import { useRouter } from "next/navigation";

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
];

function diffStyle(d: string): React.CSSProperties {
  if (d === "Fácil")   return { background: "rgba(30,160,100,.12)",  color: "rgba(80,220,150,.8)",   border: "1px solid rgba(30,160,100,.3)"  };
  if (d === "Difícil") return { background: "rgba(200,40,40,.1)",    color: "rgba(240,100,100,.8)",  border: "1px solid rgba(200,40,40,.3)"   };
  return                      { background: "rgba(200,140,20,.1)",   color: "rgba(240,190,60,.8)",   border: "1px solid rgba(200,140,20,.3)"  };
}

export default function FilterPage() {
  const router = useRouter();
  const [selected, setSelected] = React.useState<string | null>(null);

  const opciones = ["Fácil", "Media", "Difícil"];

  return (
    <main style={{ minHeight: "100vh", background: "#07070f", fontFamily: "'Syne', sans-serif", position: "relative", overflow: "hidden" }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp  { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn  { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .opt-card:hover { background: rgba(100,60,255,.13) !important; transform: translateY(-2px); }
      `}</style>

      {/* Orbs y partículas — igual que la otra pantalla */}
      <>
        <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
        <div style={{ position:"fixed", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
        {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}
      </>

      {/* Header */}
      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/question")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(100,60,255,.08)", border:"1px solid rgba(100,60,255,.2)", borderRadius:10, color:"rgba(180,150,255,.8)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>

        <span style={{ fontFamily:"'Space Mono', monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>
          DEVHUB
        </span>
        <div style={{ width:60 }} />
      </header>

      {/* Contenido */}
      <section style={{ position:"relative", zIndex:5, padding:"40px 24px", display:"flex", flexDirection:"column", alignItems:"center", gap:32, animation:"slideIn .35s ease" }}>

        <div style={{ textAlign:"center" }}>
          <p style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"3px", textTransform:"uppercase", color:"rgba(183, 163, 248, 0.95)", marginBottom:8 }}>
            Filtrar por
          </p>
          <h2 style={{ color:"#ddd0ff", fontSize:22, fontWeight:800, letterSpacing:1 }}>
            Dificultad
          </h2>
        </div>

        {/* opciones */}
        <div style={{ display:"flex", flexDirection:"column", gap:14, width:"100%", maxWidth:300 }}>
          {opciones.map((op) => {
            const isSelected = selected === op;
            const ds = diffStyle(op);
            return (
              <div
                key={op}
                className="opt-card"
                onClick={() => setSelected(isSelected ? null : op)}
                style={{
                  display:"flex", alignItems:"center", justifyContent:"space-between",
                  padding:"18px 24px", borderRadius:14, cursor:"pointer",
                  transition:"all .2s ease",
                  background: isSelected ? ds.background : "rgba(14,10,28,.88)",
                  border: isSelected ? ds.border : "1px solid rgba(100,60,255,.2)",
                  backdropFilter:"blur(16px)",
                }}
              >
                <span style={{ fontWeight:800, fontSize:15, color: isSelected ? (ds.color as string) : "#ddd0ff" }}>
                  {op}
                </span>

                {/* Círculo de la opcion*/}
                <div style={{
                  width:20, height:20, borderRadius:"50%",
                  border: isSelected ? `2px solid ${ds.color}` : "2px solid rgba(100,60,255,.3)",
                  background: isSelected ? (ds.color as string) : "transparent",
                  transition:"all .2s",
                  display:"flex", alignItems:"center", justifyContent:"center",
                }}>
                  {isSelected && <svg width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="#07070f" strokeWidth="3.5"><path d="M5 13l4 4L19 7"/></svg>}
                </div>
              </div>
            );
          })}
        </div>

        
        <button
          onClick={() => {
            // Backend connection logic — pasar el filtro seleccionado
            router.back();
          }}
          style={{
            marginTop:8, height:44, padding:"0 48px", width:"100%", maxWidth:300,
            background: selected ? "linear-gradient(135deg,#7040ff,#5020e0)" : "rgba(100,60,255,.1)",
            border: selected ? "none" : "1px solid rgba(100,60,255,.2)",
            borderRadius:12, color: selected ? "white" : "rgba(160,130,255,.4)",
            fontFamily:"'Syne', sans-serif", fontSize:12, fontWeight:800,
            letterSpacing:"3px", textTransform:"uppercase" as const,
            cursor: selected ? "pointer" : "default",
            boxShadow: selected ? "0 4px 16px rgba(90,40,220,.35)" : "none",
            transition:"all .2s",
          }}
        >
          {selected ? `Filtrar` : "Selecciona una opción"}
        </button>

      </section>
    </main>
  );
}