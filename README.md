# media-search
Sample Spring boot application to search media in google book api and in iTunes api.

It will return maximum of 5 books and maximum of 5 albums that are related to the input term. The response
elements will only contain title, authors(/artists) and information whether a book or an album.

For albums we can use the iTunes API: 
https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/#searching

For books we can use Google Books API:
https://developers.google.com/books/docs/v1/reference/volumes/list 

Sort the result by title alphabetically.

The stability of the downstream service may not be affected by the stability of the upstream services.
Results originating from one upstream service (and its stability / performance) may not affect the results originating from the other upstream service. 
The service needs to respond within a minute;

The service:
 is self-documenting
 exposes metrics on response times for upstream services
 exposes health check

Limit of results on upstream services must be configurable per environment and preconfigured to 5.


Sample request : localhost:8080/media/list/jazz     
   (with BasicAuth password admin/password)
   Add in Http Header    Authorization/Basic YWRtaW46cGFzc3dvcmQ=

'''The response is cached based on key.'''



response 

[
    {
        "type": "Album",
        "title": "50 Gospel Jazz Classics",
        "artist": "Smooth Jazz All Stars"
    },
    {
        "type": "Album",
        "title": "50 Smooth Jazz Classics",
        "artist": "Smooth Jazz All Stars"
    },
    {
        "type": "Book",
        "title": "Annual review of jazz studies",
        "authors": [
            "Rutgers University. Institute of Jazz Studies"
        ]
    },
    {
        "type": "Album",
        "title": "Ella & Friends",
        "artist": "Ella Fitzgerald"
    },
    {
        "type": "Album",
        "title": "Ella & Louis for Lovers",
        "artist": "Ella Fitzgerald & Louis Armstrong"
    },
    {
        "type": "Album",
        "title": "Ella Fitzgerald Sings the Cole Porter Songbook (Expanded Edition)",
        "artist": "Ella Fitzgerald"
    },
    {
        "type": "Book",
        "title": "Jazz",
        "authors": [
            "Morley Jones"
        ]
    },
    {
        "type": "Book",
        "title": "Jazz Music",
        "authors": [
            "Max Jones"
        ]
    },
    {
        "type": "Book",
        "title": "Love, Life and All That Jazz",
        "authors": [
            "Ahmed Faiyaz"
        ]
    },
    {
        "type": "Book",
        "title": "The Story of Jazz",
        "authors": [
            "Marshall Winslow Stearns"
        ]
    }
]


Metric to see how long the external API takes in average milli second it takes.

request  http://localhost:8080/actuator/external-api-response
 
  (with BasicAuth password admin/password)
   Add in Http Header    Authorization/Basic YWRtaW46cGFzc3dvcmQ=
   
response 

{
    "GOOGLE_BOOK_API": 1936,
    "ITUNES_MUSIC_API": 2032
}

