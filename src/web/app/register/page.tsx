"use client";
import React, { useState } from "react";
import Link from "next/link";
import { LogOut, User } from "lucide-react";
import { useRouter } from "next/navigation";

const PARTICLES = [
  {l:"5%",d:"12s",dl:"0s",s:3},{l:"15%",d:"9s",dl:"-2s",s:2},
  {l:"25%",d:"14s",dl:"-4s",s:4},{l:"35%",d:"10s",dl:"-1s",s:2},
  {l:"45%",d:"11s",dl:"-6s",s:3},{l:"55%",d:"16s",dl:"-3s",s:5},
  {l:"65%",d:"8s",dl:"-7s",s:2},{l:"72%",d:"13s",dl:"-5s",s:3},
  {l:"80%",d:"10s",dl:"-2s",s:4},{l:"88%",d:"15s",dl:"-8s",s:2},
  {l:"93%",d:"9s",dl:"-1s",s:3},{l:"10%",d:"12s",dl:"-4s",s:2},
];

export default function RegisterPage() {
  const router = useRouter();
  const [name, setName]         = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail]       = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone]       = useState("");

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne',sans-serif", position:"relative", overflow:"hidden", display:"flex", flexDirection:"column" }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat   { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp    { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideUp    { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes avatarGlow { 0%,100%{box-shadow:0 0 0 0 rgba(112,64,255,0)} 50%{box-shadow:0 0 24px 6px rgba(112,64,255,.35)} }
      `}</style>

      <div style={{ position:"absolute", inset:0, pointerEvents:"none" }}>
        <div style={{ position:"absolute", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle,rgba(90,30,200,.25) 0%,transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate" }} />
        <div style={{ position:"absolute", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle,rgba(110,50,255,.2) 0%,transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s" }} />
        <div style={{ position:"absolute", borderRadius:"50%", width:250, height:250, background:"radial-gradient(circle,rgba(80,20,180,.18) 0%,transparent 70%)", top:"40%", left:"55%", animation:"orbFloat 12s ease-in-out infinite alternate", animationDelay:"-3s" }} />
        <div style={{ position:"absolute", inset:0, zIndex:1 }}>
          {PARTICLES.map((p, i) => (
            <div key={i} style={{ position:"absolute", borderRadius:"50%", width:p.s, height:p.s, background:"rgba(160,100,255,.7)", left:p.l, bottom:-10, animation:`floatUp ${p.d} linear ${p.dl} infinite` }} />
          ))}
        </div>
      </div>

      <header style={{ position:"relative", zIndex:5, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,.15)", background:"rgba(7,7,15,.7)", backdropFilter:"blur(10px)" }}>
         <button onClick={() => router.push("/login")} style={{ display:"flex", alignItems:"center", gap:8, width:"fit-content", padding:"8px 16px", background:"rgba(98, 45, 244, 0.3)", border:"1px solid rgb(99, 60, 255)", borderRadius:10, color:"rgb(180, 150, 255)", fontFamily:"'Space Mono', monospace", fontSize:11, letterSpacing:"2px", textTransform:"uppercase", cursor:"pointer" }}>
          <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"><path d="M19 12H5M12 5l-7 7 7 7"/></svg>
          Volver
        </button>

        <span style={{ fontFamily:"'Space Mono',monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>DEVHUB</span>
        <div style={{ display:"flex", alignItems:"center", gap:14 }}>
          <div onClick={() => router.push("/profile")}  style={{ display:"flex", alignItems:"center", gap:8, cursor:"pointer", padding:"4px", borderRadius:999, border:"1px solid rgba(100,60,255,.35)", background:"rgba(100,60,255,.05)", transition:"background .2s" }}>
            <div style={{ width:36, height:36, borderRadius:"50%", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", flexShrink:0 }}>
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2"><circle cx="12" cy="8" r="4"/><path d="M4 20c0-4 3.6-7 8-7s8 3 8 7"/></svg>
            </div>
          </div>    
        </div>
      </header>

      <section style={{ flex:1, display:"flex", alignItems:"center", justifyContent:"center", padding:"30px 20px", position:"relative", zIndex:5 }}>
        <div style={{ width:"100%", maxWidth:750, background:"rgba(14,10,28,.9)", border:"1px solid rgba(100,60,255,.22)", borderRadius:22, padding:"38px 38px 34px", backdropFilter:"blur(20px)", boxShadow:"0 0 0 1px rgba(255,255,255,.03) inset,0 30px 80px rgba(80,40,200,.2)", animation:"slideUp .7s cubic-bezier(.16,1,.3,1) both" }}>

          <div style={{ width:60, height:60, borderRadius:"50%", margin:"0 auto 14px", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", border:"2px solid rgba(140,90,255,.4)", animation:"avatarGlow 3s ease-in-out infinite" }}>
            <User size={26} color="rgba(255,255,255,.9)" strokeWidth={1.8} />
          </div>

          <h2 style={{ textAlign:"center", color:"#f0eafd", fontSize:18, fontWeight:800, marginBottom:4 }}>Crea tu cuenta</h2>
          <p style={{ textAlign:"center", fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:3, textTransform:"uppercase", color:"rgba(185, 167, 247, 0.95)", marginBottom:28 }}>Únete a DevHub</p>

          <form onSubmit={async (e) => {
            e.preventDefault();
            
            if (!name || !lastName || !email || !username || !password) {
              alert("Por favor completa todos los campos obligatorios");
              return;
            }

            try {
              const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080"}/auth/register`, {
                method:"POST",
                headers:{"Content-Type":"application/json"},
                body: JSON.stringify({ 
                  firstName: name, 
                  lastName: lastName, 
                  email, 
                  username, 
                  password: password, 
                  phone 
                }),
              });
              
              if (r.ok) {
                alert("Usuario registrado exitosamente");
                router.push("/login");
              } else {
                const errorData = await r.json().catch(() => ({}));
                alert(errorData.message || "Error al registrar el usuario");
              }
            } catch (error) {
              alert("Error de conexión. Verifica que el backend esté disponible.");
            }
          }}>
            <div style={{ display:"grid", gridTemplateColumns:"1fr 1fr", gap:18, color:"rgb(255, 255, 255)"}}>
              <Field label="Nombre"             placeholder="Pepe"                    value={name}      onChange={setName} />
              <Field label="Apellido"           placeholder="Malo"                    value={lastName}  onChange={setLastName} />
              <Field label="Correo electrónico" placeholder="pepeelmalo@example.com"  value={email}     onChange={setEmail}    type="email" />
              <Field label="Username"           placeholder="@pepeelmalo"             value={username}  onChange={setUsername} />
              <Field label="Contraseña"         placeholder="••••••••"               value={password}  onChange={setPassword} type="password" />
              <Field label="Teléfono"           placeholder="+57 ...."               value={phone}     onChange={setPhone}    type="tel" />
            </div>

            <div style={{ display:"flex", alignItems:"center", gap:12, margin:"26px 0 20px" }}>
              <div style={{ flex:1, height:1, background:"rgba(100,60,255,.12)" }} />             <div style={{ flex:1, height:1, background:"rgba(100,60,255,.12)" }} />
            </div>

            <div style={{ display:"flex", alignItems:"center", justifyContent:"space-between" }}>
              <p style={{ fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgb(184, 167, 247)" }}>
                ¿Ya tienes cuenta?{" "}
                <span onClick={() => router.push("/login")}  style={{ color:"#e8e3f6", textDecoration:"none" }}>Inicia sesión</span>
              </p>
              <button type="submit" style={{ height:46, padding:"0 40px", background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:11, color:"white", fontFamily:"'Syne',sans-serif", fontSize:13, fontWeight:800, letterSpacing:4, textTransform:"uppercase", cursor:"pointer", boxShadow:"0 4px 24px rgba(90,40,220,.4)" }}>
                Registrar
              </button>
            </div>
          </form>
        </div>
      </section>
    </main>
  );
}

function Field({ label, type="text", placeholder, value, onChange}: {
  label: string; type?: string; placeholder?: string;
  value: string; onChange: (v: string) => void;
}) {
  return (
    <div style={{ display:"flex", flexDirection:"column", gap:6 }}>
      <label style={{ fontFamily:"'Space Mono',monospace", fontSize:10, fontWeight:700, letterSpacing:2.5, textTransform:"uppercase", color:"rgb(159, 130, 255)" }}>
        {label}
      </label>
      <input
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        required
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
