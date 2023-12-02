package club.nito.core.data

import club.nito.core.model.AuthStatus
import club.nito.core.model.UserInfo
import kotlinx.coroutines.flow.Flow

/**
 * 認証に関するリポジトリ
 */
public sealed interface AuthRepository {
    /**
     * 認証情報の状態
     */
    public val authStatus: Flow<AuthStatus>

    /**
     * ログインする
     */
    public suspend fun login(email: String, password: String)

    /**
     * ログアウトする
     */
    public suspend fun logout()

    /**
     * 認証ユーザー情報を更新する
     */
    public suspend fun modifyAuthUser(email: String?, password: String?): UserInfo
}
