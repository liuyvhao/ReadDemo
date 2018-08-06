package lyh.util

import org.jsoup.nodes.Document

class Head {
    companion object {
        var urlHead = "http://www.shu008.com"

        fun analysis(doc: Document, bookLists: ArrayList<BookList>) {
            val elements = doc.getElementsByClass("nlblock")
            for (element in elements) {
                var link = element.getElementsByClass("cover").attr("href")
                var img = element.select("img").attr("src")
                var title = element.select("img").attr("alt")
                var author = element.getElementsByClass("green state").text()
                var introduction = element.getElementsByClass("gray").text()
                var chapter = element.select("a").last().attr("title")
                bookLists.add(BookList(link, img, title, author, introduction, chapter))
            }
        }

        fun analysisSearch(doc: Document, bookLists: ArrayList<BookList>){
            val elements = doc.getElementsByClass("result-item result-game-item")
            for (element in elements) {
                var link = element.getElementsByClass("game-legend-a").attr("onClick").replace("window.location=","").replace("'","")
                var img = element.select("img").attr("src")
                var title = element.select("img").attr("alt").replace("<em>","").replace("</em>","")
                var author = element.getElementsByClass("result-game-item-info-tag")[0].select("span")[1].text()
                var introduction = element.getElementsByClass("result-game-item-desc").text()
                var chapter = element.getElementsByClass("result-game-item-info-tag").last().text()
                bookLists.add(BookList(link, img, title, author, introduction, chapter))
            }
        }

    }
}