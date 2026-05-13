"use client";
import { useState, useEffect } from "react";

export type UserRole = "ADMIN" | "USER" | null;

export interface CurrentUser {
  nombre: string | null;
  username: string | null;
  email: string | null;
  role: UserRole;
  ready: boolean;
}

export function useCurrentUser(): CurrentUser {
  const [user, setUser] = useState<CurrentUser>({
    nombre: null,
    username: null,
    email: null,
    role: null,
    ready: false,
  });

  useEffect(() => {
    try {
      const raw = localStorage.getItem("devhub_user");
      if (raw) {
        const u = JSON.parse(raw);
        setUser({
          nombre: u.nombre ?? u.username ?? null,
          username: u.username ?? null,
          email: u.email ?? null,
          role: u.role ?? u.rol ?? null,
          ready: true,
        });
        return;
      }
    } catch {}
    setUser((prev) => ({ ...prev, ready: true }));
  }, []);

  return user;
}
 