import { NextResponse } from 'next/server'; 
import {cookies } from 'next/headers';

export async function GET() {
    const token = cookies().get("token")?.value;
    if (!token) {
        return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }
    const r = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/auth/me`, {
        headers: { Authorization: `Bearer ${token}` },
        cache: "no-store",
    });

    if (!r.ok) {
        return NextResponse.json({ error: "Unauthorized" }, { status: 401 });
    }
    const data = await r.json();
    return NextResponse.json({ user: data });
}