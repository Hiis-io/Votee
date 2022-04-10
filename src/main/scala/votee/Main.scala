package votee

import votee.algorithms.{BordaCount, Coomb, Majority, SuperMajority}
import votee.models.{Candidate, PreferentialBallot, PreferentialCandidate}
import votee.utils.Rational

object Main extends App {
  val candidates = List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"))
  val b = PreferentialBallot[PreferentialCandidate](7, Rational(1), candidates)
  val c = b.excludeCandidates(candidates.take(1))
  println(b)
  val ballots = List(
    PreferentialBallot(7, Rational(1), List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic")))
  )
  val winner = Majority[PreferentialCandidate].run(ballots, candidates, 1)
  println(s"Winner is: ${winner}")
} 