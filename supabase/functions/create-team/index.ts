import { client, json, user } from "../_shared/http.ts";

Deno.serve(async req => {
  if (req.method !== "POST") return json({ error: "method_not_allowed" }, 405);
  if (!await user(req)) return json({ error: "unauthorized" }, 401);
  let body: { name?: string };
  try { body = await req.json(); } catch { return json({ error: "invalid_json" }, 400); }
  const name = String(body.name ?? "").trim();
  if (name.length < 2 || name.length > 120) return json({ error: "name_required" }, 400);
  const { data, error } = await client(req).rpc("create_team", { p_name: name });
  if (error) return json({ error: error.message }, 400);
  return json(data, 201);
});
