"use client";
import React, { useState, useEffect } from "react";
import { Crown, User, MessageSquare, HelpCircle, Star } from "lucide-react";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";


const Particles = [
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
  firstName?:    string;
  lastName?:     string;
  preguntasResueltas?: number;
  totalPreguntas?: number;
  puntosAcumulados?: number;
};

const applyGlassmorphismDecorator = (Component: React.FC<{ children: React.ReactNode }>, delay = "0s") => {
  const Decorated = ({ children }: { children: React.ReactNode }) => (
    <div style={{ 
      background:"rgba(14,10,28,.9)", 
      border:"1px solid rgba(100,60,255,.22)", 
      borderRadius:22, 
      padding:"42px 42px 36px", 
      backdropFilter:"blur(20px)", 
      boxShadow:"0 0 0 1px rgba(255,255,255,.03) inset,0 30px 80px rgba(80,40,200,.2)", 
      animation:"slideUp .7s cubic-bezier(.16,1,.3,1) both",
      animationDelay: delay
    }}>
      <Component>{children}</Component>
    </div>
  );
  Decorated.displayName = `GlassmorphismDecorator(${Component.displayName || 'Component'})`;
  return Decorated;
};

const ProfileCard: React.FC<{ children: React.ReactNode }> = ({ children }) => <>{children}</>;
ProfileCard.displayName = "ProfileCard";

const StatsCard: React.FC<{ children: React.ReactNode }> = ({ children }) => <>{children}</>;
StatsCard.displayName = "StatsCard";

const DecoratedProfileCard = applyGlassmorphismDecorator(ProfileCard, "0s");
const DecoratedStatsCard = applyGlassmorphismDecorator(StatsCard, "0.1s");

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

    setUser(stored);
    setForm(stored);

    const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
    const email = stored?.email;

    const ENDPOINT = email ? `${BASE}/api/users/email/${encodeURIComponent(email)}` : null;

    if (ENDPOINT) {
      fetch(ENDPOINT)
        .then(res => { 
          if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`); 
          return res.json(); 
        })
        .then((data: UserData) => {
          const mappedData = {
            nombre: data.firstName || data.nombre,
            apellido: data.lastName || data.apellido,
            email: data.email,
            username: data.username,
            phone: data.phone,
            preferencias: data.preferencias,
          };
          setUser(mappedData);
          setForm(mappedData);
          setBackendOk(true);
          localStorage.setItem("devhub_user", JSON.stringify(mappedData));
          
          setPreguntasResueltas(data.preguntasResueltas || 0);
          setTotalPreguntas(data.totalPreguntas || 100);
          setPuntosAcumulados(data.puntosAcumulados || 0);
        })
        .catch((error) => {
          console.error('Error fetching user data:', error);
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

    const ENDPOINT = `${BASE}/api/users/email/${encodeURIComponent(email ?? "")}`;

    try {
      const payload = {
        firstName: form.nombre,
        lastName: form.apellido,
        email: form.email,
        username: form.username,
        phone: form.phone,
        preferencias: form.preferencias,
      };

      const res = await fetch(ENDPOINT, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) throw new Error();
      const updated: UserData = await res.json();
      
      const mappedData = {
        nombre: updated.firstName || updated.nombre,
        apellido: updated.lastName || updated.apellido,
        email: updated.email,
        username: updated.username,
        phone: updated.phone,
        preferencias: updated.preferencias,
      };
      
      setUser(mappedData);
      setForm(mappedData);
      localStorage.setItem("devhub_user", JSON.stringify(mappedData));
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

  const [preguntasResueltas, setPreguntasResueltas] = useState(0);
  const [totalPreguntas, setTotalPreguntas] = useState(100);
  const [puntosAcumulados, setPuntosAcumulados] = useState(0);

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne',sans-serif", position:"relative", overflow:"hidden", display:"flex", flexDirection:"column" }}>
      <style>{`
        @keyframes orbFloat  { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp   { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideUp   { 0%{opacity:0;transform:translateY(32px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes ringPulse { 0%,100%{opacity:.3;transform:scale(1)} 50%{opacity:.7;transform:scale(1.04)} }
        @keyframes avatarGlow{ 0%,100%{box-shadow:0 0 24px rgba(120,70,255,.35)} 50%{box-shadow:0 0 48px rgba(120,70,255,.6)} }
        @keyframes pulse     { 0%,100%{opacity:.3} 50%{opacity:.7} }
        @keyframes countUp   { 0%{opacity:0;transform:scale(.8)} 100%{opacity:1;transform:scale(1)} }
        @keyframes glowPulse { 0%,100%{filter:drop-shadow(0 0 4px rgba(100,60,255,0))} 50%{filter:drop-shadow(0 0 12px rgba(100,60,255,.6))} }
        .field-input:focus   { border-color: rgba(100,60,255,.5) !important; outline: none; }
        @media (max-width: 1200px) {
          .profile-container { grid-template-columns: 1fr !important; }
        }
      `}</style>

      <div style={{ position:"absolute", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.25) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none" }} />
      <div style={{ position:"absolute", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.2) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none" }} />
      <div style={{ position:"absolute", borderRadius:"50%", width:280, height:280, background:"radial-gradient(circle,rgba(70,20,160,.2) 0%,transparent 70%)", top:"30%", left:"50%", animation:"orbFloat 14s ease-in-out infinite alternate", animationDelay:"-3s", pointerEvents:"none" }} />
      <div style={{ position:"absolute", inset:0, pointerEvents:"none", zIndex:1 }}>
        {Particles.map((p, i) => (
          <div key={i} style={{ position:"absolute", borderRadius:"50%", width:p.s, height:p.s, background:"rgba(160,100,255,.7)", left:p.l, bottom:-10, animation:`floatUp ${p.d} linear ${p.dl} infinite` }} />
        ))}
      </div>

      <header style={{ position:"relative", zIndex:5, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.7)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/login")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(52, 20, 141, 0.3)", border:"1px solid rgb(99, 60, 255)", borderRadius:10, color:"rgb(180, 150, 255)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
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
        <div className="profile-container" style={{ width:"100%", maxWidth:1400, display:"grid", gridTemplateColumns:"1fr 1fr", gap:24 }}>
          
          <DecoratedProfileCard>
            {!backendOk && !loading && (
              <div style={{ background:"rgba(200,140,20,.08)", border:"1px solid rgba(200,140,20,.2)", borderRadius:10, padding:"10px 16px", fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(240,190,60,.7)", marginTop:-20, marginBottom:10 }}>
                Backend no disponible — los cambios se guardarán localmente.
              </div>
            )}

            {saved && (
              <div style={{ background:"rgba(30,160,100,.1)", border:"1px solid rgba(30,160,100,.2)", borderRadius:10, padding:"10px 16px", fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(80,220,150,.8)", marginBottom:20 }}>
                Cambios guardados correctamente.
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
    
                {saving ? "Guardando " : "Guardar cambios"}
              </button>
            </div>
          </DecoratedProfileCard>

          <DecoratedStatsCard>
            <div style={{ display:"flex", flexDirection:"column", gap:20, height:"100%" }}>
              <div>
                <h2 style={{ color:"#ddd0ff", fontSize:18, fontWeight:800, letterSpacing:.5, marginBottom:8 }}>
                  Tus Estadísticas
                </h2>
                <div style={{ height:2, background:"linear-gradient(90deg,rgba(100,60,255,.3),transparent)", borderRadius:1 }} />
              </div>

              {/* ESTADISTICAS */}

  {/* Preguntas resueltas */}
  <div 
  onMouseEnter={(e) => {
      e.currentTarget.style.transform = "translateY(-4px)";
    }}
    onMouseLeave={(e) => {
      e.currentTarget.style.transform = "translateY(0)";
    }}
  
  style={{ 
    background:"linear-gradient(135deg,rgba(100,200,255,.2),rgba(50,150,255,.05))",
  border:"1px solid #64c8ff33", borderRadius:14, padding:24,
  display:"flex", flexDirection:"column", gap:16,
  position:"relative", overflow:"hidden", transition:"all .3s cubic-bezier(.16,1,.3,1)", cursor:"pointer"
    }}>

              <div style={{ position:"absolute", inset:0, background:"radial-gradient(circle at 0% 0%, #64c8ff11, transparent 60%)", pointerEvents:"none" }} />
  
  {/* caja chiquita de ?*/}
  <div style={{ display:"flex", alignItems:"center", gap:18, position:"relative", zIndex:1 }}>
    <div style={{ width:56, height:56, borderRadius:12, background:"#64c8ff15", border:"1px solid #64c8ff44", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
      <HelpCircle size={28} color="#64c8ff" />
    </div>             
    <div style={{ flex:1 }}>
      <div style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:2.5, textTransform:"uppercase", color:"#64c8ff", marginBottom:6, opacity:.8 }}>
        Preguntas resueltas
      </div>
      <div style={{ fontSize:22, fontWeight:900, color:"#ddd0ff", lineHeight:1 }}>
        {preguntasResueltas}
        <span style={{ fontSize:14, fontWeight:400, color:"rgba(160,200,255,.4)", marginLeft:6 }}>/ {totalPreguntas}</span>
      </div>
    </div>
  </div>

  {/* Barra de progreso */}
  <div style={{ position:"relative", zIndex:1, marginTop:-20 }}>
    <div style={{ display:"flex", justifyContent:"space-between", marginBottom:1 }}>
      <span style={{ fontFamily:"'Space Mono',monospace", fontSize:9, color:"rgba(100,200,255,.5)", letterSpacing:2 }}> </span>
      <span style={{ fontFamily:"'Space Mono',monospace", fontSize:17, color:"#64c8ff", letterSpacing:1 }}>
        {totalPreguntas > 0 ? Math.round((preguntasResueltas / totalPreguntas) * 100) : 0}%
      </span>
    </div>
    {/* Fondo de la barra */}
    <div style={{ height:20, borderRadius:999, background:"rgba(100,200,255,.1)", border:"1px solid rgba(100,200,255,.15)", overflow:"hidden" }}>
      {/* Relleno de la barra */}
      <div style={{ 
        height:"100%", 
        borderRadius:999,
        width: `${totalPreguntas > 0 ? (preguntasResueltas / totalPreguntas) * 100 : 0}%`,
        background:"linear-gradient(90deg,#4080ff,#64c8ff)",
        boxShadow:"0 0 8px rgba(100,200,255,.5)",
        transition:"width .8s cubic-bezier(.16,1,.3,1)",
      }} />
    </div>
  </div>
</div>

  {/*  Puntos acumulados */}
      <div 
      onMouseEnter={(e) => {
      e.currentTarget.style.transform = "translateY(-4px)";
    }}
    onMouseLeave={(e) => {
      e.currentTarget.style.transform = "translateY(0)";
    }}
      style={{ 
    background:"linear-gradient(135deg,rgba(252, 81, 161, 0.23),rgba(255, 50, 142, 0.05))",
  border:"1px solid #ff64d633", borderRadius:14, padding:24,
  display:"flex", flexDirection:"column", gap:16,
  position:"relative", overflow:"hidden", transition:"all .3s cubic-bezier(.16,1,.3,1)", cursor:"pointer"
    }}>

              <div style={{ position:"absolute", inset:0, background:"radial-gradient(circle at 0% 0%, #64c8ff11, transparent 60%)", pointerEvents:"none" }} />
  
  {/* caja chiquita de ?*/}
  <div style={{ display:"flex", alignItems:"center", gap:18, position:"relative", zIndex:1 }}>
    <div style={{ width:56, height:56, borderRadius:12, background:"#ff64bc21", border:"1px solid #ff64d644", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
      <Crown size={28} color="#ff64db" />
    </div>             
    <div style={{ flex:1 }}>
      <div style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:2.5, textTransform:"uppercase", color:"#ff64db" , marginBottom:6, opacity:.8 }}>
        Puntos acumulados
      </div>
      <div style={{ fontSize:22, fontWeight:900, color:"#ddd0ff", lineHeight:1 }}>
        {puntosAcumulados}
    
      </div>
    </div>
  </div>
</div>

              <StatCard title="Respuestas" value="" icon={MessageSquare} gradientStart="rgba(150,100,255,.2)" gradientEnd="rgba(100,50,255,.05)" accentColor="#9664ff" />
              <StatCard title="Rating" value="" icon={Star} gradientStart="rgba(255,200,100,.2)" gradientEnd="rgba(255,150,50,.05)" accentColor="#ffc864" />

            </div>
          </DecoratedStatsCard>

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

interface StatCardProps {
  title: string;
  value: string;
  icon: React.ComponentType<{ size: number; color: string }>;
  gradientStart: string;
  gradientEnd: string;
  accentColor: string;
}

function StatCard({ title, value, icon: Icon, gradientStart, gradientEnd, accentColor }: StatCardProps) {
  return (
    <div style={{ 
      background:`linear-gradient(135deg,${gradientStart},${gradientEnd})`,
      border:`1px solid ${accentColor}33`,
      borderRadius:14, 
      padding:24,
      display:"flex",
      alignItems:"center",
      gap:18,
      position:"relative",
      overflow:"hidden",
      transition:"all .3s cubic-bezier(.16,1,.3,1)",
      cursor:"pointer"
    }}
    onMouseEnter={(e) => {
      e.currentTarget.style.background = `linear-gradient(135deg,${gradientStart},${gradientEnd})`;
      e.currentTarget.style.borderColor = accentColor + "66";
      e.currentTarget.style.transform = "translateY(-4px)";
    }}
    onMouseLeave={(e) => {
      e.currentTarget.style.transform = "translateY(0)";
      e.currentTarget.style.borderColor = accentColor + "33";
    }}
    >
      <div style={{ position:"absolute", inset:0, background:`radial-gradient(circle at 0% 0%, ${accentColor}11, transparent 60%)`, pointerEvents:"none" }} />
      
      <div style={{ 
        width:56, 
        height:56, 
        borderRadius:12, 
        background:`${accentColor}15`,
        border:`1px solid ${accentColor}44`,
        display:"flex", 
        alignItems:"center", 
        justifyContent:"center",
        flexShrink:0,
        position:"relative",
        zIndex:1
      }}>
        <Icon size={28} color={accentColor} />
      </div>

      <div style={{ flex:1, position:"relative", zIndex:1 }}>
        <div style={{ fontFamily:"'Space Mono',monospace", fontSize:9, fontWeight:700, letterSpacing:2.5, textTransform:"uppercase", color:accentColor, marginBottom:6, opacity:.8 }}>
          {title}
        </div>
        <div style={{ fontSize:32, fontWeight:900, color:"#ddd0ff", lineHeight:1, animation:"countUp .6s cubic-bezier(.16,1,.3,1) both" }}>
          {value}
        </div>
      </div>
    </div>
  );
}