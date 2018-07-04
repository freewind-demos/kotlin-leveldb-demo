package example

import java.io.File
import org.fusesource.leveldbjni.JniDBFactory.factory
import org.iq80.leveldb.DB
import org.iq80.leveldb.Options


fun main(args: Array<String>) {
    writeData(db())
    batchWriteData(db())
    iterate(db())
}

private fun db() = run {
    val options = Options().apply { this.createIfMissing(true) }
    factory.open(File("./data/example.db"), options)!!
}

private fun iterate(db: DB) {
    println("----------- iterate -------------")
    db.use {
        val iterator = db.iterator()
        // important!
        iterator.seekToFirst()
        while (iterator.hasNext()) {
            val next = iterator.peekNext()
            val key = String(next.key!!)
            val value = String(next.value!!)
            println("$key = $value")
            iterator.next()
        }
    }
}

private fun batchWriteData(db: DB) {
    println("------------ batchWriteData -------------")
    db.use {
        val batch = db.createWriteBatch()
        batch.use {
            batch.delete("id1".toByteArray())
            batch.put("id2".toByteArray(), "value2".toByteArray())
            db.write(batch)
        }
    }
}

private fun writeData(db: DB) {
    println("------------- writeData ---------------")
    db.use {
        db.put("id1".toByteArray(), "value1".toByteArray())
        val value = String(db.get("id1".toByteArray()))
        println(value)
    }
}
