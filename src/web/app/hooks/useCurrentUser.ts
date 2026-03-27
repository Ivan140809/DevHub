"use client";
import { useState } from "react";
 
export function useCurrentUser() {
  const [nombre] = useState(() => {
    try {
      const raw = localStorage.getItem("devhub_user");
      if (raw) {
        const u = JSON.parse(raw);
        return u.nombre ?? u.username ?? null;
      }
    } catch {}
    return null;
  });
 
  return nombre;
}
 