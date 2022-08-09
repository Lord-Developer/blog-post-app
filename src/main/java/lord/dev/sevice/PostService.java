package lord.dev.sevice;

import lord.dev.dto.request.PostRequest;
import lord.dev.dto.response.ApiResponse;

public interface PostService {

    ApiResponse createPost(PostRequest postRequest);

    ApiResponse getAllPosts(int pageNo , int pageSize, String sortBy, String sortDir);

    ApiResponse getPostById( long id);

    ApiResponse updatePost(PostRequest postRequest, long id);

    ApiResponse deletePostById(long id);
}
