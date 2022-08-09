package lord.dev.controller;


import lord.dev.dto.request.CommentRequest;
import lord.dev.dto.response.ApiResponse;
import lord.dev.sevice.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable(value = "postId") long postId,
                                                    @Valid  @RequestBody CommentRequest commentRequest) {
        ApiResponse response = commentService.createComment(postId, commentRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("forPost/{postId}")
    public ResponseEntity<?> getCommentsByPostId(@PathVariable(value = "postId") long postId) {

        ApiResponse response = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<?> getCommentById(@PathVariable(value = "commentId") long commentId) {
        ApiResponse response = commentService.getCommentById( commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable(value = "commentId") long commentId,
                                                    @Valid @RequestBody CommentRequest commentRequest) {
        ApiResponse apiResponse = commentService.updateComment( commentId, commentRequest);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment( @PathVariable(value = "commentId") long commentId) {

        ApiResponse apiResponse = commentService.deleteComment(commentId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
