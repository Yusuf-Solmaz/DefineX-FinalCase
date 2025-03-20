package com.yms.task_service.exception.root_exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RootException{
    private int status;
    private String error;
    private Long timeStamp=System.currentTimeMillis();
}
