package votee.algorithms

import votee.models.{Ballot, Candidate, Election, PreferentialBallot, PreferentialCandidate, PreferentialElection, TieResolver, Winner}
import spire.math.Rational

import scala.collection.mutable

/**
 * Created by Abanda Ludovic on 29/03/2022.
 */

sealed trait Veto[C <: Candidate, B <: Ballot[C]] extends PreferentialElection[C, B]:
  override final def run(ballots: List[B], candidates: List[C], vacancies: Int)(tieResolver: TieResolver[C] = DEFAULT_TIE_RESOLVER): List[Winner[C]] =
    val candidateScoreMap = new mutable.HashMap[C, Rational]
    for (ballot <- ballots) {
      for (preference <- ballot.preferences) {
        if !(preference == ballot.preferences.last && ballot.preferences.length > 1) then
          //Automatically assign a score of 1 to all candidates in the ballot except the last candidate as described by the veto rule
          candidateScoreMap(preference) = candidateScoreMap.getOrElse(preference, Rational(0, 1)) + Rational(1)
      }
    }
    candidateScoreMap.toList.sortWith(_._2 > _._2).take(vacancies).map(Winner(_))
  end run
end Veto

case object  Veto:
  def run[CC <: Candidate, BB <: Ballot[CC]](ballots: List[BB], candidates: List[CC], vacancies: Int): List[Winner[CC]] =
    new Veto[CC, BB]{}.run(ballots, candidates, vacancies)()
