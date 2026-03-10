"use client";
import React from "react";
import { LogOut, User } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function RegisterPage() {
  const router = useRouter();
  const [name, setName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone] = useState("");

  return (
    <main style={{ minHeight: "100vh", background: "#f5f5f5" }}>
      {/* Creation of the register header */}
       <header
        style={{
          position: "relative",
          background: "#4d1cb5e6",
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
      <form onSubmit={async (e) => {
        e.preventDefault();
        const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/usuario/registro`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ nombre: name, apellido: lastName, email: email, username: username, contrasena: password , phone: phone }),
        });
        if (r.ok) router.push("/login");
        else alert("Error al registrar el usuario");
      }}>
      <section style={{ maxWidth: 1000, margin: "60px auto", background: "white", padding: 50, borderRadius: 32 }}>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 40 }}>
         <Field label="Nombre" value={name} onChange={setName} />
        <Field label="Apellido" value={lastName} onChange={setLastName} />
      <Field label="Correo Electrónico" value={email} onChange={setEmail} />
      <Field label="Username" value={username} onChange={setUsername} />
      <Field label="Contraseña" type="password" value={password} onChange={setPassword} />
      <Field label="Telefono" value={phone} onChange={setPhone} />
        </div>


        <div style={{ marginTop: 50, display: "flex", justifyContent: "space-around" }}>
          <button
            style={{
              background: "#5B2BBF",
              color: "white",
              border: "none",
              padding: "10px 24px",
              borderRadius: 999,
              cursor: "pointer",
              fontWeight: 700,
              fontFamily: "'Times New Roman', serif",
            }}
          >
            Registrar
          </button>
        </div>
      </section>
      </form>
    </main>
  );
}

/*Especial components for the fields*/

function Field({ label, type = "text", value, onChange }: { 
  label: string; 
  type?: string;
  value: string;
  onChange: (v: string) => void;
}) {
  return (
    <label style={{ display: "flex", flexDirection: "column", gap: 20, color: "Black" }}>
      <span style={{ fontWeight: 600 }}>{label}:</span>
      <input
        type={type}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        style={{
          fontFamily:"'Times New Roman', serif",
          height: 35,
          border: "2px solid #222",
          borderRadius: 1000,
          padding: "0 10px",
          outline: "yellow",
        }}
      />
    </label>
  );
}
