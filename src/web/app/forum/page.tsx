"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Navbar from "../components/Navbar";
import { MessageCircle, Plus, User, Calendar, Tag, ArrowLeft, Send, ThumbsUp, MessageSquare, Hash } from "lucide-react";

type Discussion = {
  id: string;
  title: string;
  content: string;
  category: string;
  authorId: string;
  authorUsername: string;
  createdAt: string;
  repliesCount: number;
  likesCount: number;
  tags?: string[];
};

type Reply = {
  id: string;
  content: string;
  authorId: string;
  authorUsername: string;
  createdAt: string;
  likesCount: number;
};

const PARTICLES = [
  { l: "5%", d: "12s", dl: "0s", s: 3 },
  { l: "15%", d: "9s", dl: "-2s", s: 2 },
  { l: "25%", d: "14s", dl: "-4s", s: 4 },
  { l: "55%", d: "16s", dl: "-3s", s: 5 },
  { l: "65%", d: "8s", dl: "-7s", s: 2 },
  { l: "80%", d: "10s", dl: "-2s", s: 4 },
  { l: "35%", d: "11s", dl: "-3s", s: 2 },
  { l: "45%", d: "14s", dl: "-6s", s: 3 },
];

const CATEGORIES = [
  { id: "general", name: "General", color: "rgba(100,60,255,.6)" },
  { id: "javascript", name: "JavaScript", color: "rgba(247,223,30,.7)" },
  { id: "java", name: "Java", color: "rgba(176,114,25,.7)" },
  { id: "python", name: "Python", color: "rgba(55,118,171,.7)" },
  { id: "career", name: "Carrera", color: "rgba(80,200,150,.7)" },
  { id: "help", name: "Ayuda", color: "rgba(200,100,50,.7)" },
  { id: "projects", name: "Proyectos", color: "rgba(150,50,180,.7)" },
];

function getCategoryInfo(catId: string) {
  return CATEGORIES.find(c => c.id === catId) || CATEGORIES[0];
}

function formatDate(dateStr: string): string {
  try {
    const date = new Date(dateStr);
    return date.toLocaleDateString("es-CO", {
      day: "numeric",
      month: "short",
      hour: "2-digit",
      minute: "2-digit",
    });
  } catch {
    return "Reciente";
  }
}

export default function ForumPage() {
  const router = useRouter();
  const [view, setView] = useState<"list" | "create" | "detail">("list");
  const [discussions, setDiscussions] = useState<Discussion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  
  const [currentUser, setCurrentUser] = useState<string | null>(null);
  const [selectedDiscussion, setSelectedDiscussion] = useState<Discussion | null>(null);
  const [replies, setReplies] = useState<Reply[]>([]);
  const [repliesLoading, setRepliesLoading] = useState(false);
  
  const [newTitle, setNewTitle] = useState("");
  const [newContent, setNewContent] = useState("");
  const [newCategory, setNewCategory] = useState("general");
  const [newTags, setNewTags] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  
  const [replyContent, setReplyContent] = useState("");
  const [replySubmitting, setReplySubmitting] = useState(false);
  const [replyError, setReplyError] = useState<string | null>(null);

  useEffect(() => {
    const stored = localStorage.getItem("devhub_user");
    if (stored) {
      try {
        const parsed = JSON.parse(stored);
        setCurrentUser(parsed.username || parsed.email || null);
      } catch {
        setCurrentUser(null);
      }
    }
  }, []);

  useEffect(() => {
    fetchDiscussions();
  }, [selectedCategory]);

  async function fetchDiscussions() {
    setLoading(true);
    setError(null);
    
    try {
      const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const url = selectedCategory 
        ? `${BASE_URL}/discussions?category=${selectedCategory}`
        : `${BASE_URL}/discussions`;
      
      const res = await fetch(url, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      if (!res.ok) throw new Error(`Error ${res.status}`);
      
      const data: Discussion[] = await res.json();
      setDiscussions(data);
    } catch (err) {
      console.error("Error fetching discussions:", err);
      setDiscussions([]);
      setError("No se pudieron cargar las discusiones.");
    } finally {
      setLoading(false);
    }
  }

  async function createDiscussion() {
    if (!newTitle.trim() || !newContent.trim()) {
      setSubmitError("El título y contenido son requeridos.");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
      setSubmitError("Debes iniciar sesión para crear una discusión.");
      return;
    }

    setSubmitting(true);
    setSubmitError(null);

    try {
      const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const tags = newTags.split(",").map(t => t.trim()).filter(t => t);
      
      const res = await fetch(`${BASE_URL}/discussions`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          title: newTitle,
          content: newContent,
          category: newCategory,
          tags,
        }),
      });

      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData.message || "Error al crear discusión");
      }

      const created: Discussion = await res.json();
      setDiscussions([created, ...discussions]);
      setNewTitle("");
      setNewContent("");
      setNewCategory("general");
      setNewTags("");
      setView("list");
    } catch (err: Error | unknown) {
      const msg = err instanceof Error ? err.message : "No se pudo crear la discusión.";
      setSubmitError(msg);
    } finally {
      setSubmitting(false);
    }
  }

  async function openDiscussion(disc: Discussion) {
    setSelectedDiscussion(disc);
    setView("detail");
    setRepliesLoading(true);
    setReplies([]);

    try {
      const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const res = await fetch(`${BASE_URL}/discussions/${disc.id}/replies`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });

      if (!res.ok) throw new Error(`Error ${res.status}`);
      
      const data: Reply[] = await res.json();
      setReplies(data);
    } catch (err) {
      console.error("Error fetching replies:", err);
    } finally {
      setRepliesLoading(false);
    }
  }

  async function submitReply() {
    if (!replyContent.trim() || !selectedDiscussion) return;

    const token = localStorage.getItem("token");
    if (!token) {
      setReplyError("Debes iniciar sesión para responder.");
      return;
    }

    setReplySubmitting(true);
    setReplyError(null);

    try {
      const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";
      const res = await fetch(`${BASE_URL}/discussions/${selectedDiscussion.id}/replies`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ content: replyContent }),
      });

      if (!res.ok) {
        const errData = await res.json().catch(() => ({}));
        throw new Error(errData.message || "Error al enviar respuesta");
      }

      const created: Reply = await res.json();
      setReplies([...replies, created]);
      setReplyContent("");
      
      setDiscussions(discussions.map(d => 
        d.id === selectedDiscussion.id 
          ? { ...d, repliesCount: d.repliesCount + 1 }
          : d
      ));
    } catch (err: Error | unknown) {
      const msg = err instanceof Error ? err.message : "No se pudo enviar la respuesta.";
      setReplyError(msg);
    } finally {
      setReplySubmitting(false);
    }
  }

  return (
    <main
      style={{
        minHeight: "100vh",
        background: "#07070f",
        fontFamily: "'Syne', sans-serif",
        position: "relative",
        overflow: "hidden",
      }}
    >
      <style>{`
        @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
        @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
        @keyframes floatUp { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
        @keyframes slideIn { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
        @keyframes popIn { 0%{opacity:0;transform:scale(.92)} 100%{opacity:1;transform:scale(1)} }
        @keyframes pulse { 0%,100%{opacity:.3} 50%{opacity:.7} }
        .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
        .disc-card { transition: all .25s cubic-bezier(.16,1,.3,1); }
        .disc-card:hover { transform: translateY(-3px); border-color: rgba(100,60,255,.4) !important; }
        .cat-btn.active { background: rgba(100,60,255,.2) !important; border-color: rgba(140,80,255,.5) !important; }
        .submit-btn:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(90,40,220,.5) !important; }
        .tag-pill { transition: all .2s ease; }
        .tag-pill:hover { background: rgba(100,60,255,.25) !important; }
      `}</style>

      <>
        <div style={{ position: "fixed", borderRadius: "50%", width: 500, height: 500, background: "radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top: -150, left: -150, animation: "orbFloat 10s ease-in-out infinite alternate", pointerEvents: "none", zIndex: 0 }} />
        <div style={{ position: "fixed", borderRadius: "50%", width: 400, height: 400, background: "radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom: -100, right: -100, animation: "orbFloat 10s ease-in-out infinite alternate", animationDelay: "-5s", pointerEvents: "none", zIndex: 0 }} />
        {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width: p.s, height: p.s, left: p.l, animationDuration: p.d, animationDelay: p.dl }} />)}
      </>

      <Navbar />

      <section style={{ position: "relative", zIndex: 5, padding: "28px 24px", display: "flex", flexDirection: "column", gap: 24, maxWidth: 1000, margin: "0 auto", animation: "slideIn .35s ease" }}>
        
        {view === "list" && (
          <>
            <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", flexWrap: "wrap", gap: 16 }}>
              <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
                <div style={{ width: 44, height: 44, borderRadius: 12, background: "linear-gradient(135deg,#7040ff,#5020e0)", display: "flex", alignItems: "center", justifyContent: "center", boxShadow: "0 4px 16px rgba(90,40,220,.35)" }}>
                  <MessageCircle size={22} color="rgba(255,255,255,.9)" strokeWidth={1.8} />
                </div>
                <div>
                  <h1 style={{ color: "#e0d4ff", fontSize: 26, fontWeight: 800, margin: 0, lineHeight: 1.2 }}>Foro de Discusión</h1>
                  <p style={{ color: "rgba(140,100,255,.6)", fontSize: 13, margin: 0, fontFamily: "'Space Mono',monospace" }}>Comparte conocimientos y resuelve dudas</p>
                </div>
              </div>
              
              <button onClick={() => setView("create")} style={{ display: "flex", alignItems: "center", gap: 8, padding: "12px 20px", background: "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 10, color: "white", fontFamily: "'Space Mono',monospace", fontSize: 12, letterSpacing: "1px", cursor: "pointer", boxShadow: "0 4px 16px rgba(90,40,220,.35)" }}>
                <Plus size={16} />
                Nueva Discusión
              </button>
            </div>

            <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
              <button onClick={() => setSelectedCategory(null)} className={`cat-btn ${!selectedCategory ? "active" : ""}`} style={{ padding: "8px 16px", background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 20, color: !selectedCategory ? "#b8a0ff" : "rgba(140,100,255,.6)", fontSize: 12, fontFamily: "'Space Mono',monospace", cursor: "pointer", transition: "all .2s ease" }}>
                Todos
              </button>
              {CATEGORIES.map(cat => (
                <button key={cat.id} onClick={() => setSelectedCategory(cat.id)} className={`cat-btn ${selectedCategory === cat.id ? "active" : ""}`} style={{ padding: "8px 16px", background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 20, color: selectedCategory === cat.id ? "#b8a0ff" : "rgba(140,100,255,.6)", fontSize: 12, fontFamily: "'Space Mono',monospace", cursor: "pointer", transition: "all .2s ease" }}>
                  {cat.name}
                </button>
              ))}
            </div>

            {error && <div style={{ background: "rgba(200,140,20,.08)", border: "1px solid rgba(200,140,20,.2)", borderRadius: 10, padding: "14px 18px", color: "rgba(240,190,60,.7)", fontFamily: "'Space Mono',monospace", fontSize: 12 }}>{error}</div>}

            {loading ? (
              <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 20, padding: 24, display: "flex", flexDirection: "column", gap: 16 }}>
                {[1, 2, 3, 4].map(i => <div key={i} style={{ height: 80, borderRadius: 14, background: "rgba(100,60,255,.07)", animation: "pulse 1.5s infinite" }} />)}
              </div>
            ) : discussions.length === 0 ? (
              <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.15)", borderRadius: 20, padding: 40, textAlign: "center" }}>
                <MessageSquare size={40} color="rgba(140,100,255,.3)" style={{ marginBottom: 12 }} />
                <p style={{ color: "rgba(140,100,255,.6)", margin: 0, fontSize: 14 }}>No hay discusiones en esta categoría. ¡Sé el primero en iniciar una!</p>
              </div>
            ) : (
              <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                {discussions.map((disc, idx) => {
                  const catInfo = getCategoryInfo(disc.category);
                  return (
                    <div key={disc.id} onClick={() => openDiscussion(disc)} className="disc-card" style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 16, padding: 20, cursor: "pointer", animation: `slideIn .3s ${idx * 0.05}s ease both` }}>
                      <div style={{ display: "flex", alignItems: "flex-start", gap: 14 }}>
                        <div style={{ width: 44, height: 44, borderRadius: 12, background: "linear-gradient(135deg,rgba(100,60,255,.3),rgba(60,30,150,.3))", border: "1px solid rgba(100,60,255,.3)", display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0 }}>
                          <User size={22} color="rgba(180,140,255,.8)" />
                        </div>
                        <div style={{ flex: 1, minWidth: 0 }}>
                          <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 6, flexWrap: "wrap" }}>
                            <span style={{ background: `${catInfo.color}22`, border: `1px solid ${catInfo.color}44`, borderRadius: 6, padding: "3px 10px", color: catInfo.color, fontSize: 11, fontFamily: "'Space Mono',monospace" }}>
                              {catInfo.name}
                            </span>
                            <span style={{ color: "#e0d4ff", fontWeight: 700, fontSize: 16 }}>{disc.title}</span>
                          </div>
                          <p style={{ color: "rgba(200,190,220,.75)", fontSize: 13, margin: "0 0 10px", lineHeight: 1.5, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>{disc.content}</p>
                          <div style={{ display: "flex", alignItems: "center", gap: 16, flexWrap: "wrap" }}>
                            <span style={{ color: "rgba(140,100,255,.6)", fontSize: 12 }}>{disc.authorUsername}</span>
                            <span style={{ color: "rgba(140,100,255,.5)", fontSize: 11, fontFamily: "'Space Mono',monospace", display: "flex", alignItems: "center", gap: 4 }}><Calendar size={12} /> {formatDate(disc.createdAt)}</span>
                            <span style={{ color: "rgba(140,100,255,.5)", fontSize: 12, display: "flex", alignItems: "center", gap: 4 }}><MessageCircle size={14} /> {disc.repliesCount} respuestas</span>
                            <span style={{ color: "rgba(140,100,255,.5)", fontSize: 12, display: "flex", alignItems: "center", gap: 4 }}><ThumbsUp size={14} /> {disc.likesCount}</span>
                            {disc.tags && disc.tags.length > 0 && (
                              <div style={{ display: "flex", gap: 6 }}>
                                {disc.tags.slice(0, 3).map(tag => (
                                  <span key={tag} className="tag-pill" style={{ background: "rgba(100,60,255,.1)", borderRadius: 12, padding: "2px 8px", color: "rgba(140,100,255,.6)", fontSize: 10, fontFamily: "'Space Mono',monospace" }}>#{tag}</span>
                                ))}
                              </div>
                            )}
                          </div>
                        </div>
                      </div>
                    </div>
                  );
                })}
              </div>
            )}
          </>
        )}

        {view === "create" && (
          <>
            <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 8 }}>
              <button onClick={() => setView("list")} style={{ background: "none", border: "none", color: "rgba(140,100,255,.7)", cursor: "pointer", padding: 8, display: "flex" }}>
                <ArrowLeft size={24} />
              </button>
              <h1 style={{ color: "#e0d4ff", fontSize: 22, fontWeight: 700, margin: 0 }}>Nueva Discusión</h1>
            </div>

            <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 20, padding: 24, display: "flex", flexDirection: "column", gap: 20 }}>
              <div>
                <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 8 }}>Título</label>
                <input type="text" value={newTitle} onChange={e => setNewTitle(e.target.value)} placeholder="¿Sobre qué quieres discutir?" style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 12, padding: 14, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 14, outline: "none" }} />
              </div>

              <div>
                <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 8 }}>Categoría</label>
                <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                  {CATEGORIES.map(cat => (
                    <button key={cat.id} onClick={() => setNewCategory(cat.id)} style={{ padding: "8px 14px", background: newCategory === cat.id ? "rgba(100,60,255,.2)" : "rgba(255,255,255,.03)", border: `1px solid ${newCategory === cat.id ? "rgba(140,80,255,.5)" : "rgba(100,60,255,.2)"}`, borderRadius: 8, color: newCategory === cat.id ? "#b8a0ff" : "rgba(140,100,255,.6)", fontSize: 12, cursor: "pointer" }}>
                      {cat.name}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 8 }}>Contenido</label>
                <textarea value={newContent} onChange={e => setNewContent(e.target.value)} placeholder="Comparte tu pregunta, idea o pensamiento..." rows={6} style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 12, padding: 14, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 14, resize: "vertical", outline: "none" }} />
              </div>

              <div>
                <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 8 }}>Etiquetas (separadas por coma)</label>
                <input type="text" value={newTags} onChange={e => setNewTags(e.target.value)} placeholder="react, hooks, tutorial" style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 12, padding: 14, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 14, outline: "none" }} />
              </div>

              {submitError && <div style={{ background: "rgba(200,60,60,.1)", border: "1px solid rgba(200,60,60,.2)", borderRadius: 8, padding: "12px 16px", color: "rgba(240,100,100,.8)", fontSize: 13 }}>{submitError}</div>}

              <div style={{ display: "flex", justifyContent: "flex-end", gap: 12 }}>
                <button onClick={() => setView("list")} style={{ padding: "12px 24px", background: "transparent", border: "1px solid rgba(100,60,255,.3)", borderRadius: 10, color: "rgba(140,100,255,.7)", fontSize: 12, cursor: "pointer" }}>Cancelar</button>
                <button onClick={createDiscussion} disabled={submitting} style={{ display: "flex", alignItems: "center", gap: 8, padding: "12px 24px", background: submitting ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 10, color: "white", fontFamily: "'Space Mono',monospace", fontSize: 12, letterSpacing: "1px", cursor: submitting ? "not-allowed" : "pointer", boxShadow: submitting ? "none" : "0 4px 16px rgba(90,40,220,.35)" }}>
                  {submitting ? "Publicando..." : <>Publicar <Send size={14} /></>}
                </button>
              </div>
            </div>
          </>
        )}

        {view === "detail" && selectedDiscussion && (
          <>
            <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 8 }}>
              <button onClick={() => setView("list")} style={{ background: "none", border: "none", color: "rgba(140,100,255,.7)", cursor: "pointer", padding: 8, display: "flex" }}>
                <ArrowLeft size={24} />
              </button>
              <span style={{ color: "rgba(140,100,255,.5)", fontSize: 12, fontFamily: "'Space Mono',monospace" }}>Volver al foro</span>
            </div>

            <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 20, padding: 24 }}>
              <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 12 }}>
                {(() => { const c = getCategoryInfo(selectedDiscussion.category); return <span style={{ background: `${c.color}22`, border: `1px solid ${c.color}44`, borderRadius: 6, padding: "3px 10px", color: c.color, fontSize: 11, fontFamily: "'Space Mono',monospace" }}>{c.name}</span>; })()}
                {selectedDiscussion.tags?.map(tag => <span key={tag} style={{ background: "rgba(100,60,255,.1)", borderRadius: 12, padding: "2px 8px", color: "rgba(140,100,255,.6)", fontSize: 10, fontFamily: "'Space Mono',monospace" }}>#{tag}</span>)}
              </div>
              
              <h1 style={{ color: "#e0d4ff", fontSize: 24, fontWeight: 800, margin: "0 0 16px", lineHeight: 1.3 }}>{selectedDiscussion.title}</h1>
              
              <div style={{ display: "flex", alignItems: "center", gap: 16, marginBottom: 20, paddingBottom: 16, borderBottom: "1px solid rgba(100,60,255,.15)" }}>
                <div style={{ width: 36, height: 36, borderRadius: 10, background: "linear-gradient(135deg,rgba(100,60,255,.3),rgba(60,30,150,.3))", border: "1px solid rgba(100,60,255,.3)", display: "flex", alignItems: "center", justifyContent: "center" }}>
                  <User size={18} color="rgba(180,140,255,.8)" />
                </div>
                <div>
                  <span style={{ color: "#e0d4ff", fontWeight: 600, fontSize: 14 }}>{selectedDiscussion.authorUsername}</span>
                  <span style={{ color: "rgba(140,100,255,.5)", fontSize: 11, fontFamily: "'Space Mono',monospace", marginLeft: 12 }}>{formatDate(selectedDiscussion.createdAt)}</span>
                </div>
              </div>

              <p style={{ color: "rgba(200,190,220,.9)", fontSize: 15, lineHeight: 1.7, margin: 0 }}>{selectedDiscussion.content}</p>
            </div>

            <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
              <h2 style={{ color: "#e0d4ff", fontSize: 18, fontWeight: 700, margin: 0 }}>Respuestas</h2>
              <span style={{ background: "rgba(100,60,255,.15)", border: "1px solid rgba(100,60,255,.25)", borderRadius: 20, padding: "4px 12px", color: "rgba(140,100,255,.8)", fontSize: 12, fontFamily: "'Space Mono',monospace" }}>{replies.length}</span>
            </div>

            <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 16, padding: 20 }}>
              <textarea value={replyContent} onChange={e => setReplyContent(e.target.value)} placeholder="Escribe tu respuesta..." rows={3} style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 12, padding: 14, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 14, resize: "none", outline: "none" }} />
              
              {replyError && <div style={{ color: "rgba(240,100,100,.8)", fontSize: 12, marginTop: 8 }}>{replyError}</div>}
              
              <div style={{ display: "flex", justifyContent: "flex-end", marginTop: 12 }}>
                <button onClick={submitReply} disabled={replySubmitting || !replyContent.trim()} style={{ display: "flex", alignItems: "center", gap: 8, padding: "10px 20px", background: replySubmitting ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 8, color: "white", fontSize: 12, cursor: replySubmitting ? "not-allowed" : "pointer" }}>
                  {replySubmitting ? "Enviando..." : <>Responder <Send size={14} /></>}
                </button>
              </div>
            </div>

            {repliesLoading ? (
              <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                {[1, 2].map(i => <div key={i} style={{ height: 60, borderRadius: 12, background: "rgba(100,60,255,.07)", animation: "pulse 1.5s infinite" }} />)}
              </div>
            ) : replies.length === 0 ? (
              <div style={{ textAlign: "center", padding: 30, color: "rgba(140,100,255,.5)" }}>
                <MessageSquare size={32} style={{ marginBottom: 8, opacity: 0.5 }} />
                <p style={{ margin: 0, fontSize: 14 }}>No hay respuestas aún. ¡Sé el primero en responder!</p>
              </div>
            ) : (
              <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
                {replies.map((reply, idx) => (
                  <div key={reply.id} style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.15)", borderRadius: 14, padding: 16, animation: `slideIn .3s ${idx * 0.05}s ease both` }}>
                    <div style={{ display: "flex", alignItems: "center", gap: 10, marginBottom: 10 }}>
                      <div style={{ width: 32, height: 32, borderRadius: 8, background: "rgba(100,60,255,.15)", display: "flex", alignItems: "center", justifyContent: "center" }}>
                        <User size={16} color="rgba(180,140,255,.7)" />
                      </div>
                      <span style={{ color: "#b8a0ff", fontWeight: 600, fontSize: 13 }}>{reply.authorUsername}</span>
                      <span style={{ color: "rgba(140,100,255,.5)", fontSize: 10, fontFamily: "'Space Mono',monospace" }}>{formatDate(reply.createdAt)}</span>
                    </div>
                    <p style={{ color: "rgba(200,190,220,.85)", fontSize: 14, margin: 0, lineHeight: 1.6 }}>{reply.content}</p>
                  </div>
                ))}
              </div>
            )}
          </>
        )}
      </section>
    </main>
  );
}