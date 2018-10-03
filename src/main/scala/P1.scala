import scala.io.Source
import akka.actor._
import akka.routing.RoundRobinRouter
import java.io._
import scala.concurrent.duration._

sealed trait Message

case object Start extends Message

case class Work(user: String, lines: Array[String]) extends Message

case class Result (r: (Double,Double,Double,Int,Int)) extends Message

class Worker extends Actor{
    val pFile = new PrintWriter(new File("PIDs.txt"))
    
    def read(user: String, lines: Array[String]): (Double,Double,Double,Int,Int) = {
        val uFile = new PrintWriter(new File(user + ".txt"))
        var resC, resM, resT: Double = 0.0
        var nProc, nProcG: Int = 0
        
        for (i<- 0 until lines.length)
        {
            if(lines(i).take(1) == user.take(1))
            {
                val p = lines(i).split(" ")
                
                val s = p(4).split(":")
                
                uFile.write(lines(i) + "\n")
                
                pFile.write(p(0) + " " + p(1) + "\n")
                
                resC = resC + p(2).toDouble
                resM = resM + p(3).toDouble
                resT = resT + (s(0).toDouble * 3600 + s(1).toDouble * 60)
                nProc+= 1
            }
            
            nProcG+= 1
        }
        
        val r = (resC,resM,resT,nProc,nProcG) 
        
        pFile.close
        uFile.close
        
        return r
    }
    
    def receive: Receive = {
        case Work(users,lines) => sender() ! Result(read(users,lines))
    }
}

class Master(users: Array[String], lines: Array[String]) extends Actor{
    val start: Long = System.currentTimeMillis
    var resultados: Int = -1
    val rest, resc, resm = new Array[Double](8)
    val nproc = new Array[Int](8)
    var medt, medc, medm: Double = 0.0
    
    val cFile = new PrintWriter(new File("Cpu e Memoria.txt"))
    val tFile = new PrintWriter(new File("Tempo.txt"))
    
    var worker = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(8)), "worker")
    
    def receive: Receive = {
        case Start => {
            for (i<- 0 until 8)
                worker ! Work(users(i), lines)
        }
        
        case Result(r) => {
            resultados+= 1
            
            resc(resultados) = r._1
            resm(resultados) = r._2
            rest(resultados) = r._3
            nproc(resultados) = r._4
            val nprocG = r._5
            
            if(resultados == 7)
            {
                for(i<- 0 until 8)
                {
                    cFile.write(users(i) + " \nmédia de cpu: " + resc(i) / nproc(i) + " \nmédia de memoria: " + resm(i) / nproc(i) + "\n\n")
        
                    tFile.write(users(i) + " \nmédia de tempo: " + rest(i) / nproc(i) + " \n\n")
                }
                
                for(i<- 0 until resc.length)
                    medc = medc + resc(i)
                    
                for(i<- 0 until resm.length)
                    medm = medm + resm(i)
                    
                for(i<- 0 until rest.length)
                    medt = medt + rest(i)
                
                cFile.write("Geral: \nmédia de cpu: " + medc / nprocG + " \nmédia de memoria: " + medm / nprocG)
                
                tFile.write("Geral: \nmédia de tempo: " + medt / nprocG)
                
                println("\nAcabou o programa \nTempo de execução: " + (System.currentTimeMillis - start).millis)
                
                cFile.close
                tFile.close
                
                context.system.shutdown()
            }
            
        }
    }
    
    
}
/*
Usuarios: 
postgres, stack, docker, ubuntu, mysql, java, root, apache

Estrutura do registro:
usuario  PID  %CPU  %memoria  time(execution time)
*/
object TesteEx1{
    def main (args: Array[String]): Unit = {
        val system = ActorSystem("MainSystem")
        val lines = Source.fromFile("pimpar.txt").getLines.toArray
        val users = Array("postgres","stack","docker","ubuntu",
        "mysql","java","root","apache")
        
        val masterActor = system.actorOf(Props(new Master(users, lines)),"masterActor")
            
        masterActor ! Start
    }
}