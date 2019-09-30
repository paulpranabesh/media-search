package com.my.company.media.service;

import com.my.company.media.exception.BookApiCommunicationException;
import com.my.company.media.exception.ITunesApiCommunicationException;
import com.my.company.media.exception.NoMediaFoundException;
import com.my.company.media.model.dto.Album;
import com.my.company.media.model.dto.Book;
import com.my.company.media.model.dto.Media;
import com.my.company.media.model.dto.google.book.BookApiResponse;
import com.my.company.media.model.dto.google.book.VolumeInfo;
import com.my.company.media.model.dto.itunes.album.ITunesApiResponse;
import com.my.company.media.service.album.AlbumRetrivalService;
import com.my.company.media.service.book.BookRetrivalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.Validate.notEmpty;

/**
 * Created by prpaul on 9/26/2019.
 */
@Service
public class MediaAggregatorService {
    private final Logger logger = LoggerFactory.getLogger(MediaAggregatorService.class);
    @Autowired
    private BookRetrivalService bookService;

    @Autowired
    private AlbumRetrivalService albumService;

    @Cacheable(cacheNames="mediaCache", key="#searchKey")
    public List<Media> searchMedia(final String searchKey) throws NoMediaFoundException{
        notEmpty(searchKey, "'searchKey' cannot be null or empty");
        List<Media> mediaInformations = new LinkedList<Media>();
        boolean bookRetrivalSuccessful = populateBooks(mediaInformations, searchKey);
        boolean albumRetrivalSuccessful = populateAlbums(mediaInformations, searchKey);
        if(bookRetrivalSuccessful || albumRetrivalSuccessful){
            List<Media> result = mediaInformations.stream().sorted(Comparator.comparing(Media::getTitle)).collect(Collectors.<Media>toList());
            if(!CollectionUtils.isEmpty(result)){
                return result;
            }
        }
        String errorMsg = "Unable to retrieve informations from external apis with search key :"+searchKey;
        logger.error(errorMsg);
        throw new NoMediaFoundException(errorMsg);
    }

    private boolean populateAlbums(List<Media> mediaInformations, String searchKey){
        try{
            ITunesApiResponse response = albumService.retrieveAlbum(searchKey);
            mediaInformations.addAll(convertItunesResponse(response));
            return true;
        }catch(ITunesApiCommunicationException e){
            logger.error("Unable to retrieve albums from itunes music api with search key :"+searchKey ,e);
            return false;
        }
    }

    private boolean populateBooks(List<Media> mediaInformations, String searchKey){
        try{
            BookApiResponse bookApiResponse = bookService.retrieveBook(searchKey);
            mediaInformations.addAll(convertGoogleBookResponse(bookApiResponse));
            return true;
        }catch(BookApiCommunicationException e){
            logger.error("Unable to retrieve books from google book api with search key :"+searchKey ,e);
            return false;
        }
    }

    private List<Book> convertGoogleBookResponse(BookApiResponse bookApiResponse){
        List<Book> books = new ArrayList<Book>();
        bookApiResponse.getItems().forEach(it -> {
            Optional<VolumeInfo> volumnInfo = Optional.ofNullable(it.getVolumeInfo());
            volumnInfo.ifPresent(vol -> {
                Book book = new Book(vol.getAuthors(), vol.getTitle());
                books.add(book);
            });

        });
        return books;
    }

    private List<Album> convertItunesResponse(ITunesApiResponse response){
        List<Album> albums = new ArrayList<Album>();
        response.getResults().forEach(it ->{
            Album album = new Album(it.getArtist(), it.getCollectionName());
            albums.add(album);
        });
        return albums;
    }

}
