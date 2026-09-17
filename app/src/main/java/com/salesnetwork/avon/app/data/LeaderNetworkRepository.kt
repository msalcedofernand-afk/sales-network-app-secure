package com.salesnetwork.avon.app.data

import android.content.Context
import com.salesnetwork.avon.app.domain.model.User
import com.salesnetwork.avon.app.domain.model.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

/**
 * Authentication is remote-only. No passwords, demo accounts or local auth
 * bypasses are stored in the APK or in SharedPreferences.
 */
class LeaderNetworkRepository private constructor(context: Context) {
    private val appContext = context.applicationContext
    private val usersMap = mutableMapOf<String, User>()
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun registerLeader(name: String, email: String, password: String): Result<User> =
        signUp(name, email, password, UserRole.LIDER, null)

    fun registerMember(name: String, email: String, password: String, leaderCode: String): Result<User> {
        val code = leaderCode.trim().uppercase()
        if (code.isBlank()) return Result.failure(IllegalArgumentException("El código de red es obligatorio."))
        return signUp(name, email, password, UserRole.MIEMBRO, code)
    }

    fun login(email: String, password: String): Result<User> {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank() || password.isBlank()) return Result.failure(IllegalArgumentException("Correo y contraseña son obligatorios."))
        return runCatching {
            val auth = request("auth/v1/token?grant_type=password", JSONObject().put("email", cleanEmail).put("password", password))
            val user = userFrom(auth.getJSONObject("user"), UserRole.MIEMBRO)
            usersMap[user.id] = user
            _currentUser.value = user
            user
        }.onFailure { _currentUser.value = null }
    }

    fun sendPasswordReset(email: String): Result<Boolean> {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank()) return Result.failure(IllegalArgumentException("El correo es obligatorio."))
        return runCatching {
            request("auth/v1/recover", JSONObject().put("email", cleanEmail).put("redirect_to", RESET_REDIRECT))
            true
        }
    }

    fun logout() {
        _currentUser.value = null
        usersMap.clear()
    }

    fun getMembersForLeader(referralCode: String): List<User> = usersMap.values.filter {
        it.leaderCode.equals(referralCode.trim(), ignoreCase = true)
    }

    fun getAllUsers(): List<User> = usersMap.values.toList()

    private fun signUp(name: String, email: String, password: String, role: UserRole, leaderCode: String?): Result<User> {
        val cleanName = name.trim()
        val cleanEmail = email.trim().lowercase()
        if (cleanName.length !in 2..120 || cleanEmail.isBlank()) return Result.failure(IllegalArgumentException("Nombre y correo válidos son obligatorios."))
        if (password.length < 8) return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 8 caracteres."))
        return runCatching {
            val auth = request("auth/v1/signup", JSONObject()
                .put("email", cleanEmail)
                .put("password", password)
                .put("data", JSONObject().put("name", cleanName)))
            val accessToken = auth.optString("access_token")
            if (accessToken.isBlank()) throw IllegalStateException("Cuenta creada. Confirma tu correo antes de iniciar sesión.")
            val user = userFrom(auth.getJSONObject("user"), role, cleanName, leaderCode)
            val function = if (role == UserRole.LIDER) "create-team" else "accept-invitation"
            val payload = if (role == UserRole.LIDER) JSONObject().put("name", "$cleanName Team") else JSONObject().put("code", leaderCode)
            callFunction(function, payload, accessToken)
            usersMap[user.id] = user
            _currentUser.value = user
            user
        }
    }

    private fun userFrom(json: JSONObject, fallbackRole: UserRole, fallbackName: String? = null, leaderCode: String? = null): User {
        val appMetadata = json.optJSONObject("app_metadata")
        val role = when (appMetadata?.optString("role")) {
            "ROOT_ADMIN" -> UserRole.ROOT_ADMIN
            "LIDER" -> UserRole.LIDER
            else -> fallbackRole
        }
        val metadata = json.optJSONObject("user_metadata")
        return User(
            id = json.getString("id"),
            name = metadata?.optString("name").orEmpty().ifBlank { fallbackName ?: json.optString("email").substringBefore("@") },
            email = json.optString("email"),
            role = role,
            referralCode = metadata?.optString("referral_code").orEmpty().ifBlank { "USER-${UUID.randomUUID().toString().take(8).uppercase()}" },
            leaderCode = leaderCode ?: metadata?.optString("leader_code")?.ifBlank { null }
        )
    }

    private fun request(path: String, body: JSONObject): JSONObject {
        val connection = (URL("$SUPABASE_URL/$path").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            connectTimeout = 8_000
            readTimeout = 8_000
            setRequestProperty("apikey", SUPABASE_PUBLISHABLE_KEY)
            setRequestProperty("Content-Type", "application/json")
        }
        connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
        val responseBody = (if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream)
            ?.bufferedReader()?.use { it.readText() }.orEmpty()
        if (connection.responseCode !in 200..299) {
            val detail = runCatching { JSONObject(responseBody).optString("msg").ifBlank { JSONObject(responseBody).optString("error_description") } }.getOrNull()
            throw IllegalStateException(detail?.ifBlank { null } ?: "No se pudo autenticar la cuenta.")
        }
        return if (responseBody.isBlank()) JSONObject() else JSONObject(responseBody)
    }

    private fun callFunction(name: String, body: JSONObject, accessToken: String): JSONObject {
        val connection = (URL("$SUPABASE_URL/functions/v1/$name").openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            doOutput = true
            connectTimeout = 8_000
            readTimeout = 8_000
            setRequestProperty("apikey", SUPABASE_PUBLISHABLE_KEY)
            setRequestProperty("Authorization", "Bearer $accessToken")
            setRequestProperty("Content-Type", "application/json")
        }
        connection.outputStream.use { it.write(body.toString().toByteArray(Charsets.UTF_8)) }
        val responseBody = (if (connection.responseCode in 200..299) connection.inputStream else connection.errorStream)
            ?.bufferedReader()?.use { it.readText() }.orEmpty()
        if (connection.responseCode !in 200..299) throw IllegalStateException("No se pudo completar el registro: ${connection.responseCode}")
        return if (responseBody.isBlank()) JSONObject() else JSONObject(responseBody)
    }

    companion object {
        private const val SUPABASE_URL = "https://xceqwexdufdgnmctsxcg.supabase.co"
        // Publishable/anon keys are client-side identifiers; service_role is never shipped.
        private const val SUPABASE_PUBLISHABLE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhjZXE3ZXhkdWZkZ25mY3RzeGNnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3ODg4MzgxMjgsImV4cCI6MjEwNDQxNDEyOH0.LqPTMoS3Q1-zdsOX9CMOahMynB5XAl-AxsjrVxSlse8"
        private const val RESET_REDIRECT = "https://sales-network-app.vercel.app/login"

        @Volatile private var instance: LeaderNetworkRepository? = null

        fun getInstance(context: Context): LeaderNetworkRepository = instance ?: synchronized(this) {
            instance ?: LeaderNetworkRepository(context).also { instance = it }
        }
    }
}
