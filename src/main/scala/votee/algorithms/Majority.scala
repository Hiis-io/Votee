package votee.algorithms

import votee.algorithms
import votee.models.{Ballot, Candidate, Election, PreferenceBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Majority_rule
 */

trait Majority[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)
    candidateScoreMap.toList.sortWith(_._2 > _._2).map(Winner(_)).filter(w => w.score > Rational(ballots.length) * MAJORITY_THRESHOLD).take(vacancies)
  end run
end Majority

case object Majority extends Majority[PreferentialCandidate, PreferenceBallot[PreferentialCandidate]]