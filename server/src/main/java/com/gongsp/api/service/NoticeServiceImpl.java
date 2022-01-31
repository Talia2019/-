package com.gongsp.api.service;

import com.gongsp.db.entity.Notice;
import com.gongsp.db.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    NoticeRepository noticeRepository;

    @Override
    public List<Notice> findByUserSeq(Integer userSeq, Integer page, Integer size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("noticeDate").descending());
        Page<Notice> noticePage = noticeRepository.findByUserSeq(userSeq, paging);
        List<Notice> noticeList = noticePage.getContent();
        return noticeList;
    }
}
