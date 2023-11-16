package club.nito.core.data

import club.nito.core.model.FetchSingleContentResult
import club.nito.core.model.UserProfile

/**
 * ユーザープロフィールに関するリポジトリ
 */
sealed interface UserRepository {
    /**
     * ユーザープロフィールを取得する
     */
    suspend fun getProfile(userId: String): FetchSingleContentResult<UserProfile>

    /**
     * 複数のユーザープロフィールを取得する
     */
    suspend fun getProfiles(userIds: List<String>): List<UserProfile>
}