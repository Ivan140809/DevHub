"use client";
import { useRouter } from "next/navigation";
import { useCurrentUser } from "../hooks/useCurrentUser";
import "./Navbar.css";

export default function Navbar() {
  const router = useRouter();
  const nombre = useCurrentUser();

  const handleProfileClick = () => {
    const raw = localStorage.getItem("devhub_user");
    const isRegistered = !!raw;
    router.push(isRegistered ? "/profile" : "/login");
  };

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
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
            <polyline points="9 22 9 12 15 12 15 22" />
          </svg>
        </button>

        <button 
          type="button"
          title="Ver preguntas"
          onClick={() => router.push("/question")} 
          className="nav-icon-btn"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="12" cy="12" r="10" />
            <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3" />
            <line x1="12" y1="17" x2="12.01" y2="17" />
          </svg>
        </button>

        <button 
          type="button"
          title="Ver ranking"
          onClick={() => router.push("/ranking")} 
          className="nav-icon-btn"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <path d="M12 2L15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2Z" />
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
          title="Configuraciones"
          onClick={() => router.push("/settings")} 
          className="nav-settings-btn"
        >
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <circle cx="12" cy="12" r="3" />
            <path d="M12 1v6m0 6v6m5.2-13.2l-4.2 4.2m0 6l4.2 4.2M23 12h-6m-6 0H1m18.2 5.2l-4.2-4.2m0-6l4.2-4.2" />
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
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="rgba(255,255,255,.9)" strokeWidth="2">
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
