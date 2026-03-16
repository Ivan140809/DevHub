import { NextResponse } from "next/server";

export async function POST(request: Request) {
  const { email, password } = await request.json();
  const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/register`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),

  });
  if (!r.ok) {
    const error = await r.json();
    return NextResponse.json({ error: error.message }, { status: r.status });
  }
  return NextResponse.json({ ok: true });
}