package eu.kanade.tachiyomi.animeextension.en.fouranime

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.SAnime
import eu.kanade.tachiyomi.source.model.SEpisode
import eu.kanade.tachiyomi.source.online.ParsedAnimeHttpSource
import okhttp3.Request
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class FourAnime : ParsedAnimeHttpSource() {

    override val name = "4anime.to"

    override val baseUrl = "https://4anime.to"

    override val lang = "en"

    override val supportsLatest = true

    override fun popularAnimeSelector(): String = "#headerDIV_4"

    override fun popularAnimeRequest(page: Int): Request = GET("$baseUrl/popular-this-week/page/$page")

    override fun popularAnimeFromElement(element: Element): SAnime {
        val anime = SAnime.create()
        anime.setUrlWithoutDomain(element.select("#headerA_5").first().attr("href"))
        anime.thumbnail_url = element.select("#headerIMG_6").first().attr("src")
        anime.title = element.select("#headerIMG_6").first().attr("title")
        return anime
    }

    override fun popularAnimeNextPageSelector(): String? = "a.nextpostslink"

    override fun episodeListSelector() = "ul.episodes.active.range li a"

    override fun episodeFromElement(element: Element): SEpisode {
        val episode = SEpisode.create()
        episode.setUrlWithoutDomain(element.attr("href"))
        episode.episode_number = element.text().toFloat()
        episode.name = "Episode " + element.text()
        return episode
    }

    override fun episodeLinkSelector() = "source"

    override fun linkFromElement(element: Element): String {
        return element.attr("src")
    }

    override fun searchAnimeFromElement(element: Element): SAnime {
        val anime = SAnime.create()
        anime.setUrlWithoutDomain(element.select("a").attr("href"))
        anime.thumbnail_url = element.select("img").first().attr("src")
        anime.title = element.select("div div").first().text()
        return anime
    }

    override fun searchAnimeNextPageSelector(): String = "a.nextpostslink"

    override fun searchAnimeSelector(): String = "#headerDIV_95"

    override fun searchAnimeRequest(page: Int, query: String, filters: FilterList): Request = GET("$baseUrl/?s=$query")

    override fun animeDetailsParse(document: Document): SAnime {
        val anime = SAnime.create()
        anime.title = document.select("p.single-anime-desktop").text()
        anime.genre = document.select("div.tag a").joinToString(", ") { it.text() }
        anime.description = document.select("div#description-mob p[class!=description-mobile]").text()
        return anime
    }

    override fun latestUpdatesNextPageSelector(): String = "a.nextpostslink"

    override fun latestUpdatesFromElement(element: Element): SAnime {
        val anime = SAnime.create()
        anime.setUrlWithoutDomain("https://4anime.to/anime" + element.select("#headerA_5").first().attr("href").removePrefix("https://4anime.to").split("-episode-").first())
        anime.thumbnail_url = element.select("#headerIMG_6").first().attr("src")
        anime.title = element.select("#headerA_5").first().attr("alt")
        return anime
    }

    override fun latestUpdatesRequest(page: Int): Request = GET("$baseUrl/recently-added/page/$page")

    override fun latestUpdatesSelector(): String = "#headerDIV_4"
}