"use client"
import React from "react";
import { LogOut,User } from "lucide-react";

export default function Profile (){
    return(
        <main style={{ minHeight: "100vh", background: "#f5f5f5" }}>
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
      </main>

      
    );
}