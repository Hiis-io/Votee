package votee.algorithms

import votee.models.{Ballot, Candidate, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 01/04/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Coombs%27_method
 * Note: This voting method requires voters to rank all the candidates
 */

sealed trait Coomb[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)
    if candidateScoreMap.toList.filter(_._2 > MAJORITY_THRESHOLD).take(1).nonEmpty then
      candidateScoreMap.toList.map(Winner(_)).filter(w => w.score > MAJORITY_THRESHOLD * Rational(ballots.length)).take(1)
    else
      val lastCandidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countLastVotes(ballots, candidates)
      val highestRankingLast: C = lastCandidateScoreMap.toList.sortWith(_._2 > _._2).head._1

      run(ballots, candidates.filterNot(_ == highestRankingLast), vacancies)
  end run
end Coomb

case object  Coomb:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new Coomb[CC, BB]{}.run(ballots, candidates, vacancies)

