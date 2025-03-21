package com.yms.comment_service.service.abstracts;

import com.yms.comment_service.dto.response.TaskResponse;


public interface TaskClientService {
    TaskResponse findTaskById(Integer id,String token);
}
