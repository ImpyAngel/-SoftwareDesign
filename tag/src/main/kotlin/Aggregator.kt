interface Aggregator {
    var lastPost: Post?
    val sortedAll: List<Post>
    fun add(posts: List<Post>)
    fun cleanUp()
}

class AggregatorImpl: Aggregator {
    override var lastPost: Post? = null

    override var sortedAll = mutableListOf<Post>()

    override fun add(posts: List<Post>) {
        var sortedPosts = posts.sortedBy { it.date }
        val wasCount = sortedAll.size
        val beforePost = lastPost
        if (beforePost != null && sortedPosts.contains(beforePost)) {
            Logger.info("Need drop posts")

            sortedPosts = sortedPosts.dropWhile {
                it.date >= beforePost.date
            }
        }
        lastPost = sortedPosts.firstOrNull()

        sortedAll.addAll(sortedPosts)
        Logger.info("Add ${sortedAll.size - wasCount} posts to aggregator")
    }

    override fun cleanUp() {
        lastPost = null
        sortedAll = mutableListOf()
    }
}
