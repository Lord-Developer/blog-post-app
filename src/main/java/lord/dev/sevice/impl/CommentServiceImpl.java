package lord.dev.sevice.impl;

import lord.dev.dto.request.CommentRequest;
import lord.dev.dto.response.ApiResponse;
import lord.dev.dto.response.CommentResponse;
import lord.dev.model.Comment;
import lord.dev.model.Post;
import lord.dev.repository.CommentRepository;
import lord.dev.repository.PostRepository;
import lord.dev.sevice.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record CommentServiceImpl(CommentRepository commentRepository,
                                 PostRepository postRepository,
                                 ModelMapper mapper) implements CommentService {

    @Override
    public ApiResponse createComment(long postId, CommentRequest commentRequest) {
        Comment comment = mapper.map(commentRequest, Comment.class);
        Optional<Post> post = postRepository.findById(postId);
        if(post.isPresent()) {
            comment.setPost(post.get());
            commentRepository.save(comment);
            return new ApiResponse("The comment was created successfully.",true);
        }
        return new ApiResponse("No post with this { id = " + postId + "} was found.", false);
    }

    @Override
    public ApiResponse getCommentsByPostId(long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentResponse> commentResponseList = comments.stream().map(comment -> mapper.map(comment, CommentResponse.class)).collect(Collectors.toList());
        return new ApiResponse("Post Comments", true, commentResponseList);
    }

    @Override
    public ApiResponse getCommentById( long commentId) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent())
            return new ApiResponse("Comment", true, comment);
        return new ApiResponse("No comment with this {id = "+commentId+"} was found.", false);
    }

    @Override
    public ApiResponse updateComment(long commentId, CommentRequest commentRequest) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isPresent()){
            Comment comment = optionalComment.get();
            comment.setText(commentRequest.getText());
            commentRepository.save(comment);
            return new ApiResponse("The comment was updated successfully.", true);
        }
        return new ApiResponse("No comment with this {id = " + commentId + "} was found.", false);
    }

    @Override
    public ApiResponse deleteComment(long commentId) {
        boolean existsById = commentRepository.existsById(commentId);
        if(existsById){
            commentRepository.deleteById(commentId);
            return new ApiResponse("The comment was deleted successfully!", true );
        }
        return new ApiResponse("No comment with this {id = " + commentId + "} was found.", false);
    }

}
