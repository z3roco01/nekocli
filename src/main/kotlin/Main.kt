package z3roco01.nekocli

import z3roco01.nekocli.network.NekowebApiConnection
import z3roco01.nekocli.serialization.ConfigFile
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    if((args.size == 1 && (args.get(0) == "help" || args.get(0) == "h")) || args.size < 2) {
        printHelp()
    }else {
        val configPath = System.getProperty("user.home")+"/.config/nekocli/config.json"
        if(args.get(0) == "login") {
            Path(configPath).createDirectory()
            val confFile = File(configPath+"/config.json")

            if(!confFile.createNewFile()) {
                println("couldnt make the config file 3:")
            }

            confFile.writeText(Json.encodeToString(ConfigFile(args.get(1))))

            return
        }

        val apiKey: String
        try {
            val configFile = File(configPath+"/config.json")
            val configData = Json.decodeFromString<ConfigFile>(configFile.readText())
            apiKey = configData.apiKey
        }catch(e: Exception) {
            println("couldnt read apikey from config file ${configPath+"/config.json"} D:")
            return
        }

        val con = NekowebApiConnection(apiKey)
        when(args.get(0)) {
            "info", "i" -> println(con.getInfo(args.get(1)))

            "createfile", "c", "touch" -> println(con.create(args.get(1), false))

            "createfolder", "cf", "mkdir" -> println(con.create(args.get(1), true))

            "delete", "d", "rm" -> println(con.delete(args.get(1)))

            "rename", "r", "mv" ->
                if (args.size < 3)
                    printHelp()
                else
                    println(con.rename(args.get(1), args.get(2)))

            "edit", "e" ->
                if (args.size < 3) {
                    printHelp()
                } else {
                    val content = File(args.get(2)).readText(Charsets.UTF_8)
                    println(con.edit(args.get(1), content))
                }

            "folderlist", "fl", "ls" ->
                try {
                    println(con.readFolder(args.get(1)))
                }catch(e: Exception) {
                    println("couldnt read folder ${args.get(1)} 3:")
                }
        }
    }
}

fun printHelp() {
    println("nekocli <command> <arguments>\n" +
            "commands(alias) :D:\n" +
            "\thelp(h): prints this\n"+
            "\tlogin <api key>: sets your api key to <api key>\n" +
            "\tinfo(i) <username>: gives info about <user>\n" +
            "\tcreatefile(c,touch) <path>: creates file with path and name <path>\n" +
            "\tcreatefolder(cf,mkdir) <path>: creates folder with path and name <path>\n" +
            "\tdelete(d,rm) <path>: deletes file/folder at <path>\n" +
            "\trename(r,mv) <old path> <new path>: moves <old path> to <new path>\n" +
            "\tedit(e) <path> <local file>: uploads data from <local file> into file at <path>\n" +
            "\tfolderlist(fl,ls) <path>: list all files/folders in <path>")
}