"use client";
import React from "react";
import { LogOut, User, Search } from "lucide-react";

export default function QuestionListPage() {
  const [searchQuery, setSearchQuery] = React.useState<string>("");

  const preguntas = [
    { id: 0, pregunta: "Cuales son las principales caracteristicas de Docker?", categoria: "Backend", dificultad: "Media" },
    { id: 1, pregunta: "Que lenguaje se usa en machine learning?", categoria: "Backend", dificultad: "Facil" },
  ];

  return (
    <main style={{ minHeight: "100vh", background: "#f5f5f5" }}>

      <header style={{
        position: "relative", background: "#4d1cb5", color: "white",
        padding: "16px 24px", boxShadow: "0 6px 20px rgba(0,0,0,0.12)",
      }}>
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
          <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
            <LogOut size={34} style={{ cursor: "pointer" }} />
          </div>
          <div style={{
            fontFamily: "'Times New Roman', serif", position: "absolute",
            left: "50%", transform: "translateX(-50%)",
            fontWeight: 800, letterSpacing: 2, fontSize: 20,
          }}>DEVHUB</div>
          <div style={{ display: "flex", alignItems: "center", gap: 18 }}>
            <span style={{ fontWeight: 700, cursor: "pointer" }}>FAQ</span>
            <User size={34} style={{ cursor: "pointer" }} />
          </div>
        </div>
        <div style={{ height: 8, marginTop: 14, background: "#3b1590", borderRadius: 999 }} />
      </header>

      <section style={{ padding: "24px 32px" }}>

        {/* Fila: título + filtrar + buscador */}
        <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", marginBottom: 6 }}>
          <span style={{ fontFamily: "'Times New Roman', serif", fontWeight: 700, fontSize: 18,color: "#000000" }}>
            Preguntas disponibles
          </span>
          <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
            {/* aca va el boton de filtrat >:/ */}
            <div style={{ position: "relative", display: "flex", alignItems: "center" }}>
              <Search size={18} style={{ position: "absolute", left: 10, color: "#232323" }} />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                style={{
                  fontFamily: "'Times New Roman', serif",
                  width: 200, height: 38,
                  border: "2px solid #222", borderRadius: 999,
                  padding: "0 14px 0 34px", outline: "none",
                  background: "white", color: "black", fontSize: 14,
                }}
              />
            </div>
          </div>
        </div>

        {/* Contador */}
        <p style={{ fontFamily: "'Times New Roman', serif", fontSize: 15, color: "#222", marginBottom: 16, marginLeft: 16 }}>
          0-{preguntas.length * 10 - (10 - preguntas.length)}
        </p>

        {/* Tabla */}
        <div style={{ border: "2px solid #222", borderRadius: 4, overflow: "hidden" }}>
          {preguntas
            .filter((p) => p.pregunta.toLowerCase().includes(searchQuery.toLowerCase()))
            .map((p, index) => (
              <div
                key={p.id}
                style={{
                  display: "flex", alignItems: "center",
                  borderBottom: index < preguntas.length - 1 ? "1px solid #222" : "none",
                  background: "white",
                }}
              >
                {/* numero de pregunta */}
                <div style={{
                  color: "#353434",
                  width: 80, minWidth: 80,
                  borderRight: "1px solid #222",
                  display: "flex", alignItems: "center", justifyContent: "center",
                  padding: "24px 0",
                  fontFamily: "'Times New Roman', serif", fontWeight: 700, fontSize: 18,
                }}>
                  {p.id}
                </div>

                {/* contenido de la tabla */}
                <div style={{ padding: "16px 24px", flex: 1 }}>
                  <p style={{
                    fontFamily: "'Times New Roman', serif", fontSize: 18,
                    color: "#222", textAlign: "start", marginBottom: 6,
                  }}>
                    {p.pregunta}
                  </p>
                  <div style={{ display: "flex", gap: 40 }}>
                    <span style={{ fontFamily: "'Times New Roman', serif", fontSize: 13, color: "#5B2BBF", fontWeight: 600 }}>
                      Categoria: {p.categoria}
                    </span>
                    <span style={{ fontFamily: "'Times New Roman', serif", fontSize: 13, color: "#5B2BBF", fontWeight: 600 }}>
                      Dificultad: {p.dificultad}
                    </span>
                  </div>
                </div>
              </div>
            ))}
        </div>

      </section>
    </main>
  );
}