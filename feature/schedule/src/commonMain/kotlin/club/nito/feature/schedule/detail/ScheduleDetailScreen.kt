package club.nito.feature.schedule.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import club.nito.core.common.NitoDateFormatter
import club.nito.core.designsystem.component.CenterAlignedTopAppBar
import club.nito.core.designsystem.component.Scaffold
import club.nito.core.designsystem.component.Text
import club.nito.core.domain.model.ParticipantSchedule
import club.nito.core.model.FetchSingleContentResult
import club.nito.core.model.schedule.ScheduleId
import club.nito.core.ui.ProfileImage
import club.nito.core.ui.koinStateMachine
import club.nito.core.ui.message.SnackbarMessageEffect
import com.seiko.imageloader.rememberImagePainter
import org.koin.core.parameter.parametersOf

@Composable
public fun ScheduleDetailRoute(
    id: ScheduleId,
    stateMachine: ScheduleDetailStateMachine = koinStateMachine(ScheduleDetailStateMachine::class) {
        parametersOf(id)
    },
) {
    stateMachine.event.collectAsState(initial = null).value?.let {
        LaunchedEffect(it.hashCode()) {
            when (it) {
                is ScheduleDetailEvent.NavigateToScheduleDetail -> {}
            }
            stateMachine.consume(it)
        }
    }

    val uiState by stateMachine.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    SnackbarMessageEffect(
        snackbarHostState = snackbarHostState,
        userMessageStateHolder = stateMachine.userMessageStateHolder,
    )

    ScheduleDetailScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        dispatch = stateMachine::dispatch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleDetailScreen(
    uiState: ScheduleDetailScreenUiState,
    snackbarHostState: SnackbarHostState,
    dispatch: (ScheduleDetailIntent) -> Unit = {},
) {
    val layoutDirection = LocalLayoutDirection.current
    val localDensity = LocalDensity.current

    val schedule = uiState.schedule
    var sendMessageContainerHeightDp by remember {
        mutableStateOf(0.dp)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "スケジュール詳細",
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                when (schedule) {
                    FetchSingleContentResult.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )

                    FetchSingleContentResult.NoContent -> Text(
                        text = "スケジュールが見つかりませんでした",
                        modifier = Modifier.align(Alignment.Center),
                    )

                    is FetchSingleContentResult.Success -> {
                        val containerModifier = Modifier
                            .padding(
                                start = innerPadding.calculateStartPadding(layoutDirection),
                                end = innerPadding.calculateEndPadding(layoutDirection),
                            )
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(
                                    top = innerPadding.calculateTopPadding(),
                                    bottom = innerPadding.calculateBottomPadding(),
                                )
                                .padding(bottom = sendMessageContainerHeightDp)
                                .padding(vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(40.dp),
                        ) {
                            VenueSection(
                                schedule = schedule.data,
                                dateFormatter = uiState.dateFormatter,
                                modifier = containerModifier,
                            )

                            MeetSection(
                                schedule = schedule.data,
                                dateFormatter = uiState.dateFormatter,
                                modifier = containerModifier,
                            )

                            ParticipantSection(
                                schedule = schedule.data,
                                onParticipateClick = { dispatch(ScheduleDetailIntent.ClickParticipate(it)) },
                                modifier = containerModifier,
                            )
                        }
                    }

                    is FetchSingleContentResult.Failure -> Text(
                        text = schedule.error?.message ?: "エラーが発生しました",
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                SendMessageContainer(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            sendMessageContainerHeightDp = with(localDensity) { coordinates.size.height.toDp() }
                        },
                    innerPadding = innerPadding,
                )
            }
        },
    )
}

@Composable
private fun VenueSection(
    schedule: ParticipantSchedule,
    dateFormatter: NitoDateFormatter,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(16.dp),
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
        ) {
            Image(
                painter = rememberImagePainter(
                    url = schedule.venue.imageUrl,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
            )

            Text(
                text = "開催情報",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = CircleShape,
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                fontSize = 14.sp,
                color = Color.Black,
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = schedule.venue.name,
            )
            Text(
                text = dateFormatter.formatDateTime(schedule.scheduledAt),
            )
            Text(
                text = schedule.description,
            )
        }
    }
}

@Composable
private fun MeetSection(
    schedule: ParticipantSchedule,
    dateFormatter: NitoDateFormatter,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSecondary,
                shape = RoundedCornerShape(16.dp),
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
        ) {
            Image(
                painter = rememberImagePainter(
                    url = schedule.meet.imageUrl,
                ),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
            )

            Text(
                text = "集合情報",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.5f),
                        shape = CircleShape,
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp),
                fontSize = 14.sp,
                color = Color.Black,
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = schedule.meet.name,
            )
            Text(
                text = dateFormatter.formatDateTime(schedule.metAt),
            )
        }
    }
}

@Composable
private fun ParticipantSection(
    schedule: ParticipantSchedule,
    onParticipateClick: (ParticipantSchedule) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "参加情報",
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items(
                items = schedule.participants,
                key = { profile -> profile.id },
            ) { profile ->
                ProfileImage(
                    profile = profile,
                    modifier = Modifier.size(48.dp),
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            OutlinedButton(
                onClick = { onParticipateClick(schedule) },
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = "参加する")
            }
        }
    }
}

@Composable
private fun SendMessageContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    innerPadding: PaddingValues = PaddingValues(),
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
) {
    val insetPadding = remember(modifier, innerPadding, layoutDirection) {
        PaddingValues(
            start = innerPadding.calculateStartPadding(layoutDirection = layoutDirection),
            end = innerPadding.calculateEndPadding(layoutDirection = layoutDirection),
            bottom = innerPadding.calculateBottomPadding(),
        )
    }

    Row(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                ),
            )
            .padding(insetPadding)
            .heightIn(min = 80.dp)
            .padding(horizontal = 16.dp)
            .padding(
                top = 16.dp,
                bottom = 8.dp,
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            enabled = false,
            placeholder = { Text(text = "Coming Soon.") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
        )

        IconButton(
            onClick = { },
            Modifier.align(Alignment.CenterVertically),
            enabled = false,
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}
