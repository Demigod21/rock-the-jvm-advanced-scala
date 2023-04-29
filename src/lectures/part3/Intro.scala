package lectures.part3

import java.util.concurrent.Executors

object Intro extends App {

  // JVM threads
  val aThread = new Thread(new Runnable{
    override def run(): Unit = println("Running in paralleld")
  })

  aThread.start() // gives the signal to the jvm to start a hvm thread

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.shutdown()

}
