package cotato.timetile.domain.search.application;

import cotato.timetile.domain.artist.domain.ArtistDocument;
import cotato.timetile.domain.artist.persistence.ArtistDocumentRepository;
import cotato.timetile.domain.event.domain.Event;
import cotato.timetile.domain.event.domain.EventDocument;
import cotato.timetile.domain.event.persistence.EventDocumentRepository;
import cotato.timetile.domain.event.persistence.EventRepository;
import cotato.timetile.domain.post.domain.Post;
import cotato.timetile.domain.post.domain.PostDocument;
import cotato.timetile.domain.post.persistence.PostDocumentRepository;
import cotato.timetile.domain.post.persistence.PostRepository;
import cotato.timetile.domain.search.api.dto.MainSearchArtistDto;
import cotato.timetile.domain.search.api.dto.MainSearchEventDto;
import cotato.timetile.domain.search.api.dto.MainSearchPostDto;
import cotato.timetile.domain.search.api.dto.MainSearchPostImageDto;
import cotato.timetile.domain.search.api.dto.MainSearchUserDto;
import cotato.timetile.domain.search.api.response.MainSearchAllResponse;
import cotato.timetile.domain.search.api.response.MainSearchArtistResponse;
import cotato.timetile.domain.search.api.response.MainSearchEventResponse;
import cotato.timetile.domain.search.api.response.MainSearchPostImageResponse;
import cotato.timetile.domain.search.api.response.MainSearchPostResponse;
import cotato.timetile.domain.search.api.response.MainSearchUserResponse;
import cotato.timetile.domain.user.domain.User;
import cotato.timetile.domain.user.domain.UserDocument;
import cotato.timetile.domain.user.persistence.UserDocumentRepository;
import cotato.timetile.domain.user.persistence.UserRepository;
import cotato.timetile.global.handler.S3Handler;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ArtistDocumentRepository artistDocumentRepository;
    private final EventRepository eventRepository;
    private final EventDocumentRepository eventDocumentRepository;
    private final UserRepository userRepository;
    private final UserDocumentRepository userDocumentRepository;
    private final PostRepository postRepository;
    private final PostDocumentRepository postDocumentRepository;
    private final S3Handler s3Handler;

    @Transactional(readOnly = true)
    public MainSearchAllResponse searchAll(String query) {
        int ARTIST_MAX_SIZE = 5;
        int POST_MAX_SIZE = 3;
        int EVENT_MAX_SIZE = 5;
        int USER_MAX_SIZE = 5;

        List<ArtistDocument> artistDocuments = artistDocumentRepository.findAllByNameMatches(query);
        int artistCount = artistDocuments.size();
        List<ArtistDocument> artists = artistDocuments.stream().limit(ARTIST_MAX_SIZE).toList();

        List<Long> userIds = userDocumentRepository.findAllByNicknameMatches(query).stream()
                .map(UserDocument::getId)
                .map(Long::parseLong)
                .toList();
        int userCount = userIds.size();
        List<User> users = userRepository.findAllById(userIds.stream().limit(USER_MAX_SIZE).toList());

        List<Long> postIds = postDocumentRepository.findAllByTitleMatchesOrContentMatches(query, query)
                .stream()
                .map(PostDocument::getId)
                .map(Long::parseLong)
                .toList();
        int postCount = postIds.size();
        List<Post> posts = postRepository.findAllById(postIds.stream().limit(POST_MAX_SIZE).toList());

        List<String> eventGroupIds = eventDocumentRepository.findAllByNameMatchesOrArtistNameMatches(query, query)
                .stream()
                .map(EventDocument::getGroupId)
                .toList();
        int eventCount = eventGroupIds.size();
        List<Event> events = eventRepository.findAllByGroupIdAndActiveIsTrueAndLatest(
                eventGroupIds.stream().limit(EVENT_MAX_SIZE).toList());

        return MainSearchAllResponse.of(
                artistCount,
                artists.stream().map(MainSearchArtistDto::of).toList(),
                userCount,
                users.stream()
                        .map(user -> MainSearchUserDto.of(user, s3Handler.getSimpleLogoUrlIfNull(user.getImageKey())))
                        .toList(),
                postCount,
                posts.stream().map(post -> MainSearchPostDto.of(post,
                                s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                s3Handler.getSimpleLogoUrlIfNull(post.getArtist().getImageUrl())))
                        .toList(),
                eventCount,
                events.stream().map(MainSearchEventDto::of).toList()
        );
    }

    @Transactional(readOnly = true)
    public MainSearchArtistResponse searchArtists(int page, String query) {
        int ARTIST_PAGE_SIZE = 20;
        PageRequest pageable = PageRequest.of(page - 1, ARTIST_PAGE_SIZE);
        Page<ArtistDocument> artistPage = artistDocumentRepository.findAllByNameMatches(query, pageable);
        List<MainSearchArtistDto> artists = artistPage.stream()
                .map(MainSearchArtistDto::of)
                .toList();
        return MainSearchArtistResponse.of(artists, artistPage);
    }

    @Transactional(readOnly = true)
    public MainSearchPostResponse searchPosts(int page, String query) {
        int POST_PAGE_SIZE = 14;
        PageRequest pageable = PageRequest.of(page - 1, POST_PAGE_SIZE, Sort.by("id").descending());
        Page<PostDocument> postPage = postDocumentRepository.findAllByTitleMatchesOrContentMatches(query, query,
                pageable);
        List<Long> postIds = postPage.stream().map(PostDocument::getId).map(Long::parseLong).toList();
        List<Post> posts = postRepository.findAllById(postIds);
        posts.sort(Comparator.comparing(Post::getId).reversed());
        return MainSearchPostResponse.of(
                posts.stream()
                        .map(post -> MainSearchPostDto.of(
                                post,
                                s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey()),
                                s3Handler.getSimpleLogoUrlIfNull(post.getAuthor().getImageKey())))
                        .toList(),
                postPage
        );
    }

    @Transactional(readOnly = true)
    public MainSearchPostImageResponse searchPostImages(Long lastPostId, String query) {
        int POST_SLICE_SIZE = 14;
        List<Long> postIds = postDocumentRepository.findAllByTitleMatchesOrContentMatchesAndIdLessThanOrderByIdDesc(
                        query, query, lastPostId.toString()).stream()
                .map(PostDocument::getId)
                .map(Long::parseLong)
                .toList();
        boolean hasNext = postIds.size() > POST_SLICE_SIZE;
        postIds = hasNext ? postIds.subList(0, POST_SLICE_SIZE) : postIds;
        List<Post> posts = postRepository.findAllById(postIds);
        Optional<Post> lastPostOpt = posts.isEmpty() ? Optional.empty() : Optional.of(posts.get(posts.size() - 1));
        return MainSearchPostImageResponse.of(
                posts.stream()
                        .map(post -> MainSearchPostImageDto.of(
                                post,
                                s3Handler.getSimpleLogoUrlIfNull(post.getMainImageKey())))
                        .toList(),
                hasNext,
                lastPostOpt.map(Post::getId).orElse(null)
        );
    }

    @Transactional(readOnly = true)
    public MainSearchEventResponse searchEvents(int page, String query) {
        int EVENT_PAGE_SIZE = 16;
        PageRequest pageable = PageRequest.of(page - 1, EVENT_PAGE_SIZE, Sort.by("startedAt").descending());
        Page<EventDocument> eventPage = eventDocumentRepository.findAllByNameMatchesOrArtistNameMatches(
                query, query, pageable);
        List<Long> eventIds = eventPage.stream().map(EventDocument::getId).map(Long::parseLong).toList();
        List<Event> events = eventRepository.findAllById(eventIds);
        events.sort(Comparator.comparing(Event::getStartedAt).reversed());
        return MainSearchEventResponse.of(
                events.stream()
                        .map(MainSearchEventDto::of)
                        .toList(),
                eventPage
        );
    }

    @Transactional(readOnly = true)
    public MainSearchUserResponse searchUsers(int page, String query) {
        int USER_PAGE_SIZE = 20;
        PageRequest pageable = PageRequest.of(page - 1, USER_PAGE_SIZE);
        Page<UserDocument> userPage = userDocumentRepository.findAllByNicknameMatches(query, pageable);
        List<Long> userIds = userPage.stream().map(UserDocument::getId).map(Long::parseLong).toList();
        List<User> users = userRepository.findAllById(userIds);
        return MainSearchUserResponse.of(
                users.stream()
                        .map(user -> MainSearchUserDto.of(
                                user,
                                s3Handler.getSimpleLogoUrlIfNull(user.getImageKey())))
                        .toList(),
                userPage
        );
    }

}
