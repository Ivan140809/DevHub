"use client";

import React from "react";
import { LogOut, User } from "lucide-react";

export default function ProfilePage() {
  return (
    <main style={{ minHeight: "100vh", background: "#f5f5f5" }}>
      
      <header
        style={{
          background: "#4d1cb5",
          color: "white",
          padding: "18px 24px",
          boxShadow: "0 2px 0 rgba(0,0,0,0.12)",
        }}
      >
        <div
          style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            maxWidth: 1200,
            margin: "0 auto",
          }}
        >
          <span style={{ display: "flex", alignItems: "center", gap: 10 }}>
            <LogOut size={34} />
          </span>

          <span style={{ fontWeight: 800, letterSpacing: 2, fontSize: 22 }}>
            DEVHUB
          </span>

          <span style={{ display: "flex", alignItems: "center", gap: 18 }}>
            <span style={{ fontWeight: 700 }}>FAQ</span>
            <User size={34} />
          </span>
        </div>
      </header>

  
      <div
        style={{
          height: 14,
          background: "#3a1690",
          boxShadow: "inset 0 -2px 0 rgba(0,0,0,0.12)",
        }}
      />

     
      <section
        style={{
          maxWidth: 1200,
          margin: "0 auto",
          padding: "50px 24px",
        }}
      >
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "360px 1fr",
            gap: 60,
            alignItems: "start",
          }}
        >
          
          <div style={{ display: "flex", justifyContent: "center" }}>
            <div
              style={{
                width: 260,
                height: 260,
                borderRadius: "50%",
                border: "4px solid #111",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                background: "white",
              }}
            >
              <User size={150} color="#111" />
            </div>
          </div>

          {/* Form fields (2 columnas, como en la imagen) */}
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "repeat(2, minmax(260px, 1fr))",
              gap: 40,
              paddingTop: 10,
            }}
          >
            <Field label="Usuario" placeholder="Usuario" />
            <Field label="Preferencias" placeholder="Preferencias" />
            <Field label="Correo" placeholder="Correo" type="email" />
            <Field label="Teléfono" placeholder="Teléfono" type="tel" />
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
}: {
  label: string;
  placeholder: string;
  type?: string;
}) {
  return (
    <label style={{ display: "flex", flexDirection: "column", gap: 10 }}>
      <span style={{ fontWeight: 700, color: "#111" }}>{label}:</span>
      <input
        type={type}
        placeholder={placeholder}
        style={{
          height: 38,
          border: "2px solid #222",
          borderRadius: 999,
          padding: "0 14px",
          outline: "none",
          color: "#111",
          background: "white",
          fontSize: 14,
        }}
      />
    </label>
  );
}