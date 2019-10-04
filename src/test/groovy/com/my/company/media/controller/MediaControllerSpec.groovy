package com.my.company.media.controller

import com.my.company.media.config.ControllerTestConfiguration
import com.my.company.media.exception.NoMediaFoundException
import com.my.company.media.model.dto.Album
import com.my.company.media.model.dto.Book
import com.my.company.media.service.MediaAggregatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import spock.lang.Specification

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Created by prpaul on 9/30/2019.
 */

@WebMvcTest
@Import([ControllerTestConfiguration])
class MediaControllerSpec extends Specification  {

    @Autowired MockMvc mvc

    @Autowired
    MediaAggregatorService aggregatorService

    def 'Given authentication provided, /media/list/physics should return proper error message'() {
        when:
        mvc.perform(get("/media/list/physics"))
                .andExpect(status().is4xxClientError()).andReturn()

        then:
        0 * aggregatorService.searchMedia('physics')
    }

    def 'Given no media data found, /media/list/physics should return proper error message'() {
        when:
        mvc.perform(get("/media/list/physics").with(httpBasic("admin", "password")))
                .andExpect(status().is4xxClientError()).andReturn()

        then:
        1 * aggregatorService.searchMedia('physics') >> {throw new NoMediaFoundException('some error')}
    }

    def 'Given some media data found, /media/list/physics should return proper content'() {
        when:
        MvcResult mvcResult = mvc.perform(get("/media/list/physics").with(httpBasic("admin", "password")))
                .andExpect(status().is2xxSuccessful()).andReturn()

        then:
        1 * aggregatorService.searchMedia('physics') >> [new Book(['Taninbum'], 'some title'), new Album('pink floyd', 'shine on crazy diamond')]
        mvcResult.response.contentAsString == '''[{"type":"Book","title":"some title","authors":["Taninbum"]},{"type":"Album","title":"shine on crazy diamond","artist":"pink floyd"}]'''
    }

}
