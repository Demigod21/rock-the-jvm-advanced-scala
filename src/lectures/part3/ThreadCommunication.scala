package lectures.part3

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
      while(container.isEmpty){
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
      container.synchronized{
        container.wait()

      }
      //here container must have some value
      println("[consumer] I have consumed" + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] working hard")
      Thread.sleep(2000)
      val value = 342

      container.synchronized{
        println("[producer] I'm producing" + value)
        container.set(value)
        container.notify()
      }
    })
    consumer.start()
    producer.start()
  }
  smartProdCons()


}
