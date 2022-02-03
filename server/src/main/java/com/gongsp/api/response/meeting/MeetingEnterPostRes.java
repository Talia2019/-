package com.gongsp.api.response.meeting;

import com.gongsp.common.model.response.BaseResponseBody;
import com.gongsp.db.entity.Meeting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingEnterPostRes extends BaseResponseBody {
    private String sessionToken;
    private Integer meetingSeq;
    private Boolean isHost;
    private String meetingTitle;
    private String meetingDesc;
    private Integer meetingCapacity;
    private Integer meetingHeadcount;

    public static MeetingEnterPostRes of(Integer statusCode, String message, String sessionToken) {
        MeetingEnterPostRes res = new MeetingEnterPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setSessionToken(sessionToken);
        return res;
    }

    public static MeetingEnterPostRes of(Integer statusCode, String message, String sessionToken, Meeting meeting, Boolean isHost) {
        MeetingEnterPostRes res = new MeetingEnterPostRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setSessionToken(sessionToken);
        res.setMeetingSeq(meeting.getMeetingSeq());
        res.setIsHost(isHost);
        res.setMeetingTitle(meeting.getMeetingTitle());
        res.setMeetingDesc(meeting.getMeetingDesc());
        res.setMeetingCapacity(meeting.getMeetingCapacity());
        res.setMeetingHeadcount(meeting.getMeetingHeadcount());
        return res;
    }
}
