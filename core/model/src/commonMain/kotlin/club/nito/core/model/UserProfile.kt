package club.nito.core.model

/**
 * ユーザープロフィール
 */
public data class UserProfile(
    val id: String,
    val username: String,
    val displayName: String,
    val avatarUrl: String,
    val website: String,
)
