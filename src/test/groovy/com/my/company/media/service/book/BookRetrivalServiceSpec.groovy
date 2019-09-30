package com.my.company.media.service.book

import com.my.company.media.configuration.AppProperties
import com.my.company.media.exception.BookApiCommunicationException
import com.my.company.media.model.dto.google.book.BookApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * Created by prpaul on 9/30/2019.
 */
class BookRetrivalServiceSpec extends Specification {

    private BookRetrivalService cut
    private AppProperties mockConfig
    private RestTemplate mockRestTemplate
    private def googleBookApi = 'https://www.googleapis.com/books/v1/volumes?q={0}&langRestrict={1}&maxResults={2}&orderBy={3}'
    private def actualUrl = 'https://www.googleapis.com/books/v1/volumes?q=rock&langRestrict=english&maxResults=4&orderBy=relevance'

    def setup() {
        cut =  new BookRetrivalService()
        mockConfig = Mock(AppProperties)
        mockRestTemplate = Mock(RestTemplate)
        cut.config = mockConfig
        cut.restTemplate = mockRestTemplate
    }

    def "passing null search key, retrieveBook throws proper exception"(){
        when:
        cut.retrieveBook(null)

        then:
        Exception e = thrown()
        e.message == "'searchKey' cannot be null or empty"

        when:
        cut.retrieveBook('')

        then:
        e = thrown()
        e.message == "'searchKey' cannot be null or empty"
    }

    def "Given proper search key, retrieveAlbum returns proper data"(){
        given:
        def mockResponse = Mock(BookApiResponse)

        when:
        def result = cut.retrieveBook('rock')

        then:
        result == mockResponse
        1 * mockConfig.getGoogleBookApi() >> googleBookApi
        1 * mockConfig.getResultSize() >> 4
        1 * mockRestTemplate.getForObject(actualUrl, BookApiResponse.class) >> mockResponse
    }

    def "Given external host not reachable , retrieveBook throws BookApiCommunicationException exception"(){
        when:
        cut.retrieveBook('rock')

        then:
        BookApiCommunicationException e = thrown()
        e.message == "Unable to retrieve book informations from Google Book Api."
        1 * mockConfig.getGoogleBookApi() >> googleBookApi
        1 * mockConfig.getResultSize() >> 4
        1 * mockRestTemplate.getForObject(actualUrl, BookApiResponse.class) >> {throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY)}
    }

    def "Given communication error with google book api, retrieveAlbum throws BookApiCommunicationException exception"(){
        when:
        cut.retrieveBook('rock')

        then:
        BookApiCommunicationException e = thrown()
        e.message == "Communication error with Google Book Api."
        1 * mockConfig.getGoogleBookApi() >> googleBookApi
        1 * mockConfig.getResultSize() >> 4
        1 * mockRestTemplate.getForObject(actualUrl, BookApiResponse.class) >> {throw new Exception('Comm error')}
    }
}
