package link

import utils.Delay
import java.io.PrintStream
import java.util.*
import kotlin.concurrent.thread

class RemoteWriter(private val robotEntity: RobotEntity) {
    private val socket = robotEntity.socket
    private var out = PrintStream(socket.getOutputStream())
    private var countCommandsWithCallBack = 0

    fun write(message: String): Boolean {
        if(socket.isConnected){
            try {
                out.println(message)
                out.flush()

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else{
            // TODO: Migrate to log
            println("Writer: no connection to the robot")
        }
        return false
    }

    fun writeDependingStatusAndChangeIt(message: String): Boolean {
        val res = robotEntity.commandsQueue.add(message)

        // TODO: Think about how to do it differently
        Delay.middle()
        robotEntity.state = State.COMMAND_EXECUTION

        return res
    }


    fun writeDependingStatus(message: String):Boolean =
            robotEntity.commandsQueue.add(message)

}