package votee.utils

//TODO Use Spire library for rational number computation
// At the time of this writing the spire library has cross compatibility issues with scala 3


// A rational number x / y is represented by 2 integers x and y
// x is called the numerator and y is called the denominator

class Rational(x: Int, y: Int) {

  // require is used to enforce a precondition on the caller
  require(y != 0, "denominator must be non-zero")

  // define a greatest common divisor method we can use to simplify rationals
  private def gcd(a: Int, b: Int): Int = Math.abs(if (b == 0) a else gcd(b, a % b))

  val g: Int = gcd(x, y)

  val numerator: Int = x / g
  val denominator: Int = y / g

  // define a second constructor
  def this(x: Int) = this(x, 1)

  // define methods on this class
  def add(r: Rational): Rational =
    new Rational(numerator * r.denominator + r.numerator * denominator, denominator * r.denominator)

  def +(r: Rational): Rational = add(r)

  // negation
  def neg = new Rational(-numerator, denominator)
  def unary_- : Rational = neg

  //Subtraction
  def sub(r: Rational): Rational = add(r.neg)

  def -(r: Rational): Rational = sub(r)

  //Multiplication
  def mult(r: Rational) =
    new Rational(numerator * r.numerator, denominator * r.denominator)

  def *(r: Rational): Rational = mult(r)

  //Division
  def div(r: Rational) =
    new Rational(numerator * r.denominator, denominator * r.numerator)

  def /(r: Rational): Rational = div(r)

  //Less than
  def less(r: Rational): Boolean = numerator * r.denominator < r.numerator * denominator

  def <(r: Rational): Boolean = less(r)
  
  def <=(r: Rational): Boolean = less(r) || equals(r)

  //Greater than
  def more(r: Rational): Boolean = numerator * r.denominator > r.numerator * denominator

  def >(r: Rational): Boolean = more(r)
  
  def >=(r: Rational): Boolean = more(r) || equals(r)
  
  //Equality
  def equals(r: Rational): Boolean = numerator == r.numerator && denominator == r.denominator
  
  def ==(r: Rational): Boolean = equals(r)

  //Maximum
  def max(r: Rational): Rational = if (less(r)) r else this

  //Minimum
  def min(r: Rational): Rational = if (more(r)) r else this

  //Inverse
  def inv: Rational = new Rational(denominator, numerator)
  def unary_/ : Rational = inv

  override
  def toString: String = s"$numerator / $denominator"
}