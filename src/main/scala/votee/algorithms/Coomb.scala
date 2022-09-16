package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}
import spire.math.Rational

import scala.annotation.tailrec
import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 01/04/2022.
 * Algorithm: https://en.wikipedia.org/wiki/Coombs%27_method
 * Note: This voting method requires voters to rank all the candidates
 */

sealed trait Coomb[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  @tailrec
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(using tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    if candidates.isEmpty then List.empty
    else
      val candidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)
      if candidateScoreMap.toList.filter(_._2 > MAJORITY_THRESHOLD * Rational(ballots.length)).take(1).nonEmpty then
        resolveTies(candidateScoreMap.toList.filter(w => w._2 > MAJORITY_THRESHOLD * Rational(ballots.length)).sortWith(_._2 > _._2)).map(Winner(_)).take(1)
      else
        val lastCandidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countLastVotes(ballots, candidates)
        val highestRankingLast: C = resolveTies(lastCandidateScoreMap.toList.sortWith(_._2 > _._2)).head._1
        run(ballots, candidates.filterNot(_ == highestRankingLast), vacancies)
  end run
end Coomb

case object  Coomb:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int)(using tieResolver: TieResolver[CC] = Election.TieResolvers.doNothingTieResolver[CC]): List[Winner[CC]] = new Coomb[CC, BB]{}.run(ballots, candidates, vacancies)

