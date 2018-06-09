import java.io.File

class RecoveryTool(val userData: File, val logData: File) {

    private val logEntries: List<LogEntry>

    init {
        logEntries = logData.readLines().map { line ->
            val (lsn, transactionID, pageID, data) = line.split(";")
            LogEntry(lsn.toLong(), transactionID.toLong(), pageID.toLong(), data)
        }
    }

    /**
     * TODO What is a "winner" transaction, except that it has to have committed?
     */
    fun analyzeAndRedo() {
        logEntries.groupBy { it.transactionID }.filter { it.value.any { it.redoInfo == PersistenceManager.COMMIT } }
                .forEach { taid, logs ->
                    println("Found committed transaction $taid")
                    logs.filter { it.redoInfo != PersistenceManager.COMMIT }.forEach {
                        val existingData = File(userData, it.pageID.toString())
                        if(existingData.exists() &&
                                (existingData.readText().split(";").firstOrNull()?.toLong() ?: 0) < it.lsn) {
                            it.toUserEntry().persist(userData)
                            println("\tPersisting $it, because persisted lsn is smaller")
                        } else {
                            println("\tNot Persisting $it, because persisted lsn is bigger/equal")
                        }
                    }
                }
    }
}