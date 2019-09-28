package org.superbiz.moviefun.albums;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumsController {

    private AlbumsRepository albumsRepository;

    public AlbumsController(AlbumsRepository repository) {
        this.albumsRepository = repository;
    }

    @PostMapping
    public void addAlbum(@RequestBody Album album) {
        this.albumsRepository.addAlbum(album);
    }

    @DeleteMapping("/{albumId")
    public void deleteAlbumId(@PathVariable Long albumId) {
        this.albumsRepository.deleteAlbumId(albumId);
    }

    @GetMapping("/count")
    public int count(@RequestParam(name = "field", required = false) String field,
                     @RequestParam(name = "key", required = false) String key) {
        if (field != null && key != null) {
            return albumsRepository.count(field, key);
        } else {
            return albumsRepository.countAll();
        }
    }

    @GetMapping("/{album_id}")
    public Album findById(@PathVariable("album_id") Long albumId) {
        return albumsRepository.find(albumId);
    }

    @GetMapping
    public List<Album> find (@RequestParam(name = "field", required = false) String field,
                             @RequestParam(name = "key", required = false) String key,
                             @RequestParam(name = "start", required = false) Integer start,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        if (field != null && key != null) {
            return albumsRepository.findRange(field, key, start, pageSize);
        } else if (start != null && pageSize != null) {
            return albumsRepository.findAll(start, pageSize);
        } else {
            return albumsRepository.getAlbums();
        }

    }
}
