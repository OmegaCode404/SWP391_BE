package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.*;
import com.example.JWTImplemenation.Entities.*;
import com.example.JWTImplemenation.Repository.ChatMessageRepository;
import com.example.JWTImplemenation.Repository.ChatSessionRepository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IChatService;
import com.example.JWTImplemenation.Service.IService.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService implements IChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchRespository watchRepository;
    @Autowired
    private IImageService imageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
@Override
    public ResponseEntity<ChatSessionDTO> startChatSession(ChatStartRequest chatStartRequest) {
        Integer watchId = chatStartRequest.getWatchId();
        Integer userId = chatStartRequest.getUserId();
        Integer appraiserId = chatStartRequest.getAppraiserId();

        // Check for existing session
        List<ChatSession> existingSessions = chatSessionRepository.findBySellerIdAndAppraiserIdAndWatchId(userId, appraiserId, watchId);
        if (!existingSessions.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        User appraiser = userRepository.findById(appraiserId).orElseThrow(() -> new IllegalArgumentException("Invalid appraiser ID"));
        Watch watch = watchRepository.findById(watchId).orElseThrow(() -> new IllegalArgumentException("Invalid watch ID"));
        ChatSession chatSession = new ChatSession();
        chatSession.setWatch(watch);
        chatSession.setSeller(user);
        chatSession.setAppraiser(appraiser);
        chatSession.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        chatSessionRepository.save(chatSession);

        return ResponseEntity.ok(convertToDTO(chatSession));
    }
@Override
    public ResponseEntity<List<ChatSessionDTO>> getChatSessionsByUserId(Integer userId) {
        List<ChatSession> chatSessions = chatSessionRepository.findBySellerIdOrAppraiserId(userId, userId);
        return ResponseEntity.ok(chatSessions.stream().map(this::convertToDTO).collect(Collectors.toList()));
    }
    @Override
    public ResponseEntity<List<ChatMessageDTO>> getChatMessages(Integer sessionId) {
        List<ChatMessage> messages = chatMessageRepository.findByChatSessionId(sessionId);
        return ResponseEntity.ok(messages.stream().map(this::convertToDTO).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<ChatMessageDTO> sendChatMessage(Integer sessionId, String content, Integer senderId) {
        ChatSession chatSession = chatSessionRepository.findById(sessionId.longValue())
                .orElseThrow(() -> new IllegalArgumentException("Invalid session ID"));
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid sender ID"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatSession(chatSession);
        chatMessage.setSender(sender);
        chatMessage.setMessage(content);
        chatMessage.setTimestamp(new Timestamp(System.currentTimeMillis()));

        chatMessageRepository.save(chatMessage);

        ChatMessageDTO messageDTO = convertToDTO(chatMessage);


        return ResponseEntity.ok(messageDTO);
    }


    private ChatMessageDTO convertToDTO(ChatMessage chatMessage) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessage.getId());
        dto.setChatSessionId(chatMessage.getChatSession().getId());
        dto.setSenderId(chatMessage.getSender().getId());
        dto.setMessage(chatMessage.getMessage());
        dto.setTimestamp(chatMessage.getTimestamp());
        return dto;
    }

    private ChatSessionDTO convertToDTO(ChatSession chatSession) {
        ChatSessionDTO dto = new ChatSessionDTO();
        dto.setId(chatSession.getId());
        dto.setWatch(convertToDTO(chatSession.getWatch()));
        dto.setAppraiser(convertToDTO(chatSession.getAppraiser()));
        dto.setSeller(convertToDTO(chatSession.getSeller()));
        dto.setCreatedDate(chatSession.getCreatedDate());

        List<ChatMessage> messages = chatSession.getMessages();
        if (messages == null) {
            messages = new ArrayList<>();
        }

        dto.setMessages(messages.stream().map(this::convertToDTO).collect(Collectors.toList()));
        return dto;
    }
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .status(user.isStatus())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .build();
    }
    private WatchDTO convertToDTO(Watch watch) {
        WatchDTO watchDTO = new WatchDTO();
        watchDTO.setId(watch.getId());
        watchDTO.setName(watch.getName());
        watchDTO.setBrand(watch.getBrand());
        watchDTO.setDescription(watch.getDescription());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setPaid(watch.isPaid());
        watchDTO.setStatus(watch.isStatus());
        watchDTO.setCreatedDate(watch.getCreatedDate());
        watchDTO.setCreatedDate(watch.getCreatedDate());
        if (watch.getImageUrl() != null) {
            List<String> imageUrls = watch.getImageUrl()
                    .stream()
                    .map(ImageUrl::getImageUrl)
                    .collect(Collectors.toList());
            watchDTO.setImageUrl(imageUrls);
        }
        if (watch.getAppraisal() != null) {
            watchDTO.setAppraisalId(watch.getAppraisal().getId());
        }
        watchDTO.setSellerId(watch.getUser().getId());
        watchDTO.setSellerName(watch.getUser().getFirstName() + " " + watch.getUser().getLastName());
        return watchDTO;
    }

}
