import java.io.File

class RecoveryTool(val userData: File, logData: File) {

    private val logEntries: List<LogEntry>

    init {
        logEntries = logData.readLines().map { line ->
            val (lsn, transactionID, pageID, data) = line.split(";")
            LogEntry(lsn.toLong(), transactionID.toLong(), pageID.toLong(), data)
        }
    }

    fun analyzeAndRedo() {
        // Group by keeps the order of the list value and also of the transactions, because it uses LinkedHashMap
        logEntries.groupBy { it.transactionID }.filter { it.value.any { it.redoInfo == PersistenceManager.COMMIT } }
                .forEach { taid, logs ->
                    println("Found committed transaction $taid")
                    logs.filter { it.redoInfo != PersistenceManager.COMMIT && it.redoInfo != PersistenceManager.BOT }.forEach {
                        val existingData = File(userData, it.pageID.toString())
                        val persistedLSN = if(existingData.exists()) existingData.readText().split(";").firstOrNull()?.toLong() ?: 0 else -1
                        if(persistedLSN < it.lsn) {
                            it.toUserEntry().persist(userData)
                            println("\tPersisting because persisted lsn=$persistedLSN < ${it.lsn} | $it")
                        } else {
                            println("\tNot Persisting because persisted lsn=$persistedLSN >= ${it.lsn} | $it")
                        }
                    }
                }
    }
}