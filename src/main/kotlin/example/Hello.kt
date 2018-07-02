package example

import java.io.File
import org.fusesource.leveldbjni.JniDBFactory.factory
import org.iq80.leveldb.Options


fun main(args: Array<String>) {
    val options = Options().apply {
        this.createIfMissing(true)
    }
    val db = factory.open(File("./data/example.db"), options)

    db.use {
        db.put("id1".toByteArray(), "value1".toByteArray())
        val value = String(db.get("id1".toByteArray()))
        println(value)
    }

    val batch = db.createWriteBatch()
    batch.use {
        batch.delete("id1".toByteArray())
        batch.put("id2".toByteArray(), "value2".toByteArray())
        db.write(batch)
    }
    
}

fun hello(name: String): String = "Hello, $name!"