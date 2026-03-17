import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class User(
    val nume: String,
    val id: Int
)

class Note(
    val titlu: String,
    val autor: User,
    val continut: String,
    val dataOra: LocalDateTime
)

interface NoteStorage
{
    fun save(note: Note)
    fun delete(id: String)
    fun loadAll(): List<Note>
}

class FileNoteStorage(private val pathDir: String) : NoteStorage
{
    init{
        File(pathDir).mkdirs()
    }

    override fun save(note: Note)
    {
        val fisier=File(pathDir,"${note.titlu}.txt")

        val data="""
            ${note.titlu}
            ${note.autor.nume}
            ${note.autor.id}
            ${note.dataOra.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}
            ${note.continut}
        """.trimIndent()
        fisier.writeText(data)
    }

    override fun delete(TITLU: String)
    {
        File(pathDir,"${TITLU}.txt").delete()
    }

    override fun loadAll(): List<Note>
    {
        val folder=File(pathDir)
        val notes=mutableListOf<Note>()

        folder.listFiles()?.forEach { file ->
            if (file.extension=="txt") {
                val lines=file.readLines()
                if (lines.size>=5)
                {
                    val note=Note(
                        titlu=lines[0],
                        autor=User(lines[1], lines[2].toInt()),
                        dataOra=LocalDateTime.parse(lines[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        continut=lines.subList(4, lines.size).joinToString("\n")
                    )
                    notes.add(note)
                }
            }
        }
        return notes
    }
}

class NoteManager(private val storage: NoteStorage)
{
    private val notes: MutableList<Note> = storage.loadAll().toMutableList()

    fun createNote(titlu: String, autor: User, continut: String)
    {
        val dataOra=LocalDateTime.now()
        val nota=Note(titlu, autor, continut, dataOra)
        notes.add(nota)
        storage.save(nota)
        println("notita creata cu titlul: $titlu")
    }

    fun listNotes()
    {
        if(notes.isEmpty())
        {
            println("nu exista notite")
            return
        }
        println("Lista notite: ")
        notes.forEach { note ->
            println("${note.titlu}: ${note.autor.nume} (${note.dataOra.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))})")
        }
    }

    fun deleteNote(TITLU: String)
    {
        val del=notes.find{it.titlu==TITLU}
        if(del!=null)
        {
            notes.remove(del)
            storage.delete(del.titlu)
            println("Notita cu titlul ${del.titlu} a fost stearsa")
        }
        else
        {
            println("Nu exista notita cu titlul $TITLU")
        }
    }

    fun getContent(TITLU: String)
    {
        val note = notes.find { it.titlu==TITLU}
        if (note!=null) {
            println("\nTitlu: ${note.titlu}")
            println(note.continut)
            println("\n")
        } else {
            println("Notita nu exista")
        }
    }
}