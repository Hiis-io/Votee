package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, Winner}
import votee.utils.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

trait VetoRule[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run[CC <: C, BB <: B](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    for (ballot <- ballots) {
      for (preference <- ballot.preferences) {
        if !(preference == ballot.preferences.last && ballot.preferences.length > 1) then
          //Automatically assign a score of 1 to all candidates in the ballot except the last candidate as described by the veto rule
          candidateScoreMap(preference) = candidateScoreMap.getOrElse(preference, Rational(0, 1)) + Rational(1, 1)
      }
    }
    candidateScoreMap.toList.sortWith(_._2 > _._2).take(vacancies).map(Winner(_))
  end run
end VetoRule

final class Veto[C <: Candidate] extends VetoRule[C, Ballot[C]]
