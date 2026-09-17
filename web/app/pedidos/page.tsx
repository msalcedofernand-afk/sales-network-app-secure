"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { getSessionContext, money } from "../../lib/app-data";

type Order = {
  id: string;
  status: string;
  total_cents: number;
  created_at: string;
  customer_id: string | null;
  order_items: { product_name: string; quantity: number; unit_price_cents: number }[];
};

export default function OrdersPage() {
  const [rows, setRows] = useState<Order[]>([]);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(true);

  async function load() {
    try {
      const { supabase, user } = await getSessionContext();
      const { data, error } = await supabase
        .from("orders")
        .select("id,status,total_cents,created_at,customer_id,order_items(product_name,quantity,unit_price_cents)")
        .eq("user_id", user.id)
        .order("created_at", { ascending: false });
      if (error) throw error;
      setRows((data ?? []) as unknown as Order[]);
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No pudimos cargar tus pedidos.");
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { void load(); }, []);

  async function changeStatus(id: string, status: string) {
    try {
      const { supabase } = await getSessionContext();
      const { error } = await supabase.rpc("update_order_status", { p_order_id: id, p_status: status });
      if (error) throw error;
      setMessage("Estado actualizado.");
      await load();
    } catch (error) {
      setMessage(error instanceof Error ? error.message : "No se pudo actualizar el pedido.");
    }
  }

  return <main>
    <section className="hero"><p className="eyebrow">VV / Ventas</p><h1>Tu negocio en movimiento.</h1><p className="lede">Pedidos, cobros y entregas en un solo lugar.</p></section>
    {message && <div className="notice" role="status">{message}</div>}
    {loading ? <div className="state card"><div className="spinner" /><p>Cargando pedidos…</p></div> : rows.length === 0 ? <div className="state card"><h2>Aún no tienes pedidos</h2><p className="muted">Agrega productos al carrito para crear el primero.</p><Link className="button" href="/catalogo">Ir al catálogo</Link></div> : <section className="list">
      {rows.map(order => <article className="card" key={order.id}>
        <div className="section-head"><div><span className="badge">{order.status}</span><h2>{new Date(order.created_at).toLocaleDateString("es-PE")}</h2></div><strong>{money(order.total_cents)}</strong></div>
        <ul>{order.order_items?.map((item, index) => <li key={`${order.id}-${index}`}>{item.quantity} × {item.product_name} · {money(item.unit_price_cents)}</li>)}</ul>
        <div className="actions">
          {order.status === "PENDIENTE" && <button onClick={() => void changeStatus(order.id, "CONFIRMADO")}>Confirmar</button>}
          {order.status === "CONFIRMADO" && <button onClick={() => void changeStatus(order.id, "COBRADO")}>Marcar cobrado</button>}
          {!['ENTREGADO', 'CANCELADO'].includes(order.status) && <button className="button ghost" onClick={() => void changeStatus(order.id, "CANCELADO")}>Cancelar</button>}
        </div>
      </article>)}
    </section>}
  </main>;
}
