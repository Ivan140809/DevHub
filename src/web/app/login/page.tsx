"use client";
import React from "react";
import Link from "next/link";
import { LogOut, User } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
export default function LoginPage() {
  const router = useRouter();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  return (
    <main style={{ minHeight: "100vh", background: "#f5f5f5" }}>
      {/* Header with icons and other elements */}
      <header
  style={{
    position: "relative",
    background: "#4d1cb5",
    color: "white",
    padding: "16px 24px",
    boxShadow: "0 6px 20px rgba(0,0,0,0.12)",
  }}
>
  <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
    
    <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
      <LogOut size={34} style={{ cursor: "pointer" }} />
    </div>

    
    <div
      style={{
        fontFamily: "'Times New Roman', serif",
        position: "absolute",
        left: "50%",
        transform: "translateX(-50%)",
        fontWeight: 800,
        letterSpacing: 2,
        fontSize: 20,
      }}
    >
      DEVHUB
    </div>


    <div style={{ display: "flex", alignItems: "center", gap: 18 }}>
      <span style={{ fontWeight: 700, cursor: "pointer" }}>FAQ</span>
      <User size={34} style={{ cursor: "pointer" }} />
    </div>
  </div>

  <div style={{ height: 8, marginTop: 14, background: "#3b1590", borderRadius: 999 }} />
</header>

      
      <section
        style={{
          marginTop: 100,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          padding: "0 16px",
        }}
      >
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center" }}>
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              marginBottom: 30,
            }}
          >
            <div
              style={{
                width: 110,
                height: 110,
                borderRadius: "50%",
                background: "linear-gradient(135deg, #5B2BBF, #7E57C2)",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                boxShadow: "0 10px 25px rgba(0,0,0,0.15)",
              }}
            >
              <User size={55} color="white" />
            </div>

            <h2
              style={{
                marginTop: 15,
                fontWeight: 700,
                fontSize: 22,
                color: "#333",
                letterSpacing: 1,
              }}
            >
              Inicia sesión en tu cuenta DEVHUB
            </h2>
          </div>
          
          <form
            onSubmit={async (e) => {
              e.preventDefault();
              const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/usuario/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email: username, password: password }),
              });
              if (r.ok) router.push("/profile");
              else alert("Credenciales incorrectas"); 
            }}
            style={{ display: "flex", flexDirection: "column", gap: 16, alignItems: "center", color: "black", fontFamily:"time" }}
          >
            <input type="text" placeholder="Email" value={username} onChange={(e) => setUsername(e.target.value)} style={inputStyle} />
            <input type="password"  color="black" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} style={inputStyle} />

            <span style={{ fontSize: 15, color: "black", cursor: "pointer" }}>
              Olvidaste la contraseña?
            </span>

            <button
              type="submit"
              style={{
                fontFamily: "'Times New Roman', serif",
                background: "#5B2BBF",
                color: "white",
                border: "none",
                padding: "10px 30px",
                borderRadius: 999,
                cursor: "pointer",
                fontWeight: 700,
              }}
            >
              Login
            </button>

            <div style={{ marginTop: 10, fontSize: 15, color: "black" , fontFamily: "'Times New Roman', serif"}}>
              No tiene cuenta?{" "}
              <Link href="/register" style={{ color: "#5B2BBF", fontWeight: 100 }}>
                Registrarse
              </Link>
            </div>
          </form>
        </div>
      </section>
    </main>
  );
}

const inputStyle: React.CSSProperties = {
fontFamily:"'Times New Roman', serif",
  width: 330,
  height: 45,
  border: "2px solid #222",
  borderRadius: 1000,
  padding: "0 10px",
  outline: "none",
  background: "white",
  color: "black",
  fontSize: 14,
};