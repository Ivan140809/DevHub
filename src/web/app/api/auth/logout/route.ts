import { NextResponse } from 'next/server';

export async function POST() {
    const response = NextResponse.json({ ok: true });
    response.cookies.set("token", "", {
        httpOnly: true,
        secure: false,
        maxAge: 0,
    });
    return response;
    }

