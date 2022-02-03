package com.gongsp.api.controller;

import com.gongsp.api.request.meeting.MeetingExitDeleteReq;
import com.gongsp.api.response.meeting.MeetingEnterPostRes;
import com.gongsp.api.service.MeetingService;
import com.gongsp.common.auth.GongUserDetails;
import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import io.openvidu.java.client.OpenVidu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    MeetingService meetingService;

    public MeetingController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }

    // 자유열람실 생성
    @PostMapping
    public ResponseEntity<? extends BaseResponseBody> createMeeting(){
        System.out.println("연결완료");
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Create meeting room"));
    }


//    getmapping 완성해서
//    getToken안에서는
//    meeting 객체를 얻어오게 한 후에
//    meetingService에 인자로 meeting 넣어서 보냄!

    // 자유열람실 입실
    // session 생성, connection 생성
    @PostMapping("/{meeting-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> getToken(@PathVariable("meeting-seq") Integer meetingSeq, Authentication authentication) {

        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());
        Optional<Meeting> opMeeting = meetingService.getMeeting(meetingSeq);
        if(!opMeeting.isPresent())
            return ResponseEntity.status(404).body(MeetingEnterPostRes.of(404, "Fail : Not valid meetingSeq"));

        Meeting meeting = opMeeting.get();
        Boolean isHost = meeting.getHostSeq().equals(userSeq);
        if(!isHost){
            //호스트가 방에 없으면 11명 이상인경우 못들어감
            int capacity = 11;
            //호스트가 방에 있으면 12명 이상인경우 못들어감
            if(meetingService.existsOnair(meeting.getHostSeq(), meetingSeq))
                capacity = 12;
            if(meeting.getMeetingHeadcount()>=capacity)
                return ResponseEntity.status(405).body(MeetingEnterPostRes.of(405, "Fail : meeting room is full"));
        }
        String token = meetingService.getToken(openVidu, userSeq, meeting);
        //tb_meeting_onair 에 칼럼추가
        meetingService.createOnair(userSeq, meetingSeq, isHost);
        //tb_meeting 의 meetingHeadcount ++
        meetingService.updateMeeting(meetingSeq, 1);

        if (token.equals("InternalError"))
            return ResponseEntity.status(409).body(MeetingEnterPostRes.of(409, "Fail : OpenViduJavaClientException", null));

        if (token.equals("GenError"))
            return ResponseEntity.status(409).body(MeetingEnterPostRes.of(409, "Fail : Generate meeting room", null));

        return ResponseEntity.ok(MeetingEnterPostRes.of(200, "Success : Entry meeting room", token, meeting, isHost));
    }

    @DeleteMapping("/{meeting-seq}/room")
    public ResponseEntity<? extends BaseResponseBody> removeUser(@PathVariable("meeting-seq") Integer meetingSeq, @RequestBody MeetingExitDeleteReq meetingExitDeleteReq, Authentication authentication) {

        GongUserDetails userDetails = (GongUserDetails) authentication.getDetails();
        Integer userSeq = Integer.parseInt((String) authentication.getPrincipal());

        System.out.println("Removing user | {sessionName, userSeq}=" + "{" + meetingSeq + "," + userSeq + "}");

        String sessionName = meetingSeq.toString();
        String token = meetingExitDeleteReq.getSessionToken();

        String result = meetingService.removeUser(sessionName, token);

        meetingService.deleteOnair(userSeq, meetingSeq);
        meetingService.updateMeeting(meetingSeq, -1);

        if("Error".equals(result))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseBody.of(409, "Fail : Remove user"));
        return ResponseEntity.ok(BaseResponseBody.of(200, "Success : Remove user"));
    }

}
