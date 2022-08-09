package lord.dev.dto.response;

import lombok.*;
import lord.dev.dto.request.PostRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private List<PostRequest> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}