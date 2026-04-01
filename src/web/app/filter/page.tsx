"use client";
import React from "react";
import { useRouter } from "next/navigation";

const Particles = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
];

const orbStyle1: React.CSSProperties = {
  position: "fixed",
  borderRadius: "50%",
  width: 500,
  height: 500,
  background: "radial-gradient(circle, rgba(90,30,200,.22) 0%, transparent 70%)",
  top: -150,
  left: -150,
  animation: "orbFloat 10s ease-in-out infinite alternate",
  pointerEvents: "none",
  zIndex: 0,
};

const orbStyle2: React.CSSProperties = {
  position: "fixed",
  borderRadius: "50%",
  width: 400,
  height: 400,
  background: "radial-gradient(circle, rgba(110,50,255,.18) 0%, transparent 70%)",
  bottom: -100,
  right: -100,
  animation: "orbFloat 10s ease-in-out infinite alternate",
  animationDelay: "-5s",
  pointerEvents: "none",
  zIndex: 0,
};

function diffStyle(d: string): React.CSSProperties {
  if (d === "Fácil")   return { background: "rgba(30,160,100,.12)",  color: "rgba(80,220,150,.8)",   border: "1px solid rgba(30,160,100,.3)"  };
  if (d === "Difícil") return { background: "rgba(200,40,40,.1)",    color: "rgba(240,100,100,.8)",  border: "1px solid rgba(200,40,40,.3)"   };
  return                      { background: "rgba(200,140,20,.1)",   color: "rgba(240,190,60,.8)",   border: "1px solid rgba(200,140,20,.3)"  };
}

function catStyle(c: string): React.CSSProperties {
  if (c === "FrontEnd")      return { background: "rgba(160, 60, 30, 0.12)",  color: "rgba(220, 150, 80, 0.8)",   border: "1px solid rgba(160, 99, 30, 0.3)"  };
  if (c === "BackEnd")       return { background: "rgba(179, 200, 40, 0.1)",    color: "rgba(240, 226, 100, 0.8)",  border: "1px solid rgba(168, 200, 40, 0.3)"   };
  if (c === "DB")            return { background: "rgba(100,100,200,.1)",  color: "rgba(128, 246, 112, 0.8)",  border: "1px solid rgba(100, 200, 112, 0.3)"  };
  if (c === "IA")            return { background: "rgba(200,100,200,.1)",  color: "rgba(106, 223, 250, 0.8)",  border: "1px solid rgba(39, 186, 235, 0.3)"  };
  if (c === "Ciberseguridad") return { background: "rgba(160, 60, 30, 0.12)",  color: "rgba(115, 1, 255, 0.8)",   border: "1px solid rgba(144, 34, 227, 0.3)"  };
  if (c === "Estructuras")   return { background: "rgba(160, 60, 30, 0.12)",  color: "rgba(249, 90, 238, 0.8)",   border: "1px solid rgba(217, 34, 238, 0.3)" };
  return {};
}

export default function FilterPage() {
  const router = useRouter();
  const [selected, setSelected] = React.useState<string | null>(null);
  const [selectedCat, setSelectedCat] = React.useState<string | null>(null);

  const opciones = ["Fácil", "Media", "Difícil"];
  const categoria = ["FrontEnd", "BackEnd", "DB", "IA", "Ciberseguridad", "Estructuras"];

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

      <>
        <div style={orbStyle1} />
        <div style={orbStyle2} />
        {Particles.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}
      </>

      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/question")} style={{  display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(52, 20, 141, 0.3)", border:"1px solid rgb(99, 60, 255)", borderRadius:10, color:"rgb(180, 150, 255)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>

        <span style={{ fontFamily:"'Space Mono', monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>
          DEVHUB
        </span>
        <div style={{ width:60 }} />
      </header>

      <section style={{ position:"relative", zIndex:5, padding:"40px 24px", display:"flex", flexDirection:"column", gap:22, animation:"slideIn .35s ease" }}>

        <div style={{ textAlign:"center" }}>
          <p style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"3px", textTransform:"uppercase", color:"rgb(200, 186, 247)" }}>
            Filtrar por
          </p>
        </div>

        <div style={{ display:"flex", justifyContent:"center", gap:60, width:"100%" }}>
          <h2 style={{ color:"#ddd0ff", fontSize:22, fontWeight:800, letterSpacing:1 }}>
            Dificultad
          </h2>
          <h2 style={{ color:"#ddd0ff", fontSize:22, fontWeight:800, letterSpacing:1 }}>
            Categoría
          </h2>
        </div>

        <div style={{ display:"flex", justifyContent:"center", gap:80, width:"100%" }}>

          <div style={{ display:"flex", flexDirection:"column", gap:14, width:300 }}>
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
                    border: isSelected ? ds.border : "1px solid rgb(99, 60, 255)",
                    backdropFilter:"blur(16px)",
                  }}
                >
                  <span style={{ fontWeight:800, fontSize:15, color: isSelected ? (ds.color as string) : "#ddd0ff" }}>
                    {op}
                  </span>

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

          <div style={{ display:"flex", flexDirection:"column", gap:14, width:300 }}>
            {categoria.map((cat) => {
              const isCatSelected = selectedCat === cat;
              const cl = catStyle(cat);
              return (
                <div
                  key={cat}
                  className="opt-card"
                  onClick={() => setSelectedCat(isCatSelected ? null : cat)}
                  style={{
                    display:"flex", alignItems:"center", justifyContent:"space-between",
                    padding:"18px 24px", borderRadius:14, cursor:"pointer",
                    transition:"all .2s ease",
                    background: isCatSelected ? cl.background : "rgba(14,10,28,.88)",
                    border: isCatSelected ? cl.border : "1px solid rgb(99, 60, 255)",
                    backdropFilter:"blur(16px)",
                  }}
                >
                  <span style={{ fontWeight:800, fontSize:15, color: isCatSelected ? (cl.color as string) : "#ddd0ff" }}>
                    {cat}
                  </span>

                  <div style={{
                    width:20, height:20, borderRadius:"50%",
                    border: isCatSelected ? `2px solid ${cl.color}` : "2px solid rgba(100,60,255,.3)",
                    background: isCatSelected ? (cl.color as string) : "transparent",
                    transition:"all .2s",
                    display:"flex", alignItems:"center", justifyContent:"center",
                  }}>
                    {isCatSelected && <svg width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="#07070f" strokeWidth="3.5"><path d="M5 13l4 4L19 7"/></svg>}
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        <div style={{ textAlign:"center" }}>
          <button 
            onClick={() => {
              router.back();
            }}
            style={{
              textAlign:"center", marginTop:20, height:44, padding:"0 48px", maxWidth:300,
              background: (selected || selectedCat) ? "linear-gradient(135deg,#7040ff,#5020e0)" : "rgba(100,60,255,.1)",
              border: (selected || selectedCat) ? "none" : "1px solid rgb(99, 60, 255)",
              borderRadius:12, color: (selected || selectedCat) ? "white" : "rgb(159, 130, 255)",
              fontFamily:"'Syne', sans-serif", fontSize:12, fontWeight:800,
              letterSpacing:"3px", textTransform:"uppercase" as const,
              cursor: (selected || selectedCat) ? "pointer" : "default",
              boxShadow: (selected || selectedCat) ? "0 4px 16px rgba(91, 40, 220, 1)" : "none",
              transition:"all .2s",
            }}
          >
            {(selected || selectedCat) ? `Filtrar` : "Selecciona una opción"}
          </button>
        </div>
      </section>
    </main>
  );
}