package lord.dev.sevice;

import lord.dev.dto.request.CommentRequest;
import lord.dev.dto.response.ApiResponse;

public interface CommentService {

    ApiResponse createComment (long postId, CommentRequest commentRequest);

    ApiResponse getCommentsByPostId(long postId);

    ApiResponse getCommentById(long commentId);

    ApiResponse updateComment(long commentId , CommentRequest commentRequest);

    ApiResponse deleteComment( long commentId);
}
