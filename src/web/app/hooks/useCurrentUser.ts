"use client";
import { useState } from "react";

export type UserRole = "ADMIN" | "USER" | null;

export interface CurrentUser {
  nombre: string | null;
  username: string | null;
  email: string | null;
  role: UserRole;
}

const initializeUser = (): CurrentUser => {
  try {
    const raw = localStorage.getItem("devhub_user");
    if (raw) {
      const u = JSON.parse(raw);
      return {
        nombre: u.nombre ?? u.username ?? null,
        username: u.username ?? null,
        email: u.email ?? null,
        role: u.role ?? u.rol ?? null,
      };
    }
  } catch {}
  
  return {
    nombre: null,
    username: null,
    email: null,
    role: null,
  };
};

export function useCurrentUser(): CurrentUser {
  const [user] = useState<CurrentUser>(initializeUser);
  return user;
}
 