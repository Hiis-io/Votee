package votee

import votee.algorithms.{BordaCount, Coomb, Majority, SuperMajority}
import votee.models.PreferentialCandidate
import votee.models.PreferentialBallot
import votee.utils.Rational

object Main extends App {
  val candidates = List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"))
  val b = PreferentialBallot[PreferentialCandidate](7, Rational(1), candidates)
  val c = b.excludeCandidates(candidates.take(1))
  println(b)
  val ballots = List(
    b,
    PreferentialBallot[PreferentialCandidate](1, Rational(1), candidates),
    PreferentialBallot[PreferentialCandidate](2, Rational(1), candidates.reverse)
  )

  val winner = Majority[PreferentialCandidate].run(ballots, candidates, 1)
  println(s"Winner is: ${winner}")
}