"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 }, { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 }, { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 }, { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "35%", d: "11s", dl: "-3s", s: 2 }, { l: "45%", d: "14s", dl: "-6s", s: 3 },
];


type Step = "email" | "code" | "password";

export default function ResetPasswordPage() {
  const router = useRouter();
  const [step, setStep] = useState<Step>("email");


  const [email, setEmail] = useState("");
  const [code, setCode] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

 
  async function handleSendEmail() {
    if (!email.trim()) { setError("Ingresa tu correo electrónico."); return; }
    setLoading(true); setError(null);
    try {
      const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const res = await fetch(`${BASE}/auth/forgot-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email }),
      });
      if (!res.ok) throw new Error("No se pudo enviar el correo.");
      setStep("code");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Error al enviar el correo.");
    } finally {
      setLoading(false);
    }
  }

 
  async function handleVerifyCode() {
    if (!code.trim()) { setError("Ingresa el código que recibiste."); return; }
    setLoading(true); setError(null);
    try {
      const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const res = await fetch(`${BASE}/auth/verify-code`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, code }),
      });
      if (!res.ok) throw new Error("Código incorrecto o expirado.");
      setStep("password");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Error al verificar el código.");
    } finally {
      setLoading(false);
    }
  }

  async function handleResetPassword() {
    if (!newPassword.trim()) { setError("Ingresa tu nueva contraseña."); return; }
    if (newPassword !== confirmPassword) { setError("Las contraseñas no coinciden."); return; }
    setLoading(true); setError(null);
    try {
      const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const res = await fetch(`${BASE}/auth/reset-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, code, newPassword }),
      });
      if (!res.ok) throw new Error("No se pudo restablecer la contraseña.");
      setSuccess(true);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Error al restablecer la contraseña.");
    } finally {
      setLoading(false);
    }
  }


  const steps: Step[] = ["email", "code", "password"];
  const stepLabels = ["Correo", "Código", "Contraseña"];
  const currentStepIndex = steps.indexOf(step);

  return (
    <main style={{
      minHeight: "100vh", background: "#07070f",
      fontFamily: "'Syne', sans-serif",
      position: "relative", overflow: "hidden",
      display: "flex", flexDirection: "column",
    }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp  { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideUp  { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes fadeIn   { 0%{opacity:0} 100%{opacity:1} }
        @keyframes checkPop { 0%{transform:scale(0)} 60%{transform:scale(1.2)} 100%{transform:scale(1)} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .reset-input:focus { border-color: rgba(140,80,255,.5) !important; box-shadow: 0 0 0 3px rgba(100,60,255,.15) !important; outline: none; }
        .reset-btn:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(90,40,220,.5) !important; }
        .reset-btn { transition: all .2s ease; }
        .back-btn:hover { background: rgba(100,60,255,.15) !important; transform: translateX(-4px); }
        .back-btn { transition: all .2s ease; }
        .step-dot { transition: all .3s ease; }
      `}</style>

      
      <div style={{ position:"fixed", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none", zIndex:0 }} />
      <div style={{ position:"fixed", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none", zIndex:0 }} />
      {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width:p.s, height:p.s, left:p.l, animationDuration:p.d, animationDelay:p.dl }} />)}

      
      <header style={{ position:"relative", zIndex:10, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.8)", backdropFilter:"blur(10px)" }}>
        <button onClick={() => router.push("/login")} className="back-btn" style={{ display:"flex", alignItems:"center", gap:8, padding:"8px 16px", background:"rgba(100,60,255,.08)", border:"1px solid rgba(100,60,255,.2)", borderRadius:10, color:"rgba(180,150,255,.8)", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <ArrowLeft size={14} /> Volver
        </button>
        <span style={{ fontFamily:"'Space Mono',monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>
          DEVHUB
        </span>
        <div style={{ width:80 }} />
      </header>

      
      <section style={{ flex:1, display:"flex", alignItems:"center", justifyContent:"center", padding:"36px 0 30px", position:"relative", zIndex:5 }}>
        <div style={{ maxHeight: 700, width:"100%", maxWidth:450, animation:"slideUp .7s cubic-bezier(.16,1,.3,1) both" }}>

          
          {!success && (
            <div style={{ display:"flex", alignItems:"center", justifyContent:"center", gap:0, marginBottom:36 }}>
              {steps.map((s, i) => (
                <React.Fragment key={s}>
                  <div style={{ display:"flex", flexDirection:"column", alignItems:"center", gap:6 }}>
                    <div className="step-dot" style={{
                      width:32, height:32, borderRadius:"50%",
                      background: i <= currentStepIndex ? "linear-gradient(135deg,#7040ff,#5020e0)" : "rgba(100,60,255,.1)",
                      border: i <= currentStepIndex ? "none" : "1px solid rgba(100,60,255,.25)",
                      display:"flex", alignItems:"center", justifyContent:"center",
                      fontFamily:"'Space Mono',monospace", fontSize:12, fontWeight:700,
                      color: i <= currentStepIndex ? "white" : "rgba(140,100,255,.4)",
                      boxShadow: i === currentStepIndex ? "0 4px 16px rgba(90,40,220,.4)" : "none",
                    }}>
                      {i < currentStepIndex ? "✓" : i + 1}
                    </div>
                    <span style={{ fontFamily:"'Space Mono',monospace", fontSize:15, letterSpacing:"1.5px", textTransform:"uppercase", color: i <= currentStepIndex ? "rgba(180,150,255,.8)" : "rgba(100,80,180,.4)" }}>
                      {stepLabels[i]}
                    </span>
                  </div>
                  {i < steps.length - 1 && (
                    <div style={{ width:60, height:1, background: i < currentStepIndex ? "rgba(112,64,255,.6)" : "rgba(100,60,255,.15)", margin:"0 8px", marginBottom:22, transition:"all .3s ease" }} />
                  )}
                </React.Fragment>
              ))}
            </div>
          )}

          
          <div style={{ background:"rgba(14,10,28,.9)", border:"1px solid rgba(100,60,255,.22)", borderRadius:22, padding:"38px 36px", backdropFilter:"blur(20px)", boxShadow:"0 0 0 1px rgba(255,255,255,.03) inset, 0 30px 80px rgba(80,40,200,.2)" }}>

            
            {success ? (
              <div style={{ textAlign:"center", display:"flex", flexDirection:"column", alignItems:"center", gap:20 }}>
                <div style={{ width:72, height:72, borderRadius:"50%", background:"linear-gradient(135deg,rgba(30,160,100,.2),rgba(80,220,150,.1))", border:"1px solid rgba(30,160,100,.3)", display:"flex", alignItems:"center", justifyContent:"center", animation:"checkPop .5s cubic-bezier(.34,1.56,.64,1) both" }}>
                  <span style={{ fontSize:32 }}>✓</span>
                </div>
                <h2 style={{ color:"#ddd0ff", fontSize:20, fontWeight:800, margin:0 }}>¡Contraseña restablecida!</h2>
                <p style={{ color:"rgba(180, 160, 255, 0.9)", fontSize:14, lineHeight:1.7, margin:0 }}>
                  Tu contraseña fue actualizada correctamente. Ya puedes iniciar sesión con tu nueva contraseña.
                </p>
                <button onClick={() => router.push("/login")} className="reset-btn" style={{ width:"100%", height:48, background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:12, color:"white", fontFamily:"'Syne',sans-serif", fontSize:13, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor:"pointer", boxShadow:"0 6px 24px rgba(90,40,220,.4)", marginTop:8 }}>
                  Ir al Login
                </button>
              </div>
            ) : step === "email" ? (
              <>
                <h2 style={{ color:"#ddd0ff", fontSize:20, fontWeight:800, margin:"0 0 8px" }}>Restablecer contraseña</h2>
                <p style={{ color:"rgba(181, 158, 254, 0.75)", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"1px", marginBottom:28, margin:"0 0 28px" }}>
                  Ingresa tu correo y te enviaremos un código de verificación.
                </p>

                <div style={{ display:"flex", flexDirection:"column", gap:6, marginBottom:24 }}>
                  <label style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:"2.5px", textTransform:"uppercase", color:"rgba(159, 130, 255, 0.83)" }}>
                    Correo electrónico
                  </label>
                  <input
                    type="email"
                    value={email}
                    onChange={e => { setEmail(e.target.value); setError(null); }}
                    placeholder="tu@email.com"
                    className="reset-input"
                    onKeyDown={e => e.key === "Enter" && handleSendEmail()}
                    style={{ height:48, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.18)", borderRadius:12, padding:"0 16px", fontFamily:"'Space Mono',monospace", fontSize:13, color:"#ddd0ff", transition:"all .2s ease" }}
                  />
                </div>

                {error && <div style={{ background:"rgba(200,80,80,.08)", border:"1px solid rgba(200,80,80,.2)", borderRadius:10, padding:"10px 14px", color:"rgba(240,120,120,.8)", fontFamily:"'Space Mono',monospace", fontSize:11, marginBottom:20 }}>⚠️ {error}</div>}

                <button onClick={handleSendEmail} disabled={loading} className="reset-btn" style={{ width:"100%", height:48, background: loading ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:12, color:"white", fontFamily:"'Syne',sans-serif", fontSize:13, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: loading ? "not-allowed" : "pointer", boxShadow: loading ? "none" : "0 6px 24px rgba(90,40,220,.4)" }}>
                  {loading ? "Enviando..." : "Enviar código"}
                </button>
              </>

            ) : step === "code" ? (
              <>
                <h2 style={{ color:"#ddd0ff", fontSize:20, fontWeight:800, margin:"0 0 8px" }}>Verifica tu correo</h2>
                <p style={{ color:"rgba(181, 158, 254, 0.75)", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"1px", margin:"0 0 28px" }}>
                  Enviamos un código a <span style={{ color:"#b8a0ff" }}>{email}</span>. Ingrésalo a continuación.
                </p>

                <div style={{ display:"flex", flexDirection:"column", gap:6, marginBottom:24 }}>
                  <label style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:"2.5px", textTransform:"uppercase", color:"rgba(159, 130, 255, 0.66)" }}>
                    Código de verificación
                  </label>
                  <input
                    type="text"
                    value={code}
                    onChange={e => { setCode(e.target.value); setError(null); }}
                    placeholder="123456"
                    maxLength={6}
                    className="reset-input"
                    onKeyDown={e => e.key === "Enter" && handleVerifyCode()}
                    style={{ height:48, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.18)", borderRadius:12, padding:"0 16px", fontFamily:"'Space Mono',monospace", fontSize:20, color:"#ddd0ff", letterSpacing:"8px", textAlign:"center", transition:"all .2s ease" }}
                  />
                </div>

                {error && <div style={{ background:"rgba(200,80,80,.08)", border:"1px solid rgba(200,80,80,.2)", borderRadius:10, padding:"10px 14px", color:"rgba(240,120,120,.8)", fontFamily:"'Space Mono',monospace", fontSize:11, marginBottom:20 }}>⚠️ {error}</div>}

                <button onClick={handleVerifyCode} disabled={loading} className="reset-btn" style={{ width:"100%", height:48, background: loading ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:12, color:"white", fontFamily:"'Syne',sans-serif", fontSize:13, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: loading ? "not-allowed" : "pointer", boxShadow: loading ? "none" : "0 6px 24px rgba(90,40,220,.4)", marginBottom:16 }}>
                  {loading ? "Verificando..." : "Verificar código"}
                </button>

              </>

            ) : (
              <>
                <h2 style={{ color:"#ddd0ff", fontSize:20, fontWeight:800, margin:"0 0 8px" }}>Nueva contraseña</h2>
                <p style={{ color:"rgba(159, 130, 255, 0.64)", fontFamily:"'Space Mono',monospace", fontSize:11, letterSpacing:"1px", margin:"0 0 28px" }}>
                  Elige una contraseña segura para tu cuenta.
                </p>

                <div style={{ display:"flex", flexDirection:"column", gap:18, marginBottom:24 }}>
                  <div style={{ display:"flex", flexDirection:"column", gap:6 }}>
                    <label style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:"2.5px", textTransform:"uppercase", color:"rgba(159, 130, 255, 0.66)" }}>
                      Nueva contraseña
                    </label>
                    <input
                      type="password"
                      value={newPassword}
                      onChange={e => { setNewPassword(e.target.value); setError(null); }}
                      placeholder="••••••••"
                      className="reset-input"
                      style={{ height:48, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.18)", borderRadius:12, padding:"0 16px", fontFamily:"'Space Mono',monospace", fontSize:14, color:"#ddd0ff", transition:"all .2s ease" }}
                    />
                  </div>
                  <div style={{ display:"flex", flexDirection:"column", gap:6 }}>
                    <label style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:"2.5px", textTransform:"uppercase", color:"rgba(160,130,255,.55)" }}>
                      Confirmar contraseña
                    </label>
                    <input
                      type="password"
                      value={confirmPassword}
                      onChange={e => { setConfirmPassword(e.target.value); setError(null); }}
                      placeholder="••••••••"
                      className="reset-input"
                      onKeyDown={e => e.key === "Enter" && handleResetPassword()}
                      style={{ height:48, background:"rgba(255,255,255,.04)", border:"1px solid rgba(100,60,255,.18)", borderRadius:12, padding:"0 16px", fontFamily:"'Space Mono',monospace", fontSize:14, color:"#ddd0ff", transition:"all .2s ease" }}
                    />
                  </div>
                </div>

                {error && <div style={{ background:"rgba(200,80,80,.08)", border:"1px solid rgba(200,80,80,.2)", borderRadius:10, padding:"10px 14px", color:"rgba(240,120,120,.8)", fontFamily:"'Space Mono',monospace", fontSize:11, marginBottom:20 }}>⚠️ {error}</div>}

                <button onClick={handleResetPassword} disabled={loading} className="reset-btn" style={{ width:"100%", height:48, background: loading ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:12, color:"white", fontFamily:"'Syne',sans-serif", fontSize:13, fontWeight:800, letterSpacing:"3px", textTransform:"uppercase", cursor: loading ? "not-allowed" : "pointer", boxShadow: loading ? "none" : "0 6px 24px rgba(90,40,220,.4)" }}>
                  {loading ? "Guardando..." : "Restablecer contraseña"}
                </button>
              </>
            )}
          </div>
        </div>
      </section>
    </main>
  );
}