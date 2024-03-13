package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
public record MsgDTO(
        int result, // 성공 : 1 / 실패 : 0
        String msg // 메시지
) {

}
