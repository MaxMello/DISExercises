import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


/**
 * Object = Singleton class
 * Singleton PersistenceManager that handles transactions
 */
object PersistenceManager {

    private val initializationTime = System.currentTimeMillis()
    val userData = File("/tmp/DIS05/session$initializationTime/userData/")
    val logData = File("/tmp/DIS05/session$initializationTime/persistenceManager.log")
    private val logSequenceNr = AtomicLong(0) // Thread safe long
    val buffer = ConcurrentHashMap<Long, TransactionData>() // Thread safe Map of transactionID to TransactionData
    const val COMMIT = "[COMMIT]" // Value in LOG to mark a commit
    const val BOT = "[BOT]"

    init {
        synchronized(this) {
            userData.mkdirs()
            logData.parentFile.mkdirs()
            if (logData.exists()) {
                // If there is a logFile already, read the last LSN
                logSequenceNr.set(logData.readLines().lastOrNull()?.split(";")?.firstOrNull()?.toLong() ?: 0)
            } else {
                logData.createNewFile()
            }
            println("Initialized with LSN=$logSequenceNr and folder $userData")
        }
    }

    @Synchronized
    fun beginTransaction(): Long {
        val transactionID =  Math.abs(UUID.randomUUID().leastSignificantBits) // Generate some large random number
        buffer[transactionID] = TransactionData() // Already initialize the buffer
        LogEntry(logSequenceNr.incrementAndGet(), transactionID, -1L, BOT).persist(logData)
        return transactionID
    }

    @Synchronized
    fun commit(transactionID: Long) {
        buffer[transactionID]?.apply {
            this.committed = true
        } ?: throw IllegalStateException("Transaction with ID $transactionID never started")

        LogEntry(logSequenceNr.incrementAndGet(), transactionID, -1L, COMMIT).persist(logData)
    }

    @Synchronized
    fun write(transactionID: Long, pageID: Long, data: String) {
        require(data != COMMIT) // We do not allow the COMMIT symbol as input
        require(data != BOT) // We do not allow the Begin of Transaction symbol as input

        val logEntry = LogEntry(logSequenceNr.incrementAndGet(), transactionID, pageID, data)
        logEntry.persist(logData)

        // Because we use a list instead of a HashMap, we need to manually remove old page redoInfo
        val oldPageData = buffer[transactionID]?.userData?.find { it.pageID == pageID }
        if(oldPageData != null) {
            buffer[transactionID]?.userData?.remove(oldPageData)
            println("Overwriting old page data for pageID $pageID in buffer")
        }
        buffer[transactionID]?.userData?.add(UserEntry(logEntry.lsn, pageID, data))

        // When more than 5 pages in buffer, all committed ones get persisted
        if(buffer.map { it.value.userData.size }.sum() > 5) {
            // Get all transactions that are committed
            val committedTransactionIDs = buffer.filter { it.value.committed }.map { it.key }.toList()
            if(committedTransactionIDs.isNotEmpty()) {
                println("Buffer has more than 5 pages, write ${committedTransactionIDs.size} committed transactions to persistent storage")
                buffer.filter { it.key in committedTransactionIDs }.map { it.value.userData }.flatten().forEach {
                    it.persist(userData)
                    println("\tPersisting $it")
                }
                committedTransactionIDs.forEach {
                    buffer.remove(it)
                }
            }
        }
    }
}


// Data classes are normal classes with auto generated hashcode, equals, toString

/**
 * Data class holding a single log entry information
 */
data class LogEntry(val lsn: Long, val transactionID: Long, val pageID: Long, val redoInfo: String) {

    /**
     * Persist log information into a given file
     */
    fun persist(file: File) {
        file.appendText("$lsn;$transactionID;$pageID;$redoInfo\n")
    }

    fun toUserEntry(): UserEntry {
        require(redoInfo != PersistenceManager.COMMIT)
        require(redoInfo != PersistenceManager.BOT)
        return UserEntry(lsn, pageID, redoInfo)
    }
}

/**
 * Data class holding the actual data
 */
data class UserEntry(val lsn: Long, val pageID: Long, val data: String) {

    /**
     * Persist data for page inside a given directory
     */
    fun persist(dir: File) {
        File(dir, pageID.toString()).writeText("$lsn;$data")
    }

}

/**
 * Data class holding TransactionData, making use of default arguments for parameter-less initialization
 */
data class TransactionData(var committed: Boolean = false, val userData: MutableList<UserEntry> = mutableListOf())