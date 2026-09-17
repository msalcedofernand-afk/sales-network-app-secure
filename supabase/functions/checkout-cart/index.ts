import { client, json, user } from "../_shared/http.ts";

Deno.serve(async req => {
  if (req.method !== "POST") return json({ error: "method_not_allowed" }, 405);
  if (!await user(req)) return json({ error: "unauthorized" }, 401);
  let body: { cart_id?: string; customer_id?: string | null; idempotency_key?: string };
  try { body = await req.json(); } catch { return json({ error: "invalid_json" }, 400); }
  if (!body.cart_id || !body.idempotency_key) return json({ error: "cart_id_and_idempotency_key_required" }, 400);

  // The database function owns the transaction, price calculation, tenant
  // validation and idempotency handling. The Edge Function only authenticates
  // the request and returns its result.
  const { data, error } = await client(req).rpc("checkout_cart", {
    p_cart_id: body.cart_id,
    p_customer_id: body.customer_id ?? null,
    p_idempotency_key: body.idempotency_key,
  });
  if (error) return json({ error: error.message }, 400);
  return json(data, data?.existing ? 200 : 201);
});

