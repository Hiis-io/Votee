package votee

import votee.algorithms.{Contingent, Majority, Veto}
import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialCandidate}
import votee.utils.Rational

object Main extends App {
  val candidates = List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"), PreferentialCandidate("temgoua", "Temgoua"))
  val b = PreferentialBallot[PreferentialCandidate](5, Rational(1), candidates.reverse)
  val c = b.excludeCandidates(candidates.take(1))
//  println(b)
  val ballots = List(
    PreferentialBallot(7, Rational(1), List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"))),
    b,
    c
  )
  val winner = Contingent.run(ballots, candidates, 2)
  println(s"Winner is: ${winner}")
} 