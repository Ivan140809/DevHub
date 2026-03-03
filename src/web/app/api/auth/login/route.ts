import { ok } from "assert";
import { NextResponse } from "next/server";

export async function POST(request: Request) {
    const body = await request.json();
    const r= await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
    });
    if (!r.ok) {
        const error = await r.json();
        return NextResponse.json({ error: error.message }, { status: r.status });
     }
     const data= await r.json();
    const response = NextResponse.json({ok: true});
    response.cookies.set("token", data.token, {
        httpOnly: true,
        secure: false,
        sameSite: "lax",
        path: "/",
    });
    return response;
}