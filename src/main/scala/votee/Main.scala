package votee

import votee.algorithms.{Majority, SuperMajority, Coomb}
import votee.models.PreferentialCandidate
import votee.models.PreferenceBallot
import votee.utils.Rational

object Main extends App {
  val candidates = List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"))
  val ballots = List(
    PreferenceBallot[PreferentialCandidate](1, Rational(1), candidates),
    PreferenceBallot[PreferentialCandidate](2, Rational(1), candidates.reverse),
    PreferenceBallot[PreferentialCandidate](3, Rational(1), candidates),
    PreferenceBallot[PreferentialCandidate](3, Rational(1), candidates)
  )

  val winner = Coomb.run(ballots, candidates, 1)
  println(s"Winner is: ${winner.head.candidate.name}")
}