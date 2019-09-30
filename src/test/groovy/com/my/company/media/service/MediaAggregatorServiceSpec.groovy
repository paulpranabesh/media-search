package com.my.company.media.service

import com.my.company.media.exception.BookApiCommunicationException
import com.my.company.media.exception.ITunesApiCommunicationException
import com.my.company.media.exception.NoMediaFoundException
import com.my.company.media.model.dto.Media
import com.my.company.media.model.dto.google.book.BookApiResponse
import com.my.company.media.model.dto.google.book.Item
import com.my.company.media.model.dto.google.book.VolumeInfo
import com.my.company.media.model.dto.itunes.album.ITunesAlbum
import com.my.company.media.model.dto.itunes.album.ITunesApiResponse
import com.my.company.media.service.album.AlbumRetrivalService
import com.my.company.media.service.book.BookRetrivalService
import spock.lang.Specification

/**
 * Created by prpaul on 9/30/2019.
 */
class MediaAggregatorServiceSpec extends Specification{

    private MediaAggregatorService cut
    private BookRetrivalService mockBookService
    private AlbumRetrivalService mockAlbumService

    def setup() {
        cut =  new MediaAggregatorService()
        mockBookService = Mock(BookRetrivalService)
        mockAlbumService = Mock(AlbumRetrivalService)
        cut.bookService = mockBookService
        cut.albumService = mockAlbumService
    }

    def "passing null search key, searchMedia throws proper exception"(){
        when:
        cut.searchMedia(null)

        then:
        Exception e = thrown()
        e.message == "'searchKey' cannot be null or empty"

        when:
        cut.searchMedia('')

        then:
        e = thrown()
        e.message == "'searchKey' cannot be null or empty"
    }

    def "Given google api and itunes respond, searchMedia returns proper sorted information"(){
        setup:
        def volumnInfo = new VolumeInfo(authors: ['Taninbum'], title:'Algorithm')
        def item = new Item(volumeInfo: volumnInfo)
        def bookApiResponse = new BookApiResponse(items:[item])
        def iTuneResponse = new ITunesApiResponse(results : [new ITunesAlbum(artist: 'Elvis', collectionName: 'Some Song')])

        when:
        List<Media> result = cut.searchMedia('rock')

        then:
        1 * mockBookService.retrieveBook('rock') >> bookApiResponse
        1 * mockAlbumService.retrieveAlbum('rock') >> iTuneResponse
        result.size() == 2
        result[0].title == 'Algorithm'
        result[1].title == 'Some Song'
    }

    def "Given google api does not respond, searchMedia returns proper information only from iTunes"(){
        setup:
        def iTuneResponse = new ITunesApiResponse(results : [new ITunesAlbum(artist: 'Elvis', collectionName: 'Some Song')])

        when:
        List<Media> result = cut.searchMedia('rock')

        then:
        1 * mockBookService.retrieveBook('rock') >> {throw new BookApiCommunicationException('some error', Mock(Exception));}
        1 * mockAlbumService.retrieveAlbum('rock') >> iTuneResponse
        result.size() == 1
        result[0].title == 'Some Song'
    }

    def "Given iTunes api does not respond, searchMedia returns proper information only from google book"(){
        setup:
        def volumnInfo = new VolumeInfo(authors: ['Taninbum'], title:'Algorithm')
        def item = new Item(volumeInfo: volumnInfo)
        def bookApiResponse = new BookApiResponse(items:[item])

        when:
        List<Media> result = cut.searchMedia('rock')

        then:
        1 * mockBookService.retrieveBook('rock') >> bookApiResponse
        1 * mockAlbumService.retrieveAlbum('rock') >> {throw new ITunesApiCommunicationException('some error', Mock(Exception));}
        result.size() == 1
        result[0].title == 'Algorithm'
    }

    def "Given none of iTunes and google api respond, searchMedia throws NoMediaFoundException"(){
        when:
        cut.searchMedia('rock')

        then:
        1 * mockBookService.retrieveBook('rock') >> {throw new BookApiCommunicationException('some error', Mock(Exception));}
        1 * mockAlbumService.retrieveAlbum('rock') >> {throw new ITunesApiCommunicationException('some error', Mock(Exception));}
        NoMediaFoundException e = thrown()
        e.message == "Unable to retrieve informations from external apis with search key :rock"
    }

    def "Given google api and itunes respond with empty result, searchMedia throws NoMediaFoundException"(){
        when:
        cut.searchMedia('rock')

        then:
        1 * mockBookService.retrieveBook('rock') >> new BookApiResponse()
        1 * mockAlbumService.retrieveAlbum('rock') >> new ITunesApiResponse()
        NoMediaFoundException e = thrown()
        e.message == "Unable to retrieve informations from external apis with search key :rock"
    }
}
