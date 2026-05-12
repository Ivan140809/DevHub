"use client";
import React, { Suspense, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { Lock } from "lucide-react";

const particles = [
  { left: "5%",  dur: "12s", delay: "0s",  size: 3, op: 0.5 },
  { left: "20%", dur: "9s",  delay: "-2s", size: 2, op: 0.4 },
  { left: "60%", dur: "14s", delay: "-4s", size: 4, op: 0.6 },
  { left: "80%", dur: "10s", delay: "-1s", size: 2, op: 0.3 },
];

const BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

function ResetPasswordForm() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const emailFromQuery = searchParams.get("email") ?? "";

  const [email, setEmail] = useState(emailFromQuery);
  const [code, setCode] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirm, setConfirm] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    if (newPassword !== confirm) {
      setError("Las contraseñas no coinciden.");
      return;
    }

    setLoading(true);
    try {
      const res = await fetch(`${BASE}/auth/reset-password`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, code, newPassword }),
      });

      const data = await res.json().catch(() => ({}));

      if (!res.ok) {
        throw new Error(data.message || `Error ${res.status}`);
      }

      setSuccess(true);
      setTimeout(() => router.push("/login"), 2500);
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Error al restablecer la contraseña.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ width: "100%", maxWidth: 400, background: "rgba(14,10,28,0.88)", border: "1px solid rgba(100,60,255,0.2)", borderRadius: 22, padding: "38px 34px 32px", backdropFilter: "blur(20px)", boxShadow: "0 30px 80px rgba(80,40,200,0.18)", animation: "slideUp .7s cubic-bezier(.16,1,.3,1) both" }}>

      <div style={{ width: 68, height: 68, borderRadius: "50%", margin: "0 auto 18px", background: "linear-gradient(145deg,#7040ff,#4020b0)", display: "flex", alignItems: "center", justifyContent: "center", border: "2px solid rgba(140,90,255,0.4)" }}>
        <Lock size={28} color="rgba(255,255,255,0.9)" strokeWidth={1.8} />
      </div>

      <h2 style={{ textAlign: "center", color: "#ddd0ff", fontSize: 19, fontWeight: 800, marginBottom: 5 }}>
        Restablecer contraseña
      </h2>
      <p style={{ textAlign: "center", fontFamily: "'Space Mono',monospace", fontSize: 10, letterSpacing: 3, textTransform: "uppercase", color: "rgba(159,130,255,0.76)", marginBottom: 28 }}>
        Ingresa el código que recibiste por SMS
      </p>

      {success ? (
        <div style={{ textAlign: "center", padding: "20px 0" }}>
          <div style={{ fontSize: 36, marginBottom: 12 }}>✓</div>
          <p style={{ color: "rgba(80,220,150,0.9)", fontFamily: "'Space Mono',monospace", fontSize: 12 }}>
            Contraseña actualizada. Redirigiendo al login...
          </p>
        </div>
      ) : (
        <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
          <div>
            <label style={lbl}>Email</label>
            <input className="dh-inp" type="email" value={email} onChange={(e) => setEmail(e.target.value)} style={inp} placeholder="tu@email.com" />
          </div>

          <div>
            <label style={lbl}>Código SMS</label>
            <input className="dh-inp" type="text" value={code} onChange={(e) => setCode(e.target.value)} style={{ ...inp, letterSpacing: 6, textAlign: "center", fontSize: 20, fontWeight: 700 }} placeholder="123456" maxLength={6} />
          </div>

          <div>
            <label style={lbl}>Nueva contraseña</label>
            <input className="dh-inp" type="password" value={newPassword} onChange={(e) => setNewPassword(e.target.value)} style={inp} placeholder="••••••••" />
          </div>

          <div>
            <label style={lbl}>Confirmar contraseña</label>
            <input className="dh-inp" type="password" value={confirm} onChange={(e) => setConfirm(e.target.value)} style={inp} placeholder="••••••••" />
          </div>

          {error && (
            <p style={{ color: "rgba(240,100,100,0.85)", fontFamily: "'Space Mono',monospace", fontSize: 11, margin: 0 }}>
              {error}
            </p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="dh-btn"
            style={{ width: "100%", height: 46, marginTop: 4, background: loading ? "rgba(100,60,255,0.3)" : "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 11, color: "white", fontFamily: "'Syne',sans-serif", fontSize: 13, fontWeight: 800, letterSpacing: 4, textTransform: "uppercase", cursor: loading ? "not-allowed" : "pointer", boxShadow: "0 4px 24px rgba(90,40,220,0.4)", transition: "transform .15s, box-shadow .2s" }}
          >
            {loading ? "Procesando..." : "Cambiar contraseña"}
          </button>

          <p style={{ textAlign: "center", fontFamily: "'Space Mono',monospace", fontSize: 11, color: "rgb(185,163,254)", margin: 0 }}>
            <span onClick={() => router.push("/forgot-password")} style={{ color: "#dfd6f5", cursor: "pointer", textDecoration: "underline" }}>
              Reenviar código
            </span>
          </p>
        </form>
      )}
    </div>
  );
}

export default function ResetPasswordPage() {
  return (
    <main style={{ minHeight: "100vh", background: "#07070f", fontFamily: "'Syne',sans-serif", position: "relative", overflow: "hidden", display: "flex", flexDirection: "column" }}>
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Space+Mono:wght@400;700&family=Syne:wght@400;700;800&display=swap');
        @keyframes orbFloat { from { transform: translate(0,0); } to { transform: translate(40px,30px); } }
        @keyframes floatUp  { 0% { transform: translateY(110vh); opacity: 0; } 10% { opacity: 1; } 90% { opacity: 1; } 100% { transform: translateY(-10vh); opacity: 0; } }
        @keyframes slideUp  { from { opacity: 0; transform: translateY(30px) scale(.96); } to { opacity: 1; transform: translateY(0) scale(1); } }
        .dh-inp::placeholder { color: rgba(150,120,220,0.3); }
        .dh-inp:focus { border-color: rgba(110,70,255,0.55) !important; background: rgba(100,60,255,0.08) !important; outline: none; }
        .dh-btn:hover  { transform: translateY(-2px); box-shadow: 0 8px 32px rgba(90,40,220,0.55) !important; }
        .dh-btn:active { transform: translateY(1px); }
      `}</style>

      <div style={{ position: "absolute", borderRadius: "50%", width: 500, height: 500, background: "radial-gradient(circle, rgba(90,30,200,0.22) 0%, transparent 70%)", top: -150, left: -150, animation: "orbFloat 10s ease-in-out infinite alternate", pointerEvents: "none" }} />
      <div style={{ position: "absolute", borderRadius: "50%", width: 400, height: 400, background: "radial-gradient(circle, rgba(110,50,255,0.18) 0%, transparent 70%)", bottom: -100, right: -100, animation: "orbFloat 10s ease-in-out infinite alternate", animationDelay: "-5s", pointerEvents: "none" }} />

      <div style={{ position: "absolute", inset: 0, pointerEvents: "none", zIndex: 1 }}>
        {particles.map((p, i) => (
          <div key={i} style={{ position: "absolute", borderRadius: "50%", width: p.size, height: p.size, background: "rgba(140,90,255,0.6)", left: p.left, bottom: "-10px", opacity: p.op, animation: `floatUp ${p.dur} linear ${p.delay} infinite` }} />
        ))}
      </div>

      <header style={{ position: "relative", zIndex: 5, display: "flex", alignItems: "center", justifyContent: "center", padding: "16px 28px", borderBottom: "1px solid rgba(100,60,255,.15)", background: "rgba(7,7,15,.7)", backdropFilter: "blur(10px)" }}>
        <span style={{ fontFamily: "'Space Mono',monospace", fontWeight: 700, fontSize: 16, letterSpacing: 6, color: "#b8a0ff" }}>DEVHUB</span>
      </header>

      <section style={{ flex: 1, display: "flex", alignItems: "center", justifyContent: "center", padding: "30px 16px", position: "relative", zIndex: 5 }}>
        <Suspense fallback={null}>
          <ResetPasswordForm />
        </Suspense>
      </section>
    </main>
  );
}

const lbl: React.CSSProperties = {
  display: "block", fontFamily: "'Space Mono',monospace",
  fontSize: 10, fontWeight: 700, letterSpacing: 2.5, textTransform: "uppercase",
  color: "rgb(191,175,242)", marginBottom: 6,
};
const inp: React.CSSProperties = {
  width: "100%", height: 44,
  background: "rgba(255,255,255,0.04)",
  border: "1px solid rgba(100,60,255,0.15)",
  borderRadius: 10, padding: "0 14px",
  fontFamily: "'Space Mono',monospace", fontSize: 13,
  color: "#ddd0ff",
  transition: "border-color .25s, background .25s",
};
