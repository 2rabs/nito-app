import Dependencies
import NitoKmp

public struct GetRecentScheduleUseCaseProvider {
    private static var getRecentScheduleUseCase: GetRecentScheduleUseCase {
        Container.shared.get(type: GetRecentScheduleUseCase.self)
    }

    public let execute: () -> AsyncThrowingStream<FetchSingleContentResult, Error>
}

extension GetRecentScheduleUseCaseProvider: DependencyKey {
    @MainActor
    static public var liveValue: GetRecentScheduleUseCaseProvider =
        GetRecentScheduleUseCaseProvider(
            execute: {
                getRecentScheduleUseCase.invoke().stream()
            }
        )
}

extension DependencyValues {
    public var getRecentScheduleUseCase: GetRecentScheduleUseCaseProvider {
        get { self[GetRecentScheduleUseCaseProvider.self] }
        set { self[GetRecentScheduleUseCaseProvider.self] = newValue }
    }
}
