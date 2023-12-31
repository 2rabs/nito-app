package club.nito.core.model.participant

/**
 * 参加情報
 * @param scheduleId スケジュールID
 * @param userId 参加ユーザーID
 * @param status 参加状況
 */
public data class Participant(
    val scheduleId: String,
    val userId: String,
    val status: ParticipantStatus,
)
