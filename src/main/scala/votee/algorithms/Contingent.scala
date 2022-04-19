package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Contingent_vote
 */

sealed trait Contingent[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    val candidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)

    val sortedTotals: List[(C, Rational)] = candidateScoreMap.toList.sortWith(_._2 > _._2)
    if sortedTotals.head._2 > MAJORITY_THRESHOLD then
      List(Winner(sortedTotals.head))
    else
      val firstRoundCandidates: List[C] = sortedTotals.take(2).map(_._1)
      val excludedCandidates = candidates.filterNot(firstRoundCandidates.contains(_))

      ballots.filterNot(ballot => firstRoundCandidates.contains(ballot.preferences.head)).foreach { ballot =>
        ballot.preferences.find(firstRoundCandidates.contains(_)) match {
          case Some(candidate) => candidateScoreMap(candidate) = ballot.weight + candidateScoreMap.getOrElse(candidate, Rational(0))
          case _ =>
        }
      }
      List(candidateScoreMap.toList.sortWith(_._2 > _._2).map(Winner(_)).head)
  end run
end Contingent

case object  Contingent:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new Contingent[CC, BB]{}.run(ballots, candidates, vacancies)()


