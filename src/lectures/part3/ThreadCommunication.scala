package lectures.part3

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
  *
  *  producer consumer problem
  * producer[x] setting a value
  * consumer -> extract x out of container
  * they're running in parallel
   */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def get: Int = {
      val result = value
      value = 0
      result
    }

    def set(newValue: Int): Unit = value = newValue
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while (container.isEmpty) {
        println("[consumer] actively waiting")
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 32
      println("[producer] I have produced " + value)
      container.set(value)
    })
    consumer.start()
    producer.start()

  }
  //naiveProdCons()


  // wait and notify
  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting..")
      container.synchronized {
        container.wait()

      }
      //here container must have some value
      println("[consumer] I have consumed" + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] working hard")
      Thread.sleep(2000)
      val value = 342

      container.synchronized {
        println("[producer] I'm producing" + value)
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }
  //smartProdCons()

  /*
  * working with buffer
   */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty waiting")
            buffer.wait()
          }
          // here must be at least one value
          val x = buffer.dequeue()
          println("[consumer] I consumed " + x)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full")
            buffer.wait()
          }
          //at least one empty space
          println("[producer] producing " + i)
          buffer.enqueue(i)
          i += 1
          buffer.notify()
        }
      }
    })

    consumer.start()
    producer.start()
  }

  /*
    level 3
    multi producers, multi consumers
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty waiting")
            buffer.wait()
          }
          // here must be at least one value
          val x = buffer.dequeue()
          println(s"[consumer $id] I consumed " + x)
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer$id] buffer is full")
            buffer.wait()
          }
          //at least one empty space
          println(s"[producer$id] producing " + i)
          buffer.enqueue(i)
          i += 1
          buffer.notify()
        }
      }
    }
  }

  def multiProdCons(nConsumer: Int, nProducer: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    (1 to nConsumer).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducer).foreach(i => new Producer(i, buffer, capacity).start())
  }

  /*
  * Exercise
  * 1) think of an example where notifyAll acts in a different way
  * 2) create a deadlock
  * 3) create a livelock
   */
  // EXERCISE 1 - NOTIFY ALL
  def testNotifyAll(): Unit = {
    val bell = new Object
    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hurray!")
      }
    }).start())
    new Thread(() => {
      Thread.sleep(1999)
      bell.synchronized {
        println(s"[announcer] rock and roll")
        bell.notifyAll()
      }
    })
  }

  // EXERCISE 2 - DEADLOCK
  case class Friend(name: String){
    def bow(other: Friend) = {
      this.synchronized{
        println(s"I'm bowing to my friend $other")
        other.rise(this)
        println(s"$this my friend $other has risen")
      }
    }
    def rise(other:Friend) = {
      this.synchronized{
        println(s"I'm rising t my friend $other")
      }
    }
  }

  val sam = Friend("sam")
  val pierre = Friend("pierre")

  new Thread(() => {
    sam.bow(pierre)
  }).start()

  new Thread(() => {
    pierre.bow(sam)
  }).start()



}
