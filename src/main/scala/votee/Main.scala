package votee

import votee.algorithms.{Approval, BordaCount, Contingent, ExhaustiveBallot, Majority, Veto}
import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, TieResolver}
import votee.utils.Rational

import scala.util.Random

object Main extends App {
  val candidates = List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"), PreferentialCandidate("temgoua", "Temgoua"))
  val b = PreferentialBallot[PreferentialCandidate](5, Rational(1), candidates.reverse)
  val c = b.excludeCandidates(candidates.take(1))
//  println(b)
  val ballots = List(
    PreferentialBallot(7, Rational(1), List(PreferentialCandidate("abanda", "Abanda"), PreferentialCandidate("ludovic", "Ludovic"))),
    b,
    c,
    b.excludeCandidates(candidates.reverse.take(2))
  )

//  val tieResolution: TieResolver[PreferentialCandidate] = (candidateScores: List[(PreferentialCandidate, Rational)], vacancies: Int) => candidateScores.sortWith(_._2 < _._2).take(vacancies)
  given tieResolver: TieResolver[PreferentialCandidate] = Election.TieResolvers.randomTieResolver
  val winner = Approval.run(ballots, candidates, 2)
  println(s"Winner is: ${winner}")
} 