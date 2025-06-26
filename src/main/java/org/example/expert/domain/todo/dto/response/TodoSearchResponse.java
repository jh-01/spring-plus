package org.example.expert.domain.todo.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.expert.domain.todo.dto.request.TodoSearchRequest;

@Getter
@Setter
@Builder
public class TodoSearchResponse {
    private Long id;
    private String title;
    private Long managerNum;
    private Long commentNum;

    @QueryProjection
    public TodoSearchResponse(Long id, String title, Long managerNum, Long commentNum){
        this.id = id;
        this.title = title;
        this.managerNum = managerNum;
        this.commentNum = commentNum;
    }
}
