"use client";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../../hooks/useCurrentUser";

type Dificultad = "Fácil" | "Media" | "Difícil";

const PARTICLES = [
  { l:"5%", d:"12s", dl:"0s", s:3 }, { l:"15%", d:"9s", dl:"-2s", s:2 },
  { l:"55%", d:"16s", dl:"-3s", s:5 }, { l:"80%", d:"10s", dl:"-2s", s:4 },
];

export default function AddQuestionPage() {
  const router = useRouter();
  const nombre = useCurrentUser();
  function handleProfile() {
    const raw = localStorage.getItem("devhub_user");
    router.push(raw ? "/profile" : "/login");
  }
  const [pregunta, setPregunta]   = useState("");
  const [opciones, setOpciones]   = useState(["", "", ""]);
  const [correcta, setCorrecta]   = useState<number | null>(null);
  const [categoria, setCategoria] = useState("");
  const [dificultad, setDificultad] = useState<Dificultad | null>(null);
  const [loading, setLoading]     = useState(false);
  const [mounted, setMounted]     = useState(false);
  useEffect(() => setMounted(true), []);

  const letters = ["A", "B", "C"];

  function updateOpt(i: number, val: string) {
    const next = [...opciones]; next[i] = val; setOpciones(next);
  }

  async function submit() {
    if (!pregunta.trim() || opciones.some(o => !o.trim()) || correcta === null || !categoria.trim() || !dificultad) {
      alert("Completa todos los campos y selecciona la respuesta correcta."); return;
    }

    const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const ENDPOINT = `${BASE_URL}/question/add`;
    

    const body = { pregunta, opciones, correcta, categoria, dificultad };

    setLoading(true);
    try {
      const res = await fetch(ENDPOINT, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });
      if (!res.ok) throw new Error(`Error ${res.status}`);
      router.push("/question");
    } catch {
      alert("No se pudo conectar al backend. Revisa que esté corriendo.");
    } finally {
      setLoading(false);
    }
  }

  const difColors: Record<Dificultad, React.CSSProperties> = {
    "Fácil":   { background:"rgba(30,160,100,.15)",  borderColor:"rgba(80,200,130,.4)",  color:"rgba(80,220,150,.9)"   },
    "Media":   { background:"rgba(100,60,255,.25)",  borderColor:"rgba(140,80,255,.6)",  color:"#e0d4ff"               },
    "Difícil": { background:"rgba(200,40,40,.1)",    borderColor:"rgba(220,80,80,.35)",  color:"rgba(240,100,100,.85)" },
  };

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne', sans-serif", position:"relative", overflow:"hidden" }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp  { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn  { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .fta:focus  { border-color: rgba(100,60,255,.5) !important; outline:none; }
        .finp:focus { border-color: rgba(100,60,255,.5) !important; outline:none; }
      `}</style>

      {mounted && <>
        <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
        <div style={{ position:"fixed", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
        {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}
      </>}

      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <div onClick={() => router.push("/question")} style={{ width:34, height:34, borderRadius:"50%", border:"1px solid rgba(100,60,255,.35)", display:"flex", alignItems:"center", justifyContent:"center", cursor:"pointer", background:"rgba(100,60,255,.05)" }}>
          <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="#b8a0ff" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
        </div>
        <span style={{ fontFamily:"'Space Mono', monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>DEVHUB</span>
        <div onClick={handleProfile} style={{ display:"flex", alignItems:"center", gap:8, cursor:"pointer", padding: nombre ? "4px 12px 4px 4px" : "4px", borderRadius:999, border:"1px solid rgba(100,60,255,.35)", background:"rgba(100,60,255,.05)", transition:"background .2s" }}>
          <div style={{ width:26, height:26, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
          </div>
          {nombre && <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(200,180,255,.8)", whiteSpace:"nowrap" }}>{nombre}</span>}
        </div>
      </header>

      <section style={{ position:"relative", zIndex:5, padding:"28px 24px", display:"flex", flexDirection:"column", gap:20, animation:"slideIn .35s ease" }}>
        <button onClick={() => router.push("/question")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(100,60,255,.08)", border:"1px solid rgba(100,60,255,.2)", borderRadius:10, color:"rgba(180,150,255,.8)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>

        <div style={{ background:"rgba(14,10,28,.88)", border:"1px solid rgba(100,60,255,.2)", borderRadius:20, padding:28, backdropFilter:"blur(16px)", display:"flex", flexDirection:"column", gap:24 }}>
          <div>
            <label style={{ fontWeight:700, fontSize:14, color:"#ddd0ff", display:"block", marginBottom:6 }}>
              Pregunta
              <span style={{ display:"block", fontSize:12, color:"rgba(160,130,255,.5)", fontWeight:400, marginTop:2, fontFamily:"'Space Mono', monospace" }}>Escribe la pregunta que deseas agregar a la plataforma.</span>
            </label>
            <textarea className="fta" value={pregunta} onChange={e => setPregunta(e.target.value)} placeholder="¿Cuál es la salida de console.log(typeof null)?"
              style={{ width:"100%", height:80, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.2)", borderRadius:12, padding:"12px 16px", fontFamily:"'Syne', sans-serif", fontSize:14, color:"#e0d4ff", resize:"vertical", lineHeight:1.6 }} />
          </div>

          <div style={{ display:"flex", gap:32, flexWrap:"wrap" }}>
            <div style={{ flex:1, minWidth:220 }}>
              <label style={{ fontWeight:700, fontSize:14, color:"#ddd0ff", display:"block", marginBottom:10 }}>
                Opciones de respuesta
                <span style={{ display:"block", fontSize:12, color:"rgba(160,130,255,.5)", fontWeight:400, marginTop:2, fontFamily:"'Space Mono', monospace" }}>Haz clic en la letra para marcarla como correcta.</span>
              </label>
              <div style={{ display:"flex", flexDirection:"column", gap:12 }}>
                {opciones.map((opt, i) => (
                  <div key={i} style={{ display:"flex", alignItems:"center", gap:12, cursor:"pointer" }} onClick={() => setCorrecta(i)}>
                    <div style={{ width:36, height:36, borderRadius:"50%", border:"1.5px solid", display:"flex", alignItems:"center", justifyContent:"center", fontFamily:"'Space Mono', monospace", fontSize:13, fontWeight:700, flexShrink:0, transition:"all .15s", ...(correcta === i ? { background:"rgba(100,60,255,.3)", borderColor:"rgba(140,80,255,.7)", color:"#e0d4ff" } : { borderColor:"rgba(100,60,255,.3)", color:"rgba(140,100,255,.6)", background:"transparent" }) }}>
                      {letters[i]}
                    </div>
                    <input className="finp" type="text" value={opt} onChange={e => updateOpt(i, e.target.value)} onClick={e => e.stopPropagation()} placeholder={`Opción ${letters[i]}`}
                      style={{ flex:1, height:38, background:"rgba(255,255,255,.04)", border:`1px solid ${correcta === i ? "rgba(100,60,255,.4)" : "rgba(100,60,255,.2)"}`, borderRadius:999, padding:"0 16px", fontFamily:"'Syne', sans-serif", fontSize:13, color:"#e0d4ff", transition:"border-color .2s" }} />
                    {correcta === i && <span style={{ fontFamily:"'Space Mono', monospace", fontSize:10, letterSpacing:"1.5px", textTransform:"uppercase", color:"rgba(160,130,255,.5)", flexShrink:0 }}>✓ correcta</span>}
                  </div>
                ))}
              </div>
            </div>

            <div style={{ display:"flex", flexDirection:"column", gap:20, minWidth:200 }}>
              <div>
                <label style={{ fontWeight:700, fontSize:14, color:"#ddd0ff", display:"block", marginBottom:6 }}>
                  Categoría
                  <span style={{ display:"block", fontSize:12, color:"rgba(160,130,255,.5)", fontWeight:400, marginTop:2, fontFamily:"'Space Mono', monospace" }}>Ej: JavaScript, Python, Bases de datos</span>
                </label>
                <input className="finp" type="text" value={categoria} onChange={e => setCategoria(e.target.value)} placeholder="Frontend"
                  style={{ width:"100%", height:38, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.2)", borderRadius:999, padding:"0 16px", fontFamily:"'Syne', sans-serif", fontSize:13, color:"#e0d4ff" }} />
              </div>
              <div>
                <label style={{ fontWeight:700, fontSize:14, color:"#ddd0ff", display:"block", marginBottom:10 }}>Dificultad</label>
                <div style={{ display:"flex", gap:8, flexWrap:"wrap" }}>
                  {(["Fácil","Media","Difícil"] as Dificultad[]).map(d => (
                    <div key={d} onClick={() => setDificultad(d)} style={{ display:"flex", alignItems:"center", cursor:"pointer", padding:"6px 16px", border:"1px solid", borderRadius:999, fontSize:13, transition:"all .15s", fontWeight: dificultad === d ? 700 : 400, ...(dificultad === d ? difColors[d] : { background:"rgba(255,255,255,.03)", borderColor:"rgba(100,60,255,.25)", color:"rgba(180,150,255,.7)" }) }}>
                      {d}
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          <div style={{ display:"flex", justifyContent:"center", marginTop:8 }}>
            <button onClick={submit} disabled={loading}
              style={{ height:40, padding:"0 32px", background: loading ? "rgba(100,60,255,.3)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", fontFamily:"'Syne', sans-serif", fontSize:11, fontWeight:800, letterSpacing:"4px", textTransform:"uppercase", cursor: loading ? "not-allowed" : "pointer", boxShadow:"0 4px 16px rgba(90,40,220,.35)" }}>
              {loading ? "Guardando" : "Agregar pregunta"}
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}