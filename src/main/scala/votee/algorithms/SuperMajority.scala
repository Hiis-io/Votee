package votee.algorithms

import votee.models.{Candidate, Election, PreferenceBallot, PreferentialCandidate, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 30/03/2022.
 * Algorithm described at https://en.wikipedia.org/wiki/Supermajority
 */

trait MajorityWithPercentage[C <: Candidate, B <: PreferenceBallot[C]](majorityPercentage: Rational = Rational(1,2)) extends Election[C, B, Winner[C]]:
  require(!(majorityPercentage < Rational(1,2)) && !(majorityPercentage > Rational(1) ))
  override def run(ballots: List[B], candidates: List[C], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]

    //We are interested only in the first Candidate in the ballot and by default a score of 1/1 is granted to the candidate
    for (ballot <- ballots)
      candidateScoreMap(ballot.preferences.head) =
        Rational(1) * ballot.weight + candidateScoreMap.getOrElse(ballot.preferences.head, Rational(0))

    candidateScoreMap.toList.sortWith(_._2 > _._2).map(Winner(_)).filter(w => w.score > majorityPercentage).take(vacancies)
  end run
end MajorityWithPercentage

final case class SuperMajority(majorityPercentage: Rational) extends MajorityWithPercentage[PreferentialCandidate, PreferenceBallot[PreferentialCandidate]](majorityPercentage)

