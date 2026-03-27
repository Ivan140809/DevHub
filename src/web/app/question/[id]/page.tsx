"use client";
import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { useCurrentUser } from "../../hooks/useCurrentUser";


type OpcionDTO = { texto: string; esCorrecta: boolean };

type Pregunta = {
  id: string;
  titulo: string;
  enunciado: string;
  category: string;
  difficulty: "FACIL" | "MEDIA" | "DIFICIL";
  opciones: OpcionDTO[];
};

const MOCK: Pregunta = {
  id: "1",
  titulo: "Docker",
  enunciado: "¿Cuáles son las principales características de Docker?",
  category: "BACKEND",
  difficulty: "MEDIA",
  opciones: [
    { texto: "Portabilidad y contenedores aislados", esCorrecta: true },
    { texto: "Solo funciona en Linux", esCorrecta: false },
    { texto: "Reemplaza completamente las VMs", esCorrecta: false },
  ],
};

const PARTICLES = [
  { l:"5%", d:"12s", dl:"0s", s:3 }, { l:"15%", d:"9s", dl:"-2s", s:2 },
  { l:"25%", d:"14s", dl:"-4s", s:4 }, { l:"65%", d:"8s", dl:"-7s", s:2 },
   { l:"5%", d:"12s", dl:"0s", s:3 }, { l:"15%", d:"9s", dl:"-2s", s:2 },
  { l:"25%", d:"14s", dl:"-4s", s:4 }, { l:"65%", d:"8s", dl:"-7s", s:2 },
];

function diffLabel(d: string) {
  if (d === "FACIL")   return "Fácil";
  if (d === "DIFICIL") return "Difícil";
  return "Media";
}

function diffStyle(d: string): React.CSSProperties {
  if (d === "FACIL")   return { background:"rgba(30,160,100,.12)",  color:"rgba(80,220,150,.8)",  border:"1px solid rgba(30,160,100,.2)"  };
  if (d === "DIFICIL") return { background:"rgba(200,40,40,.1)",    color:"rgba(240,100,100,.8)", border:"1px solid rgba(200,40,40,.2)"   };
  return                      { background:"rgba(200,140,20,.1)",   color:"rgba(240,190,60,.8)",  border:"1px solid rgba(200,140,20,.2)"  };
}
const letters = ["A","B","C","D","E"];

export default function QuestionDetailPage() {
  const router   = useRouter();
  const { id }   = useParams();
  const nombre = useCurrentUser();
  function handleProfile() {
    const raw = localStorage.getItem("devhub_user");
    router.push(raw ? "/profile" : "/login");
  }
  const [pregunta, setPregunta] = useState<Pregunta | null>(null);
  const [loading, setLoading]   = useState(true);
  const [error, setError]       = useState<string | null>(null);
  const [selected, setSelected] = useState<number | null>(null);
  const [answered, setAnswered] = useState(false);
  const [comentario, setComentario] = useState("");
  const [rating, setRating] = useState<number>(5);
  const [comentarioEnviado, setComentarioEnviado] = useState(false);
  const [comentarioLoading, setComentarioLoading] = useState(false);
  const [comentarioError, setComentarioError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) return;
    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const ENDPOINT = `${BASE_URL}/question/${id}`;

    fetch(ENDPOINT)
      .then(res => {
        if (!res.ok) throw new Error(`Error ${res.status}`);
        return res.json();
      })
      .then((data: Pregunta) => {
        setSelected(null);
        setAnswered(false);
        setPregunta(data);
        setError(null);
        setLoading(false);
      })
      .catch(() => {
        setSelected(null);
        setAnswered(false);
        setPregunta(MOCK);
        setError("Backend no disponible — mostrando datos de ejemplo.");
        setLoading(false);
      });
  }, [id]);

  async function enviarComentario() {
    if (!comentario.trim() || !id) return;
    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    setComentarioLoading(true);
    setComentarioError(null);
    try {
      const res = await fetch(`${BASE_URL}/question/${id}/reviews`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ comment: comentario, rating }),
      });
      if (!res.ok) throw new Error();
      setComentarioEnviado(true);
    } catch {
      setComentarioError("No se pudo enviar el comentario. Intenta de nuevo.");
    } finally {
      setComentarioLoading(false);
    }
  }


  const correctaIdx = pregunta?.opciones.findIndex(o => o.esCorrecta) ?? -1;

  function optStyle(i: number): React.CSSProperties {
    if (!answered) return selected === i
      ? { background:"rgba(100,60,255,.2)", borderColor:"rgba(140,80,255,.6)", transform:"translateX(6px) scale(1.01)", boxShadow:"0 0 20px rgba(120,60,255,.3), inset 0 0 20px rgba(100,60,255,.08)" }
      : { background:"rgba(255,255,255,.03)", borderColor:"rgba(100,60,255,.2)" };
    if (i === correctaIdx) return { background:"rgba(30,160,100,.15)", borderColor:"rgba(80,200,130,.5)" };
    if (i === selected)    return { background:"rgba(200,40,40,.1)",   borderColor:"rgba(220,80,80,.4)"  };
    return { background:"rgba(255,255,255,.03)", borderColor:"rgba(100,60,255,.1)" };
  }

  function letterStyle(i: number): React.CSSProperties {
    if (!answered) return { borderColor:"rgba(100,60,255,.4)", color:"#b8a0ff", background:"transparent" };
    if (i === correctaIdx) return { borderColor:"rgba(80,200,130,.6)", color:"rgba(80,220,150,.9)", background:"rgba(30,160,100,.15)" };
    if (i === selected)    return { borderColor:"rgba(220,80,80,.5)",  color:"rgba(240,100,100,.8)", background:"transparent" };
    return { borderColor:"rgba(100,60,255,.2)", color:"rgba(140,100,255,.4)", background:"transparent" };
  }

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne', sans-serif", position:"relative", overflow:"hidden" }}>
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
        <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
        <div style={{ position:"fixed", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
        {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}
      </>

      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <div onClick={() => router.push("/question")} style={{ width:34, height:34, borderRadius:"50%", border:"1px solid rgba(100,60,255,.35)", display:"flex", alignItems:"center", justifyContent:"center", cursor:"pointer", background:"rgba(100,60,255,.05)" }}>
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="#b8a0ff" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
        </div>
        <span style={{ fontFamily:"'Space Mono', monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>DEVHUB</span>
        <div style={{ display:"flex", alignItems:"center", gap:14 }}>
          <span onClick={() => router.push("/question/add")} style={{ fontSize:11, fontWeight:700, letterSpacing:3, color:"rgba(180,160,255,.6)", cursor:"pointer", textTransform:"uppercase" }}>+ Agregar</span>
          <div onClick={handleProfile} style={{ display:"flex", alignItems:"center", gap:8, cursor:"pointer", padding: nombre ? "4px 12px 4px 4px" : "4px", borderRadius:999, border:"1px solid rgba(100,60,255,.35)", background:"rgba(100,60,255,.05)", transition:"background .2s" }}>
            <div style={{ width:26, height:26, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
            </div>
            {nombre && <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(200,180,255,.8)", whiteSpace:"nowrap" }}>{nombre}</span>}
          </div>
        </div>
      </header>

      <section style={{ position:"relative", zIndex:5, padding:"28px 24px", display:"flex", flexDirection:"column", gap:20, animation:"slideIn .35s ease" }}>
        <button onClick={() => router.push("/question")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(100,60,255,.08)", border:"1px solid rgba(100,60,255,.2)", borderRadius:10, color:"rgba(180,150,255,.8)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>

        {error && (
          <div style={{ background:"rgba(200,140,20,.08)", border:"1px solid rgba(200,140,20,.2)", borderRadius:10, padding:"10px 16px", fontFamily:"'Space Mono', monospace", fontSize:11, color:"rgba(240,190,60,.7)" }}>
            {error}
          </div>
        )}

        {loading ? (
          <div style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.2)", borderRadius:20, padding:24, display:"flex", flexDirection:"column", gap:16 }}>
            <div style={{ display:"flex", gap:16 }}>
              <div style={{ width:50, height:40, borderRadius:4, background:"rgba(100,60,255,.1)", animation:"pulse 1.5s infinite" }} />
              <div style={{ flex:1, height:40, borderRadius:4, background:"rgba(100,60,255,.1)", animation:"pulse 1.5s infinite" }} />
            </div>
            {[1,2,3].map(i => <div key={i} style={{ height:56, borderRadius:14, background:"rgba(100,60,255,.07)", animation:"pulse 1.5s infinite" }} />)}
          </div>
        ) : pregunta && <>
          <div style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.2)", borderRadius:20, padding:24, backdropFilter:"blur(16px)", display:"flex", flexDirection:"column", gap:16 }}>
            <div style={{ display:"flex", alignItems:"flex-start", gap:16 }}>
              <span style={{ fontFamily:"'Space Mono', monospace", fontSize:38, fontWeight:700, color:"rgba(140,100,255,.2)", lineHeight:1, flexShrink:0 }}>{String(pregunta.id).padStart(2,"0")}</span>
              <p style={{ color:"#e0d4ff", fontSize:19, fontWeight:800, lineHeight:1.4, flex:1 }}>{pregunta.enunciado}</p>
            </div>
            <div style={{ height:1, background:"rgba(100,60,255,.15)" }} />
            <div style={{ display:"flex", gap:10, flexWrap:"wrap", alignItems:"center" }}>
              <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(186, 166, 250, 0.4)" }}>Categoría</span>
              <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"1.5px", textTransform:"uppercase", padding:"3px 12px", borderRadius:999, fontWeight:700, background:"rgba(100,60,255,.15)", color:"rgba(180,150,255,.7)", border:"1px solid rgba(100,60,255,.2)" }}>{pregunta.category}</span>
              <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(186, 166, 250, 0.4)", marginLeft:8 }}>Dificultad</span>
              <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"1.5px", textTransform:"uppercase", padding:"3px 12px", borderRadius:999, fontWeight:700, ...diffStyle(pregunta.difficulty) }}>{diffLabel(pregunta.difficulty)}</span>
            </div>
          </div>

          <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"3px", textTransform:"uppercase", color:"rgba(231, 224, 255, 0.4)" }}>Selecciona la respuesta correcta</span>

          <div style={{ display:"flex", flexDirection:"column", gap:10 }}>
            {pregunta.opciones.map((opt, i) => (
              <button key={i} className="opt-btn" disabled={answered} onClick={() => setSelected(i)}
                style={{ width:"100%", display:"flex", alignItems:"center", gap:16, padding:"14px 20px", border:"1px solid", borderRadius:14, cursor: answered ? "default" : "pointer", textAlign:"left", animation:`popIn .3s ease ${i*0.07}s both`, ...optStyle(i) }}>
                <div style={{ width:36, height:36, borderRadius:"50%", border:"1.5px solid", display:"flex", alignItems:"center", justifyContent:"center", fontFamily:"'Space Mono', monospace", fontSize:13, fontWeight:700, flexShrink:0, ...letterStyle(i) }}>
                  {letters[i]}
                </div>
                <span style={{ color:"#ddd0ff", fontSize:14, fontWeight:700, lineHeight:1.4, flex:1 }}>{opt.texto}</span>
                {answered && i === correctaIdx && <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"1px", textTransform:"uppercase", padding:"3px 10px", borderRadius:999, background:"rgba(30,160,100,.2)", color:"rgba(80,220,150,.9)", border:"1px solid rgba(30,160,100,.25)", flexShrink:0 }}>Correcta</span>}
                {answered && i === selected && i !== correctaIdx && <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"1px", textTransform:"uppercase", padding:"3px 10px", borderRadius:999, background:"rgba(200,40,40,.12)", color:"rgba(240,100,100,.8)", border:"1px solid rgba(200,40,40,.2)", flexShrink:0 }}>Incorrecta</span>}
              </button>
            ))}
          </div>

          {answered && (
            <div style={{ borderRadius:14, padding:"16px 20px", display:"flex", flexDirection:"column", gap:6, ...(selected === correctaIdx ? { background:"rgba(30,160,100,.1)", border:"1px solid rgba(30,160,100,.2)" } : { background:"rgba(200,40,40,.08)", border:"1px solid rgba(200,40,40,.2)" }) }}>
              <span style={{ fontWeight:800, fontSize:15, color: selected === correctaIdx ? "rgba(80,220,150,.9)" : "rgba(240,100,100,.8)" }}>
                {selected === correctaIdx ? "¡Correcto!" : "Incorrecto"}
              </span>
              <span style={{ fontSize:13, color:"rgba(200,180,255,.65)", lineHeight:1.6 }}>
                {selected === correctaIdx ? "Muy bien! Respondiste correctamente" : `La respuesta correcta era: ${pregunta.opciones[correctaIdx]?.texto}`}
              </span>
            </div>
          )}

          <div style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.2)", borderRadius:20, padding:24, backdropFilter:"blur(16px)", display:"flex", flexDirection:"column", gap:14 }}>
            <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Deja un comentario</span>
            {comentarioEnviado ? (
              <div style={{ padding:"12px 16px", borderRadius:12, background:"rgba(30,160,100,.08)", border:"1px solid rgba(30,160,100,.2)", fontFamily:"'Space Mono', monospace", fontSize:12, color:"rgba(80,220,150,.8)" }}>
                ✓ Comentario enviado
              </div>
            ) : (
              <div style={{ display:"flex", flexDirection:"column", gap:10 }}>
                <div style={{ display:"flex", alignItems:"center", gap:8 }}>
                  <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"1.5px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Rating</span>
                  {[1,2,3,4,5].map(n => (
                    <span key={n} onClick={() => setRating(n)} style={{ fontSize:18, cursor:"pointer", opacity: n <= rating ? 1 : 0.25, transition:"opacity .15s" }}>&#9733;</span>
                  ))}
                </div>
                <textarea
                  value={comentario}
                  onChange={e => setComentario(e.target.value)}
                  placeholder="Escribe tu comentario sobre esta pregunta..."
                  rows={3}
                  style={{ width:"100%", background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.2)", borderRadius:12, padding:"12px 16px", fontFamily:"'Syne', sans-serif", fontSize:13, color:"#e0d4ff", resize:"vertical", lineHeight:1.6, outline:"none" }}
                />
                {comentarioError && (
                  <span style={{ fontFamily:"'Space Mono', monospace", fontSize:11, color:"rgba(240,100,100,.8)" }}>{comentarioError}</span>
                )}
                <button
                  onClick={enviarComentario}
                  disabled={!comentario.trim() || comentarioLoading}
                  style={{ alignSelf:"flex-end", height:36, padding:"0 24px", background: !comentario.trim() ? "rgba(100,60,255,.2)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Syne', sans-serif", fontSize:11, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: !comentario.trim() ? "not-allowed" : "pointer", boxShadow:"0 4px 16px rgba(90,40,220,.25)" }}>
                  {comentarioLoading ? "Enviando..." : "Enviar"}
                </button>
              </div>
            )}
          </div>

          <div style={{ display:"flex", gap:12, flexWrap:"wrap" }}>
            {!answered && (
              <button disabled={selected === null} onClick={() => setAnswered(true)}
                style={{ height:40, padding:"0 24px", background: selected === null ? "rgba(100,60,255,.3)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Syne', sans-serif", fontSize:11, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: selected === null ? "not-allowed" : "pointer", boxShadow:"0 4px 16px rgba(90,40,220,.35)" }}>
                Confirmar respuesta
              </button>
            )}
            <button onClick={() => router.push("/question")}
              style={{ height:40, padding:"0 20px", background:"rgba(100,60,255,.08)", border:"1px solid rgba(100,60,255,.25)", borderRadius:10, color:"rgba(180,150,255,.8)", fontFamily:"'Syne', sans-serif", fontSize:11, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor:"pointer" }}>
              Volver al listado
            </button>
          </div>
        </>}
      </section>
    </main>
  );
}