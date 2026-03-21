"use client";
import { useEffect, useState } from "react";

export function useCurrentUser() {
  const [nombre, setNombre] = useState<string | null>(null);

  useEffect(() => {
    try {
      const raw = localStorage.getItem("devhub_user");
      if (raw) {
        const u = JSON.parse(raw);
        setNombre(u.nombre ?? u.username ?? null);
      }
    } catch {}
  }, []);

  return nombre;
}
