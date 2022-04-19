package votee.algorithms

import votee.algorithms
import votee.models.{Ballot, BallotOps, Candidate, Election, PreferentialElection, TieResolver, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Majority_rule
 */

sealed trait Majority[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    val candidateScoreMap: mutable.HashMap[C, Rational] = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)
    candidateScoreMap.toList.sortWith(_._2 > _._2).map(Winner(_)).filter(w => w.score > Rational(ballots.length) * MAJORITY_THRESHOLD).take(vacancies)
  end run
end Majority

case object  Majority:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new Majority[CC, BB]{}.run(ballots, candidates, vacancies)()