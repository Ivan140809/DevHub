"use client";
import React, { useState } from "react";
import { LogOut, User } from "lucide-react";
import { useRouter } from "next/navigation";

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 },
  { l: "35%", d: "10s", dl: "-1s", s: 2 },
  { l: "45%", d: "11s", dl: "-6s", s: 3 },
  { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 },
  { l: "72%", d: "13s", dl: "-5s", s: 3 },
  { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "88%", d: "15s", dl: "-8s", s: 2 },
  { l: "93%", d: "9s", dl: "-1s", s: 3 },
  { l: "10%", d: "12s", dl: "-4s", s: 2 },
];

type UserData = {
  nombre?: string;
  apellido?: string;
  email?: string;
  username?: string;
  phone?: string;
  preferencias?: string;
};

export default function ProfilePage() {
  const router = useRouter();

  const [user] = useState<UserData>(() => {
    if (typeof window === "undefined") return {};

    try {
      const storedUser = localStorage.getItem("devhub_user");
      return storedUser ? JSON.parse(storedUser) : {};
    } catch {
      return {};
    }
  });

  function handleLogout() {
    localStorage.removeItem("devhub_user");
    router.push("/login");
  }

  return (
    <main
      style={{
        minHeight: "100vh",
        background: "#07070f",
        fontFamily: "'Syne',sans-serif",
        position: "relative",
        overflow: "hidden",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <div
        style={{
          position: "absolute",
          borderRadius: "50%",
          width: 500,
          height: 500,
          background:
            "radial-gradient(circle,rgba(90,30,200,.25) 0%,transparent 70%)",
          top: -150,
          left: -150,
          animation: "orbFloat 10s ease-in-out infinite alternate",
          pointerEvents: "none",
        }}
      />
      <div
        style={{
          position: "absolute",
          borderRadius: "50%",
          width: 400,
          height: 400,
          background:
            "radial-gradient(circle,rgba(110,50,255,.2) 0%,transparent 70%)",
          bottom: -100,
          right: -100,
          animation: "orbFloat 10s ease-in-out infinite alternate",
          animationDelay: "-5s",
          pointerEvents: "none",
        }}
      />
      <div
        style={{
          position: "absolute",
          borderRadius: "50%",
          width: 280,
          height: 280,
          background:
            "radial-gradient(circle,rgba(70,20,160,.2) 0%,transparent 70%)",
          top: "30%",
          left: "50%",
          animation: "orbFloat 14s ease-in-out infinite alternate",
          animationDelay: "-3s",
          pointerEvents: "none",
        }}
      />
      <div style={{ position: "absolute", inset: 0, pointerEvents: "none", zIndex: 1 }}>
        {PARTICLES.map((p, i) => (
          <div
            key={i}
            style={{
              position: "absolute",
              borderRadius: "50%",
              width: p.s,
              height: p.s,
              background: "rgba(160,100,255,.7)",
              left: p.l,
              bottom: -10,
              animation: `floatUp ${p.d} linear ${p.dl} infinite`,
            }}
          />
        ))}
      </div>

      <header
        style={{
          position: "relative",
          zIndex: 5,
          display: "flex",
          alignItems: "center",
          justifyContent: "space-between",
          padding: "16px 28px",
          borderBottom: "1px solid rgba(100,60,255,.15)",
          background: "rgba(7,7,15,.7)",
          backdropFilter: "blur(10px)",
        }}
      >
        <div style={iconBtn} onClick={handleLogout}>
          <LogOut size={15} color="#b8a0ff" style={{ cursor: "pointer" }} />
        </div>

        <span
          style={{
            fontFamily: "'Space Mono',monospace",
            fontWeight: 700,
            fontSize: 16,
            letterSpacing: 6,
            color: "#b8a0ff",
            textShadow: "0 0 20px rgba(150,100,255,.5)",
            position: "absolute",
            left: "50%",
            transform: "translateX(-50%)",
          }}
        >
          DEVHUB
        </span>

        <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
          <span
            style={{
              fontSize: 11,
              fontWeight: 700,
              letterSpacing: 3,
              color: "rgba(180,160,255,.5)",
              cursor: "pointer",
              textTransform: "uppercase",
            }}
          >
            FAQ
          </span>
          <div style={iconBtn}>
            <User size={15} color="#b8a0ff" />
          </div>
        </div>
      </header>

      <section
        style={{
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          padding: "36px 20px",
          position: "relative",
          zIndex: 5,
        }}
      >
        <div
          style={{
            width: "100%",
            maxWidth: 780,
            background: "rgba(14,10,28,.9)",
            border: "1px solid rgba(100,60,255,.22)",
            borderRadius: 22,
            padding: "42px 42px 36px",
            backdropFilter: "blur(20px)",
            boxShadow:
              "0 0 0 1px rgba(255,255,255,.03) inset,0 30px 80px rgba(80,40,200,.2)",
            animation: "slideUp .7s cubic-bezier(.16,1,.3,1) both",
          }}
        >
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 16,
              marginBottom: 36,
            }}
          >
            <div
              style={{
                position: "relative",
                width: 130,
                height: 130,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
            >
              <div
                style={{
                  position: "absolute",
                  inset: -8,
                  borderRadius: "50%",
                  border: "1px solid rgba(120,70,255,.25)",
                  animation: "ringPulse 3s ease-in-out infinite",
                }}
              />
              <div
                style={{
                  width: 130,
                  height: 130,
                  borderRadius: "50%",
                  background: "linear-gradient(145deg,#7040ff,#4020b0)",
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  border: "2px solid rgba(140,90,255,.5)",
                  animation: "avatarGlow 3s ease-in-out infinite",
                  position: "relative",
                  zIndex: 1,
                }}
              >
                <User size={58} color="rgba(255,255,255,.9)" strokeWidth={1.5} />
              </div>
            </div>

            <div style={{ color: "#ddd0ff", fontSize: 18, fontWeight: 800, letterSpacing: 0.5 }}>
              {[user.nombre, user.apellido].filter(Boolean).join(" ") || "Mi Perfil"}
            </div>

            <div
              style={{
                fontFamily: "'Space Mono',monospace",
                fontSize: 10,
                letterSpacing: 3,
                textTransform: "uppercase",
                color: "rgba(189, 178, 237, 0.45)",
                background: "rgba(100,60,255,.1)",
                border: "1px solid rgba(100,60,255,.2)",
                padding: "4px 14px",
                borderRadius: 999,
              }}
            >
              {user.username ? `@${user.username}` : "DevHub Member"}
            </div>
          </div>

          <div style={{ height: 1, background: "rgba(100,60,255,.12)", marginBottom: 28 }} />

          <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 18  }}>
            <Field label="Usuario" value={user.username || ""} placeholder="@usuario" />
            <Field
            
              label="Preferencias"
              value={user.preferencias || ""}
              placeholder="Frontend, Backend..."
            />
            <Field
              label="Correo electrónico"
              value={user.email || ""}
              placeholder="tu@email.com"
              type="email"
            />
            <Field
              label="Teléfono"
              value={user.phone || ""}
              placeholder="+57 300 000 0000"
              type="tel"
            />
          </div>

          <div style={{ display: "flex", justifyContent: "flex-end", gap: 12, marginTop: 26 }}>
            <button
              onClick={handleLogout}
              style={{
                height: 40,
                padding: "0 24px",
                background: "transparent",
                border: "1px solid rgba(100,60,255,.3)",
                borderRadius: 10,
                color: "rgba(180,150,255,.6)",
                fontFamily: "'Syne',sans-serif",
                fontSize: 12,
                fontWeight: 700,
                letterSpacing: 2,
                textTransform: "uppercase",
                cursor: "pointer",
              }}
            >
              Cerrar sesión
            </button>

            <button
              style={{
                height: 40,
                padding: "0 28px",
                background: "linear-gradient(135deg,#7040ff,#5020e0)",
                border: "none",
                borderRadius: 10,
                color: "white",
                fontFamily: "'Syne',sans-serif",
                fontSize: 12,
                fontWeight: 800,
                letterSpacing: 3,
                textTransform: "uppercase",
                cursor: "pointer",
                boxShadow: "0 4px 20px rgba(90,40,220,.4)",
              }}
            >
              Guardar cambios
            </button>
          </div>
        </div>
      </section>
    </main>
  );
}

function Field({
  label,
  placeholder,
  type = "text",
  value,
}: {
  label: string;
  placeholder: string;
  type?: string;
  value: string;
}) {
  return (
    <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
      <label
        style={{
          fontFamily: "'Space Mono',monospace",
          fontSize: 10,
          fontWeight: 700,
          letterSpacing: 2.5,
          textTransform: "uppercase",
          color: "rgba(160,130,255,.45)",
        }}
      >
        {label}
      </label>
      <input
        type={type}
        placeholder={placeholder}
        defaultValue={value}
        style={{
          height: 44,
          background: "rgba(255,255,255,.04)",
          border: "1px solid rgba(100,60,255,.18)",
          borderRadius: 10,
          padding: "0 14px",
          fontFamily: "'Space Mono',monospace",
          fontSize: 13,
          color: "#ddd0ff",
          outline: "none",
        }}
      />
    </div>
  );
}

const iconBtn: React.CSSProperties = {
  width: 34,
  height: 34,
  borderRadius: "50%",
  border: "1px solid rgba(100,60,255,.35)",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
  cursor: "pointer",
  background: "rgba(100,60,255,.05)",
};