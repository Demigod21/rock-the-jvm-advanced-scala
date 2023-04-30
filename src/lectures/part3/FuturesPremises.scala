package lectures.part3

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

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


}
