package chervotkin.dev.eventmanager.web;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailMessage,
        LocalDateTime dateTime
) {
}
