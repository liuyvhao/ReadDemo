package lyh.util

data class BookList(var link: String, var img: String, var title: String, var author: String, var introduction: String, var chapter: String)

data class Chapter(var title: String, var link: String,var check: Boolean = false)

data class Collect(var name: String, var img: String, var link: String, var time: String, var check: Boolean = false)