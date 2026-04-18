"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import { Trophy, Book, Award, CloudLightning } from "lucide-react";


const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "35%", d: "11s", dl: "-3s", s: 2 }, { l: "45%", d: "14s", dl: "-6s", s: 3 },
  { l: "72%", d: "13s", dl: "-5s", s: 3 }, { l: "88%", d: "15s", dl: "-8s", s: 2 },
  { l: "93%", d: "9s", dl: "-1s", s: 3 }, { l: "10%", d: "12s", dl: "-4s", s: 2 },
];

const FEATURES = [
  {
    icon: <CloudLightning />,
    title: "Preguntas Técnicas Reales",
    desc: "Practica con preguntas usadas en procesos de selección reales en empresas de tecnología.",
  },
  {
    icon:  <Award />,
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


export default function HomePage() {
  const router = useRouter();
  const [visible, setVisible] = useState(false);

const handleProfileClick = () => {
    const raw = localStorage.getItem("devhub_user");
    const isRegistered = !!raw;
    router.push(isRegistered ? "/profile" : "/register");
  };

  useEffect(() => {
    setTimeout(() => setVisible(true), 100);
  }, []);

  return (
    <main style={{
      minHeight: "100vh",
      background: "#07070f",
      fontFamily: "'Syne', sans-serif",
      position: "relative",
      overflow: "hidden",
    }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp  { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes fadeUp   { 0%{opacity:0;transform:translateY(32px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes fadeIn   { 0%{opacity:0} 100%{opacity:1} }
        @keyframes shimmer  { 0%{background-position:200% center} 100%{background-position:-200% center} }
        @keyframes pulse    { 0%,100%{opacity:.3} 50%{opacity:.7} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .feat-card:hover { transform: translateY(-6px) !important; border-color: rgba(100,60,255,.4) !important; }
        .feat-card { transition: all .3s cubic-bezier(.16,1,.3,1); }
        .cta-btn:hover { transform: scale(1.04); box-shadow: 0 8px 32px rgba(112,64,255,.5) !important; }
        .cta-btn { transition: all .2s ease; }
        .sec-btn:hover { background: rgba(100,60,255,.15) !important; border-color: rgba(100,60,255,.5) !important; }
        .sec-btn { transition: all .2s ease; }
      `}</style>

      {/* Orbs de fondo */}
      <div style={{ position:"fixed", borderRadius:"50%", width:600, height:600, background:"radial-gradient(circle,rgba(90,30,200,.2) 0%,transparent 70%)", top:-200, left:-200, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
      <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(110,50,255,.15) 0%,transparent 70%)", bottom:-150, right:-150, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
      <div style={{ position:"fixed", borderRadius:"50%", width:300, height:300, background:"radial-gradient(circle,rgba(80,20,180,.15) 0%,transparent 70%)", top:"40%", left:"60%", animation:"orbFloat 14s ease-in-out infinite alternate", animationDelay:"-3s", pointerEvents:"none", zIndex:0 }} />
      {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}

          <Navbar />

      {/* Header */}
      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/FAQ")}className="cta-btn"
          style={{ height:36, padding:"0 20px", background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer", boxShadow:"0 4px 16px rgba(90,40,220,.35)" }}>
          Preguntas Frecuentes
          </button>
        <span style={{ fontFamily:"'Space Mono',monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)" }}> </span>
        
        <div style={{ display:"flex", gap:12 }}>
          <button onClick={handleProfileClick} className="cta-btn"
          style={{ height:36, padding:"0 20px", background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer", boxShadow:"0 4px 16px rgba(90,40,220,.35)" }}>
          Login
          </button>
          <button onClick={handleProfileClick}  className="cta-btn"
          style={{ height:36, padding:"0 20px", background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer", boxShadow:"0 4px 16px rgba(90,40,220,.35)" }}>
            Registrarse
          </button>
        </div>
      </header>

      <section style={{ position:"relative", zIndex:5, display:"flex", flexDirection:"column", alignItems:"center", justifyContent:"center", marginTop:-40 , padding:"80px 24px 60px", textAlign:"center", animation: visible ? "fadeUp .8s ease both" : "none" }}>
        {/* Titulo */}
        <div style={{ marginBottom:16, animation:"fadeUp .8s .1s ease both" }}>
          <h1 style={{
            fontSize: "clamp(36px, 6vw, 72px)",
            fontWeight: 800,
            margin: 0,
            lineHeight: 1.1,
            background: "linear-gradient(135deg, #ffffff 0%, #b8a0ff 40%, #7040ff 100%)",
            backgroundSize: "200% auto",
            WebkitBackgroundClip: "text",
            WebkitTextFillColor: "transparent",
            backgroundClip: "text",
            animation: "shimmer 7s linear infinite",
          }}>
            DevHub<span style={{ color:"#7040ff", WebkitTextFillColor:"#a080ff" }}></span>
          </h1>
        </div>

        {/* Slogan */}
        <p style={{ fontFamily:"'Space Mono',monospace", fontSize:13, letterSpacing:"3px", textTransform:"uppercase", color:"rgb(208, 197, 250)", marginBottom:24, animation:"fadeUp .8s .2s ease both" }}>
          Entrena hoy, destaca mañana.
        </p>

        {/* Descripcion principal */}
        <p style={{ maxWidth:620, fontSize:16, lineHeight:1.8, color:"rgb(172, 150, 248)", marginBottom:40, animation:"fadeUp .8s .3s ease both" }}>
          La plataforma especializada en preparación de entrevistas técnicas para ingeniería de sistemas y tecnología. Practica, rompe tus limites, mide tu progreso y llega listo a tu próxima oportunidad laboral!
        </p>

        {/* Botones CTA */}
        <div style={{ display:"flex", gap:14, flexWrap:"wrap", justifyContent:"center", animation:"fadeUp .8s .4s ease both" }}>
          <button onClick={() => router.push("/register")} className="cta-btn"
          style={{ height: 46, padding: "0 20px", background: "rgba(52, 26, 122, 0.3)", border: "1px solid rgb(99, 60, 255)", borderRadius: 10,
          color: " rgb(180, 150, 255)", fontFamily: "'Syne', sans-serif", fontSize: 14,
          fontWeight: 800, letterSpacing: "3px", textTransform: "uppercase" as const, cursor: "pointer",
          boxShadow: "0 4px 16px rgba(91, 40, 220, 0.75)", width: "fit-content", }}>
            Empezar gratis
          </button>

          <button onClick={() => router.push("/question")} className="cta-btn"
           style={{ height: 46, padding: "0 20px", background: "rgba(52, 26, 122, 0.3)", border: "1px solid rgb(99, 60, 255)", borderRadius: 10,
          color: " rgb(180, 150, 255)", fontFamily: "'Syne', sans-serif", fontSize: 14,
          fontWeight: 800, letterSpacing: "3px", textTransform: "uppercase" as const, cursor: "pointer",
          boxShadow: "0 4px 16px rgba(91, 40, 220, 0.75)", width: "fit-content", }}>
            Ver preguntas
          </button>
        </div>

        {/* cajas azules */}
        <div style={{ display:"flex", gap:24, marginTop:52, flexWrap:"wrap", justifyContent:"center", animation:"fadeUp .8s .5s ease both" }}>
          {[["Preguntas", "100+"], ["Categorías", "7"], ["Eficiencia", "100%"]].map(([label, val]) => (
            <div key={label} style={{ display:"flex", flexDirection:"column", alignItems:"center", gap:4, padding:"14px 24px", background:"rgba(21, 4, 90, 0.55)", border:"1px solid rgba(99, 60, 255, 0.54)", borderRadius:14, backdropFilter:"blur(10px)" }}>
              <span style={{ fontSize:22, fontWeight:900, color:"#ddd0ff" }}>{val}</span>
              <span style={{ fontFamily:"'Space Mono',monospace", fontSize:9, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(159, 130, 255, 0.92)" }}>{label}</span>
            </div>
          ))}
        </div>
      </section>

        <div style={{ position:"relative", zIndex:5, height:1, background:"linear-gradient(90deg,transparent,rgba(100,60,255,.3),transparent)", margin:"0 24px" }} />

      
      <section style={{ position:"relative", zIndex:5, padding:"60px 24px 80px", maxWidth:1100, margin:"0 auto" }}>
        <div style={{ textAlign:"center", marginBottom:48 }}>
          <h2 style={{ color:"#ddd0ff", fontSize:28, fontWeight:800, margin:0 }}>Todo lo que necesitas para prepararte</h2>
        </div>

        <div style={{ display:"grid", gridTemplateColumns:"repeat(auto-fit, minmax(240px, 1fr))", gap:20 }}>
          {FEATURES.map((f, i) => (
            <div key={f.title} className="feat-card" style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.18)", borderRadius:18, padding:"28px 24px", backdropFilter:"blur(16px)", animation:`fadeUp .6s ${.1 * i + .2}s ease both` }}>
              <div style={{ color:"#9973fb",fontSize:32, marginBottom:16 }}>{f.icon}</div>
              <h3 style={{ color:"#ddd0ff", fontSize:15, fontWeight:800, marginBottom:10, letterSpacing:.3 }}>{f.title}</h3>
              <p style={{ color:"rgba(180,160,255,.55)", fontSize:13, lineHeight:1.7, margin:0 }}>{f.desc}</p>
            </div>
          ))}    
        </div>
      </section>

    

    </main>
  );
}