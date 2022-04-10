package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 31/03/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Contingent_vote
 */

trait ContingentRule[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run[CC <: C, BB <: B](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)

    val sortedTotals: List[(C, Rational)] = candidateScoreMap.toList.sortWith(_._2 > _._2)
    if sortedTotals.head._2 > MAJORITY_THRESHOLD * Rational(ballots.length) then
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
end ContingentRule

final class Contingent[C <: Candidate] extends ContingentRule[C, Ballot[C]]


