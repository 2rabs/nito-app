package club.nito.core.network.participation

import club.nito.core.model.participant.Participant
import club.nito.core.model.participant.ParticipantDeclaration

/**
 * 参加情報を扱うリモートデータソース
 */
sealed interface ParticipantRemoteDataSource {
    /**
     * 該当の予定の参加情報を取得する
     *
     * @param scheduleId 参加情報を取得するスケジュールID
     */
    suspend fun getParticipants(scheduleId: String): List<Participant>

    /**
     * 該当の予定の参加情報を取得する
     *
     * @param scheduleIds 参加情報を取得するスケジュールID配列
     */
    suspend fun getParticipants(scheduleIds: List<String>): List<Participant>

    /**
     * 該当のスケジュールに参加する
     *
     * @param declaration 参加表明データ
     */
    suspend fun participate(declaration: ParticipantDeclaration): Long
}