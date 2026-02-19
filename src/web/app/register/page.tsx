import React from "react";
import { LogOut, User } from "lucide-react";

export default function RegisterPage() {
  return (
    <main style={{ minHeight: "100vh", background: "#f5f5f5" }}>
      {/* Creation of the register header */}
      <header style={{ background: "#5B2BBF", color: "white", padding: "18px 24px" }}>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <span style={{ fontWeight: 700 }}>DEVHUB</span>
          <span style={{ fontWeight: 600 }}>FAQ</span>
          <span style={{ fontWeight: 500 }}><LogOut size={20} /></span>
          <span style={{ fontWeight: 500 }}><User size={20} /></span>
        </div>
      </header>
    <section style={{ maxWidth: 1000, margin: "60px auto", background: "white", padding: 50, borderRadius: 32 }}>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(3, 1fr)", gap: 40 }}>
          <Field label="Nombre" />
          <Field label="Apellido" />
          <Field label="Correo Electrónico" />
          <Field label="Username" />
          <Field label="Contraseña" type="password" />
          <Field label="Preferencias" />
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
            }}
          >
            Registrar
          </button>
        </div>
      </section>
    </main>
  );
}

function Field({ label, type = "text" }: { label: string; type?: string }) {
  return (
    <label style={{ display: "flex", flexDirection: "column", gap: 20, color: "Black" }}>
      <span style={{ fontWeight: 600 }}>{label}:</span>
      <input
        type={type}
        style={{
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