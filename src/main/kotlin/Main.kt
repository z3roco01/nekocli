package z3roco01.nekocli

import z3roco01.nekocli.network.NekowebApiConnection
import java.io.File

fun main(args: Array<String>) {
    if((args.size == 1 && (args.get(0) == "help" || args.get(0) == "h")) || args.size < 2) {
        printHelp()
    }else {
        val con = NekowebApiConnection("6b309b72056c57730e592757de2391776be1f34076704a4fb4dc274ef61b00c1")
        when(args.get(0)) {
            "info", "i" -> println(con.getInfo(args.get(1)))

            "createfile", "c" -> println(con.create(args.get(1), false))

            "createfolder", "cf" -> println(con.create(args.get(1), true))

            "delete", "d" -> println(con.delete(args.get(1)))

            "rename", "r" ->
                if(args.size < 3)
                    printHelp()
                else
                    println(con.rename(args.get(1), args.get(2)))
            "edit", "e" ->
                if(args.size <3) {
                    printHelp()
                }else{
                    val content = File(args.get(2)).readText(Charsets.UTF_8)
                    println(con.edit(args.get(1), content))
                }

            "folderlist", "fl" -> println(con.readFolder(args.get(1)))
        }
    }
}

fun printHelp() {
    println("nekocli <command> <arguments>\n" +
            "commands(alias) :D:\n" +
            "\thelp(h): prints this\n"+
            "\tinfo(i) <username>: gives info about <user>\n" +
            "\tcreatefile(c) <path>: creates file with path and name <path>\n" +
            "\tcreatefolder(cf) <path>: creates folder with path and name <path>\n" +
            "\tdelete(d) <path>: deletes file/folder at <path>\n" +
            "\trename(r) <old path> <new path>: moves <old path> to <new path>\n" +
            "\tedit(e) <path> <local file>: uploads data from <local file> into file at <path>\n" +
            "\tfolderlist(fl) <path>: list all files/folders in <path>")
}