import java.io.File

fun transaction(vararg str: String, doCommit: Boolean = true) = Pair(listOf(*str), doCommit)

fun main(args: Array<String>) {

    val clients = listOf(Client(1, listOf(
            transaction("Informatik", "Datenbanken"),
            transaction("NoSQL", "Microservice"))),
    Client(2, listOf(
            transaction("Geographie", "Europa", doCommit = false),
            transaction("Asien", "Afrika", "Haiti"))),
    Client(3, listOf(
            transaction("Politik"),
            transaction("Trump", "Putin", "Merkel", "Marcon", doCommit = false))),
    Client(4, listOf(
            transaction("Sprachwissenschaften", "Deutsch", "Englisch"),
            transaction("Spanisch", "Latein", "Griechisch", "Japanisch", "Finnisch", "Französisch"))))

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