package club.nito.core.network.participation

import club.nito.core.model.participant.Participant
import club.nito.core.model.participant.ParticipantDeclaration
import club.nito.core.model.participant.ParticipantStatus
import club.nito.core.model.schedule.ScheduleId

/**
 * 参加情報を扱うリモートデータソース
 */
public sealed interface ParticipantRemoteDataSource {
    /**
     * 該当の予定の参加情報を取得する
     *
     * @param scheduleId 参加情報を取得するスケジュールID
     */
    public suspend fun getParticipants(scheduleId: String): List<Participant>

    /**
     * 該当の予定の参加情報を取得する
     *
     * @param scheduleIds 参加情報を取得するスケジュールID配列
     */
    public suspend fun getParticipants(scheduleIds: List<String>): List<Participant>

    /**
     * 該当の予定に対象のユーザーが参加しているかどうかを取得する
     *
     * @param scheduleId 参加情報を取得するスケジュールID
     * @param userId 対象のユーザーID
     */
    public suspend fun existParticipantByUserId(scheduleId: ScheduleId, userId: String): Boolean

    /**
     * リモートから該当の予定の対象のユーザーの参加情報を取得する
     *
     * @param scheduleId 参加情報を取得するスケジュールID
     * @param userId 対象のユーザーID
     * @return 参加情報
     */
    public suspend fun fetchParticipantStatus(scheduleId: ScheduleId, userId: String): ParticipantStatus

    /**
     * 該当スケジュールへの参加状況を追加する
     *
     * @param participant 参加表明データ
     */
    public suspend fun upsertParticipate(participant: Participant): Participant

    /**
     * 該当スケジュールへの参加状況を更新する
     *
     * @param declaration 参加表明データ
     */
    public suspend fun updateParticipate(declaration: ParticipantDeclaration): Participant
}
