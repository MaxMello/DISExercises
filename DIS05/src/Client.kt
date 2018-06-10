import java.util.*

class Client(private val clientID: Long, private val writes: List<Pair<List<String>, Boolean>>,
             private val autoStart: Boolean = true) : Thread() {

    private val pageRange = (0..9)

    /**
     * Extension function that allows us to chose a random value in a range
     */
    private fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

    override fun run() {
        val myPageOffset = clientID * 10
        writes.forEach {
            val transactionID = PersistenceManager.beginTransaction()
            println("Client $clientID started transaction $transactionID")
            it.first.forEach { str ->
                Thread.sleep((Math.random() * 1500).toLong())
                val page = pageRange.random().toLong() + myPageOffset
                PersistenceManager.write(transactionID, page, str)
                println("Client $clientID in transaction $transactionID writing to page $page data $str")
            }
            if(it.second) { // doCommit
                Thread.sleep((Math.random() * 500).toLong())
                PersistenceManager.commit(transactionID)
            }
        }
        println("Client $clientID commited and is finished now")
    }

    init {
        if(this.autoStart) {
            this.start()
        }
    }

}