package lectures.part3

import java.util.concurrent.Executors

object Intro extends App {

  // JVM threads
  val aThread = new Thread(new Runnable{
    override def run(): Unit = println("Running in paralleld")
  })

  //aThread.start() // gives the signal to the jvm to start a hvm thread

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  //threadHello.start()
  //threadGoodbye.start()
  // different runs produce different results

  // executors
  val pool = Executors.newFixedThreadPool(10)
  //pool.execute(() => println("something in the thread pool"))

  //pool.shutdown()

  def runInParallel = {
    var x = 0

    val thread1 = new Thread( () => {
      x = 1
    } )
    val thread2 = new Thread( () => {
      x = 2
    } )
    //thread1.start()
    //thread2.start()
    println(x)
  }

  //for (_ <- 1 to 100) runInParallel
  // race conditiion

  class BankAccount(var amount: Int) {
    override def toString: String = "" + amount

  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    println(s"I bought a $thing")
    println(s"My account is now "+ account)
  }

  /*
    for(_ <- 1 to 1000) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iPhone 14", 4000))
    thread1.start()
    thread2.start()
    Thread.sleep(100)
    println()
  }
   */


  // option 1 : use synchronized()
  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized{
      account.amount -= price
      println(s"I bought a $thing")
      println(s"My account is now "+ account)
    }
  }

  // option 2 : use @volatile

  /*
  * Exercises
  * 1) Construct 50 inception threads
  * in reverse order (println hello from thread #3)
   */
  def inceptionThread(maxThread: Int, i: Int = 1): Thread = new Thread(() => {
    if(i < maxThread) {
      val newThread = inceptionThread(maxThread, i+1)
      newThread.start()
      newThread.join()
    }
    println(s"Hello from thread $i")
  })
  inceptionThread(50).start()
  /*
  * 2)
  *
   */
  var x = 0
  val threads = (1 to 100).map(_ => new Thread(()=> x += 1))
  threads.foreach(_.start())

  /*
  * 1) biggest value possible? => 100
  * 2) smallest value possible for x? => 1
  *
  * threads can act in parallel
  * they all can read 0 and start with x = 1
  *
   */


  /*
  * 3 sleep fallacy
  *
   */

  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "scala is awesome"
  })

  message = "scala sucks"
  awesomeThread.start()
  Thread.sleep(2000)
  awesomeThread.join() //that's the solution -> we make sure that awesome thread has finished
  print(message)
  /*
  * What's the value of message? almost always scala is awesome
  * Is it guarantted? not guaranteed
  * Why? Why not?
  * (main thread)
  * message = sucks
  * main.start() ->  sleep -> relieves execution
  * also awesome can sleep -> relieves execution
  * CPU can chose which thread to pick up -> can pick the radom
  *
   */

  // how do we fix this?
  // synch does NOT work HERE -> it works for concurrent modifications, this is a sequential problem
  // we need thread join

}
