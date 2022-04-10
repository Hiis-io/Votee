package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Supermajority
 */

trait MajorityWithPercentage[C <: Candidate, B <: Ballot[C]](majorityPercentage: Rational = Rational(1,2)) extends PreferentialElection[C, B]:
  require(!(majorityPercentage < Rational(1,2)) && !(majorityPercentage > Rational(1) ))
  override def run[CC <: C, BB <: B](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = mutable.HashMap.empty ++ countFirstVotes(ballots, candidates)
    candidateScoreMap.toList.sortWith(_._2 > _._2).map(Winner(_)).filter(w => w.score > Rational(ballots.length) * majorityPercentage).take(vacancies)
  end run
end MajorityWithPercentage

final case class SuperMajority[C <: Candidate](majorityPercentage: Rational) extends MajorityWithPercentage[C, Ballot[C]](majorityPercentage)