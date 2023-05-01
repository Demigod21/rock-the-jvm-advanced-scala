package lectures.part5

object Variance extends App {

  trait Animal

  class Dog extends Animal

  class Cat extends Animal

  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - type substitution of generics

  // should a Cage Cat inherit from Cage Animal?
  class Cage[T]

  // 1- yes - covariance
  class CCage[+T]

  val ccage: CCage[Animal] = new CCage[Cat]

  // 2 - no - invariance
  class ICage[T] // types are different
  // val icage: ICage[Animal] = new ICage[Cat] -- types mismatch!!

  // 3 - HELL NO - OPPOSITE - contravariance
  class XCage[-T]

  val xcage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant

  // covariant postitions
  class CovariantCage[+T](val animal: T) // COVARIANT POSITION


  // class ContravariantCage[-T](val animal: T)
  //contravariant type t occurs in coveriant position
  /*
   * val catCage: XCage[Cat] = new XCage[Animal](new Crocodile) --- this is wrong!
   */

  // class CovariantVariableCage[+T](var animal: T) --- VAR are in CONTRAVARIANT POSITION
  // covariant type T appears in contravariant position!

  /**
   * val ccage: CCage[Animal] = new CCage[Cat](new Cat)
   * ccage.animal = new Crocodile ---- THIS IS WRONG
   */

  // class ContraVariantVariableCage[-T](var animal: T) --- VAR is in COVARIANT POSITION
  /*
   * val catCage: XCage[Cat] = new XCage[Animal](new Crocodile) --- this is wrong!
   */

  // VARIABLES ARE BOTH COVARIANT AND CONTRAVARIANT
  class InvariantVariableCage[T](var animal: T) // ok

  /*
  trait AnotherCovariantCage[+T]{
    def addAnimal(animal: T) // method arguments are in CONTRAVARIANT POSITION
  }
  val cccage: CCage[Animal] = new CCage[Dog]
  ccage.add(new Cat) --- WROOONG

   */

  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  // acc.addAnimal(new Dog) -- compiler stops us because it's contravariant of cat- only cat or below
  acc.addAnimal(new Cat)

  class Kitty extends Cat

  acc.addAnimal(new Kitty)

  class MyList[+A] {
    // def add[A](element: A) : MyList[A] --- cant do
    def add[B >: A](element: B): MyList[B] = new MyList[B] // widening the type
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat) // compiler avoids before widening, but now Cat (B) is a supertype of Kitty(A)
  val evenMoreAnimals = moreAnimals.add(new Dog) // Dog is an Animal (B), so compiler wide the return type

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types
  class PetShop[-T] {
    // def get(isItAPuppy: Boolean): T // METHOD RETURN TYPES ARE IN COVARIANT POSITION

    /**
     * val catShop = new PetShop[Animal]{
     * def get(): Animal = new Cat
     * }
     *
     * val dogShop: new PetShop[Dog] = catShop
     *
     * THIS IS WRONG, COMPILER STOPS ME
     */
    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]

  //val evilCat = shop.get(true, new Cat) // inferred tuype arguments do not conferm to method get type
  class Malinois extends Dog

  val malinois = shop.get(true, new Malinois)

  /**
   * BIG RULE
   * - method arguments are in CONTRAVARIANT position
   * - return types are in COVARIANT position
   */
}
