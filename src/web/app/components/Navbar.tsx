"use client";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";
import "./Navbar.css";
import { HelpCircle, Settings,House, Trophy,Crown } from "lucide-react";
import { useState } from "react";

export default function Navbar() {
  const router = useRouter();
  const nombre = useCurrentUser();

  const handleProfileClick = () => {
    const raw = localStorage.getItem("devhub_user");
    const isRegistered = !!raw;
    router.push(isRegistered ? "/profile" : "/login");
  };

  //conectar con el backend para obtener los puntos acumulados del usuario 
  const [puntosAcumulados, setPuntosAcumulados] = useState(0);

  return (
    <header style={{
      position: "relative",
      zIndex: 10,
      display: "flex",
      alignItems: "center",
      justifyContent: "space-between",
      padding: "16px 28px",
      borderBottom: "1px solid rgba(100,60,255,.15)",
      background: "rgba(7,7,15,.8)",
      backdropFilter: "blur(10px)",
    }}>
      <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
        <button 
          type="button"
          title="Ir al inicio"
          onClick={() => router.push("/home")} 
          className="nav-icon-btn"
        >
          <svg width="156" height="18"  viewBox="0 0 26 26" fill="none" stroke="currentColor" strokeWidth="2">
            <House size={28}/>
          </svg>
        </button>

        <button 
          type="button"
          title="Ver preguntas"
          onClick={() => router.push("/question")} 
          className="nav-icon-btn"
        >
          <svg width="156" height="18" viewBox="0 0 26 26" fill="none" stroke="currentColor" strokeWidth="2">
        
            <HelpCircle size={28}/>
          </svg>
        </button>

        <button 
          type="button"
          title="Ver ranking"
          onClick={() => router.push("/ranking")} 
          className="nav-icon-btn"
        >
          <svg width="156" height="18" viewBox="0 0 26 26" fill="none" stroke="currentColor" strokeWidth="2">
            <Trophy size={28}/>
          </svg>
        </button>
      </div>


      <span style={{
        fontFamily: "'Space Mono', monospace",
        fontWeight: 700,
        fontSize: 16,
        letterSpacing: 6,
        color: "#b8a0ff",
        textShadow: "0 0 20px rgba(150,100,255,.5)",
        position: "absolute",
        left: "50%",
        transform: "translateX(-50%)",
      }}>
        DEVHUB
      </span>

      <div style={{ display: "flex", alignItems: "center", gap: 12 }}>

        <button 
          type="button"
          title="Puntos Acumulados"
          
          className="nav-points-btn"
        >
          <svg width="156" height="18"  viewBox="0 0 26 26" fill="none" stroke="currentColor" strokeWidth="2">
            <Crown size={28}/>
          </svg>
          <div style={{ color:"#ddd0ff", fontSize:13, fontWeight:600, letterSpacing:.5, marginRight:11 }}>
        {puntosAcumulados}
      </div>
        </button>

        <button 
          type="button"
          title="Configuraciones"
          onClick={() => router.push("/settings")} 
          className="nav-settings-btn"
        >
          <svg width="156" height="18"  viewBox="0 0 26 26" fill="none" stroke="currentColor" strokeWidth="2">
            
            <Settings size={28}/>
            
          </svg>
        </button>

        <div 
          onClick={handleProfileClick} 
          role="button"
          tabIndex={0}
          title={nombre ? `Perfil de ${nombre}` : "Ir a perfil"}
          onKeyDown={(e) => e.key === 'Enter' && handleProfileClick()}
          className={`nav-profile-btn ${nombre ? 'has-name' : ''}`}
        >
          <div style={{
            width: 36,
            height: 36,
            borderRadius: "50%",
            background: "linear-gradient(145deg,#7040ff,#4020b0)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            flexShrink: 0,
          }}>
            <svg width="17" height="17" viewBox="0 0 24 26" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2">
              <circle cx="12" cy="8" r="4" />
              <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
            </svg>
          </div>
          {nombre && <span style={{
            fontFamily: "'Space Mono',monospace",
            fontSize: 11,
            color: "rgba(200,180,255,.8)",
            whiteSpace: "nowrap",
          }}>
            {nombre}
          </span>}
        </div>
      </div>
    </header>
  );
}
