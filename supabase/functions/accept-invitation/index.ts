import { client, json, user } from "../_shared/http.ts";

Deno.serve(async req => {
  if (req.method !== "POST") return json({ error: "method_not_allowed" }, 405);
  if (!await user(req)) return json({ error: "unauthorized" }, 401);
  let body: { code?: string };
  try { body = await req.json(); } catch { return json({ error: "invalid_json" }, 400); }
  if (!body.code) return json({ error: "code_required" }, 400);
  const { data, error } = await client(req).rpc("accept_invitation", {
    p_code: String(body.code).trim().toUpperCase(),
  });
  if (error) return json({ error: error.message }, 400);
  return json(data);
});
