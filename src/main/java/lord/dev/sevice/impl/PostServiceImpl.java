package lord.dev.sevice.impl;

import lord.dev.dto.request.PostRequest;
import lord.dev.dto.response.ApiResponse;
import lord.dev.dto.response.PostResponse;
import lord.dev.exception.ResourceNotFoundException;
import lord.dev.model.Post;
import lord.dev.repository.PostRepository;
import lord.dev.sevice.PostService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public record PostServiceImpl(ModelMapper mapper,
                              PostRepository postRepository) implements PostService {

    @Override
    public ApiResponse createPost(PostRequest postRequest) {
        boolean existsByTitle = postRepository.existsByTitle(postRequest.getTitle());
        if (!existsByTitle){
            Post post = mapper.map(postRequest, Post.class);
            postRepository.save(post);
            return new ApiResponse("The post was created successfully.",true);
        }
        return new ApiResponse("There is already a post with this title.", false);
    }

    @Override
    public ApiResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        //create pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Post> posts= postRepository.findAll(pageable);

        //get content for page object
        List<Post> listOfPosts = posts.getContent();

        List<PostRequest> content =  listOfPosts.stream().map(post->mapper.map(post, PostRequest.class)).collect(Collectors.toList());

        //create postResponse object to return
        PostResponse postResponse = new PostResponse();

        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return new ApiResponse("Post List", true, postResponse);
    }

    @Override
    public ApiResponse getPostById(long id) {
        Post post = getPostByIdFromDatabase(id);
        PostRequest postRequest = mapper.map(post, PostRequest.class);
        return new ApiResponse("Post", true, postRequest);
    }

    @Override
    public ApiResponse updatePost(PostRequest postRequest, long id) {
        if(postRepository.existsByTitle(postRequest.getTitle()))
            return new ApiResponse("There is already a post with this title.", false);
        Optional<Post> optionalPost = postRepository.findById(id);
        Post post;
        if(optionalPost.isPresent()){
            post = optionalPost.get();
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            postRequest.setDescription(postRequest.getDescription());
            postRepository.save(post);
            return new ApiResponse("The post was updated successfully.", true);
        }
        return new ApiResponse("The post was not found with " + id +".", false );
    }

    @Override
    public ApiResponse deletePostById(long id) {
        if(postRepository().existsById(id)) {
            postRepository.deleteById(id);
            return new ApiResponse("The post was deleted successfully.", true);
        }
         else throw  new ResourceNotFoundException("PostId " + id + " not found");

    }

    private Post getPostByIdFromDatabase(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
    }


}
