"use client";
import React, { useState, useEffect } from "react";
import { LogOut, User } from "lucide-react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";


const PARTICLES = [
  { l:"5%",  d:"12s", dl:"0s",  s:3 }, { l:"15%", d:"9s",  dl:"-2s", s:2 },
  { l:"25%", d:"14s", dl:"-4s", s:4 }, { l:"35%", d:"10s", dl:"-1s", s:2 },
  { l:"45%", d:"11s", dl:"-6s", s:3 }, { l:"55%", d:"16s", dl:"-3s", s:5 },
  { l:"65%", d:"8s",  dl:"-7s", s:2 }, { l:"72%", d:"13s", dl:"-5s", s:3 },
  { l:"80%", d:"10s", dl:"-2s", s:4 }, { l:"88%", d:"15s", dl:"-8s", s:2 },
  { l:"93%", d:"9s",  dl:"-1s", s:3 }, { l:"10%", d:"12s", dl:"-4s", s:2 },
];

type UserData = {
  nombre?:       string;
  apellido?:     string;
  email?:        string;
  username?:     string;
  phone?:        string;
  preferencias?: string;
};

export default function ProfilePage() {
  const router = useRouter();
  const nombre = useCurrentUser();
  const [user, setUser]           = useState<UserData>({});
  const [form, setForm]           = useState<UserData>({});
  const [loading, setLoading]     = useState(true);
  const [saving, setSaving]       = useState(false);
  const [backendOk, setBackendOk] = useState(true);
  const [saved, setSaved]         = useState(false);

  useEffect(() => {
    let stored: UserData = {};
    try {
      const raw = localStorage.getItem("devhub_user");
      if (raw) stored = JSON.parse(raw);
    } catch {}

    // Show stored data immediately so the user sees their info right away
    setUser(stored);
    setForm(stored);

    const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const email = stored?.email;

    // Cambiar endpoint
    const ENDPOINT = email ? `${BASE}/api/users/${encodeURIComponent(email)}` : null;

    if (ENDPOINT) {
      fetch(ENDPOINT)
        .then(res => { if (!res.ok) throw new Error(); return res.json(); })
        .then((data: UserData) => {
          setUser(data);
          setForm(data);
          setBackendOk(true);
          localStorage.setItem("devhub_user", JSON.stringify(data));
        })
        .catch(() => {
          setBackendOk(false);
        })
        .finally(() => setLoading(false));
    } else {
      setBackendOk(false);
      setLoading(false);
    }
  }, []);

  function handleLogout() {
    localStorage.removeItem("devhub_user");
    router.push("/login");
  }

  async function handleSave() {
    setSaving(true);
    setSaved(false);

    const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const email = form?.email;

    // Cambia endpoint
    const ENDPOINT = `${BASE}/api/users/${encodeURIComponent(email ?? "")}`;

    try {
      const res = await fetch(ENDPOINT, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (!res.ok) throw new Error();
      const updated: UserData = await res.json();
      setUser(updated);
      setForm(updated);
      localStorage.setItem("devhub_user", JSON.stringify(updated));
      setBackendOk(true);
    } catch {
      localStorage.setItem("devhub_user", JSON.stringify(form));
      setUser(form);
      setBackendOk(false);
    } finally {
      setSaving(false);
      setSaved(true);
      setTimeout(() => setSaved(false), 2500);
    }
  }

  function updateField(key: keyof UserData, val: string) {
    setForm(prev => ({ ...prev, [key]: val }));
  }

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne',sans-serif", position:"relative", overflow:"hidden", display:"flex", flexDirection:"column" }}>
      <style>{`
        @keyframes orbFloat  { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp   { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideUp   { 0%{opacity:0;transform:translateY(32px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes ringPulse { 0%,100%{opacity:.3;transform:scale(1)} 50%{opacity:.7;transform:scale(1.04)} }
        @keyframes avatarGlow{ 0%,100%{box-shadow:0 0 24px rgba(120,70,255,.35)} 50%{box-shadow:0 0 48px rgba(120,70,255,.6)} }
        @keyframes pulse     { 0%,100%{opacity:.3} 50%{opacity:.7} }
        .field-input:focus   { border-color: rgba(100,60,255,.5) !important; outline: none; }
      `}</style>

      <div style={{ position:"absolute", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.25) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none" }} />
      <div style={{ position:"absolute", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.2) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none" }} />
      <div style={{ position:"absolute", borderRadius:"50%", width:280, height:280, background:"radial-gradient(circle,rgba(70,20,160,.2) 0%,transparent 70%)", top:"30%", left:"50%", animation:"orbFloat 14s ease-in-out infinite alternate", animationDelay:"-3s", pointerEvents:"none" }} />
      <div style={{ position:"absolute", inset:0, pointerEvents:"none", zIndex:1 }}>
        {PARTICLES.map((p, i) => (
          <div key={i} style={{ position:"absolute", borderRadius:"50%", width:p.s, height:p.s, background:"rgba(160,100,255,.7)", left:p.l, bottom:-10, animation:`floatUp ${p.d} linear ${p.dl} infinite` }} />
        ))}
      </div>

      <header style={{ position:"relative", zIndex:5, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.7)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/login")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(98, 45, 244, 0.3)", border:"1px solid rgb(99, 60, 255)", borderRadius:10, color:"rgb(180, 150, 255)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>
        <span style={{ fontFamily:"'Space Mono',monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>DEVHUB</span>
        <div style={{ display:"flex", alignItems:"center", gap:14 }}>
          
        <div onClick={() => router.push("/profile")} style={{ display:"flex", alignItems:"center", gap:8, cursor:"pointer", padding: nombre ? "4px 12px 4px 4px" : "4px", borderRadius:999, border:"1px solid rgba(100,60,255,.35)", background:"rgba(100,60,255,.05)", transition:"background .2s" }}>
          <div style={{ width:36, height:36, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
          </div>
          {nombre && <span style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(200,180,255,.8)", whiteSpace:"nowrap" }}>{nombre}</span>}
        </div>
        </div>
      </header>

      <section style={{ flex:1, display:"flex", alignItems:"center", justifyContent:"center", padding:"36px 20px", position:"relative", zIndex:5, marginTop:-10 }}>
        <div style={{ width:"100%", maxWidth:780, background:"rgba(14,10,28,.9)", border:"1px solid rgba(100,60,255,.22)", borderRadius:22, padding:"42px 42px 36px", backdropFilter:"blur(20px)", boxShadow:"0 0 0 1px rgba(255,255,255,.03) inset,0 30px 80px rgba(80,40,200,.2)", animation:"slideUp .7s cubic-bezier(.16,1,.3,1) both"}}>

          {!backendOk && !loading && (
            <div style={{ background:"rgba(200,140,20,.08)", border:"1px solid rgba(200,140,20,.2)", borderRadius:10, padding:"10px 16px", fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(240,190,60,.7)", marginTop:-20, marginBottom:10 }}>
              ⚠ Backend no disponible — los cambios se guardarán localmente.
            </div>
          )}

          {saved && (
            <div style={{ background:"rgba(30,160,100,.1)", border:"1px solid rgba(30,160,100,.2)", borderRadius:10, padding:"10px 16px", fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(80,220,150,.8)", marginBottom:20 }}>
              ✓ Cambios guardados correctamente.
            </div>
          )}

          <div style={{ display:"flex", flexDirection:"column", alignItems:"center", gap:16, marginBottom:36 }}>
            <div style={{ position:"relative", width:130, height:130, display:"flex", alignItems:"center", justifyContent:"center" }}>
              <div style={{ position:"absolute", inset:-8, borderRadius:"50%", border:"1px solid rgba(68, 0, 255, 0.84)", animation:"ringPulse 3s ease-in-out infinite" }} />
              <div style={{ width:130, height:130, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", border:"2px solid rgb(126, 73, 249)", animation:"avatarGlow 3s ease-in-out infinite", position:"relative", zIndex:1 }}>
                <User size={58} color="rgba(255,255,255,.9)" strokeWidth={1.5} />
              </div>
            </div>
            <div style={{ color:"#ddd0ff", fontSize:18, fontWeight:800, letterSpacing:.5 }}>
              {loading ? "..." : [user.nombre, user.apellido].filter(Boolean).join(" ") || "Mi Perfil"}
            </div>

            <div
              style={{
                fontFamily: "'Space Mono',monospace",
                fontSize: 10,
                letterSpacing: 3,
                textTransform: "uppercase",
                color: "rgba(189, 178, 237, 0.86)",
                background: "rgba(100,60,255,.1)",
                border: "1px solid rgba(100,60,255,.2)",
                padding: "4px 14px",
                borderRadius: 999,
              }}
            >
              {user.username ? `@${user.username}` : "DevHub Member"}
            </div>
          </div>

          <div style={{ height:1, background:"rgba(100,60,255,.12)", marginBottom:28 }} />

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 18,  color:"rgb(208, 187, 240)"  }}>
            <Field label="Usuario"            value={form.username     ?? ""} onChange={v => updateField("username",     v)} placeholder="@usuario" />
            <Field label="Preferencias"       value={form.preferencias ?? ""} onChange={v => updateField("preferencias", v)} placeholder="Frontend, Backend..." />
            <Field label="Correo electrónico" value={form.email        ?? ""} onChange={v => updateField("email",        v)} placeholder="tu@email.com"     type="email" />
            <Field label="Teléfono"           value={form.phone        ?? ""} onChange={v => updateField("phone",        v)} placeholder="+57 300 000 0000" type="tel" />
          </div>

          <div style={{ display:"flex", justifyContent:"flex-end", gap:12, marginTop:26 }}>
            <button onClick={handleLogout} style={{ height:40, padding:"0 24px", background:"transparent", border:"1px solid rgba(99, 60, 255, 0.69)", borderRadius:10, color:"rgba(180, 150, 255, 0.74)", fontFamily:"'Syne',sans-serif", fontSize:12, fontWeight:700, letterSpacing:2, textTransform:"uppercase", cursor:"pointer" }}>
              Cerrar sesión
            </button>
            <button onClick={handleSave} disabled={saving || loading} 
            style={{ height:40, padding:"0 28px", background: saving ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:10, color:"white", 
            fontFamily:"'Syne',sans-serif", fontSize:12, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: saving ? "not-allowed" : "pointer", width: "fit-content", boxShadow:"0 4px 20px rgba(90,40,220,.4)" }}>
  
              {saving ? "Guardando..." : "Guardar cambios"}
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}

function Field({ label, placeholder, type="text", value, onChange }: {
  label: string; placeholder: string; type?: string; value: string; onChange: (v: string) => void;
}) {
  return (
    <div style={{ display:"flex", flexDirection:"column", gap:6 }}>
      <label style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:2.5, textTransform:"uppercase", color:"rgb(159, 130, 255)" }}>
        {label}
      </label>
      <input
        className="field-input"
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={e => onChange(e.target.value)}
        style={{ height:44, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.18)", borderRadius:10, padding:"0 14px", fontFamily:"'Space Mono',monospace", fontSize:13, color:"#ddd0ff", outline:"none" }}
      />
    </div>
  );
}

const iconBtn: React.CSSProperties = {
  width:34, height:34, borderRadius:"50%",
  border:"1px solid rgba(100,60,255,.35)",
  display:"flex", alignItems:"center", justifyContent:"center",
  cursor:"pointer", background:"rgba(100,60,255,.05)",
};