import React from "react";
import { LogOut, User } from "lucide-react";

export default function RegisterPage() {
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
    <section style={{ maxWidth: 1000, margin: "60px auto", background: "white", padding: 50, borderRadius: 32 }}>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 40 }}>
          <Field label="Nombre" />
          <Field label="Apellido" />
          <Field label="Correo Electrónico" />
          <Field label="Username" />
          <Field label="Contraseña" type="password" />
          <Field label="Telefono" />
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
    </main>
  );
}

/*Especial components for the fields*/

function Field({ label, type = "text" }: { label: string; type?: string }) {
  return (
    <label style={{ display: "flex", flexDirection: "column", gap: 20, color: "Black" }}>
      <span style={{ fontWeight: 600 }}>{label}:</span>
      <input
        type={type}
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

