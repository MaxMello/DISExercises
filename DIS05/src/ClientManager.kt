import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

object ClientManager {

    private val offsetValue = AtomicInteger(10)
    private val pageRange = (0..9)

    fun startNewClient(clientID: Long, writes: List<List<String>>): Thread {
        return thread {
            val myPageOffset = offsetValue.getAndAdd(10)
            writes.forEach {
                val transactionID = PersistenceManager.beginTransaction()
                println("Client $clientID started transaction $transactionID")
                it.forEach { str ->
                    Thread.sleep((Math.random() * 1500).toLong())
                    val page = pageRange.random().toLong() + myPageOffset
                    PersistenceManager.write(transactionID, page, str)
                    println("Client $clientID wrote data $it in transaction $transactionID to page $page")
                }
                Thread.sleep((Math.random() * 500).toLong())
                PersistenceManager.commit(transactionID)
            }
            println("Client $clientID commited and is finished now")
        }
    }

    private fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

}


fun main(args: Array<String>) {

    PersistenceManager.reset()
    Thread.sleep(2500)

    val clients = listOf(ClientManager.startNewClient(1, listOf(
            listOf("Informatik", "Datenbanken"),
            listOf("NoSQL", "Microservice"))),
    ClientManager.startNewClient(2, listOf(
            listOf("Geographie", "Europa"),
            listOf("Asien", "Afrika", "Haiti"))),
    ClientManager.startNewClient(3, listOf(
            listOf("Politik"),
            listOf("Trump", "Putin", "Merkel"))),
    ClientManager.startNewClient(4, listOf(
            listOf("Sprachwissenschaften", "Deutsch", "Englisch"),
            listOf("Spanisch", "Latein", "Griechisch", "Japanisch", "Finnisch", "Französisch"))))

    clients.forEach {
        it.join()
    }

    println("\n\n===================== LOG FILE =====================\n\n")
    println(PersistenceManager.logData.readText().replace(";", "\t\t"))

    println("\n\n===================== BUFFER DATA =====================\n\n")

    PersistenceManager.buffer.forEach { transactionID: Long, data: TransactionData ->
        println("TAID=$transactionID | $data")
    }

    println("\n\n===================== PERSISTENT PAGES =====================\n\n")

    PersistenceManager.userData.listFiles()?.sortedBy { it.name.toLong() }?.forEach {
        println("${it.name}: \t\t ${it.readText()}\n")
    } ?: println("Nothing was persisted")

}