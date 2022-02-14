package com.gongsp.api.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface LogTimeService {
    void createLogTime(Integer userSeq);
    boolean existsLog(Integer userSeq, LocalDate date);
    void updateMeetingLogTime(Integer userSeq, Integer logMeeting, LocalTime logStart);
    void updateStudyLogTime(Integer userSeq, Integer logStudy, LocalTime logStartTime);
    Integer getTotalTime();
    LocalTime getEndTime(Integer userSeq, LocalDate today);
}
