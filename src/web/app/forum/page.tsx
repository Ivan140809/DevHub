"use client";
import Navbar from "../components/Navbar";
import { MessageCircle, User, Calendar, ArrowLeft, Send, MessageSquare, Sparkles, Zap, Heart, Trash2 } from "lucide-react";
import { useCurrentUser, UserRole } from "../hooks/useCurrentUser";
import { useState, useEffect, useCallback } from "react";

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

type Reaction = {
  emoji: string;
  count: number;
  userReacted: boolean;
};

type Reply = {
  id: string;
  content: string;
  authorId: string;
  authorUsername: string;
  createdAt: string;
  likesCount: number;
  happyFace?: number;
  sadFace?: number;
  reactions?: Reaction[];
  replies?: Reply[];
};

type BackendComment = {
  id: string;
  title: string;
  content: string;
  category: string;
  tags: string[];
  username: string;
  createdAt?: string;
  replies?: BackendComment[];
  happyFace: number;
  sadFace: number;
};

type ReactionChoice = "HAPPYFACE" | "SADFACE";

const EMOJI_REACTIONS: Array<{ emoji: string; label: string; type: ReactionChoice }> = [
  { emoji: "😊", label: "Feliz", type: "HAPPYFACE" },
  { emoji: "😢", label: "Triste", type: "SADFACE" },
];

const mapCommentToDiscussion = (comment: BackendComment): Discussion => ({
  id: comment.id,
  title: comment.title || (comment.content.length > 70 ? `${comment.content.slice(0, 70).trim()}...` : comment.content),
  content: comment.content,
  category: comment.category || "general",
  authorId: "",
  authorUsername: comment.username,
  createdAt: comment.createdAt || new Date().toISOString(),
  repliesCount: comment.replies?.length ?? 0,
  likesCount: comment.happyFace + comment.sadFace,
  tags: comment.tags || [],
});

const mapCommentToReply = (comment: BackendComment): Reply => ({
  id: comment.id,
  content: comment.content,
  authorId: "",
  authorUsername: comment.username,
  createdAt: comment.createdAt || new Date().toISOString(),
  likesCount: comment.happyFace + comment.sadFace,
  happyFace: comment.happyFace,
  sadFace: comment.sadFace,
  replies: comment.replies?.map(mapCommentToReply),
});

type ViewMode = "list" | "create" | "detail";

type Category = {
  id: string;
  name: string;
  color: string;
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

const CATEGORIES: Category[] = [
  { id: "general", name: "General", color: "rgba(100,60,255,.6)" },
  { id: "javascript", name: "JavaScript", color: "rgba(247,223,30,.7)" },
  { id: "java", name: "Java", color: "rgba(176,114,25,.7)" },
  { id: "python", name: "Python", color: "rgba(55,118,171,.7)" },
  { id: "career", name: "Carrera", color: "rgba(80,200,150,.7)" },
  { id: "help", name: "Ayuda", color: "rgba(200,100,50,.7)" },
  { id: "projects", name: "Proyectos", color: "rgba(150,50,180,.7)" },
];

const REACTION_OPTIONS = [
  { emoji: "😊", label: "Feliz" },
  { emoji: "😢", label: "Triste" },
];

const BASE_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

const GLOBAL_STYLES = `
  @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=Space+Mono:wght@400;700&display=swap');
  @keyframes orbFloat { 0%{transform:translate(0,0)} 100%{transform:translate(20px,20px)} }
  @keyframes floatUp { 0%{transform:translateY(0);opacity:.7} 100%{transform:translateY(-100vh);opacity:0} }
  @keyframes slideIn { 0%{opacity:0;transform:translateY(28px)} 100%{opacity:1;transform:translateY(0)} }
  @keyframes popIn { 0%{opacity:0;transform:scale(.6) translateY(8px)} 100%{opacity:1;transform:scale(1) translateY(0)} }
  @keyframes pulse { 0%,100%{opacity:.3} 50%{opacity:.7} }
  @keyframes shimmer { 0%{background-position:-200% 0} 100%{background-position:200% 0} }
  @keyframes bounce { 0%,100%{transform:translateY(0)} 50%{transform:translateY(-4px)} }
  @keyframes glow { 0%,100%{box-shadow:0 0 5px rgba(112,64,255,.3)} 50%{box-shadow:0 0 20px rgba(112,64,255,.6)} }
  @keyframes heartBeat { 0%{transform:scale(1)} 25%{transform:scale(1.3)} 50%{transform:scale(1)} 75%{transform:scale(1.3)} 100%{transform:scale(1)} }
  @keyframes reactionPop { 0%{transform:scale(1)} 50%{transform:scale(1.4)} 100%{transform:scale(1)} }
  .dh-particle { position:fixed; border-radius:50%; background:rgba(160,100,255,.7); bottom:-10px; animation:floatUp linear infinite; pointer-events:none; z-index:0; }
  .disc-card { transition: all .25s cubic-bezier(.16,1,.3,1); }
  .disc-card:hover { transform: translateY(-4px); border-color: rgba(140,80,255,.5) !important; box-shadow: 0 12px 40px rgba(90,40,220,.25) !important; }
  .cat-btn.active { background: rgba(100,60,255,.25) !important; border-color: rgba(140,80,255,.6) !important; color: #b8a0ff !important; }
  .cat-btn:hover:not(.active) { border-color: rgba(140,80,255,.4) !important; color: #b8a0ff !important; }
  .new-disc-btn:hover { transform: translateY(-3px) scale(1.02) !important; box-shadow: 0 8px 30px rgba(90,40,220,.5) !important; }
  .tag-pill { transition: all .2s ease; }
  .tag-pill:hover { background: rgba(100,60,255,.25) !important; transform: scale(1.05); }
  .like-btn { transition: all .2s ease; }
  .like-btn:hover { transform: scale(1.15); }
  .like-btn.liked { animation: heartBeat 0.6s ease; }
  .reply-btn { transition: all .25s cubic-bezier(.34,1.56,.64,1); }
  .reply-btn:hover { transform: translateX(4px) scale(1.02); }
  .submit-reply { transition: all .25s cubic-bezier(.16,1,.3,1); }
  .submit-reply:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(90,40,220,.5) !important; }
  .submit-reply.sending { animation: pulse 1s infinite; }
  .input-glow:focus { border-color: rgba(140,80,255,.5) !important; box-shadow: 0 0 0 3px rgba(100,60,255,.15) !important; }
  .reply-textarea:focus { border-color: rgba(140,80,255,.5) !important; box-shadow: 0 0 0 3px rgba(100,60,255,.15) !important; }
  .back-btn:hover { background: rgba(100,60,255,.15) !important; transform: translateX(-4px); }
  .category-select.selected { background: rgba(100,60,255,.2) !important; border-color: rgba(140,80,255,.5) !important; }
  .reaction-btn { transition: all .15s ease; }
  .reaction-btn:hover { transform: scale(1.25) translateY(-4px); }
  .reaction-btn.reacted { animation: reactionPop .3s ease; }
  .reaction-picker { animation: popIn .2s cubic-bezier(.34,1.56,.64,1) both; }
`;

const getCategoryInfo = (catId: string): Category =>
  CATEGORIES.find(c => c.id === catId) || CATEGORIES[0];

const formatDate = (dateStr: string): string => {
  const date = dateStr ? new Date(dateStr) : new Date();
  if (Number.isNaN(date.getTime())) {
    return new Date().toLocaleDateString("es-CO", {
      day: "numeric", month: "short", hour: "2-digit", minute: "2-digit",
    });
  }

  return date.toLocaleDateString("es-CO", {
    day: "numeric", month: "short", hour: "2-digit", minute: "2-digit",
  });
};

const LoadingSkeleton = () => (
  <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 20, padding: 24, display: "flex", flexDirection: "column" as const, gap: 16 }}>
    {[1, 2, 3, 4].map(i => <div key={i} style={{ height: 80, borderRadius: 14, background: "rgba(100,60,255,.07)", animation: "pulse 1.5s infinite" }} />)}
  </div>
);

const EmptyState = ({ message, subMessage }: { message: string; subMessage: string }) => (
  <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.15)", borderRadius: 20, padding: 50, textAlign: "center" as const }}>
    <div style={{ width: 64, height: 64, borderRadius: 16, background: "rgba(100,60,255,.1)", display: "flex", alignItems: "center", justifyContent: "center", margin: "0 auto 16px" }}>
      <MessageSquare size={32} color="rgba(140,100,255,.4)" />
    </div>
    <p style={{ color: "rgba(140,100,255,.6)", margin: 0, fontSize: 15 }}>{message}</p>
    <p style={{ color: "rgba(140,100,255,.4)", margin: "8px 0 0", fontSize: 13 }}>{subMessage}</p>
  </div>
);

const ErrorBanner = ({ message }: { message: string }) => (
  <div style={{ background: "rgba(200,140,20,.08)", border: "1px solid rgba(200,140,20,.2)", borderRadius: 10, padding: "14px 18px", color: "rgba(240,190,60,.7)", fontFamily: "'Space Mono',monospace", fontSize: 12 }}>
    {message}
  </div>
);

const BackgroundEffects = () => (
  <>
    <div style={{ position: "fixed" as const, borderRadius: "50%", width: 500, height: 500, background: "radial-gradient(circle,rgba(90,30,200,.22) 0%,transparent 70%)", top: -150, left: -150, animation: "orbFloat 10s ease-in-out infinite alternate", pointerEvents: "none" as const, zIndex: 0 }} />
    <div style={{ position: "fixed" as const, borderRadius: "50%", width: 400, height: 400, background: "radial-gradient(circle,rgba(110,50,255,.18) 0%,transparent 70%)", bottom: -100, right: -100, animation: "orbFloat 10s ease-in-out infinite alternate", animationDelay: "-5s", pointerEvents: "none" as const, zIndex: 0 }} />
    {PARTICLES.map((p, i) => <div key={i} className="dh-particle" style={{ width: p.s, height: p.s, left: p.l, animationDuration: p.d, animationDelay: p.dl }} />)}
  </>
);


function ReactionBar({ replyId, initialHappyFace, initialSadFace, onReactionUpdate }: { replyId: string; initialHappyFace: number; initialSadFace: number; onReactionUpdate: (happyFace: number, sadFace: number) => void }) {
  const [counts, setCounts] = useState<{ HAPPYFACE: number; SADFACE: number }>({ HAPPYFACE: initialHappyFace, SADFACE: initialSadFace });
  const [sending, setSending] = useState(false);

  useEffect(() => {
    setCounts({ HAPPYFACE: initialHappyFace, SADFACE: initialSadFace });
  }, [initialHappyFace, initialSadFace]);

  async function toggleReaction(type: ReactionChoice) {
    if (sending) return;
    const token = localStorage.getItem("token");
    if (!token) {
      alert("Debes iniciar sesión para reaccionar.");
      return;
    }

    setSending(true);
    try {
      const res = await fetch(`${BASE_URL}/comments/${replyId}/reactions?reaction=${type}`, {
        method: "POST",
        headers: { Authorization: `Bearer ${token}` },
      });
      if (!res.ok) throw new Error(`Error ${res.status}`);
      const updated: BackendComment = await res.json();
      setCounts({ HAPPYFACE: updated.happyFace, SADFACE: updated.sadFace });
      onReactionUpdate(updated.happyFace, updated.sadFace);
    } catch (err) {
      console.error("No se pudo enviar la reacción", err);
      alert("Error al enviar la reacción. Intenta de nuevo.");
    } finally {
      setSending(false);
    }
  }

  return (
    <div style={{ display: "flex", alignItems: "center", gap: 10, flexWrap: "wrap" as const, marginTop: 10, paddingLeft: 48 }}>
      {EMOJI_REACTIONS.map(r => (
        <button
          key={r.emoji}
          onClick={() => toggleReaction(r.type)}
          title={r.label}
          disabled={sending}
          className="reaction-btn"
          style={{
            display: "flex", alignItems: "center", gap: 6,
            padding: "8px 12px",
            background: "rgba(100,60,255,.08)",
            border: "1px solid rgba(100,60,255,.2)",
            borderRadius: 999,
            cursor: sending ? "not-allowed" : "pointer",
            fontSize: 14,
            color: "rgba(230,220,255,.9)",
          }}
        >
          <span>{r.emoji}</span>
          <span style={{ fontFamily: "'Space Mono',monospace", fontSize: 12, color: "rgba(140,100,255,.75)", fontWeight: 700 }}>
            {counts[r.type]}
          </span>
        </button>
      ))}
    </div>
  );
}

interface DiscussionListProps {
  discussions: Discussion[];
  loading: boolean;
  error: string | null;
  selectedCategory: string | null;
  sortByPopular: boolean;
  onCategoryChange: (cat: string | null) => void;
  onSortByPopularChange: (sort: boolean) => void;
  onCreateClick: () => void;
  onDiscussionClick: (disc: Discussion) => void;
  onLikeToggle: (id: string) => void;
  likedDiscussions: Set<string>;
  isAuthenticated: boolean;
}

function DiscussionList({
  discussions,
  loading,
  error,
  selectedCategory,
  sortByPopular,
  onCategoryChange,
  onSortByPopularChange,
  onCreateClick,
  onDiscussionClick,
  onLikeToggle,
  likedDiscussions,
  isAuthenticated,
}: DiscussionListProps) {
  return (
    <>
      <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between", flexWrap: "wrap" as const, gap: 16 }}>
        <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
          <div style={{ width: 48, height: 48, borderRadius: 14, background: "linear-gradient(135deg,#7040ff,#5020e0)", display: "flex", alignItems: "center", justifyContent: "center", boxShadow: "0 6px 24px rgba(90,40,220,.4)", animation: "glow 3s ease-in-out infinite" }}>
            <MessageCircle size={24} color="rgba(255,255,255,.95)" strokeWidth={1.8} />
          </div>
          <div>
            <h1 style={{ color: "#e0d4ff", fontSize: 28, fontWeight: 800, margin: 0, lineHeight: 1.2 }}>Foro de Discusión</h1>
            <p style={{ color: "rgba(140,100,255,.6)", fontSize: 13, margin: 0, fontFamily: "'Space Mono',monospace", display: "flex", alignItems: "center", gap: 6 }}>
              <Sparkles size={12} /> Comparte conocimientos y resuelve dudas
            </p>
          </div>
        </div>
        <button onClick={onCreateClick} className="new-disc-btn" style={{ display: "flex", alignItems: "center", gap: 10, padding: "14px 24px", background: "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 12, color: "white", fontFamily: "'Space Mono',monospace", fontSize: 12, letterSpacing: "1px", cursor: "pointer", boxShadow: "0 6px 20px rgba(90,40,220,.4)", fontWeight: 600 }}>
          <Zap size={16} /> Nueva Discusión
        </button>
      </div>

      <div style={{ display: "flex", gap: 10, flexWrap: "wrap" as const }}>
        <button onClick={() => onCategoryChange(null)} className={`cat-btn ${!selectedCategory ? "active" : ""}`} style={{ padding: "10px 18px", background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 22, color: !selectedCategory ? "#b8a0ff" : "rgba(140,100,255,.6)", fontSize: 12, fontFamily: "'Space Mono',monospace", cursor: "pointer", transition: "all .2s ease", fontWeight: 600 }}>
          Todos
        </button>
        {CATEGORIES.map(cat => (
          <button key={cat.id} onClick={() => onCategoryChange(cat.id)} className={`cat-btn ${selectedCategory === cat.id ? "active" : ""}`} style={{ padding: "10px 18px", background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 22, color: selectedCategory === cat.id ? "#b8a0ff" : "rgba(140,100,255,.6)", fontSize: 12, fontFamily: "'Space Mono',monospace", cursor: "pointer", transition: "all .2s ease", fontWeight: 600 }}>
            {cat.name}
          </button>
        ))}
        <button onClick={() => onSortByPopularChange(!sortByPopular)} className={`cat-btn ${sortByPopular ? "active" : ""}`} style={{ padding: "10px 18px", background: sortByPopular ? "rgba(255,100,150,.15)" : "rgba(14,10,28,.88)", border: "1px solid rgba(255,100,150,.3)", borderRadius: 22, color: sortByPopular ? "rgba(255,150,180,.9)" : "rgba(140,100,255,.6)", fontSize: 12, fontFamily: "'Space Mono',monospace", cursor: "pointer", transition: "all .2s ease", fontWeight: 600, display: "flex", alignItems: "center", gap: 6 }}>
          <Heart size={14} fill={sortByPopular ? "rgba(255,100,150,.6)" : "none"} /> Más Populares
        </button>
      </div>

      {error && <ErrorBanner message={error} />}
      {loading && <LoadingSkeleton />}
      {!loading && discussions.length === 0 && <EmptyState message="No hay discusiones en esta categoría." subMessage="¡Sé el primero en iniciar una!" />}

      {!loading && discussions.length > 0 && (
        <div style={{ display: "flex", flexDirection: "column" as const, gap: 14 }}>
          {(sortByPopular ? [...discussions].sort((a, b) => (b.likesCount + b.repliesCount) - (a.likesCount + a.repliesCount)) : discussions).map((disc, idx) => {
            const catInfo = getCategoryInfo(disc.category);
            const isLiked = likedDiscussions.has(disc.id);
            return (
              <div key={disc.id} onClick={() => onDiscussionClick(disc)} className="disc-card" style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 18, padding: 22, cursor: "pointer", animation: `slideIn .4s ${idx * 0.06}s ease both` }}>
                <div style={{ display: "flex", alignItems: "flex-start", gap: 16 }}>
                  <div style={{ width: 48, height: 48, borderRadius: 14, background: "linear-gradient(135deg,rgba(100,60,255,.35),rgba(60,30,150,.35))", border: "1px solid rgba(100,60,255,.4)", display: "flex", alignItems: "center", justifyContent: "center", flexShrink: 0 }}>
                    <User size={24} color="rgba(180,140,255,.9)" />
                  </div>
                  <div style={{ flex: 1, minWidth: 0 }}>
                    <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 8, flexWrap: "wrap" as const }}>
                      <span style={{ background: `${catInfo.color}22`, border: `1px solid ${catInfo.color}44`, borderRadius: 8, padding: "4px 12px", color: catInfo.color, fontSize: 11, fontFamily: "'Space Mono',monospace", fontWeight: 600 }}>{catInfo.name}</span>
                      <span style={{ color: "#e0d4ff", fontWeight: 700, fontSize: 17 }}>{disc.title}</span>
                    </div>
                    <p style={{ color: "rgba(200,190,220,.75)", fontSize: 14, margin: "0 0 14px", lineHeight: 1.5, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" as const }}>{disc.content}</p>
                    <div style={{ display: "flex", alignItems: "center", gap: 18, flexWrap: "wrap" as const }}>
                      <span style={{ color: "rgba(140,100,255,.7)", fontSize: 13, fontWeight: 600 }}>{disc.authorUsername}</span>
                      <span style={{ color: "rgba(140,100,255,.5)", fontSize: 11, fontFamily: "'Space Mono',monospace", display: "flex", alignItems: "center", gap: 5 }}><Calendar size={13} /> {formatDate(disc.createdAt)}</span>
                      <span style={{ color: "rgba(140,100,255,.5)", fontSize: 13, display: "flex", alignItems: "center", gap: 5 }}><MessageCircle size={15} /> {disc.repliesCount}</span>
                      <button className={`like-btn ${isLiked ? "liked" : ""}`} onClick={(e) => { e.stopPropagation(); onLikeToggle(disc.id); }} style={{ background: "none", border: "none", display: "flex", alignItems: "center", gap: 5, cursor: "pointer", padding: "4px 8px", borderRadius: 8 }}>
                        <Heart size={15} fill={isLiked ? "rgba(255,100,150,.6)" : "transparent"} color={isLiked ? "rgba(255,100,150,.8)" : "rgba(140,100,255,.5)"} />
                        <span style={{ color: isLiked ? "rgba(255,100,150,.8)" : "rgba(140,100,255,.5)", fontSize: 13 }}>{isLiked ? disc.likesCount + 1 : disc.likesCount}</span>
                      </button>
                      {disc.tags && disc.tags.length > 0 && (
                        <div style={{ display: "flex", gap: 8 }}>
                          {disc.tags.slice(0, 3).map(tag => (
                            <span key={tag} className="tag-pill" style={{ background: "rgba(100,60,255,.12)", borderRadius: 14, padding: "3px 10px", color: "rgba(140,100,255,.7)", fontSize: 11, fontFamily: "'Space Mono',monospace" }}>#{tag}</span>
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
  );
}

interface CreateDiscussionProps {
  title: string; content: string; category: string; tags: string;
  error: string | null; submitting: boolean;
  onTitleChange: (v: string) => void; onContentChange: (v: string) => void;
  onCategoryChange: (v: string) => void; onTagsChange: (v: string) => void;
  onSubmit: () => void; onCancel: () => void;
}

function CreateDiscussion({ title, content, category, tags, error, submitting, onTitleChange, onContentChange, onCategoryChange, onTagsChange, onSubmit, onCancel }: CreateDiscussionProps) {
  return (
    <>
      <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 8 }}>
        <button onClick={onCancel} className="back-btn" style={{ background: "rgba(100,60,255,.08)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 10, color: "rgba(140,100,255,.7)", cursor: "pointer", padding: 10, display: "flex", transition: "all .2s ease" }}><ArrowLeft size={22} /></button>
        <h1 style={{ color: "#e0d4ff", fontSize: 24, fontWeight: 700, margin: 0 }}>Nueva Discusión</h1>
      </div>
      <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 22, padding: 28, display: "flex", flexDirection: "column" as const, gap: 22 }}>
        <div>
          <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 10, fontWeight: 600 }}>Título de tu discusión</label>
          <input type="text" value={title} onChange={e => onTitleChange(e.target.value)} placeholder="¿Sobre qué quieres discutir?" className="input-glow" style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 14, padding: 16, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 15, outline: "none", transition: "all .2s ease" }} />
        </div>
        <div>
          <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 10, fontWeight: 600 }}>Categoría</label>
          <div style={{ display: "flex", gap: 10, flexWrap: "wrap" as const }}>
            {CATEGORIES.map(cat => (
              <button key={cat.id} onClick={() => onCategoryChange(cat.id)} className={`category-select ${category === cat.id ? "selected" : ""}`} style={{ padding: "10px 16px", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 10, color: category === cat.id ? "#b8a0ff" : "rgba(140,100,255,.6)", fontSize: 12, cursor: "pointer", transition: "all .2s ease", fontWeight: 600 }}>{cat.name}</button>
            ))}
          </div>
        </div>
        <div>
          <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 10, fontWeight: 600 }}>Contenido</label>
          <textarea value={content} onChange={e => onContentChange(e.target.value)} placeholder="Comparte tu pregunta, idea o pensamiento..." rows={7} className="input-glow" style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 14, padding: 16, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 15, resize: "vertical" as const, outline: "none", transition: "all .2s ease" }} />
        </div>
        <div>
          <label style={{ display: "block", color: "rgba(140,100,255,.7)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginBottom: 10, fontWeight: 600 }}>Etiquetas (separadas por coma)</label>
          <input type="text" value={tags} onChange={e => onTagsChange(e.target.value)} placeholder="react, hooks, tutorial" className="input-glow" style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 14, padding: 16, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 15, outline: "none", transition: "all .2s ease" }} />
        </div>
        {error && <ErrorBanner message={error} />}
        <div style={{ display: "flex", justifyContent: "flex-end", gap: 14, marginTop: 8 }}>
          <button onClick={onCancel} style={{ padding: "14px 28px", background: "transparent", border: "1px solid rgba(100,60,255,.3)", borderRadius: 12, color: "rgba(140,100,255,.7)", fontSize: 13, cursor: "pointer", fontWeight: 600, transition: "all .2s ease" }}>Cancelar</button>
          <button onClick={onSubmit} disabled={submitting} className="new-disc-btn" style={{ display: "flex", alignItems: "center", gap: 10, padding: "14px 28px", background: submitting ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 12, color: "white", fontFamily: "'Space Mono',monospace", fontSize: 13, letterSpacing: "1px", cursor: submitting ? "not-allowed" : "pointer", boxShadow: submitting ? "none" : "0 6px 20px rgba(90,40,220,.4)", fontWeight: 600, transition: "all .2s ease" }}>
            {submitting ? "Publicando " : (<><Send size={16} /> Publicar</>)}
          </button>
        </div>
      </div>
    </>
  );
}

interface DiscussionDetailProps {
  discussion: Discussion;
  replies: Reply[];
  loading: boolean;
  replyContent: string;
  replyError: string | null;
  replyFocused: boolean;
  replySubmitting: boolean;
  onReplyContentChange: (v: string) => void;
  onReplyFocus: () => void;
  onReplyBlur: () => void;
  onReplySubmit: () => void;
  onBack: () => void;
  currentUserRole: UserRole;
  onDeleteReply: (replyId: string) => void;
  onReplyReactionUpdate: (replyId: string, happyFace: number, sadFace: number) => void;
}

function DiscussionDetail({
  discussion,
  replies,
  loading,
  replyContent,
  replyError,
  replyFocused,
  replySubmitting,
  onReplyContentChange,
  onReplyFocus,
  onReplyBlur,
  onReplySubmit,
  onBack,
  currentUserRole,
  onDeleteReply,
  onReplyReactionUpdate,
}: DiscussionDetailProps) {
  const catInfo = getCategoryInfo(discussion.category);
  const [sortByReactions, setSortByReactions] = useState(false);
  const canReply = true;
  const canDeleteReply = false;

  // Helper para sumar las reacciones de una respuesta
  const getReactionCount = (reply: Reply) => {
    let count = reply.likesCount || 0;
    if (reply.reactions) {
      count += reply.reactions.reduce((sum, r) => sum + r.count, 0);
    }
    return count;
  };

  const sortedReplies = sortByReactions
    ? [...replies].sort((a, b) => getReactionCount(b) - getReactionCount(a))
    : replies;

  return (
    <>
      <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 8 }}>
        <button onClick={onBack} className="back-btn" style={{ background: "rgba(100,60,255,.08)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 10, color: "rgba(140,100,255,.7)", cursor: "pointer", padding: 10, display: "flex", transition: "all .2s ease" }}><ArrowLeft size={22} /></button>
        <span style={{ color: "rgba(140,100,255,.5)", fontSize: 12, fontFamily: "'Space Mono',monospace" }}>Volver al foro</span>
      </div>

      <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 22, padding: 28 }}>
        <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 16, flexWrap: "wrap" as const }}>
          <span style={{ background: `${catInfo.color}22`, border: `1px solid ${catInfo.color}44`, borderRadius: 8, padding: "4px 12px", color: catInfo.color, fontSize: 11, fontFamily: "'Space Mono',monospace", fontWeight: 600 }}>{catInfo.name}</span>
          {discussion.tags?.map(tag => (<span key={tag} style={{ background: "rgba(100,60,255,.12)", borderRadius: 14, padding: "3px 10px", color: "rgba(140,100,255,.7)", fontSize: 11, fontFamily: "'Space Mono',monospace" }}>#{tag}</span>))}
        </div>
        <h1 style={{ color: "#e0d4ff", fontSize: 26, fontWeight: 800, margin: "0 0 20px", lineHeight: 1.3 }}>{discussion.title}</h1>
        <div style={{ display: "flex", alignItems: "center", gap: 16, marginBottom: 24, paddingBottom: 20, borderBottom: "1px solid rgba(100,60,255,.15)" }}>
          <div style={{ width: 44, height: 44, borderRadius: 12, background: "linear-gradient(135deg,rgba(100,60,255,.35),rgba(60,30,150,.35))", border: "1px solid rgba(100,60,255,.4)", display: "flex", alignItems: "center", justifyContent: "center" }}><User size={22} color="rgba(180,140,255,.9)" /></div>
          <div>
            <span style={{ color: "#e0d4ff", fontWeight: 700, fontSize: 15 }}>{discussion.authorUsername}</span>
            <span style={{ color: "rgba(140,100,255,.5)", fontSize: 12, fontFamily: "'Space Mono',monospace", marginLeft: 16 }}>{formatDate(discussion.createdAt)}</span>
          </div>
        </div>
        <p style={{ color: "rgba(200,190,220,.9)", fontSize: 16, lineHeight: 1.8, margin: 0 }}>{discussion.content}</p>
      </div>

      <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
        <div style={{ display: "flex", alignItems: "center", gap: 16 }}>
          <h2 style={{ color: "#e0d4ff", fontSize: 20, fontWeight: 700, margin: 0, display: "flex", alignItems: "center", gap: 8 }}><MessageCircle size={20} /> Respuestas</h2>
          <span style={{ background: "rgba(100,60,255,.18)", border: "1px solid rgba(100,60,255,.3)", borderRadius: 20, padding: "5px 14px", color: "rgba(140,100,255,.85)", fontSize: 12, fontFamily: "'Space Mono',monospace", fontWeight: 600 }}>{replies.length}</span>
        </div>
        <button
          onClick={() => setSortByReactions(!sortByReactions)}
          className={`cat-btn ${sortByReactions ? "active" : ""}`}
          style={{
            padding: "8px 16px",
            background: sortByReactions ? "rgba(255,100,150,.15)" : "rgba(14,10,28,.88)",
            border: sortByReactions ? "1px solid rgba(255,100,150,.3)" : "1px solid rgba(100,60,255,.2)",
            borderRadius: 22,
            color: sortByReactions ? "rgba(255,150,180,.9)" : "rgba(140,100,255,.6)",
            fontSize: 12,
            fontFamily: "'Space Mono',monospace",
            cursor: "pointer",
            fontWeight: 600,
            display: "flex",
            alignItems: "center",
            gap: 6,
            transition: "all .2s ease"
          }}
        >
          <Heart size={14} fill={sortByReactions ? "rgba(255,100,150,.6)" : "none"} />
          Más Reaccionados
        </button>
      </div>

      {canReply ? (
        <div style={{ background: replyFocused ? "rgba(14,10,28,.95)" : "rgba(14,10,28,.88)", border: `1px solid ${replyFocused ? "rgba(140,80,255,.5)" : "rgba(100,60,255,.2)"}`, borderRadius: 18, padding: 20, transition: "all .3s ease" }}>
          <textarea value={replyContent} onChange={e => onReplyContentChange(e.target.value)} onFocus={onReplyFocus} onBlur={onReplyBlur} placeholder="Escribe tu respuesta..." rows={4} className="reply-textarea" style={{ width: "100%", background: "rgba(255,255,255,.03)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 14, padding: 16, color: "#e0d4ff", fontFamily: "'Syne',sans-serif", fontSize: 15, resize: "none" as const, outline: "none", transition: "all .2s ease" }} />
          {replyError && <div style={{ color: "rgba(240,100,100,.85)", fontSize: 13, marginTop: 12 }}>⚠️ {replyError}</div>}
          <div style={{ display: "flex", justifyContent: "flex-end", marginTop: 16 }}>
            <button onClick={onReplySubmit} disabled={replySubmitting || !replyContent.trim()} className={`submit-reply ${replySubmitting ? "sending" : ""}`} style={{ display: "flex", alignItems: "center", gap: 10, padding: "14px 24px", background: replySubmitting ? "rgba(100,60,255,.4)" : "linear-gradient(135deg,#7040ff,#5020e0)", border: "none", borderRadius: 12, color: "white", fontSize: 13, fontWeight: 600, cursor: replySubmitting || !replyContent.trim() ? "not-allowed" : "pointer", boxShadow: replySubmitting ? "none" : "0 6px 20px rgba(90,40,220,.4)", transition: "all .2s ease" }}>
              {replySubmitting ? "Enviando..." : (<><Send size={16} /> Responder</>)}
            </button>
          </div>
        </div>
      ) : (
        <div style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.2)", borderRadius: 18, padding: 20, color: "rgba(180,180,255,.85)", fontSize: 14, fontFamily: "'Space Mono',monospace" }}>
          Responder no está disponible con el backend actual.
        </div>
      )}

      {loading ? <LoadingSkeleton /> : sortedReplies.length === 0 ? (
        <EmptyState message="No hay respuestas aún." subMessage="¡Sé el primero en comentar!" />
      ) : (
        <div style={{ display: "flex", flexDirection: "column" as const, gap: 14 }}>
          {sortedReplies.map((reply, idx) => (
            <div key={reply.id} className="reply-btn" style={{ background: "rgba(14,10,28,.88)", border: "1px solid rgba(100,60,255,.15)", borderRadius: 16, padding: 18, animation: `slideIn .35s ${idx * 0.08}s ease both` }}>
              <div style={{ display: "flex", alignItems: "center", gap: 12, marginBottom: 12, justifyContent: "space-between" }}>
                <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
                  <div style={{ width: 36, height: 36, borderRadius: 10, background: "linear-gradient(135deg,rgba(100,60,255,.25),rgba(60,30,150,.25))", border: "1px solid rgba(100,60,255,.3)", display: "flex", alignItems: "center", justifyContent: "center" }}>
                    <User size={18} color="rgba(180,140,255,.8)" />
                  </div>
                  <span style={{ color: "#b8a0ff", fontWeight: 700, fontSize: 14 }}>{reply.authorUsername}</span>
                  <span style={{ color: "rgba(140,100,255,.5)", fontSize: 11, fontFamily: "'Space Mono',monospace" }}>
                    {formatDate(reply.createdAt)}
                  </span>
                </div>
                {currentUserRole === "ADMIN" && canDeleteReply && (
  <div style={{ display: "flex", gap: 8 }}>
    
    
    <button
      onClick={() => {
        // Lógica para editar comentario :P
      }}
      style={{
        background: "rgba(100,60,255,.15)",
        border: "1px solid rgba(100,60,255,.3)",
        borderRadius: 8,
        padding: "6px 12px",
        color: "rgba(180,150,255,.8)",
        cursor: "pointer",
        display: "flex",
        alignItems: "center",
        gap: 6,
        fontSize: 12,
        fontWeight: 600,
        transition: "all .2s ease",
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.background = "rgba(100,60,255,.25)";
        e.currentTarget.style.borderColor = "rgba(100,60,255,.5)";
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.background = "rgba(100,60,255,.15)";
        e.currentTarget.style.borderColor = "rgba(100,60,255,.3)";
      }}
    >
      Editar
    </button>

    
    <button
      onClick={() => onDeleteReply(reply.id)}
      style={{
        background: "rgba(200,100,100,.15)",
        border: "1px solid rgba(200,100,100,.3)",
        borderRadius: 8,
        padding: "6px 12px",
        color: "rgba(240,120,120,.8)",
        cursor: "pointer",
        display: "flex",
        alignItems: "center",
        gap: 6,
        fontSize: 12,
        fontWeight: 600,
        transition: "all .2s ease",
      }}
      onMouseEnter={(e) => {
        e.currentTarget.style.background = "rgba(200,100,100,.25)";
        e.currentTarget.style.borderColor = "rgba(200,100,100,.5)";
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.background = "rgba(200,100,100,.15)";
        e.currentTarget.style.borderColor = "rgba(200,100,100,.3)";
      }}
    >
      <Trash2 size={14} /> Eliminar
    </button>

  </div>
)}
              </div>
              <p style={{ color: "rgba(200,190,220,.9)", fontSize: 15, margin: 0, lineHeight: 1.6, paddingLeft: 48 }}>{reply.content}</p>

              <ReactionBar replyId={reply.id} initialHappyFace={reply.happyFace ?? 0} initialSadFace={reply.sadFace ?? 0} onReactionUpdate={(happyFace, sadFace) => {
                onReplyReactionUpdate(reply.id, happyFace, sadFace);
              }} />
            </div>
          ))}
        </div>
      )}
    </>
  );
}

export default function ForumPage() {
  const currentUser = useCurrentUser();
  const isAuthenticated = !!currentUser.email;

  const [view, setView] = useState<ViewMode>("list");
  const [discussions, setDiscussions] = useState<Discussion[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [sortByPopular, setSortByPopular] = useState(false);
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
  const [replyFocused, setReplyFocused] = useState(false);
  const [likedDiscussions, setLikedDiscussions] = useState<Set<string>>(new Set());

  const canCreateDiscussion = true;

  useEffect(() => { fetchDiscussions(); }, [selectedCategory]);

  const fetchDiscussions = useCallback(async () => {
    setLoading(true); setError(null);
    try {
      const url = `${BASE_URL}/comments/top`;
      const res = await fetch(url, { method: "GET", headers: { "Content-Type": "application/json" } });
      if (!res.ok) throw new Error(`Error ${res.status}`);
      const backendComments: BackendComment[] = await res.json();
      setDiscussions(backendComments.map(mapCommentToDiscussion));
    } catch {
      setDiscussions([]);
      setError("No se pudieron cargar las discusiones.");
    } finally {
      setLoading(false);
    }
  }, [selectedCategory]);

  const createDiscussion = useCallback(async () => {
    if (!newTitle.trim() || !newContent.trim()) {
      setSubmitError("Título y contenido son requeridos.");
      return;
    }

    setSubmitting(true);
    setSubmitError(null);

    const token = localStorage.getItem("token");
    if (!token) {
      setSubmitError("Debes iniciar sesión para crear una discusión.");
      setSubmitting(false);
      return;
    }

    try {
      const tagsArray = newTags.split(",").map(t => t.trim()).filter(t => t);
      const res = await fetch(`${BASE_URL}/comments`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          title: newTitle,
          content: newContent,
          category: newCategory,
          tags: tagsArray,
        }),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Error ${res.status}: ${errorText}`);
      }

      
      setNewTitle("");
      setNewContent("");
      setNewCategory("general");
      setNewTags("");

      
      await fetchDiscussions();

      
      setView("list");
    } catch (err: unknown) {
      const errorMessage = err instanceof Error ? err.message : "Error al crear la discusión.";
      setSubmitError(errorMessage);
    } finally {
      setSubmitting(false);
    }
  }, [newTitle, newContent, newCategory, newTags, fetchDiscussions]);

  const openDiscussion = useCallback(async (disc: Discussion) => {
    setSelectedDiscussion(disc); setView("detail"); setRepliesLoading(true); setReplies([]);
    try {
      const res = await fetch(`${BASE_URL}/comments/${disc.id}`, { method: "GET", headers: { "Content-Type": "application/json" } });
      if (!res.ok) throw new Error(`Error ${res.status}`);
      const backendComment: BackendComment = await res.json();
      setReplies(backendComment.replies?.map(mapCommentToReply) ?? []);
    } catch {
      setReplies([]);
    } finally {
      setRepliesLoading(false);
    }
  }, []);

  const submitReply = useCallback(async () => {
    if (!replyContent.trim()) {
      setReplyError("El contenido de la respuesta no puede estar vacío.");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
      setReplyError("Debes iniciar sesión para responder.");
      return;
    }

    setReplySubmitting(true);
    setReplyError(null);

    try {
      const res = await fetch(`${BASE_URL}/comments/${selectedDiscussion?.id}/replies`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({ content: replyContent }),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(`Error ${res.status}: ${errorText}`);
      }

      const updatedComment: BackendComment = await res.json();
      setReplies(updatedComment.replies?.map(mapCommentToReply) ?? []);
      setReplyContent("");
    } catch (err: unknown) {
      const errorMessage = err instanceof Error ? err.message : "Error al enviar la respuesta.";
      setReplyError(errorMessage);
    } finally {
      setReplySubmitting(false);
    }
  }, [replyContent, selectedDiscussion]);

  const toggleLike = useCallback((discId: string) => {
    setLikedDiscussions(prev => { const n = new Set(prev); n.has(discId) ? n.delete(discId) : n.add(discId); return n; });
  }, []);

  const deleteReply = useCallback(async (replyId: string) => {
    setReplyError("Eliminar comentarios no está disponible con el backend actual.");
  }, []);

  const updateReplyReaction = useCallback((replyId: string, happyFace: number, sadFace: number) => {
    setReplies(prev => prev.map(r => r.id === replyId ? { ...r, happyFace, sadFace, likesCount: happyFace + sadFace } : r));
  }, []);

  const goBack = () => {
    setView("list");
    setSelectedDiscussion(null);
    setReplies([]);
  };

  return (
    <main style={{ minHeight: "100vh", background: "#07070f", fontFamily: "'Syne', sans-serif", position: "relative" as const, overflow: "hidden" }}>
      <style>{GLOBAL_STYLES}</style>
      <BackgroundEffects />
      <Navbar />
      <section style={{ position: "relative" as const, zIndex: 5, padding: "28px 24px", display: "flex", flexDirection: "column" as const, gap: 24, maxWidth: 1000, margin: "0 auto", animation: "slideIn .35s ease" }}>
        {view === "list" && <DiscussionList discussions={discussions} loading={loading} error={error} selectedCategory={selectedCategory} sortByPopular={sortByPopular} onCategoryChange={setSelectedCategory} onSortByPopularChange={setSortByPopular} onCreateClick={() => setView("create")} onDiscussionClick={openDiscussion} onLikeToggle={toggleLike} likedDiscussions={likedDiscussions} isAuthenticated={isAuthenticated && canCreateDiscussion} />}

        {view === "create" && <CreateDiscussion title={newTitle} content={newContent} category={newCategory} tags={newTags} error={submitError} submitting={submitting} onTitleChange={setNewTitle} onContentChange={setNewContent} onCategoryChange={setNewCategory} onTagsChange={setNewTags} onSubmit={createDiscussion} onCancel={goBack} />}

        {view === "detail" && selectedDiscussion && <DiscussionDetail discussion={selectedDiscussion} replies={replies} loading={repliesLoading} replyContent={replyContent} replyError={replyError} replyFocused={replyFocused} replySubmitting={replySubmitting} onReplyContentChange={setReplyContent} onReplyFocus={() => setReplyFocused(true)} onReplyBlur={() => setReplyFocused(false)} onReplySubmit={submitReply} onBack={goBack} currentUserRole={currentUser.role} onDeleteReply={deleteReply} onReplyReactionUpdate={updateReplyReaction} />}
      </section>
    </main>
  );
}