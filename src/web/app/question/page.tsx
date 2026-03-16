"use client";
import React from "react";
import { LogOut, User } from "lucide-react";

export default function AddQuestionPage() {
  const [pregunta, setPregunta] = React.useState<string>("");
  const [opcionA, setOpcionA] = React.useState<string>("");
  const [opcionB, setOpcionB] = React.useState<string>("");
  const [opcionC, setOpcionC] = React.useState<string>("");
  const [categoria, setCategoria] = React.useState<string>("");
  const [dificultad, setDificultad] = React.useState<string>("");

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

      <section style={{ padding: "24px 32px",  justifyContent: "center"}}>
        <p style={{ fontFamily: "'Times New Roman', serif", fontSize: 15, color: "#222", marginBottom: 24, justifyContent: "center" }}>
          
        </p>

        <form
          onSubmit={(e) => {
            e.preventDefault();
            // Backend connection logic
          }}
          style={{ fontFamily: "'Times New Roman', serif", color: "#222",  justifyContent: "center" }}
        >
          <div style={{ marginBottom: 32, display: "flex", flexDirection: "column", alignItems: "center" }}>
            <label style={labelStyle}>   
              <span style={{ fontWeight: 700 }}>Pregunta</span>
             <span style={{ display: "block", fontSize: 13, color: "#313030", fontWeight: 400 }}>
              Escribe la pregunta que deseas agregar a la plataforma.
             </span>
            </label>
            <textarea
              value={pregunta}
              onChange={(e) => setPregunta(e.target.value)}
              style={textareaStyle}
            />
          </div>

          <div style={{ display: "flex", justifyContent: "center", flexWrap: "wrap", gap: 120 }}>
            <div>
              
              <label style={labelStyle}>Escribe las opciones de respuesta para la pregunta:</label>
              <div style={{ display: "flex", alignItems: "center", gap: 10, marginTop: 25 }}>
                <span style={{ fontWeight: 700 }}>Opción A</span>
                <input type="text" value={opcionA} onChange={(e) => setOpcionA(e.target.value)} style={smallInputStyle} />
              </div>
              <div style={{ display: "flex", alignItems: "center", gap: 10, marginTop: 30 }}>
                <span style={{ fontWeight: 700 }}>Opción B</span>
                <input type="text" value={opcionB} onChange={(e) => setOpcionB(e.target.value)} style={smallInputStyle} />
              </div>
              <div style={{ display: "flex", alignItems: "center", gap: 10, marginTop: 30 }}>
                <span style={{ fontWeight: 700 }}>Opción C</span>
                <input type="text" value={opcionC} onChange={(e) => setOpcionC(e.target.value)} style={smallInputStyle} />
              </div>
            </div>
                    
            <div style={{ display: "flex", flexDirection: "column",  gap: 25, marginTop: 10 }}>
              <div>
                  <label style={labelStyle}>   
                     <span style={{ fontWeight: 700 }}>Categoria</span>
                      <span style={{ display: "block", fontSize: 13, color: "#303030", fontWeight: 400 }}>
                      Ej: JavaScript, Python, Bases de datos
                        </span>
                       </label>
                <input type="text" value={categoria} onChange={(e) => setCategoria(e.target.value)} style={{ ...smallInputStyle, marginTop: 8 }} />
              </div>
              <div>
                <label style={labelStyle}>  
                  <span style={{ fontWeight: 700 }}>Dificultad</span>
                  <span style={{ display: "block", fontSize: 13, color: "#303030", fontWeight: 400 }}>
                    Fácil, Media o Difícil
                    </span>
                </label>
                <input type="text" value={dificultad} onChange={(e) => setDificultad(e.target.value)} style={{ ...smallInputStyle, marginTop: 8 }} />
              </div>
            </div>
          </div>

          <div style={{ display: "flex", justifyContent: "center", marginTop: 36 }}>
            <button type="submit" style={{
              fontFamily: "'Times New Roman', serif",
              background: "#5B2BBF", color: "white", border: "none",
              padding: "10px 36px", borderRadius: 999, cursor: "pointer",
              fontWeight: 700, fontSize: 15,
            }}>
              Agregar
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}

const labelStyle: React.CSSProperties = {
  fontFamily: "'Times New Roman', serif",
  fontWeight: 700, fontSize: 15, color: "#222",
  display: "block", marginBottom: 4,
};

const textareaStyle: React.CSSProperties = {
  fontFamily: "'Times New Roman', serif",
  width: "100%", maxWidth: 640, height: 80,
  border: "2px solid #222", borderRadius: 16,
  padding: "10px 14px", outline: "none",
  background: "white", color: "black", fontSize: 14,
  resize: "vertical", display: "block", marginTop: 6,
};

const smallInputStyle: React.CSSProperties = {
  fontFamily: "'Times New Roman', serif",
  width: 180, height: 38,
  border: "2px solid #222", borderRadius: 999,
  padding: "0 14px", outline: "none",
  background: "white", color: "black", fontSize: 14,
};