"use client";
import React, { useState } from "react";
import Link from "next/link";
import { LogOut, User } from "lucide-react";
import { useRouter } from "next/navigation";

export default function LoginPage() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const particles = [
    { left:"5%",  dur:"12s", delay:"0s",   size:3, op:.5 },
    { left:"15%", dur:"9s",  delay:"-2s",  size:2, op:.4 },
    { left:"25%", dur:"14s", delay:"-4s",  size:4, op:.6 },
    { left:"35%", dur:"10s", delay:"-1s",  size:2, op:.3 },
    { left:"45%", dur:"11s", delay:"-6s",  size:3, op:.5 },
    { left:"55%", dur:"16s", delay:"-3s",  size:5, op:.4 },
    { left:"65%", dur:"8s",  delay:"-7s",  size:2, op:.6 },
    { left:"72%", dur:"13s", delay:"-5s",  size:3, op:.3 },
    { left:"80%", dur:"10s", delay:"-2s",  size:4, op:.5 },
    { left:"88%", dur:"15s", delay:"-8s",  size:2, op:.4 },
    { left:"93%", dur:"9s",  delay:"-1s",  size:3, op:.6 },
    { left:"10%", dur:"12s", delay:"-4s",  size:2, op:.3 },
  ];

  return (
    <main style={{ minHeight:"100vh", background:"#07070f", fontFamily:"'Syne',sans-serif", position:"relative", overflow:"hidden", display:"flex", flexDirection:"column" }}>

      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Space+Mono:wght@400;700&family=Syne:wght@400;700;800&display=swap');

        @keyframes orbFloat {
          from { transform: translate(0,0) scale(1); }
          to   { transform: translate(40px,30px) scale(1.1); }
        }
        @keyframes floatUp {
          0%   { transform: translateY(110vh) translateX(0px);  opacity:0; }
          10%  { opacity:1; }
          90%  { opacity:1; }
          100% { transform: translateY(-10vh) translateX(20px); opacity:0; }
        }
        @keyframes slideUp {
          from { opacity:0; transform:translateY(30px) scale(.96); }
          to   { opacity:1; transform:translateY(0) scale(1); }
        }
        @keyframes avatarGlow {
          0%,100% { box-shadow: 0 0 0 0 rgba(110,60,255,0.4), 0 0 20px rgba(100,50,255,0.2); }
          50%     { box-shadow: 0 0 0 10px rgba(110,60,255,0), 0 0 30px rgba(100,50,255,0.4); }
        }

        .dh-inp::placeholder { color: rgba(150,120,220,0.3); }
        .dh-inp:focus {
          border-color: rgba(110,70,255,0.55) !important;
          background: rgba(100,60,255,0.08) !important;
          box-shadow: 0 0 0 3px rgba(100,60,255,0.1) !important;
        }
        .dh-btn:hover  { transform:translateY(-2px); box-shadow:0 8px 32px rgba(90,40,220,0.55) !important; }
        .dh-btn:active { transform:translateY(1px); }
        .dh-icon:hover { border-color:rgba(120,80,255,0.7) !important; background:rgba(100,60,255,0.12) !important; }
        .dh-forgot:hover { color:#b8a0ff !important; }
        .dh-faq:hover    { color:#b8a0ff !important; }
        .dh-reg-link:hover { color:#b8a0ff !important; }
      `}</style>

      
      <div style={{ position:"absolute", borderRadius:"50%", width:500, height:500, background:"radial-gradient(circle, rgba(90,30,200,0.22) 0%, transparent 70%)", top:-150, left:-150, animation:"orbFloat 10s ease-in-out infinite alternate", pointerEvents:"none" }} />
      <div style={{ position:"absolute", borderRadius:"50%", width:400, height:400, background:"radial-gradient(circle, rgba(110,50,255,0.18) 0%, transparent 70%)", bottom:-100, right:-100, animation:"orbFloat 10s ease-in-out infinite alternate", animationDelay:"-5s", pointerEvents:"none" }} />

      
      <div style={{ position:"absolute", inset:0, pointerEvents:"none", zIndex:1 }}>
        {particles.map((p, i) => (
          <div key={i} style={{
            position:"absolute", borderRadius:"50%",
            width:p.size, height:p.size,
            background:"rgba(140,90,255,0.6)",
            left:p.left, bottom:"-10px",
            opacity:p.op,
            animation:`floatUp ${p.dur} linear ${p.delay} infinite`,
          }} />
        ))}
      </div>

      
      <header style={{ position:"relative", zIndex:5, display:"flex", alignItems:"center", justifyContent:"space-between", padding:"16px 28px", borderBottom:"1px solid rgba(100,60,255,0.15)", background:"rgba(7,7,15,0.6)", backdropFilter:"blur(10px)" }}>
        <div className="dh-icon" style={iconBtn}>
          <LogOut size={15} color="#b8a0ff" />
        </div>
        <span style={{ fontFamily:"'Space Mono',monospace", fontWeight:700, fontSize:16, letterSpacing:6, color:"#b8a0ff", textShadow:"0 0 20px rgba(150,100,255,0.5)", position:"absolute", left:"50%", transform:"translateX(-50%)" }}>
          DEVHUB
        </span>
        <div style={{ display:"flex", alignItems:"center", gap:14 }}>
          <span className="dh-faq" style={{ fontSize:11, fontWeight:700, letterSpacing:3, color:"rgba(180,160,255,0.5)", cursor:"pointer", textTransform:"uppercase", transition:"color .2s" }}>FAQ</span>
          <div className="dh-icon" style={iconBtn}>
            <User size={15} color="#b8a0ff" />
          </div>
        </div>
      </header>

      
      <section style={{ flex:1, display:"flex", alignItems:"center", justifyContent:"center", padding:"30px 16px", position:"relative", zIndex:5 }}>
        <div style={{ width:"100%", maxWidth:380, background:"rgba(14,10,28,0.88)", border:"1px solid rgba(100,60,255,0.2)", borderRadius:22, padding:"38px 34px 32px", backdropFilter:"blur(20px)", boxShadow:"0 0 0 1px rgba(255,255,255,0.03) inset, 0 30px 80px rgba(80,40,200,0.18), 0 2px 8px rgba(0,0,0,0.5)", animation:"slideUp .7s cubic-bezier(.16,1,.3,1) both" }}>

          
          <div style={{ width:68, height:68, borderRadius:"50%", margin:"0 auto 18px", background:"linear-gradient(145deg,#7040ff,#4020b0)", display:"flex", alignItems:"center", justifyContent:"center", border:"2px solid rgba(140,90,255,0.4)", animation:"avatarGlow 3s ease-in-out infinite" }}>
            <User size={28} color="rgba(255,255,255,0.9)" strokeWidth={1.8} />
          </div>

          <h2 style={{ textAlign:"center", color:"#ddd0ff", fontSize:19, fontWeight:800, letterSpacing:.5, marginBottom:5 }}>
            Bienvenido de vuelta
          </h2>
          <p style={{ textAlign:"center", fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:3, textTransform:"uppercase", color:"rgba(160,130,255,0.4)", marginBottom:28 }}>
            Inicia sesión en tu cuenta
          </p>

          <form onSubmit={async (e) => {
            e.preventDefault();
            const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/usuario/login`, {
              method:"POST",
              headers:{ "Content-Type":"application/json" },
              body: JSON.stringify({ email: username, password }),
            });
            if (r.ok) {
          const data = await r.json();
          localStorage.setItem("devhub_user", JSON.stringify(data));
          router.push("/profile"); 
          } else {
          alert("Credenciales incorrectas");
         }
          }}>

            <label style={lbl}>Email</label>
            <input className="dh-inp" type="email" placeholder="tu@email.com"
              value={username} onChange={(e) => setUsername(e.target.value)} style={inp} />

            <label style={{ ...lbl, marginTop:14 }}>Contraseña</label>
            <input className="dh-inp" type="password" placeholder="••••••••"
              value={password} onChange={(e) => setPassword(e.target.value)} style={inp} />

            <div className="dh-forgot" style={{ textAlign:"right", margin:"10px 0 22px", fontFamily:"'Space Mono',monospace", fontSize:10, letterSpacing:1, color:"rgba(140,100,255,0.5)", cursor:"pointer", transition:"color .2s" }}>
              ¿Olvidaste la contraseña?
            </div>

            <button type="submit" className="dh-btn" style={{ width:"100%", height:46, background:"linear-gradient(135deg,#7040ff,#5020e0)", border:"none", borderRadius:11, color:"white", fontFamily:"'Syne',sans-serif", fontSize:13, fontWeight:800, letterSpacing:4, textTransform:"uppercase", cursor:"pointer", boxShadow:"0 4px 24px rgba(90,40,220,0.4)", transition:"transform .15s, box-shadow .2s" }}>
              Login
            </button>

            <div style={{ display:"flex", alignItems:"center", gap:12, margin:"20px 0 16px" }}>
              <div style={{ flex:1, height:1, background:"rgba(100,60,255,0.12)" }} />
              <span style={{ fontFamily:"'Space Mono',monospace", fontSize:9, letterSpacing:2, textTransform:"uppercase", color:"rgba(140,110,200,0.3)" }}>¿nuevo aquí?</span>
              <div style={{ flex:1, height:1, background:"rgba(100,60,255,0.12)" }} />
            </div>

            <p style={{ textAlign:"center", fontFamily:"'Space Mono',monospace", fontSize:11, color:"rgba(140,120,200,0.45)" }}>
              ¿No tienes cuenta?{" "}
              <Link href="/register" className="dh-reg-link" style={{ color:"#9070e0", textDecoration:"none", transition:"color .2s" }}>
                Regístrate
              </Link>
            </p>
          </form>
        </div>
      </section>
    </main>
  );
}

const iconBtn: React.CSSProperties = {
  width:34, height:34, borderRadius:"50%",
  border:"1px solid rgba(100,60,255,0.35)",
  display:"flex", alignItems:"center", justifyContent:"center",
  cursor:"pointer", background:"rgba(100,60,255,0.05)",
  transition:"border-color .2s, background .2s",
};
const lbl: React.CSSProperties = {
  display:"block", fontFamily:"'Space Mono',monospace",
  fontSize:10, fontWeight:700, letterSpacing:2.5, textTransform:"uppercase",
  color:"rgba(160,130,255,0.45)", marginBottom:6,
};
const inp: React.CSSProperties = {
  width:"100%", height:44,
  background:"rgba(255,255,255,0.04)",
  border:"1px solid rgba(100,60,255,0.15)",
  borderRadius:10, padding:"0 14px",
  fontFamily:"'Space Mono',monospace", fontSize:13,
  color:"#ddd0ff", outline:"none",
  transition:"border-color .25s, background .25s, box-shadow .25s",
};