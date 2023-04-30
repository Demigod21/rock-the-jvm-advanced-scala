package lectures.part3

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}

object FuturesPremises extends App {

  def calculateMeaningLife: Int = {
    Thread.sleep(3000)
    42
  }

  val aFuture = Future {
    calculateMeaningLife
  }

  println(aFuture.value) // Option[Try[Int]]
  println("Waiting on the future")
  aFuture.onComplete(t => t match {
    case Success(meaningOfLife) => println(s"Meaning of life is $meaningOfLife")
    case Failure(exception) => println(s"I have failed with $exception")
  })
  Thread.sleep(3100)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )
    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {
      //fetching from the database
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      //fetching from the database
      Thread.sleep(random.nextInt(300))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }

    //client poke
    val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
    mark.onComplete {
      case Success(markProfile) => {
        val bill = SocialNetwork.fetchBestFriend(markProfile)
        bill.onComplete {
          case Success(billProfile) => markProfile.poke(billProfile)
          case Failure(execption) => execption.printStackTrace()
        }
      }
      case Failure(execption) => execption.printStackTrace()
    }
    Thread.sleep(1200)

    // functional composition of futures
    // map, flatmap, filter

    val nameOnTheWall: Future[String] = mark.map(profile => profile.name)
    val markBestFriend: Future[Profile] = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
    val zucksBestFriendRestricted: Future[Profile] = markBestFriend.filter(profile => profile.name.startsWith("Z"))


    // for compr
    for {
      mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
      bill <- SocialNetwork.fetchBestFriend(mark)
    } mark.poke(bill)


    val aProfileNoMatterWhat: Future[Profile] = SocialNetwork.fetchProfile("unkowkn").recover {
      case e: Throwable => Profile("fb.id.0-dummy", "Forever Alone")
    }

    val aFetchedProfileNoMatterWhat: Future[Profile] = SocialNetwork.fetchProfile("unkowkn").recoverWith {
      case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")
    }

    val fallbackResult: Future[Profile] = SocialNetwork.fetchProfile("unkowkn").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  }


  //online banking app
  case class User(name: String)

  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "RTJVM Banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching in the database
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, mercantName: String, amount: Double): Future[Transaction] = Future {
      Thread.sleep(900)
      Transaction(user.name, mercantName, amount, "success")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch user
      // create transaction
      // WAIT for transaction to finish
      val status = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status
      Await.result(status, 2.seconds)
    }
  }

  // promises
  // futures are read only when they're done

  val promise = Promise[Int]() // controller over a future
  val future = promise.future

  // thread 1 - consumer
  future.onComplete {
    case Success(result) => println(s"[consumer] I've received $result")
  }

  // thread 2 - producer
  val producer = new Thread(() => {
    println(s"[producer] crunching numbers")
    Thread.sleep(1000)
    // "fulfilling" the promise
    promise.success(42)
    println("[producer] done")
  })

  /*
  * 1) fulfill a future immediately with a value
  * 2) inSequence(fa, fb) -> run fb after fa is completed
  * 3) first(fa, fb) return => new future of the first future completed
  * 4) last(fa, fb) reutrn => new Future with last value
  * 5) retryUntil[T](action: () => Future[T], condition: T => Boolean) : Future[T]
  * run the action, we obtain T, we apply condition, if true => we return the value
   */

  def fulfill[T](value: T): Future[T] = Future(value)

  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] = {
    first.flatMap(_ => second)
  }

  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise1 = Promise[A]

    fa.onComplete(promise1.tryComplete)
    fb.onComplete(promise1.tryComplete)

    promise1.future
  }

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]
    // 1 promise which both future will try complete
    // the one who fails, will complete the second promise -> this is our last

    val checkAndComplete = (result: Try[A]) => if (!bothPromise.tryComplete(result))
      lastPromise.complete(result)

    fa.onComplete(checkAndComplete)
    fb.onComplete(checkAndComplete)

    lastPromise.future

  }


  def retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T] = {
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action, condition)
      }
  }


}
