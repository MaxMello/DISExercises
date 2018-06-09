import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong


object PersistenceManager {

    val userData = File("/tmp/DIS05/userData/session${System.currentTimeMillis()}/")
    val logData = File("/tmp/DIS05/persistenceManager.log")
    private val logSequenceNr = AtomicLong(0)
    val buffer = ConcurrentHashMap<Long, TransactionData>()
    private const val COMMIT = "[COMMIT]"

    init {
        synchronized(this) {
            userData.mkdirs()
            logData.parentFile.mkdirs()
            if (logData.exists()) {
                logSequenceNr.set(logData.readLines().lastOrNull()?.split(";")?.firstOrNull()?.toLong() ?: 0)
            } else {
                logData.createNewFile()
            }
            println("Initialized with LSN=$logSequenceNr and folder $userData")
        }
    }

    fun reset() {
        logData.delete()
        logSequenceNr.set(0)
    }

    @Synchronized
    fun beginTransaction(): Long {
        val transactionID =  Math.abs(UUID.randomUUID().leastSignificantBits)
        buffer[transactionID] = TransactionData()
        return transactionID
    }

    @Synchronized
    fun commit(transactionID: Long) {
        buffer[transactionID]?.apply {
            this.committed = true
        } ?: throw IllegalStateException("Transaction with ID $transactionID never started")

        buffer[transactionID]?.userData?.forEach {
            val logEntry = LogEntry(logSequenceNr.incrementAndGet(), transactionID, it.pageID, COMMIT)
            logEntry.persist(logData)
            println("Log commit for transaction $transactionID")
        }
    }

    @Synchronized
    fun write(transactionID: Long, pageID: Long, data: String) {
        val logEntry = LogEntry(logSequenceNr.incrementAndGet(), transactionID, pageID, data)
        logEntry.persist(logData)

        // Because we use a list instead of a HashMap, we need to manually remove old page redoInfo
        val oldPageData = buffer[transactionID]?.userData?.find { it.pageID == pageID }
        if(oldPageData != null) {
            buffer[transactionID]?.userData?.remove(oldPageData)
            println("Overwriting old page data for pageID $pageID in buffer")
        }
        buffer[transactionID]?.userData?.add(UserEntry(logEntry.lsn, pageID, data))

        // When more than 5 pages were committed
        val committedTransactionIDs = buffer.filter { it.value.committed }.map { it.key }.toList()
        if(buffer.filter { it.key in committedTransactionIDs }.map { it.value.userData.size }.sum() > 5) {
            println("Buffer has more than 5 committed pages, write to persistent storage")
            buffer.values.filter { it.committed }.map { it.userData }.flatten().forEach {
                it.persist(userData)
            }
            committedTransactionIDs.forEach {
                buffer.remove(it)
            }
        }
    }

    private fun getLogEntry(lsn: Long): LogEntry? {
        logData.readLines().firstOrNull { line -> line.startsWith(lsn.toString()) }?.let {
            line ->
            val (_, transactionID, pageID, data) = line.split(";")
            return LogEntry(lsn, transactionID.toLong(), pageID.toLong(), data)
        } ?: return null
    }


}

data class LogEntry(val lsn: Long, val transactionID: Long, val pageID: Long, val redoInfo: String) {

    fun persist(file: File) {
        println("Logging $this")
        file.appendText("$lsn;$transactionID;$pageID;$redoInfo\n")
    }
}

data class UserEntry(val lsn: Long, val pageID: Long, val data: String) {

    fun persist(dir: File) {
        println("Persisting $this")
        File(dir, pageID.toString()).writeText("$lsn;$data")
    }

}

data class TransactionData(var committed: Boolean = false, val userData: MutableList<UserEntry> = mutableListOf())