package com.gongsp.api.controller;

import com.gongsp.api.request.meeting.MeetingCreatePostReq;
import com.gongsp.api.request.meeting.MeetingExitDeleteReq;
import com.gongsp.api.request.meeting.MeetingParameter;
import com.gongsp.api.response.meeting.MeetingDetailGetRes;
import com.gongsp.api.response.meeting.MeetingEnterPostRes;
import com.gongsp.api.response.meeting.MeetingListGetRes;
import com.gongsp.api.response.meeting.MeetingRes;
import com.gongsp.api.service.*;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import com.gongsp.db.repository.LogTimeRepository;
import io.openvidu.java.client.OpenVidu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.SchemaOutputResolver;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    // OpenVidu object as entrypoint of the SDK
    private OpenVidu openVidu;

//    // Collection to pair session names and OpenVidu Session objects
//    // ConcurrentHashMap : Multi-Thread 환경에서 사용할 수 있도록 나온 클래스
//    private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
//
//    // Collection to pair session names and tokens (the inner Map pairs tokens and role associated)
//    private Map<String, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

    // URL where our OpenVidu server is listening
    private String OPENVIDU_URL;

    // Secret shared with our OpenVidu server
    private String SECRET;

    @Autowired
    private MeetingService meetingService;
    @Autowired
    private MeetingOnairService meetingOnairService;
    @Autowired
    private LogTimeService logTimeService;
    @Autowired
    private UserService userService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private BlacklistMeetingService blacklistMeetingService;


    public MeetingController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    // 자유열람실 목록 조회
    @GetMapping
    public ResponseEntity<? extends BaseResponseBody> getMeetingList(MeetingParameter meetingParameter, Authentication authentication) {
        if (meetingParameter.getPage() == null)
            return ResponseEntity.ok(MeetingListGetRes.of(409, "Fail : Get Meeting List. No page"));
        if (meetingParameter.getType() == null)
            return ResponseEntity.ok(MeetingListGetRes.of(409, "Fail : Get Meeting List. No Category type"));
        List<MeetingRes> data = meetingService.getMeetingList(meetingParameter, Integer.parseInt((String) authentication.getPrincipal()));
        return ResponseEntity.ok(MeetingListGetRes.of(200, "Success : Get Meeting List", meetingParameter, data.size(), data));
    }

    // 자유열람실 생성
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> createMeeting(@ModelAttribute MeetingCreatePostReq meetingCreatePostReq, Authentication authentication) {
        Integer userSeq =  Integer.parseInt((String) authentication.getPrincipal());
        if(meetingService.isUserOwnMeeting(userSeq))
            return ResponseEntity.ok(BaseResponseBody.of(408, "Fail : User already has meeting room"));
        if(meetingService.createMeeting(meetingCreatePostReq, userSeq, storageService.store(meetingCreatePostReq.getMeetingImg()))==null)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Create meeting room"));
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Create meeting room"));
    }

    // 자유열람실 상세조회
    @GetMapping("/{meeting-seq}")
    public ResponseEntity<? extends BaseResponseBody> getMeetingDetail(@PathVariable("meeting-seq") Integer meetingSeq, Authentication authentication) {
        MeetingDetailGetRes meetingDetailGetRes = meetingService.getMeetingDetail(meetingSeq);
        if(meetingDetailGetRes == null)
            return ResponseEntity.ok(BaseResponseBody.of(409, "Fail : Get meeting detail"));
        return ResponseEntity.ok(MeetingDetailGetRes.of(200, "Success : Get meeting detail", meetingDetailGetRes));
    }

    // 자유열람실 강퇴
    @GetMapping("/{meeting-seq}/kick/{user-seq}")
    public ResponseEntity<? extends BaseResponseBody> kickUserFromMeeting(@PathVariable("meeting-seq") Integer meetingSeq, @PathVariable("user-seq") Integer userSeq, Authentication authentication) {

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Create meeting room"));
    }

//    getapping 완성해서
//    getToken안에서는
//    meeting 객체를 얻어오게 한 후에
//    meetingService에 인자로 meeting 넣어서 보냄!

    // 자유열람실 입실
    @PostMapping("/{meeting-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> getToken(@PathVariable("meeting-seq") Integer meetingSeq, Authentication authentication) {

        // 존재하는 자유열람실만 입실 할 수 있음
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        Optional<Meeting> opMeeting = meetingService.getMeeting(meetingSeq);
        if (!opMeeting.isPresent())
            return ResponseEntity.ok(MeetingEnterPostRes.of(404, "Fail : Not valid meetingSeq"));

        // 이미 방에 들어가 있는경우 입실 불가능 -> 이거 처리 다시해야될거같긴 한데..
        if (meetingOnairService.existsOnair(userSeq, meetingSeq))
            return ResponseEntity.ok(MeetingEnterPostRes.of(406, "Fail : Already exists in meeting room"));

        // 블랙리스트인 경우 입실 못함
        // 애초에 목록에서 안보이도록 제외시키긴 했는데 혹시 url로 들어가려는 시도를 할 수 있으니?
        if(blacklistMeetingService.isUserInBlacklist(userSeq, meetingSeq))
            return ResponseEntity.ok(MeetingEnterPostRes.of(407, "Fail : User is in blacklist"));

        // 호스트 유무에 따른 정원
        Meeting meeting = opMeeting.get();
        Boolean isHost = meeting.getHostSeq().equals(userSeq);
        if (!isHost) {
            //호스트가 방에 없으면 11명 이상인경우 못들어감
            int capacity = 11;
            //호스트가 방에 있으면 12명 이상인경우 못들어감
            if (meetingOnairService.existsOnair(meeting.getHostSeq(), meetingSeq))
                capacity = 12;
            if (meeting.getMeetingHeadcount() >= capacity)
                return ResponseEntity.ok(MeetingEnterPostRes.of(405, "Fail : meeting room is full"));
        }

        // token 얻기 (session 생성 후 connection 생성)
        String token = meetingService.getToken(openVidu, userSeq, meeting);
//        System.out.println("토큰!!" + token);

        //tb_meeting_onair 칼럼추가
        meetingOnairService.createOnair(userSeq, meetingSeq, isHost);

        //tb_meeting 의 meetingHeadcount ++
        meetingService.updateMeeting(meetingSeq, 1);

        //당일 최초로 공부를 시작한 사용자
        if (!logTimeService.existsLog(userSeq, LocalDate.now())) {
//            System.out.println("공부기록삽입");
            logTimeService.createLogTime(userSeq);
        }

        if (token.equals("InternalError"))
            return ResponseEntity.ok(MeetingEnterPostRes.of(409, "Fail : OpenViduJavaClientException", null));

        if (token.equals("GenError"))
            return ResponseEntity.ok(MeetingEnterPostRes.of(409, "Fail : Generate meeting room", null));

        return ResponseEntity.ok(MeetingEnterPostRes.of(200, "Success : Entry meeting room", token, meeting, isHost));
    }

    // 자유열람실 퇴실
    @DeleteMapping("/{meeting-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> removeUser(@PathVariable("meeting-seq") Integer meetingSeq, @RequestBody MeetingExitDeleteReq meetingExitDeleteReq, Authentication authentication) {

        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());

//        System.out.println(meetingExitDeleteReq.toString());
//        System.out.println(logTimeRepository.findLogTimeByLogSeq(2));
//        System.out.println("Removing user | {sessionName, userSeq}=" + "{" + meetingSeq + "," + userSeq + "}");

        String sessionName = meetingSeq.toString();
        String token = meetingExitDeleteReq.getSessionToken();

        // session, connection 해제
        String result = meetingService.removeUser(sessionName, token, meetingSeq);
        if ("Error".equals(result))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseBody.of(409, "Fail : Remove user"));

        // tb_meeting_onair 칼럼 삭제
        meetingOnairService.deleteOnair(userSeq, meetingSeq);

        // tb_meeting 의 meetingHeadcount --
        meetingService.updateMeeting(meetingSeq, -1);

        // 시간 넘어온거 누적
        logTimeService.updateMeetingLogTime(userSeq, meetingExitDeleteReq.getLogMeeting(), meetingExitDeleteReq.getLogStartTime());
        userService.updateUserLogTime(userSeq, meetingExitDeleteReq.getLogMeeting());

        // 만석인 방에서 나갈경우 알림보내기

        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Remove user"));
    }

}
