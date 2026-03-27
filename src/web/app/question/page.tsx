"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";
 

type Pregunta = {
  id: string;
  titulo: string;
  category: string;
  difficulty: "FACIL" | "MEDIA" | "DIFICIL";
};

type Review = {
  id?: string;
  comment: string;
  rating: number;
  createdAt?: string;
};

const MOCK: Pregunta[] = [
  { id: "1", titulo: "¿Cuáles son las principales características de Docker?", category: "BACKEND", difficulty: "MEDIA" },
  { id: "2", titulo: "¿Qué lenguaje se usa principalmente en machine learning?", category: "BACKEND", difficulty: "FACIL" },
];


function diffLabel(d: string) {
  if (d === "FACIL")   return "Fácil";
  if (d === "DIFICIL") return "Difícil";
  return "Media";
}
 
const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
];
 
function diffStyle(d: string): React.CSSProperties {
  if (d === "FACIL")   return { background: "rgba(30,160,100,.12)",  color: "rgba(80,220,150,.8)",   border: "1px solid rgba(30,160,100,.2)"  };
  if (d === "DIFICIL") return { background: "rgba(200,40,40,.1)",    color: "rgba(240,100,100,.8)",  border: "1px solid rgba(200,40,40,.2)"   };
  return                      { background: "rgba(200,140,20,.1)",   color: "rgba(240,190,60,.8)",   border: "1px solid rgba(200,140,20,.2)"  };
}
 
const tagBase: React.CSSProperties = {
  fontFamily: "'Space Mono', monospace", fontSize: 10,
  letterSpacing: "1.5px", textTransform: "uppercase" as const,
  padding: "3px 12px", borderRadius: 999, fontWeight: 700,
};
 
export default function QuestionListPage() {
  const router = useRouter();
  const nombre = useCurrentUser();
  function handleProfile() {
    const raw = localStorage.getItem("devhub_user");
    router.push(raw ? "/profile" : "/login");
  }
  const [preguntas, setPreguntas] = useState<Pregunta[]>(MOCK);
  const [loading, setLoading]     = useState(true);
  const [error, setError]         = useState<string | null>(null);
  const [search, setSearch]       = useState("");
  const [modalOpen, setModalOpen]       = useState(false);
  const [modalPregunta, setModalPregunta] = useState<Pregunta | null>(null);
  const [modalComment, setModalComment]   = useState("");
  const [modalRating, setModalRating]     = useState(5);
  const [modalLoading, setModalLoading]   = useState(false);
  const [modalOk, setModalOk]             = useState(false);
  const [modalError, setModalError]       = useState<string | null>(null);
  const [reviewsModalOpen, setReviewsModalOpen] = useState(false);
  const [selectedPreguntaForReviews, setSelectedPreguntaForReviews] = useState<Pregunta | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [reviewsLoading, setReviewsLoading] = useState(false);
  const [reviewsError, setReviewsError] = useState<string | null>(null);

  function openModal() {
    setModalOpen(true);
    setModalPregunta(null);
    setModalComment("");
    setModalRating(5);
    setModalOk(false);
    setModalError(null);
  }

  function openReviewsModal() {
    setReviewsModalOpen(true);
    setSelectedPreguntaForReviews(null);
    setReviews([]);
    setReviewsLoading(false);
    setReviewsError(null);
  }

  async function loadReviewsForQuestion(pregunta: Pregunta) {
    setSelectedPreguntaForReviews(pregunta);
    setReviews([]);
    setReviewsLoading(true);
    setReviewsError(null);
    
    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    try {
      const res = await fetch(`${BASE_URL}/question/${pregunta.id}/reviews`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
      if (!res.ok) throw new Error();
      const data: Review[] = await res.json();
      setReviews(data);
    } catch {
      setReviewsError("No se pudieron cargar las reseñas.");
      setReviews([]);
    } finally {
      setReviewsLoading(false);
    }
  }

  async function enviarReview() {
    if (!modalPregunta || !modalComment.trim()) return;
    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    setModalLoading(true);
    setModalError(null);
    try {
      const res = await fetch(`${BASE_URL}/question/${modalPregunta.id}/reviews`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ comment: modalComment, rating: modalRating }),
      });
      if (!res.ok) throw new Error();
      setModalOk(true);
    } catch {
      setModalError("No se pudo enviar la reseña. Intenta de nuevo.");
    } finally {
      setModalLoading(false);
    }
  }
  useEffect(() => {
    // cambiar esto si es diferente el endpoint
    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const ENDPOINT = `${BASE_URL}/question/all`;
 
 
    fetch(ENDPOINT)
      .then(res => {
        if (!res.ok) throw new Error(`Error ${res.status}`);
        return res.json();
      })
      .then((data: Pregunta[]) => {
        setPreguntas(data);
        setError(null);
      })
      .catch(() => {
        // Si el backend no está listo, se usan los datos mock
        setPreguntas(MOCK);
        setError("Backend no disponible — mostrando datos de ejemplo.");
      })
      .finally(() => setLoading(false));
  }, []);
 
  const filtered = preguntas.filter(p =>
    p.titulo.toLowerCase().includes(search.toLowerCase()) ||
    p.category.toLowerCase().includes(search.toLowerCase())
  );
 
  return (
    <main style={{ minHeight: "100vh", background: "#07070f", fontFamily: "'Syne', sans-serif", position: "relative", overflow: "hidden" }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat  { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp   { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn   { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes pulse     { 0%,100%{opacity:.3} 50%{opacity:.7} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .q-row:hover { background: rgba(100,60,255,.08) !important; }
        .fab:hover   { transform: scale(1.08) !important; }
      `}</style>
 
      <>
        <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
        <div style={{ position:"fixed", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
        {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}
      </>
 
      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/login")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(98, 45, 244, 0.3)", border:"1px solid rgb(99, 60, 255)", borderRadius:10, color:"rgb(180, 150, 255)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>
        <div style={{ width:34, height:34, visibility:"hidden" }} />
        <span style={{ fontFamily:"'Space Mono', monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>DEVHUB</span>
        <div style={{ display:"flex", alignItems:"center", gap:14 }}>
          <span onClick={() => router.push("/question/add")} style={{  display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(98, 45, 244, 0.3)", border:"1px solid rgb(99, 60, 255)", borderRadius:10, color:"rgb(196, 174, 253)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer"  }}>+ Agregar</span>
          <div onClick={() => router.push("/profile")} style={{ display:"flex", alignItems:"center", gap:8, cursor:"pointer", padding: nombre ? "4px 12px 4px 4px" : "4px", borderRadius:999, border:"1px solid rgba(100,60,255,.35)", background:"rgba(100,60,255,.05)", transition:"background .2s" }}>
            <div style={{ width:36, height:36, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
            </div>
            {nombre && <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(200,180,255,.8)", whiteSpace:"nowrap" }}>{nombre}</span>}
          </div>
        </div>
      </header>
 
      <section style={{ position:"relative", zIndex:5, padding:"28px 24px", display:"flex", flexDirection:"column", gap:20, animation:"slideIn .35s ease" }}>
 
        
        {error && (
          <div style={{ background:"rgba(200,140,20,.08)", border:"1px solid rgba(200,140,20,.2)", borderRadius:10, padding:"10px 16px", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"1px", color:"rgba(240,190,60,.7)" }}>
            {error}
          </div>
        )}

        <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between", flexWrap:"wrap", gap:12 }}>
          <span style={{ color:"#ddd0ff", fontSize:16, fontWeight:800 }}>Preguntas disponibles</span>
          <div style={{ position:"relative", display:"flex", alignItems:"center" }}>
            <svg style={{ position:"absolute", left:11, width:14, height:14, color:"rgb(159, 130, 255)", pointerEvents:"none" }} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.35-4.35"/></svg>
            <input type="text" value={search} onChange={e => setSearch(e.target.value)} placeholder="Buscar pregunta "
              style={{ height:36, width:220, background:"rgba(244, 20, 20, 0.04)", border:"1px solid rgba(99, 60, 255, 0.64)", color:"#ddd0ff",borderRadius:10, padding:"0 14px 0 34px",fontFamily:"'Space Mono', monospace", fontSize:12, outline:"none" }} />
          </div>

          
        </div>

          <div style={{ display:"flex", alignItems:"end", justifyContent:"end", flexWrap:"wrap", gap:12 }}>

<button onClick={() => router.push("/filter")} style={{
  height: 36, padding: "0 20px", alignItems:"end",
  background: "linear-gradient(135deg,#7040ff,#5020e0)",
  border: "none", borderRadius: 10, color: "white",
  fontFamily: "'Syne', sans-serif", fontSize: 11,
  fontWeight: 800, letterSpacing: "3px",
  textTransform: "uppercase" as const,
  cursor: "pointer",
  boxShadow: "0 4px 16px rgba(91, 40, 220, 0.75)",
  width: "fit-content",  
}}>
  Filtrar
</button>
    </div>
 
        <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(159, 130, 255, 0.62)" }}>
          {loading ? "Cargando" : `${filtered.length} pregunta${filtered.length !== 1 ? "s" : ""}`}
        </span>
 
        <div style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.2)", borderRadius:16, overflow:"hidden", backdropFilter:"blur(16px)" }}>
          {loading ? (
            
            [1,2,3].map(i => (
              <div key={i} style={{ display:"flex", alignItems:"stretch", borderBottom:"1px solid rgba(100,60,255,.1)", padding:"20px" }}>
                <div style={{ width:40, height:16, borderRadius:4, background:"rgb(99, 60, 255)", animation:"pulse 1.5s infinite", marginRight:20 }} />
                <div style={{ flex:1, display:"flex", flexDirection:"column", gap:8 }}>
                  <div style={{ width:"70%", height:14, borderRadius:4, background:"rgba(100,60,255,.1)", animation:"pulse 1.5s infinite" }} />
                  <div style={{ width:"30%", height:10, borderRadius:4, background:"rgba(99, 60, 255, 0.95)", animation:"pulse 1.5s infinite" }} />
                </div>
              </div>
            ))
          ) : filtered.length === 0 ? (
            <div style={{ padding:48, textAlign:"center", fontFamily:"'Space Mono', monospace", fontSize:12, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(140, 110, 200, 0.81)" }}>Sin resultados</div>
          ) : filtered.map((p, i) => (
            <div key={p.id} className="q-row" onClick={() => router.push(`/question/${p.id}`)}
              style={{ display:"flex", alignItems:"stretch", borderBottom: i < filtered.length - 1 ? "1px solid rgba(100,60,255,.1)" : "none", cursor:"pointer", transition:"background .15s" }}>
              <div style={{ minWidth:56, borderRight:"1px solid rgba(100,60,255,.1)", display:"flex", alignItems:"center", justifyContent:"center", fontFamily:"'Space Mono', monospace", fontSize:14, fontWeight:700, color:"rgba(171, 149, 238, 0.9)" }}>
                {String(i + 1).padStart(2, "0")}
              </div>
              <div style={{ flex:1, padding:"16px 20px", display:"flex", flexDirection:"column", gap:8 }}>
                <p style={{ color:"#e0d4ff", fontSize:14, fontWeight:700, lineHeight:1.5 }}>{p.titulo}</p>
                <div style={{ display:"flex", gap:8, flexWrap:"wrap" }}>
                  <span style={{ ...tagBase, background:"rgba(100,60,255,.15)", color:"rgb(180, 150, 255)", border:"1px solid rgba(100,60,255,.2)" }}>{p.category}</span>
                  <span style={{ ...tagBase, ...diffStyle(p.difficulty) }}>{diffLabel(p.difficulty)}</span>
                </div>
              </div>
              <div style={{ display:"flex", alignItems:"center", padding:"0 16px", color:"rgb(121, 80, 255)" }}>
                <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M9 18l6-6-6-6"/></svg>
              </div>
            </div>
          ))}
        </div>
      </section>
      
      <button onClick={openModal} style={{
         height: 36, padding: "0 20px", alignItems:"end",
         background: "linear-gradient(135deg,#7040ff,#5020e0)",
        border: "none", borderRadius: 10, color: "white",
        fontFamily: "'Syne', sans-serif", fontSize: 10, 
        fontWeight: 800, letterSpacing: "3px",
        textTransform: "uppercase" as const,
        cursor: "pointer",
        boxShadow: "0 4px 16px rgba(91, 40, 220, 0.75)",
        width: "fit-content",  
        position:"absolute", bottom:28, left:28, zIndex:10,   }}>
        Añadir reseña
      </button>

       <button onClick={openReviewsModal} style={{
         height: 36, padding: "0 20px", alignItems:"end",
         background: "linear-gradient(135deg,#7040ff,#5020e0)",
        border: "none", borderRadius: 10, color: "white",
        fontFamily: "'Syne', sans-serif", fontSize: 10, 
        fontWeight: 800, letterSpacing: "3px",
        textTransform: "uppercase" as const,
        cursor: "pointer",
        boxShadow: "0 4px 16px rgba(91, 40, 220, 0.75)",
        width: "fit-content",  
        position:"absolute", bottom:28, left:260, zIndex:10,   }}>
        Ver reseñas
      </button>
 
      <button className="fab" title="Agregar pregunta" onClick={() => router.push("/question/add")}
        style={{ position:"fixed", bottom:28, right:28, zIndex:20, width:52, height:52, borderRadius:"50%", background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", cursor:"pointer", display:"flex", alignItems:"center", justifyContent:"center", boxShadow:"0 6px 24px rgba(90,40,220,.5)", transition:"transform .15s" }}>
        <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="white" strokeWidth="2.5"><path d="M12 5v14M5 12h14"/></svg>
      </button>

      {/* Modal para añadir reseña */}
      {modalOpen && (
        <div onClick={() => setModalOpen(false)} style={{ position:"fixed", inset:0, zIndex:50, background:"rgba(0,0,0,.6)", backdropFilter:"blur(4px)", display:"flex", alignItems:"center", justifyContent:"center", padding:24 }}>
          <div onClick={e => e.stopPropagation()} style={{ width:"100%", maxWidth:520, background:"rgba(14,10,28,.97)", border:"1px solid rgba(100,60,255,.3)", borderRadius:20, padding:28, display:"flex", flexDirection:"column", gap:20, boxShadow:"0 24px 80px rgba(60,20,180,.4)" }}>

            <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between" }}>
              <span style={{ fontFamily:"'Space Mono',monospace", fontSize:12, letterSpacing:"3px", textTransform:"uppercase", color:"#b8a0ff" }}>Añadir reseña</span>
              <div onClick={() => setModalOpen(false)} style={{ cursor:"pointer", color:"rgba(160,130,255,.5)", fontSize:18, lineHeight:1 }}>&#x2715;</div>
            </div>

            {modalOk ? (
              <div style={{ padding:"16px", borderRadius:12, background:"rgba(30,160,100,.08)", border:"1px solid rgba(30,160,100,.2)", fontFamily:"'Space Mono',monospace", fontSize:12, color:"rgba(80,220,150,.8)", textAlign:"center" }}>
                ✓ Reseña enviada correctamente
              </div>
            ) : (
              <>
                <div style={{ display:"flex", flexDirection:"column", gap:8 }}>
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Pregunta</span>
                  <select
                    value={modalPregunta?.id ?? ""}
                    onChange={e => setModalPregunta(preguntas.find(p => p.id === e.target.value) ?? null)}
                    style={{ height:40, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.25)", borderRadius:10, padding:"0 14px", fontFamily:"'Syne',sans-serif", fontSize:13, color: modalPregunta ? "#e0d4ff" : "rgba(160,130,255,.4)", outline:"none", cursor:"pointer" }}
                  >
                    <option value="" disabled style={{ background:"#0e0a1c" }}>Selecciona una pregunta...</option>
                    {preguntas.map(p => (
                      <option key={p.id} value={p.id} style={{ background:"#0e0a1c", color:"#e0d4ff" }}>{p.titulo}</option>
                    ))}
                  </select>
                </div>

                <div style={{ display:"flex", flexDirection:"column", gap:8 }}>
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Rating</span>
                  <div style={{ display:"flex", gap:6 }}>
                    {[1,2,3,4,5].map(n => (
                      <span key={n} onClick={() => setModalRating(n)} style={{ fontSize:22, cursor:"pointer", opacity: n <= modalRating ? 1 : 0.2, transition:"opacity .15s" }}>&#9733;</span>
                    ))}
                  </div>
                </div>

                <div style={{ display:"flex", flexDirection:"column", gap:8 }}>
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Comentario</span>
                  <textarea
                    value={modalComment}
                    onChange={e => setModalComment(e.target.value)}
                    placeholder="Escribe tu reseña sobre esta pregunta..."
                    rows={4}
                    style={{ width:"100%", background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.2)", borderRadius:12, padding:"12px 16px", fontFamily:"'Syne',sans-serif", fontSize:13, color:"#e0d4ff", resize:"vertical", lineHeight:1.6, outline:"none" }}
                  />
                </div>

                {modalError && (
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(240,100,100,.8)" }}>{modalError}</span>
                )}

                <button
                  onClick={enviarReview}
                  disabled={!modalPregunta || !modalComment.trim() || modalLoading}
                  style={{ height:40, background: (!modalPregunta || !modalComment.trim()) ? "rgba(100,60,255,.2)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Syne',sans-serif", fontSize:11, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: (!modalPregunta || !modalComment.trim()) ? "not-allowed" : "pointer", boxShadow:"0 4px 16px rgba(90,40,220,.3)" }}>
                  {modalLoading ? "Enviando..." : "Enviar reseña"}
                </button>
              </>
            )}
          </div>
        </div>
      )}

      {/* Modal para ver reseñas */}
      {reviewsModalOpen && (
        <div onClick={() => setReviewsModalOpen(false)} style={{ position:"fixed", inset:0, zIndex:50, background:"rgba(0,0,0,.6)", backdropFilter:"blur(4px)", display:"flex", alignItems:"center", justifyContent:"center", padding:24 }}>
          <div onClick={e => e.stopPropagation()} style={{ width:"100%", maxWidth:620, background:"rgba(14,10,28,.97)", border:"1px solid rgba(100,60,255,.3)", borderRadius:20, padding:28, display:"flex", flexDirection:"column", gap:20, boxShadow:"0 24px 80px rgba(60,20,180,.4)", maxHeight:"80vh", overflow:"auto" }}>

            <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between", position:"sticky", top:0, background:"rgba(14,10,28,.97)", paddingBottom:16, borderBottom:"1px solid rgba(100,60,255,.15)" }}>
              <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Ver reseñas</span>
              <div onClick={() => setReviewsModalOpen(false)} style={{ cursor:"pointer", color:"rgba(160,130,255,.5)", fontSize:20, lineHeight:1, flexShrink:0 }}>&#x2715;</div>
            </div>

            <div style={{ display:"flex", flexDirection:"column", gap:8 }}>
              <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:"2px", textTransform:"uppercase", color:"rgba(160,130,255,.5)" }}>Selecciona una pregunta</span>
              <select
                value={selectedPreguntaForReviews?.id ?? ""}
                onChange={e => {
                  const p = preguntas.find(q => q.id === e.target.value);
                  if (p) loadReviewsForQuestion(p);
                }}
                style={{ height:40, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.25)", borderRadius:10, padding:"0 14px", fontFamily:"'Syne',sans-serif", fontSize:13, color: selectedPreguntaForReviews ? "#e0d4ff" : "rgba(160,130,255,.4)", outline:"none", cursor:"pointer" }}
              >
                <option value="" disabled style={{ background:"#0e0a1c" }}>Elige una pregunta...</option>
                {preguntas.map(p => (
                  <option key={p.id} value={p.id} style={{ background:"#0e0a1c", color:"#e0d4ff" }}>{p.titulo}</option>
                ))}
              </select>
            </div>

            {selectedPreguntaForReviews && (
              <>
                <div style={{ display:"flex", alignItems:"center", gap:8, padding:"12px 16px", background:"rgba(100,60,255,.08)", borderRadius:12, border:"1px solid rgba(100,60,255,.15)" }}>
                  <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:"1px", textTransform:"uppercase", color:"rgba(160,130,255,.6)" }}>Pregunta:</span>
                  <span style={{ color:"#ddd0ff", fontSize:12, fontWeight:700 }}>{selectedPreguntaForReviews.titulo}</span>
                </div>

                {reviewsLoading ? (
                  <div style={{ display:"flex", flexDirection:"column", gap:12 }}>
                    {[1,2,3].map(i => (
                      <div key={i} style={{ display:"flex", flexDirection:"column", gap:8, padding:16, background:"rgba(100,60,255,.05)", borderRadius:12 }}>
                        <div style={{ height:12, width:"40%", borderRadius:4, background:"rgba(100,60,255,.1)", animation:"pulse 1.5s infinite" }} />
                        <div style={{ height:14, width:"100%", borderRadius:4, background:"rgba(100,60,255,.08)", animation:"pulse 1.5s infinite" }} />
                      </div>
                    ))}
                  </div>
                ) : reviewsError ? (
                  <div style={{ padding:"16px", borderRadius:12, background:"rgba(200,40,40,.08)", border:"1px solid rgba(200,40,40,.2)", fontFamily:"'Space Mono',monospace", fontSize:12, color:"rgba(240,100,100,.8)" }}>
                    {reviewsError}
                  </div>
                ) : reviews.length === 0 ? (
                  <div style={{ padding:32, textAlign:"center", fontFamily:"'Space Mono', monospace", fontSize:12, letterSpacing:"1px", color:"rgba(140, 110, 200, 0.6)" }}>
                    Sin reseñas aún
                  </div>
                ) : (
                  <div style={{ display:"flex", flexDirection:"column", gap:12 }}>
                    {reviews.map((review, i) => (
                      <div key={i} style={{ display:"flex", flexDirection:"column", gap:8, padding:16, background:"rgba(100,60,255,.05)", borderRadius:12, border:"1px solid rgba(100,60,255,.1)" }}>
                        <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between" }}>
                          <div style={{ display:"flex", gap:4 }}>
                            {[1,2,3,4,5].map(star => (
                              <span key={star} style={{ fontSize:14, color: star <= review.rating ? "rgba(240,190,60,.9)" : "rgba(100,100,100,.3)" }}>
                                ★
                              </span>
                            ))}
                          </div>
                          {review.createdAt && (
                            <span style={{ fontFamily:"'Space Mono',monospace", fontSize:10, color:"rgba(160,130,255,.5)" }}>
                              {new Date(review.createdAt).toLocaleDateString()}
                            </span>
                          )}
                        </div>
                        <p style={{ color:"#ddd0ff", fontSize:13, lineHeight:1.6, margin:0 }}>
                          {review.comment}
                        </p>
                      </div>
                    ))}
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      )}

    </main>
  );
}