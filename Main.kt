import java.util.Scanner
import java.time.LocalDateTime

fun main() {
    val storage=FileNoteStorage("./notite_disk")
    val manager=NoteManager(storage)
    val user=User("Serban",1)
    val sc=Scanner(System.`in`)
    var stop=false

    while(!stop) {
        println("\n1.Lista 2.Citeste 3.Nou 4.Sterge 5.Exit")
        print("Alege: ")
        when(sc.nextLine()) {
            "1"->manager.listNotes()
            "2"->{
                print("Titlu: ")
                manager.getContent(sc.nextLine())
            }
            "3"->{
                print("Titlu: "); val t=sc.nextLine()
                print("Continut: "); val c=sc.nextLine()
                manager.createNote(t,user,c)
            }
            "4"->{
                print("Titlu: "); manager.deleteNote(sc.nextLine())
            }
            "5"->stop=true
        }
    }
}