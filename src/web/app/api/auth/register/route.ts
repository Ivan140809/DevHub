import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const body = await request.json();
  const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080"}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!r.ok) {
    const error = await r.json().catch(() => ({}));
    return NextResponse.json({ error: error.message ?? "Error al registrar" }, { status: r.status });
  }
  return NextResponse.json({ ok: true });
}