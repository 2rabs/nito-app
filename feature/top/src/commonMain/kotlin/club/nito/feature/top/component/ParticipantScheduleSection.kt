package club.nito.feature.top.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.nito.core.common.NitoDateFormatter
import club.nito.core.designsystem.component.Text
import club.nito.core.domain.model.ParticipantSchedule
import club.nito.core.model.FetchSingleContentResult
import club.nito.core.model.NitoError
import club.nito.core.ui.ParticipantScheduleItem

@Composable
internal fun ParticipantScheduleSection(
    recentSchedule: FetchSingleContentResult<ParticipantSchedule>,
    dateTimeFormatter: NitoDateFormatter,
    modifier: Modifier = Modifier,
    onRecentScheduleClick: (schedule: ParticipantSchedule) -> Unit = {},
    onScheduleListClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "スケジュール",
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 8.dp),
            )

            when (recentSchedule) {
                FetchSingleContentResult.Loading -> LoadingParticipantSchedule()
                FetchSingleContentResult.NoContent -> NoParticipantSchedule()
                is FetchSingleContentResult.Success -> ParticipantScheduleItem(
                    schedule = recentSchedule.data,
                    dateTimeFormatter = dateTimeFormatter,
                    onScheduleClick = onRecentScheduleClick,
                )

                is FetchSingleContentResult.Failure -> FailureParticipantSchedule(
                    error = recentSchedule.error,
                )
            }

            TextButton(
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .padding(horizontal = 8.dp),
                onClick = onScheduleListClick,
            ) {
                Text(
                    text = "スケジュール一覧を見る",
                )
            }
        }
    }
}

@Composable
private fun LoadingParticipantSchedule(
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 48.dp)
        .padding(8.dp),
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically,
) {
    CircularProgressIndicator()
}

@Composable
private fun NoParticipantSchedule(
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 48.dp)
        .padding(8.dp),
    verticalArrangement = Arrangement.Center,
) {
    Text(text = "スケジュールがありません")
}

@Composable
private fun FailureParticipantSchedule(
    error: NitoError?,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .heightIn(min = 48.dp)
        .padding(8.dp),
    verticalArrangement = Arrangement.Center,
) {
    Text(text = error?.message ?: "スケジュールの取得に失敗しました")
}
