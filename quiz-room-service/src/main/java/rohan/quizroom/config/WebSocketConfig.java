package rohan.quizroom.config;


import rohan.quizroom.dto.*;
import rohan.quizroom.service.RoomManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RoomWebSocketController {

    private final RoomManagerService roomManager;

    /**
     * Create new room
     * Client sends to: /app/room/create
     * Response sent to: /user/queue/room/created (user-specific)
     */
    @MessageMapping("/room/create")
    @SendToUser("/queue/room/created")
    public RoomCreatedResponse createRoom(
            @Valid @Payload CreateRoomRequest request,
            Principal principal
    ) {
        log.info("WebSocket: Create room request from {}", principal.getName());

        try {
            return roomManager.createRoom(request, principal.getName());
        } catch (Exception e) {
            log.error("Error creating room", e);
            throw new RuntimeException("Failed to create room: " + e.getMessage());
        }
    }

    /**
     * Join room
     * Client sends to: /app/room/join
     * Response broadcasted to: /topic/room/{roomCode}
     */
    @MessageMapping("/room/join")
    public void joinRoom(
            @Valid @Payload JoinRoomRequest request,
            Principal principal
    ) {
        log.info("WebSocket: Join room request from {} for room {}",
                principal.getName(), request.getRoomCode());

        try {
            roomManager.joinRoom(request, principal.getName());
        } catch (Exception e) {
            log.error("Error joining room", e);
            // Send error to user
            throw new RuntimeException("Failed to join room: " + e.getMessage());
        }
    }

    /**
     * Player ready
     * Client sends to: /app/room/ready
     * Response broadcasted to: /topic/room/{roomCode}
     */
    @MessageMapping("/room/ready")
    public void playerReady(
            @Valid @Payload PlayerReadyRequest request,
            Principal principal
    ) {
        log.info("WebSocket: Player ready from {} in room {}",
                principal.getName(), request.getRoomCode());

        try {
            roomManager.playerReady(request, principal.getName());
        } catch (Exception e) {
            log.error("Error marking player ready", e);
            throw new RuntimeException("Failed to mark ready: " + e.getMessage());
        }
    }

    /**
     * Submit answer
     * Client sends to: /app/room/answer
     * Response broadcasted to: /topic/room/{roomCode}/answer
     */
    @MessageMapping("/room/answer")
    public void submitAnswer(
            @Valid @Payload SubmitAnswerRequest request,
            Principal principal
    ) {
        log.info("WebSocket: Answer submission from {} for Q{} in room {}",
                principal.getName(), request.getQuestionNumber(), request.getRoomCode());

        try {
            roomManager.submitAnswer(request, principal.getName());
        } catch (Exception e) {
            log.error("Error submitting answer", e);
            throw new RuntimeException("Failed to submit answer: " + e.getMessage());
        }
    }
}