package org.superbiz.moviefun;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.superbiz.moviefun.albumsapi.AlbumClient;
import org.superbiz.moviefun.albumsapi.AlbumFixtures;
import org.superbiz.moviefun.albumsapi.AlbumInfo;
import org.superbiz.moviefun.moviesapi.MovieFixtures;
import org.superbiz.moviefun.moviesapi.MovieInfo;
import org.superbiz.moviefun.moviesapi.MoviesClient;

import java.util.Map;

@Controller
public class HomeController {

    private final MoviesClient moviesRepository;
    private final AlbumClient albumsRepository;
    private final MovieFixtures movieFixtures;
    private final AlbumFixtures albumFixtures;

    public HomeController(MoviesClient moviesRepository, AlbumClient albumsBean, MovieFixtures movieFixtures, AlbumFixtures albumFixtures) {
        this.moviesRepository = moviesRepository;
        this.albumsRepository = albumsBean;
        this.movieFixtures = movieFixtures;
        this.albumFixtures = albumFixtures;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/setup")
    public String setup(Map<String, Object> model) {
        for (MovieInfo movie : movieFixtures.load()) {
            moviesRepository.addMovie(movie);
        }

        for (AlbumInfo album : albumFixtures.load()) {
            albumsRepository.addAlbum(album);
        }

        model.put("movies", moviesRepository.getMovies());
        model.put("albums", albumsRepository.getAlbums());

        return "setup";
    }
}
