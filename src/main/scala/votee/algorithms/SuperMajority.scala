package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}
import spire.math.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Supermajority
 */

sealed trait SuperMajority[C <: Candidate, B <: Ballot[C]](majorityPercentage: Rational = Rational(1,2)) extends PreferentialElection[C, B]:
  require(!(majorityPercentage < Rational(1,2)) && !(majorityPercentage > Rational(1) ))
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    countFirstVotes(ballots, candidates)
      .toList.sortWith(_._2 > _._2)
      .map(Winner(_))
      .filter(w => w.score > Rational(ballots.length) * majorityPercentage)
      .take(vacancies)
  end run
end SuperMajority

case object SuperMajority:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int, majorityPercentage: Rational): List[Winner[CC]] =
    new SuperMajority[CC, BB](majorityPercentage){}.run(ballots, candidates, vacancies)()