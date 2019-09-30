package com.my.company.media.service.album

import com.my.company.media.configuration.AppProperties
import com.my.company.media.exception.ITunesApiCommunicationException
import com.my.company.media.model.dto.itunes.album.ITunesApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * Created by prpaul on 9/30/2019.
 */
class AlbumRetrivalServiceSpec extends Specification{

    private AlbumRetrivalService cut
    private AppProperties mockConfig
    private RestTemplate mockRestTemplate
    private def iTunesLink = 'https://itunes.apple.com/search?term={0}&music={1}&entity={2}&limit={3}&lang={4}'
    private actualUrl = 'https://itunes.apple.com/search?term=rock&music=albumTerm&entity=album&limit=4&lang=en_us'

    def setup() {
        cut =  new AlbumRetrivalService()
        mockConfig = Mock(AppProperties)
        mockRestTemplate = Mock(RestTemplate)
        cut.config = mockConfig
        cut.restTemplate = mockRestTemplate
    }

    def "passing null search key, retrieveAlbum throws proper exception"(){
        when:
        cut.retrieveAlbum(null)

        then:
        Exception e = thrown()
        e.message == "'searchKey' cannot be null or empty"

        when:
        cut.retrieveAlbum('')

        then:
        e = thrown()
        e.message == "'searchKey' cannot be null or empty"
    }

    def "Given proper search key, retrieveAlbum returns proper data"(){
        given:
        def mockResponse = Mock(ITunesApiResponse)

        when:
        def result = cut.retrieveAlbum('rock')

        then:
        result == mockResponse
        1 * mockConfig.getiTunesAlbumApi() >> iTunesLink
        1 * mockConfig.getResultSize() >> 4
        1 * mockRestTemplate.getForObject(actualUrl, ITunesApiResponse.class) >> mockResponse
    }

    def "Given external host not reachable , retrieveAlbum throws ITunesApiCommunicationException exception"(){
        when:
        cut.retrieveAlbum('rock')

        then:
        ITunesApiCommunicationException e = thrown()
        e.message == "Unable to retrieve albums from iTunes music Api."
        1 * mockConfig.getiTunesAlbumApi() >> iTunesLink
        1 * mockConfig.getResultSize() >> 4
        1 * mockRestTemplate.getForObject(actualUrl, ITunesApiResponse.class) >> {throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY)}
    }

    def "Given communication error with iTune api, retrieveAlbum throws ITunesApiCommunicationException exception"(){
        when:
        cut.retrieveAlbum('rock')

        then:
        ITunesApiCommunicationException e = thrown()
        e.message == "Communication error with iTunes Api for Music."
        1 * mockConfig.getiTunesAlbumApi() >> iTunesLink
        1 * mockConfig.getResultSize() >> 4
        1 * mockRestTemplate.getForObject(actualUrl, ITunesApiResponse.class) >> {throw new Exception('Comm error')}
    }

}
