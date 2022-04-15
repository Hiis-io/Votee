package votee

import votee.algorithms.{BordaCount, Contingent, ExhaustiveBallot, Majority, Veto}
import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialCandidate}
import votee.utils.Rational

object Main extends App {
  val candidates = List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"), PreferentialCandidate("temgoua", "Temgoua"))
  val b = PreferentialBallot[PreferentialCandidate](5, Rational(3,2), candidates.reverse)
  val c = b.excludeCandidates(candidates.take(1))
//  println(b)
  val ballots = List(
    PreferentialBallot(7, Rational(2), List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"))),
    b,
    c,
    b.excludeCandidates(candidates.reverse.take(2))
  )
  val winner = Majority.run(ballots, candidates, 3)
  println(s"Winner is: ${winner}")
} 