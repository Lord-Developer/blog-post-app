package lord.dev.controller;

import lord.dev.dto.request.PostRequest;
import lord.dev.dto.response.ApiResponse;
import lord.dev.dto.response.PostResponse;
import lord.dev.sevice.PostService;
import lord.dev.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest postRequest){
        return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        ApiResponse allPosts = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
        public ResponseEntity<?> getPostById(@PathVariable(name = "id") long id){
        return  ResponseEntity.ok(postService.getPostById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
        public ResponseEntity<?> updatePost(@Valid @RequestBody PostRequest postRequest, @PathVariable(name = "id") long id){

        ApiResponse apiResponse = postService.updatePost(postRequest, id);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name = "id") long id){
        ApiResponse apiResponse = postService.deletePostById(id);
        return new  ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
