"use client";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import Navbar from "../components/Navbar";

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "35%", d: "11s", dl: "-3s", s: 2 }, { l: "45%", d: "14s", dl: "-6s", s: 3 },
  { l: "72%", d: "13s", dl: "-5s", s: 3 }, { l: "88%", d: "15s", dl: "-8s", s: 2 },
  { l: "93%", d: "9s", dl: "-1s", s: 3 }, { l: "10%", d: "12s", dl: "-4s", s: 2 },
];

const FAQS = [
  {
    q: "¿Qué es DevHub?",
    a: "DevHub es una plataforma web especializada en preparación de entrevistas técnicas para ingeniería de sistemas y tecnologías de la información.",
  },
  {
    q: "¿Para quién está diseñada la plataforma?",
    a: "Está diseñada para estudiantes de ingeniería de sistemas y profesionales de TI que quieran prepararse para procesos de selección laboral en el sector tecnológico.",
  },
  {
    q: "¿Cómo funciona DevHub?",
    a: "La plataforma facilita la práctica de preguntas técnicas reales, organizadas por categoría y dificultad. Puedes responderlas, ver tu progreso y compararte con otros usuarios en el ranking.",
  },
  {
    q: "¿Qué tipo de preguntas encontraré?",
    a: "Encontrarás preguntas técnicas de áreas como Frontend, Backend, Bases de datos, Estructuras de datos, Algoritmos, IA y Ciberseguridad, con niveles Fácil, Media y Difícil.",
  },
  {
    q: "¿Cómo se calcula mi progreso?",
    a: "Tu progreso se calcula con base en el número de preguntas que has respondido frente al total disponible en la plataforma. Puedes verlo en tu perfil con una barra de progreso.",
  },
  {
    q: "¿Qué es el ranking?",
    a: "El ranking muestra a los 50 usuarios con mayor puntaje acumulado en la plataforma. Es una forma de motivarte comparando tu desempeño con el de otros.",
  },
  {
    q: "¿Por qué DevHub es diferente a otras plataformas?",
    a: "DevHub surge específicamente para cerrar la brecha entre la formación universitaria y las competencias que exige el mercado laboral. No es un recurso genérico, está alineado con dinámicas reales de procesos de selección en tecnología.",
  },
];

export default function FAQPage() {
  const [visible, setVisible] = useState(false);
  const [openIndex, setOpenIndex] = useState<number | null>(null);

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
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .faq-item:hover { border-color: rgba(100,60,255,.35) !important; }
        .faq-item { transition: all .2s ease; }
      `}</style>

      {/* Orbs */}
      <div style={{ position:"fixed", borderRadius:"50%", width:600, height:600, background:"radial-gradient(circle,rgba(90,30,200,.2) 0%,transparent 70%)", top:-200, left:-200, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
      <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(110,50,255,.15) 0%,transparent 70%)", bottom:-150, right:-150, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
      {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}

      <Navbar />

      {/* Contenido */}
      <section style={{ position:"relative", zIndex:5, padding:"48px 24px 80px", maxWidth:720, margin:"0 auto", animation: visible ? "fadeUp .8s ease both" : "none" }}>

        {/* Título */}
        <div style={{ textAlign:"center", marginBottom:48 }}>
          
          <h1 style={{ color:"#ddd0ff", fontSize:28, fontWeight:800, margin:0, letterSpacing:.5 }}>
            Preguntas Frecuentes
          </h1>
        </div>

        {/* Acordeón */}
        <div style={{ display:"flex", flexDirection:"column", gap:12 }}>
          {FAQS.map((faq, i) => {
            const isOpen = openIndex === i;
            return (
              <div
                key={i}
                className="faq-item"
                onClick={() => setOpenIndex(isOpen ? null : i)}
                style={{
                  background: isOpen ? "rgba(100,60,255,.08)" : "rgba(14,10,28,.88)",
                  border: isOpen ? "1px solid rgba(99, 60, 255, 0.7)" : "1px solid rgba(99, 60, 255, 0.33)",
                  borderRadius: 14,
                  overflow: "hidden",
                  cursor: "pointer",
                  backdropFilter: "blur(16px)",
                }}
              >
                {/* Pregunta */}
                <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between", padding:"20px 24px", gap:16 }}>
                  <span style={{ color: isOpen ? "#ddd0ff" : "rgba(220,210,255,.8)", fontSize:15, fontWeight:700, lineHeight:1.4 }}>
                    {faq.q}
                  </span>
                  {/* Ícono + / x */}
                  <div style={{
                    width:28, height:28, borderRadius:"50%", flexShrink:0,
                    background: isOpen ? "rgba(100,60,255,.25)" : "rgba(100,60,255,.08)",
                    border: "1px solid rgba(100,60,255,.25)",
                    display:"flex", alignItems:"center", justifyContent:"center",
                    transition:"all .2s",
                  }}>
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="#b8a0ff" strokeWidth="2.5">
                      {isOpen
                        ? <path d="M5 12h14" />
                        : <path d="M12 5v14M5 12h14" />
                      }
                    </svg>
                  </div>
                </div>

                {/* Respuesta */}
                {isOpen && (
                  <div style={{ padding:"0 24px 20px" }}>
                    <div style={{ height:1, background:"rgba(100,60,255,.12)", marginBottom:16 }} />
                    <p style={{ color:"rgba(180, 160, 255, 0.73)", fontSize:14, lineHeight:1.8, margin:0 }}>
                      {faq.a}
                    </p>
                  </div>
                )}
              </div>
            );
          })}
        </div>

      </section>
    </main>
  );
}