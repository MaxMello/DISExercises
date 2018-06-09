import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * Object = Singleton class
 * Generates new clients (=threads), each with it's unique page range, which will write strings to random pages
 * in their range.
 */
object ClientManager {

    private val pageRange = (0..9)

    /**
     * @param clientID: Min = 1, identifier for client, used for page range
     * @param writes: List of List of Strings. Each inner list is one transaction
     */
    fun startNewClient(clientID: Long, writes: List<List<String>>, doCommit: Boolean = true): Thread {
        return thread {
            val myPageOffset = clientID * 10
            writes.forEach {
                val transactionID = PersistenceManager.beginTransaction()
                println("Client $clientID started transaction $transactionID")
                it.forEach { str ->
                    Thread.sleep((Math.random() * 1500).toLong())
                    val page = pageRange.random().toLong() + myPageOffset
                    PersistenceManager.write(transactionID, page, str)
                    println("Client $clientID in transaction $transactionID writing to page $page data $str")
                }
                if(doCommit) {
                    Thread.sleep((Math.random() * 500).toLong())
                    PersistenceManager.commit(transactionID)
                }
            }
            println("Client $clientID commited and is finished now")
        }
    }

    /**
     * Extension function that allows us to chose a random value in a range
     */
    private fun ClosedRange<Int>.random() =
            Random().nextInt(endInclusive - start) +  start

}


fun main(args: Array<String>) {

    val clients = listOf(ClientManager.startNewClient(1, listOf(
            listOf("Informatik", "Datenbanken"),
            listOf("NoSQL", "Microservice"))),
    ClientManager.startNewClient(2, listOf(
            listOf("Geographie", "Europa"),
            listOf("Asien", "Afrika", "Haiti"))),
    ClientManager.startNewClient(3, listOf(
            listOf("Politik"),
            listOf("Trump", "Putin", "Merkel", "Marcon")), false),
    ClientManager.startNewClient(4, listOf(
            listOf("Sprachwissenschaften", "Deutsch", "Englisch"),
            listOf("Spanisch", "Latein", "Griechisch", "Japanisch", "Finnisch", "Französisch"))))

    // Wait for all clients to finish
    clients.forEach {
        it.join()
    }

    println("\n\n===================== LOG FILE =====================\n\n")
    println("LSN\t\tTAID\t\t\t\tPage\t\tRedoInfo")
    println(PersistenceManager.logData.readText().replace(";", "\t\t"))

    println("\n\n===================== BUFFER DATA =====================\n\n")
    println("TAID\t\t\t\tCommitted\t\tLSN\t\tPage\t\tData")
    PersistenceManager.buffer.forEach { transactionID: Long, data: TransactionData ->
        data.userData.forEach {
            println("$transactionID\t\t${data.committed}\t\t${it.lsn}\t\t${it.pageID}\t\t${it.data}")
        }
    }

    println("\n\n===================== PERSISTENT PAGES =====================\n\n")
    println("Page\t\tLSN\t\tData")
    PersistenceManager.userData.listFiles()?.sortedBy { it.name.toLong() }?.forEach {
        println("${it.name} \t\t ${it.readText().replace(";", "\t\t")}")
    } ?: println("Nothing was persisted")


    println("\n\n===================== RECOVERY TOOL =====================\n\n")

    val basePath = if(args.isNotEmpty()) {
        File(args[0])
    } else {
        File("/tmp/DIS05/").listFiles()?.sortedDescending()?.first()
    }
    println("Using $basePath for recovery")
    val recoveryTool = RecoveryTool(File(basePath, "userData"), File(basePath, "persistenceManager.log"))
    recoveryTool.analyzeAndRedo()

    println("\n\n===================== PERSISTENT PAGES =====================\n\n")
    println("Page\t\tLSN\t\tData")
    recoveryTool.userData.listFiles()?.sortedBy { it.name.toLong() }?.forEach {
        println("${it.name} \t\t ${it.readText().replace(";", "\t\t")}")
    } ?: println("Nothing was persisted")

}