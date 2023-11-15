package com.example.astontest.core.dto.responsedto;

import com.example.astontest.core.dto.responsedto.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Класс обертка для обработки ошибок и успешных ответов во всех сервисах
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseResult {
    /**
     * Сообщение от сервера
     */
    private String message;

    /**
     * Статус выполнения операции {@link ResponseStatus}
     */
    private ResponseStatus status;

    /**
     * Тело ответа если в нем есть необходимость
     */
    private Object body;
}
