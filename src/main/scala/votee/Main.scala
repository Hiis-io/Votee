package votee

import votee.algorithms.{Approval, BaldWin, BordaCount, Contingent, ExhaustiveBallot, Majority, Veto}
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
  given tieResolver: TieResolver[PreferentialCandidate] = Election.TieResolvers.reverseTieResolver[PreferentialCandidate]
  val winner = BaldWin.run(ballots, candidates, 2)
  println(s"Winner is: ${winner}")

//  def mySort(list: List[(Int, String)]): List[(Int, String)] =
//    val sorted = list.sortWith(_._1 > _._1)
//
//     def partition(l: List[(Int, String)]): List[(Int, String)] =
//       if l.isEmpty || l.length == 1 then l
//       else l.partition(_._1 == l.head._1) match
//        case (bigger, smaller) =>
//          println(s"current Bigger $bigger")
//          println(s"current Smaller $smaller")
//          bigger.reverse ++ partition(smaller)
//
//     partition(sorted)
//  end mySort
//
//  val list = List((-1, "A"), (-2, "A"), (3, "A"), (4, "A"), (8, "A"), (3, "B"), (2, "A"), (-1, "B"))
//
//  println(Random.shuffle(List(1, 2)))

} 