package io.hiis.votee

import io.hiis.votee.algorithms.BordaCount
import io.hiis.votee.models.{
  Election,
  PreferentialBallot,
  PreferentialCandidate,
  TieResolver,
  Winner
}
import spire.math.Rational

import scala.util.Random

object Main extends App:
  val candidates =
    List(PreferentialCandidate("a", "A"), PreferentialCandidate("b", "B"))
  val ballots: List[PreferentialBallot[PreferentialCandidate]] = List(
    PreferentialBallot(1, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(2, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(3, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(4, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(5, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(6, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(7, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(9, Rational(1), Random.shuffle(candidates)),
    PreferentialBallot(10, Rational(1), Random.shuffle(candidates))
  )

  given tieResolver: TieResolver[PreferentialCandidate] =
    Election.TieResolvers.doNothingTieResolver[PreferentialCandidate]

  val winner: Seq[Winner[PreferentialCandidate]] =
    BordaCount.run(ballots, candidates, 1)
  println(s"Winner is: $winner")
end Main
