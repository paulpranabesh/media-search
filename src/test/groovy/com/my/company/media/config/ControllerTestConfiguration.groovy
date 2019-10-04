package com.my.company.media.config

import com.my.company.media.configuration.AppProperties
import com.my.company.media.service.MediaAggregatorService
import com.my.company.media.service.album.AlbumRetrivalService
import com.my.company.media.service.book.BookRetrivalService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import spock.mock.DetachedMockFactory

/**
 * Created by prpaul on 9/30/2019.
 */
@TestConfiguration
public class  ControllerTestConfiguration {
    private final detachedMockFactory = new DetachedMockFactory()

    @Bean
    MediaAggregatorService mockMediaAggregatorService() {
        detachedMockFactory.Mock(MediaAggregatorService)
    }

    @Bean
    BookRetrivalService bookService() {
        detachedMockFactory.Mock(BookRetrivalService)
    }

    @Bean
    AlbumRetrivalService albumService() {
        detachedMockFactory.Mock(AlbumRetrivalService)
    }

    @Bean
    AppProperties config() {
        detachedMockFactory.Mock(AppProperties)
    }

    @Bean
    RestTemplate restTemplate() {
        detachedMockFactory.Mock(RestTemplate)
    }

}
